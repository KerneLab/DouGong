package org.kernelab.dougong.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
import org.kernelab.basis.io.DataWriter;
import org.kernelab.basis.io.TextDataSource;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;
import org.kernelab.dougong.semi.dml.AbstractSubquery;
import org.kernelab.dougong.semi.dml.PredeclaredView;

public class EntityMaker
{
	public static final Pattern IMPORT_PATTERN = Pattern.compile("^import\\s+(\\S+)\\s*;$");

	public static final File make(Provider provider, SQLKit kit, ResultSetMetaData meta, String name, Class<?> sup,
			String pkg, File base, String schema, String charSet, File template)
			throws FileNotFoundException, SQLException
	{
		return new EntityMaker() //
				.provider(provider) //
				.kit(kit) //
				.meta(meta) //
				.name(name) //
				.sup(sup) //
				.pkg(pkg) //
				.base(base) //
				.schema(schema) //
				.charSet(charSet) //
				.template(template) //
				.make() //
				.file();
	}

	public static final File makeSubquery(Provider provider, SQLKit kit, ResultSet rs, String name, String pkg,
			File base, String schema, String charSet) throws FileNotFoundException, SQLException
	{
		return make(provider, //
				kit, //
				rs.getMetaData(), //
				name, //
				AbstractSubquery.class, //
				pkg, //
				base, //
				schema, //
				charSet, //
				null);
	}

	public static final File makeTable(Provider provider, SQLKit kit, String name, String pkg, File base, String schema,
			String charSet) throws FileNotFoundException, SQLException
	{
		String tab = (Tools.notNullOrEmpty(schema) ? schema + "." : "") + name;
		return make(provider, //
				kit, //
				kit.query("SELECT * FROM " + tab + " WHERE 0=1").getMetaData(), //
				name, //
				AbstractTable.class, //
				pkg, //
				base, //
				schema, //
				charSet, //
				null);
	}

	public static final File makeView(Provider provider, SQLKit kit, PredeclaredView view, String pkg, File base,
			String schema, String charSet) throws FileNotFoundException, SQLException
	{
		String name = view.getClass().getSimpleName();
		String clsName = view.getClass().getCanonicalName();
		if (name == null || name.length() == 0 //
				|| clsName == null || clsName.indexOf('$') >= 0 || clsName.indexOf('[') >= 0)
		{
			throw new RuntimeException("Invalid PredeclaredView class: " + clsName);
		}

		String sql = view.select().toString();

		return make(provider, //
				kit, //
				kit.query(sql, view.parameters()).getMetaData(), //
				name, //
				PredeclaredView.class, //
				pkg, //
				base, //
				schema, //
				charSet, //
				new File(Tools.getFolderPath(Tools.getFilePath(base)) + clsName.replace('.', File.separatorChar)
						+ ".java"));
	}

	private Provider								provider;

	private SQLKit									kit;

	private ResultSetMetaData						meta;

	private String									name;

	private Class<?>								sup;

	private String									pkg;

	private File									base;

	private String									schema;

	private String									cs;

	private Set<String>								imports	= new LinkedHashSet<String>();

	private File									template;

	private List<String>							templateBody;

	private Map<String, Integer>					primaryKey;

	private Map<Pair<String, String>, List<String>>	foreignKeys;

	public File base()
	{
		return base;
	}

	public EntityMaker base(File base)
	{
		this.base = base;
		return this;
	}

	public String charSet()
	{
		return cs;
	}

	public EntityMaker charSet(String charSet)
	{
		this.cs = charSet != null ? charSet : Charset.defaultCharset().name();
		return this;
	}

