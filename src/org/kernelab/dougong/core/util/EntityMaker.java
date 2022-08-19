package org.kernelab.dougong.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
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
import org.kernelab.dougong.semi.dml.PredefinedView;

public class EntityMaker
{
	public static class ColumnInfo
	{
		private String	name;

		private String	type;

		private int		precision	= 0;

		private int		scale		= 0;

		private int		nullable	= ResultSetMetaData.columnNullableUnknown;

		public ColumnInfo(String name, String type, int precision, int scale, int nullable)
		{
			this.setName(name).setType(type).setPrecision(precision).setScale(scale).setNullable(nullable);
		}

		public String getName()
		{
			return name;
		}

		public int getNullable()
		{
			return nullable;
		}

		public int getPrecision()
		{
			return precision;
		}

		public int getScale()
		{
			return scale;
		}

		public String getType()
		{
			return type;
		}

		public ColumnInfo setName(String name)
		{
			this.name = name;
			return this;
		}

		public ColumnInfo setNullable(int nullable)
		{
			this.nullable = nullable;
			return this;
		}

		public ColumnInfo setPrecision(int precision)
		{
			this.precision = precision;
			return this;
		}

		public ColumnInfo setScale(int scale)
		{
			this.scale = scale;
			return this;
		}

		public ColumnInfo setType(String type)
		{
			this.type = type;
			return this;
		}
	}

	public class JavaStyleMaker implements Maker
	{
		@Override
		public String getClassDeclaration()
		{
			return "public" + (isOutputAsInnerClass() ? " static" : "") + " class " + name() + " extends "
					+ sup().getSimpleName() + nl() + "{";
		}

		@Override
		public String getColumn(String name)
		{
			return "public Column\t" + name + ";";
		}

		@Override
		public String getForeignKeyField(String name)
		{
			return indent(1) + "@ForeignKeyMeta" + nl() //
					+ indent(1) + "public static final String\t" + name + "\t= \"" + Tools.escape(name) + "\";";
		}

		@Override
		public String getForeignKeyMethod(String name, String table, String columns)
		{
			String indent = indent(1);
			return indent + "@ForeignKeyMeta" + nl() //
					+ indent + "public ForeignKey " + name + "(" + table + " ref" + ")" + nl() //
					+ indent + "{" + nl() //
					+ indent + indent + "return foreignKey(ref, " + columns + ");" + nl() //
					+ indent + "}";
		}

		@Override
		public String getStatement(String statement)
		{
			return statement + ";";
		}

		@Override
		public String getTypeMeta(ColumnInfo info)
		{
			String text = "@TypeMeta(";
			text += "type = \"" + Tools.escape(info.getType()) + "\"";
			if (info.getPrecision() != 0)
			{
				text += ", precision = " + info.getPrecision();
			}
			if (info.getScale() != 0)
			{
				text += ", scale = " + info.getScale();
			}
			if (info.getNullable() != ResultSetMetaData.columnNullableUnknown)
			{
				text += ", nullable = TypeMeta." + (info.getNullable() == TypeMeta.NO_NULLS ? "NO_NULLS" : "NULLABLE");
			}
			text += ")";
			return text;
		}

		@Override
		public String indent(int num)
		{
			return Tools.repeat('\t', num);
		}

