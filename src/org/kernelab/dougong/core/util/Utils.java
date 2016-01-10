package org.kernelab.dougong.core.util;

import java.lang.reflect.Field;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;

public class Utils
{
	public static String getNameFromClass(Class<?> cls)
	{
		if (cls != null)
		{
			String name = null;

			NameMeta meta = cls.getAnnotation(NameMeta.class);

			if (meta != null)
			{
				name = meta.name();
			}

			if (Tools.isNullOrEmpty(name))
			{
				name = cls.getSimpleName();
			}

			return name;
		}
		else
		{
			return null;
		}
	}

	public static String getNameFromField(Field field)
	{
		if (field != null)
		{
			String name = null;

			NameMeta meta = field.getAnnotation(NameMeta.class);

			if (meta != null)
			{
				name = meta.name();
			}

			if (Tools.isNullOrEmpty(name))
			{
				name = field.getName();
			}

			return name;
		}
		else
		{
			return null;
		}
	}

	public static String getNameFromNamed(Named named)
	{
		if (named != null)
		{
			if (Tools.notNullOrEmpty(named.name()))
			{
				return named.name();
			}
			else
			{
				return getNameFromClass(named.getClass());
			}
		}
		else
		{
			return null;
		}
	}

	public static String getNameFromObject(Object obj)
	{
		if (obj != null)
		{
			return getNameFromClass(obj.getClass());
		}
		else
		{
			return null;
		}
	}

	public static String getSchemaFromMember(Member member)
	{
		if (member != null)
		{
			String schema = member.schema();

			if (Tools.isNullOrEmpty(schema))
			{
				MemberMeta meta = member.getClass().getAnnotation(MemberMeta.class);

				if (meta != null)
				{
					schema = meta.schema();

					if (schema.length() == 0 && meta.follow())
					{
						schema = Utils.getSchemaFromPackage(member.getClass());
					}

					schema = Tools.isNullOrEmpty(schema) ? null : schema;
				}
			}

			return schema;
		}
		else
		{
			return null;
		}
	}

	public static String getSchemaFromPackage(Class<?> cls)
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

	public static void main(String[] args)
	{
		Tools.debug(Utils.class.getSimpleName());
	}

	/**
	 * Output the alias name of the object, if given, to the buffer.
	 * 
	 * @param provider
	 * @param buffer
	 * @param object
	 * @return The given buffer.
	 */
	public static StringBuilder outputAlias(Provider provider, StringBuilder buffer, Object object)
	{
		if (object instanceof Alias)
		{
			provider.provideOutputAlias(buffer, ((Alias) object));
		}
		return buffer;
	}

	public static StringBuilder outputExpr(StringBuilder buf, Object obj)
	{
		if (obj instanceof Expression)
		{
			boolean isResult = obj instanceof Result;
			if (isResult)
			{
				buf.append('(');
			}
			((Expression) obj).toStringExpressed(buf);
			if (isResult)
			{
				buf.append(')');
			}
		}
		else if (obj instanceof Text)
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
		return buf;
	}
}