	protected EntityMaker fetchForeignKeys(KeysFetcher keysFetcher)
	{
		if (keysFetcher != null && this.isEntity())
		{
			try
			{
				this.foreignKeys = keysFetcher.foreignKeys(kit(), name(), schema());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (this.foreignKeys == null)
		{
			this.foreignKeys = new HashMap<Pair<String, String>, List<String>>();
		}

		if (!this.foreignKeys.isEmpty())
		{
			this.imports.add(ForeignKey.class.getName());
			this.imports.add(ForeignKeyMeta.class.getName());
		}

		return this;
	}

	protected EntityMaker fetchPrimaryKey(KeysFetcher keysFetcher)
	{
		if (keysFetcher != null && this.isEntity())
		{
			try
			{
				this.primaryKey = keysFetcher.primaryKey(kit(), name(), schema());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (this.primaryKey == null)
		{
			this.primaryKey = new HashMap<String, Integer>();
		}

		if (!this.primaryKey.isEmpty())
		{
			this.imports.add(PrimaryKeyMeta.class.getName());
		}

		return this;
	}

	public File file()
	{
		return new File(Tools.getFolderPath(Tools.getFilePath(base())) //
				+ pkg().replace('.', File.separatorChar) //
				+ File.separatorChar + name() + ".java");
	}

	protected Map<Pair<String, String>, List<String>> getForeignKeys()
	{
		return foreignKeys;
	}

	protected Map<String, Integer> getPrimaryKey()
	{
		return primaryKey;
	}

	protected boolean isEntity()
	{
		return Entity.class.isAssignableFrom(sup());
	}

	public SQLKit kit()
	{
		return kit;
	}

	public EntityMaker kit(SQLKit kit)
	{
		this.kit = kit;
		return this;
	}

	public EntityMaker make() throws FileNotFoundException, SQLException
	{
		File file = file();

		File dir = file.getParentFile();

		if (!dir.isDirectory())
		{
			dir.mkdirs();
		}

		DataWriter out = new DataWriter().setCharsetName(charSet()).setDataFile(file());

		KeysFetcher keysFetcher = provider().provideKeysFetcher();
		this.fetchPrimaryKey(keysFetcher);
		this.fetchForeignKeys(keysFetcher);

		this.outputHead(out).outputBody(out);

		out.close();

		return this;
	}

	public ResultSetMetaData meta()
	{
		return meta;
	}

	public EntityMaker meta(ResultSet rs)
	{
		try
		{
			return meta(rs.getMetaData());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	public EntityMaker meta(ResultSetMetaData meta)
	{
		this.meta = meta;
		return this;
	}

	public String name()
	{
		return name;
	}

	public EntityMaker name(String name)
	{
		this.name = name;
		return this;
	}

	protected EntityMaker outputBody(DataWriter out) throws SQLException
	{
		Map<String, Integer> pk = this.getPrimaryKey();

		out.write("@NameMeta(name = \"" + JSON.EscapeString(name()) + "\")");
		out.write("public class " + name() + " extends " + sup().getSimpleName());
		out.write("{");

		for (Pair<String, String> pair : this.getForeignKeys().keySet())
		{
			outputForeignKeyField(out, pair.value);
			out.write();
		}

		int columns = meta().getColumnCount();

		boolean first = true;

		String column = null, name = null;
		int precision = 0, scale = 0, nullable = 0;

		for (int i = 1; i <= columns; i++)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.write();
			}
			column = meta().getColumnLabel(i);
			name = JSON.EscapeString(column);
			out.write("\t@NameMeta(name = \"" + name + "\")");

			if (this.isEntity())
			{
				precision = meta().getPrecision(i);
				scale = meta().getScale(i);
				nullable = meta().isNullable(i);
				out.print("\t@TypeMeta(");
				out.print("type = \"" + JSON.EscapeString(meta().getColumnTypeName(i)) + "\"");
				if (precision != 0)
				{
					out.print(", precision = " + precision);
				}
				if (scale != 0)
				{
					out.print(", scale = " + scale);
				}
				if (nullable != ResultSetMetaData.columnNullableUnknown)
				{
					out.print(", nullable = TypeMeta." + (nullable == TypeMeta.NO_NULLS ? "NO_NULLS" : "NULLABLE"));
				}
				out.write(")");

				out.write("\t@DataMeta(alias = \"" + Tools.mapUnderlineNamingToCamelStyle(name) + "\")");

				if (pk.get(column) != null)
				{
					out.write("\t@PrimaryKeyMeta(ordinal = " + pk.get(column) + ")");
				}
			}
			out.write("\tpublic Column\t" + wash(column) + ";");
		}

		for (Entry<Pair<String, String>, List<String>> entry : this.getForeignKeys().entrySet())
		{
			out.write();
			outputForeignKeyMethod(out, entry.getKey().key, entry.getKey().value, entry.getValue());
		}

		if (this.templateBody != null)
		{
			out.write();
			for (String line : this.templateBody)
			{
				out.write(line);
			}
		}
		else
		{
			out.write("}");
		}

		return this;
	}

	protected void outputForeignKeyField(DataWriter out, String name)
	{
		out.write("\t@ForeignKeyMeta");
		out.write("\tpublic static final String\t" + wash(name) + "\t= \"" + JSON.EscapeString(wash(name)) + "\";");
	}

	protected void outputForeignKeyMethod(DataWriter out, String table, String name, List<String> columns)
	{
		StringBuilder buf = new StringBuilder();
		for (String column : columns)
		{
			buf.append(", ");
			buf.append(wash(column));
		}

		out.write("\t@ForeignKeyMeta");
		out.write("\tpublic ForeignKey " + wash(name) + "(" + table + " ref" + ")");
		out.write("\t{");
		out.write("\t\treturn foreignKey(ref" + buf.toString() + ");");
		out.write("\t}");
	}

	protected EntityMaker outputHead(DataWriter out)
	{
		out.write("package " + pkg() + ";");
		out.write();
		this.outputImports(out);
		out.write();
		out.print("@MemberMeta(");
		if (schema() != null)
		{
			if (schema().length() == 0)
			{
				out.print("follow = " + true);
			}
			else
			{
				out.print("schema = \"" + JSON.EscapeString((String) schema()) + "\"");
			}
		}
		out.write(")");
		return this;
	}

	protected EntityMaker outputImports(DataWriter out)
	{
		imports.add(Column.class.getName());
		imports.add(MemberMeta.class.getName());
		imports.add(NameMeta.class.getName());
		if (this.isEntity())
		{
			imports.add(TypeMeta.class.getName());
			imports.add(DataMeta.class.getName());
		}
		imports.add(sup().getName());

		for (String line : this.imports)
		{
			out.write("import " + line + ";");
		}

		return this;
	}

	protected EntityMaker parseTemplate()
	{
		if (this.template() != null)
		{
			templateBody = new LinkedList<String>();

			boolean beginBody = false;
			int idx = -1;

			Matcher importMatcher = IMPORT_PATTERN.matcher("");

			for (String line : new TextDataSource(this.template(), Charset.forName(this.charSet()), "\n"))
			{
				line = line.replaceFirst("(.*)\r$", "$1");

				if (!beginBody)
				{
					if (importMatcher.reset(line.trim()).matches())
					{
						imports.add(importMatcher.group(1));
					}
					else if ((idx = line.indexOf('{')) >= 0)
					{
						beginBody = true;
						String body = line.substring(idx + 1);
						if (body.length() > 0)
						{
							templateBody.add(body);
						}
					}
				}
				else
				{
					templateBody.add(line);
				}
			}
		}

		return this;
	}

	public String pkg()
	{
		return pkg;
	}

	public EntityMaker pkg(String pkg)
	{
		this.pkg = pkg;
		return this;
	}

	public Provider provider()
	{
		return provider;
	}

	public EntityMaker provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	public String schema()
	{
		return schema;
	}

	public EntityMaker schema(String schema)
	{
		this.schema = schema;
		return this;
	}

	public Class<?> sup()
	{
		return sup;
	}

	public EntityMaker sup(Class<?> sup)
	{
		this.sup = sup;
		return this;
	}

	public EntityMaker sup(String sup)
	{
		try
		{
			return sup(Class.forName(sup));
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	public File template()
	{
		return template;
	}

	public EntityMaker template(File template)
	{
		this.template = template;
		this.parseTemplate();
		return this;
	}

	protected String wash(String name)
	{
		return name.replaceAll("\\W+", "_");
	}
}