		@Override
		public void parseTemplate()
		{
			boolean beginBody = false;
			int idx = -1;

			Matcher importMatcher = Pattern.compile("^import\\s+(\\S+)\\s*;$").matcher("");

			for (String line : new TextDataSource(template(), Charset.forName(charSet()), "\n"))
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
						String head = line.substring(0, idx + 1);
						if (head.length() > 0)
						{
							templateHead.add(head);
						}
						String body = line.substring(idx + 1);
						if (body.length() > 0)
						{
							templateBody.add(body);
						}
					}
					else if (Tools.notNullOrWhite(line) && !line.matches("^package\\s+.+$"))
					{
						templateHead.add(line);
					}
				}
				else
				{
					templateBody.add(line);
				}
			}
		}
	}

	public static interface Maker
	{
		public String getClassDeclaration();

		public String getColumn(String name);

		public String getForeignKeyField(String name);

		public String getForeignKeyMethod(String name, String table, String columns);

		public String getStatement(String statement);

		public String getTypeMeta(ColumnInfo info);

		public String indent(int num);

		public void parseTemplate();
	}

	public class ScalaStyleMaker implements Maker
	{
		@Override
		public String getClassDeclaration()
		{
			return "class " + name() + " extends " + sup().getSimpleName() + " {";
		}

		@Override
		public String getColumn(String name)
		{
			return "lazy val " + name + ": Column = newColumn(\"" + name + "\")";
		}

		@Override
		public String getForeignKeyField(String name)
		{
			return indent(1) + "@ForeignKeyMeta" + nl() //
					+ indent(1) + "val " + name + ": String = \"" + Tools.escape(name) + "\"";
		}

		@Override
		public String getForeignKeyMethod(String name, String table, String columns)
		{
			return indent(1) + "@ForeignKeyMeta" + nl() //
					+ indent(1) + "def " + name + "(ref: " + table + "): ForeignKey = foreignKey(ref, " + columns + ")";
		}

		@Override
		public String getStatement(String statement)
		{
			return statement;
		}

		@Override
		public String getTypeMeta(ColumnInfo info)
		{
			String text = "@TypeMeta(";
			text += "`type` = \"" + Tools.escape(info.getType()) + "\"";
			if (info.getPrecision() != 0)
			{
				text += ", precision = " + info.getPrecision();
			}
			if (info.getScale() != 0)
			{
				text += ", scale = " + info.getScale();
			}
			if (info.getNullable() != ResultSetMetaData.columnNullableUnknown)
			{
				text += ", nullable = TypeMeta." + (info.getNullable() == TypeMeta.NO_NULLS ? "NO_NULLS" : "NULLABLE");
			}
			text += ")";
			return text;
		}

		@Override
		public String indent(int num)
		{
			return Tools.repeat("  ", num);
		}

		@Override
		public void parseTemplate()
		{
			boolean importEnd = false, classBegin = false;

			Matcher importMatcher = Pattern.compile("^import\\s+(\\S+)\\s*;?\\s*$").matcher("");

			Matcher classMatcher = Pattern.compile("^\\s*class\\b.*$").matcher("");

			for (String line : new TextDataSource(template(), Charset.forName(charSet()), "\n"))
			{
				line = line.replaceFirst("(.*)\r$", "$1");

				if (!importEnd)
				{
					if (importMatcher.reset(line.trim()).matches())
					{
						imports.add(importMatcher.group(1));
					}
					else if (classMatcher.reset(line).matches())
					{
						importEnd = true;
						templateHead.add(line);
						if (line.indexOf('{') >= 0)
						{
							classBegin = true;
						}
					}
				}
				else
				{
					if (!classBegin)
					{
						templateHead.add(line);
						if (line.indexOf('{') >= 0)
						{
							classBegin = true;
						}
					}
					else
					{
						templateBody.add(line);
					}
				}
			}
		}
	}

	public static final String	JAVA_STYLE	= "java";

	public static final String	SCALA_STYLE	= "scala";

	public static String fillParametersWithNull(String sql)
	{
		return sql.replaceAll("\\?[^?]+?\\?", "NULL");
	}

	public static File make(Provider provider, SQLKit kit, List<ColumnInfo> meta, String name, Class<?> sup, String pkg,
			File base, String schema, boolean innerClass, String charSet, String style, File template)
			throws IOException, SQLException
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
				.innerClass(innerClass) //
				.charSet(charSet) //
				.style(style) //
				.template(template) //
				.make() //
				.file();
	}

	public static File make(Provider provider, SQLKit kit, ResultSetMetaData meta, String name, Class<?> sup,
			String pkg, File base, String schema, boolean innerClass, String charSet, String style, File template)
			throws IOException, SQLException
	{
		return make(provider, kit, resultSetMetaToColumnList(meta), name, sup, pkg, base, schema, innerClass, charSet,
				style, template);
	}

	public static File makeSubquery(Provider provider, SQLKit kit, List<ColumnInfo> meta, String name, File base,
			Class<?> inClass, String charSet, String style) throws IOException, SQLException
	{
		String clsName = inClass.getCanonicalName();
		if (Tools.isNullOrEmpty(inClass.getSimpleName()) //
				|| clsName == null || clsName.indexOf('$') >= 0 || clsName.indexOf('[') >= 0)
		{
			throw new RuntimeException("Invalid class: " + clsName);
		}

		return make(provider, //
				kit, //
				meta, //
				name, //
				AbstractSubquery.class, //
				inClass.getPackage().getName(), //
				base, //
				null, //
				true, //
				charSet, //
				style, //
				new File(Tools.getFolderPath(Tools.getFilePath(base)) + clsName.replace('.', File.separatorChar) + "."
						+ style));
	}

	public static File makeSubquery(Provider provider, SQLKit kit, List<ColumnInfo> meta, String name, String pkg,
			File base, String charSet, String style) throws IOException, SQLException
	{
		return make(provider, //
				kit, //
				meta, //
				name, //
				AbstractSubquery.class, //
				pkg, //
				base, //
				null, //
				false, //
				charSet, //
				style, //
				null);
	}

	public static File makeSubquery(Provider provider, SQLKit kit, ResultSet rs, String name, File base,
			Class<?> inClass, String charSet, String style) throws IOException, SQLException
	{
		return makeSubquery(provider, kit, resultSetMetaToColumnList(rs.getMetaData()), name, base, inClass, charSet,
				style);
	}

	public static File makeSubquery(Provider provider, SQLKit kit, ResultSet rs, String name, String pkg, File base,
			String charSet, String style) throws IOException, SQLException
	{
		return makeSubquery(provider, kit, resultSetMetaToColumnList(rs.getMetaData()), name, pkg, base, charSet,
				style);
	}

	public static File makeTable(Provider provider, SQLKit kit, String name, String pkg, File base, String schema,
			String charSet, String style) throws IOException, SQLException
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
				false, //
				charSet, //
				style, //
				null);
	}

	public static File makeView(Provider provider, SQLKit kit, PredefinedView view, File base, String charSet,
			String style) throws IOException, SQLException
	{
		Class<?> cls = view.getClass();
		String name = view.getClass().getSimpleName();
		String clsName = view.getClass().getCanonicalName();
		if (Tools.isNullOrEmpty(name) //
				|| clsName == null || clsName.indexOf('$') >= 0 || clsName.indexOf('[') >= 0)
		{
			throw new RuntimeException("Invalid PredeclaredView class: " + clsName);
		}

		String sql = view.select().toString();

		JSON params = view.parameters();

		return make(provider, //
				kit, //
				(params == null ? kit.query(fillParametersWithNull(sql)) : kit.query(sql, params)).getMetaData(), //
				name, //
				PredefinedView.class, //
				cls.getPackage().getName(), //
				base, //
				null, //
				false, //
				charSet, //
				style, //
				new File(Tools.getFolderPath(Tools.getFilePath(base)) + clsName.replace('.', File.separatorChar) + "."
						+ style));
	}

	public static List<ColumnInfo> resultSetMetaToColumnList(ResultSetMetaData meta) throws SQLException
	{
		List<ColumnInfo> list = new LinkedList<ColumnInfo>();

		int columns = meta.getColumnCount();

		for (int i = 1; i <= columns; i++)
		{
			list.add(new ColumnInfo(meta.getColumnLabel(i), meta.getColumnTypeName(i), meta.getPrecision(i),
					meta.getScale(i), meta.isNullable(i)));
		}

		return list;
	}

	private Provider								provider;

	private SQLKit									kit;

	private List<ColumnInfo>						meta;

	private String									name;

	private Class<?>								sup;

	private String									pkg;

	private File									base;

	private boolean									innerClass;

	private String									schema;

	private String									cs;

	private Set<String>								imports	= new TreeSet<String>();

	private String									style;

	private Maker									styler;

	private File									template;

	private List<String>							templateHead;

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
		if (kit() != null && keysFetcher != null && this.isEntity())
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
		if (kit() != null && keysFetcher != null && this.isEntity())
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
		if (this.isOutputAsInnerClass())
		{
			return this.template();
		}
		else if (base() != null)
		{
			return new File(Tools.getFolderPath(Tools.getFilePath(base())) //
					+ (Tools.isNullOrWhite(pkg()) ? "" : (pkg().replace('.', File.separatorChar) + File.separatorChar)) //
					+ name() + "." + style());
		}
		else
		{
			return null;
		}
	}

	protected Map<Pair<String, String>, List<String>> getForeignKeys()
	{
		return foreignKeys;
	}

	protected Map<String, Integer> getPrimaryKey()
	{
		return primaryKey;
	}

	public boolean innerClass()
	{
		return innerClass;
	}

	public EntityMaker innerClass(boolean innerClass)
	{
		this.innerClass = innerClass;
		return this;
	}

	protected boolean isEntity()
	{
		return Entity.class.isAssignableFrom(sup());
	}

	protected boolean isOutputAsInnerClass()
	{
		return innerClass() && template() != null;
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

	public EntityMaker make() throws IOException, SQLException
	{
		File file = file();

		if (file != null)
		{
			File dir = file.getParentFile();

			if (!dir.isDirectory())
			{
				dir.mkdirs();
			}
		}

		Writer out = null;

		if (file != null)
		{
			out = new OutputStreamWriter(new FileOutputStream(file, false), charSet());
		}
		else
		{
			out = new OutputStreamWriter(System.out);
		}

		KeysFetcher keysFetcher = provider().provideKeysFetcher();
		this.fetchPrimaryKey(keysFetcher);
		this.fetchForeignKeys(keysFetcher);

		this.outputHead(out).outputBody(out);

		if (file != null)
		{
			out.close();
		}
		else
		{
			out.flush();
		}

		return this;
	}

	public List<ColumnInfo> meta()
	{
		return meta;
	}

	public EntityMaker meta(List<ColumnInfo> meta)
	{
		this.meta = meta;
		return this;
	}

	public EntityMaker meta(ResultSet rs)
	{
		try
		{
			return this.meta(rs.getMetaData());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	public EntityMaker meta(ResultSetMetaData meta)
	{
		try
		{
			return this.meta(resultSetMetaToColumnList(meta));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
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

	protected String nl()
	{
		return "\n";
	}

	protected EntityMaker nl(Writer out) throws IOException
	{
		out.write(nl());
		return this;
	}

	protected EntityMaker outputBody(Writer out) throws SQLException, IOException
	{
		println(out, "@NameMeta(name = \"" + Tools.escape(name()) + "\")");
		println(out, styler().getClassDeclaration());

		Map<String, Integer> pk = this.getPrimaryKey();

		for (Pair<String, String> pair : this.getForeignKeys().keySet())
		{
			outputForeignKeyField(out, pair.value);
			nl(out);
		}

		boolean first = true;

		String column = null, name = null, indent = styler().indent(1);

		for (ColumnInfo info : meta())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				nl(out);
			}
			column = info.getName();
			name = Tools.escape(column);

			println(out, indent + "@NameMeta(name = \"" + name + "\")");

			if (this.isEntity())
			{
				println(out, indent + styler().getTypeMeta(info));

				println(out, indent + "@DataMeta(alias = \"" + Tools.mapUnderlineNamingToCamelStyle(name) + "\")");

				if (pk.get(column) != null)
				{
					println(out, indent + "@PrimaryKeyMeta(ordinal = " + pk.get(column) + ")");
				}
			}
			println(out, indent + styler().getColumn(wash(column)));
		}

		for (Entry<Pair<String, String>, List<String>> entry : this.getForeignKeys().entrySet())
		{
			nl(out);
			outputForeignKeyMethod(out, entry.getKey().key, entry.getKey().value, entry.getValue());
		}

		if (this.isOutputAsInnerClass())
		{
			println(out, "}");
		}

		if (this.templateBody != null)
		{
			nl(out);
			for (String line : this.templateBody)
			{
				println(out, line);
			}
		}
		else
		{
			println(out, "}");
		}

		return this;
	}

	protected void outputForeignKeyField(Writer out, String name) throws IOException
	{
		println(out, styler().getForeignKeyField(wash(name)));
	}

	protected void outputForeignKeyMethod(Writer out, String table, String name, List<String> columns)
			throws IOException
	{
		StringBuilder buf = new StringBuilder();
		for (String column : columns)
		{
			if (buf.length() > 0)
			{
				buf.append(", ");
			}
			buf.append(wash(column));
		}
		println(out, styler().getForeignKeyMethod(wash(name), table, buf.toString()));
	}

	protected EntityMaker outputHead(Writer out) throws IOException
	{
		if (Tools.notNullOrWhite(pkg()))
		{
			println(out, styler().getStatement("package " + pkg())).nl(out);
		}

		this.outputImports(out).nl(out);

		if (this.isOutputAsInnerClass())
		{
			for (String line : templateHead)
			{
				println(out, line);
			}
		}

		if (schema() != null)
		{
			out.write("@MemberMeta(");

			if (schema().length() == 0)
			{
				out.write("follow = " + true);
			}
			else
			{
				out.write("schema = \"" + Tools.escape(schema()) + "\"");
			}

			println(out, ")");
		}

		return this;
	}

	protected EntityMaker outputImports(Writer out) throws IOException
	{
		imports.add(Column.class.getName());
		imports.add(NameMeta.class.getName());
		if (this.schema() != null)
		{
			imports.add(MemberMeta.class.getName());
		}
		if (this.isEntity())
		{
			imports.add(TypeMeta.class.getName());
			imports.add(DataMeta.class.getName());
		}
		imports.add(sup().getName());

		for (String cls : this.imports)
		{
			println(out, styler().getStatement("import " + cls));
		}

		return this;
	}

	protected EntityMaker parseTemplate()
	{
		if (this.template() != null)
		{
			templateHead = new LinkedList<String>();
			templateBody = new LinkedList<String>();
			styler().parseTemplate();
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

	protected EntityMaker println(Writer out, String text) throws IOException
	{
		if (text != null)
		{
			out.write(text);
			nl(out);
		}
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

	public String style()
	{
		return style;
	}

	public EntityMaker style(String style)
	{
		if (SCALA_STYLE.equalsIgnoreCase(style))
		{
			this.style = SCALA_STYLE;
			this.styler = new ScalaStyleMaker();
		}
		else
		{
			this.style = JAVA_STYLE;
			this.styler = new JavaStyleMaker();
		}
		return this;
	}

	public Maker styler()
	{
		return styler;
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
