package org.kernelab.dougong.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.basis.io.DataWriter;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.AbstractSubquery;
import org.kernelab.dougong.semi.dml.AbstractTable;

public class EntityMaker
{
	public static final File make(ResultSetMetaData meta, String name, Class<?> sup, String pkg, File base,
			String schema, String charSet) throws FileNotFoundException, SQLException
	{
		return new EntityMaker() //
				.meta(meta) //
				.name(name) //
				.sup(sup) //
				.pkg(pkg) //
				.base(base) //
				.schema(schema) //
				.charSet(charSet) //
				.make() //
				.file();
	}

	public static final File makeSubquery(ResultSet rs, String name, String pkg, File base, String schema,
			String charSet) throws FileNotFoundException, SQLException
	{
		return make(rs.getMetaData(), //
				name, //
				AbstractSubquery.class, //
				pkg, //
				base, //
				schema, //
				charSet);
	}

	public static final File makeTable(SQLKit kit, String name, String pkg, File base, String schema, String charSet)
			throws FileNotFoundException, SQLException
	{
		return make(kit.query("SELECT * FROM " + name + " WHERE 0=1").getMetaData(), //
				name, //
				AbstractTable.class, //
				pkg, //
				base, //
				schema, //
				charSet);
	}

	private ResultSetMetaData	meta;

	private String				name;

	private Class<?>			sup;

	private String				pkg;

	private File				base;

	private String				schema;

	private String				cs;

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
		this.cs = charSet;
		return this;
	}

	public File file()
	{
		return new File(Tools.getFolderPath(Tools.getFilePath(base())) //
				+ pkg().replace('.', File.separatorChar) //
				+ File.separatorChar + name() + ".java");
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
		out.write("public class " + name() + " extends " + sup().getSimpleName());
		out.write("{");

		int columns = meta().getColumnCount();

		boolean first = true;

		String column = null;

		for (int i = 1; i <= columns; i++)
		{
			column = meta().getColumnLabel(i);
			if (first)
			{
				first = false;
			}
			else
			{
				out.write();
			}
			out.write("\t@NameMeta(name = \"" + JSON.EscapeString(column) + "\")");
			out.write("\tpublic Column\t" + wash(column) + ";");
		}

		out.write("}");

		return this;
	}

	protected EntityMaker outputHead(DataWriter out)
	{
		out.write("package " + pkg() + ";");
		out.write();
		out.write("import " + Column.class.getName() + ";");
		out.write("import " + MemberMeta.class.getName() + ";");
		out.write("import " + NameMeta.class.getName() + ";");
		out.write("import " + sup().getName() + ";");
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

	public String pkg()
	{
		return pkg;
	}

	public EntityMaker pkg(String pkg)
	{
		this.pkg = pkg;
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

	protected String wash(String name)
	{
		return name.replaceAll("\\W+", "_");
	}
}
