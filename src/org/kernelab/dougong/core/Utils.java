package org.kernelab.dougong.core;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;

public class Utils
{
	public static String getSchemaNameFromPackage(Class<?> cls)
	{
		if (cls != null)
		{
			String pkg = cls.getPackage().getName();
			return pkg.substring(pkg.lastIndexOf('.') + 1);
		}
		else
		{
			return null;
		}
	}

	public static String getTableNameFromClass(Class<?> cls)
	{
		if (cls != null)
		{
			return cls.getSimpleName();
		}
		else
		{
			return null;
		}
	}

	public static void main(String[] args)
	{
		Tools.debug(Utils.class.getSimpleName());
	}

	public static void text(StringBuilder buf, Object obj)
	{
		if (obj instanceof Text)
		{
			((Text) obj).toString(buf);
		}
		else if (obj == null)
		{
			buf.append(SQL.NULL);
		}
		else
		{
			buf.append(obj.toString());
		}
	}

	public static void textAliased(StringBuilder buf, Object obj)
	{
		if (obj instanceof Alias)
		{
			((Alias) obj).toStringAliased(buf);
		}
		else
		{
			text(buf, obj);
		}
	}
}
