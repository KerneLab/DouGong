package org.kernelab.dougong.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.MappingMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;

public class Utils
{
	public static String getDataAliasFromField(Field field)
	{
		if (field != null)
		{
			DataMeta meta = field.getAnnotation(DataMeta.class);

			if (meta != null)
			{
				if (Tools.notNullOrEmpty(meta.alias()))
				{
					return meta.alias();
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static Expression getDataExpressionFromField(SQL sql, Field field)
	{
		if (field != null)
		{
			DataMeta meta = field.getAnnotation(DataMeta.class);

			if (meta != null)
			{
				if (Tools.notNullOrEmpty(meta.value()))
				{
					return sql.expr(meta.value());
				}
				else if (Tools.notNullOrEmpty(meta.alias()))
				{
					return sql.param(meta.alias());
				}
				else
				{
					return sql.param(getNameFromField(field));
				}
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static Map<String, Field> getMappingFields(Class<?> cls, Map<String, Field> result)
	{
		if (cls != null)
		{
			if (result == null)
			{
				result = new LinkedHashMap<String, Field>();
			}

			result = getMappingFields(cls.getSuperclass(), result);

			String name = null;

			for (Field field : cls.getDeclaredFields())
			{
				if (!Modifier.isStatic(field.getModifiers()))
				{
					name = field.getName();

					if (field.isAnnotationPresent(MappingMeta.class))
					{
						result.put(name, field);
					}
					else if (result.containsKey(name))
					{
						result.remove(name);
					}
				}
			}
		}
		return result;
	}

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

	public static <T> JSON mapObjectToJSON(T obj, JSON json, Map<String, Field> map)
	{
		if (obj != null)
		{
			if (json == null)
			{
				json = new JSON();
			}

			if (map != null && !map.isEmpty())
			{
				for (Entry<String, Field> entry : map.entrySet())
				{
					try
					{
						json.attr(entry.getKey(), Tools.access(obj, entry.getValue()));
					}
					catch (Exception e)
					{
					}
				}
			}
			else
			{
				boolean cache = map != null;

				String target = null;

				for (Field field : getMappingFields(obj.getClass(), null).values())
				{
					target = field.getAnnotation(MappingMeta.class).target();

					if ("\0".equals(target))
					{
						target = field.getName();
					}

					if (Tools.notNullOrEmpty(target))
					{
						try
						{
							json.attr(target, Tools.access(obj, field));
							if (cache)
							{
								map.put(target, field);
							}
						}
						catch (Exception e)
						{
						}
					}
				}
			}
		}
		return json;
	}

	public static <T> T mapResultSetToObject(ResultSet rs, T obj, Map<Field, String> map)
	{
		if (rs != null && obj != null)
		{
			if (map != null && !map.isEmpty())
			{
				for (Entry<Field, String> entry : map.entrySet())
				{
					try
					{
						Tools.access(obj, entry.getKey(), rs.getObject(entry.getValue()));
					}
					catch (Exception e)
					{
					}
				}
			}
			else
			{
				boolean cache = map != null;

				String[] sources = null;

				out: for (Field field : getMappingFields(obj.getClass(), null).values())
				{
					sources = field.getAnnotation(MappingMeta.class).source();

					if (sources.length == 0)
					{
						try
						{
							Tools.access(obj, field, rs.getObject(field.getName()));
							if (cache)
							{
								map.put(field, field.getName());
							}
						}
						catch (Exception e)
						{
						}
					}
					else
					{
						for (String source : sources)
						{
							if (Tools.notNullOrEmpty(source))
							{
								try
								{
									Tools.access(obj, field, rs.getObject(source));
									if (cache)
									{
										map.put(field, source);
									}
									continue out;
								}
								catch (Exception e)
								{
								}
							}
							else
							{
								continue out;
							}
						}
					}
				}
			}
		}
		return obj;
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
			((Expression) obj).toStringExpress(buf);
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
