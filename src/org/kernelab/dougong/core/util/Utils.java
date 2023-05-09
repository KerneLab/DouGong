package org.kernelab.dougong.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Label;
import org.kernelab.dougong.core.dml.cond.AtomicCondition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.dml.cond.ReflectiveCondition;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.MappingMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;

public class Utils
{
	public static <E> Collection<E> copy(Collection<E> source, Collection<E> target)
	{
		target.addAll(source);
		return target;
	}

	public static <E> E[] copy(E[] source)
	{
		if (source == null)
		{
			return null;
		}
		return Arrays.copyOf(source, source.length);
	}

	public static <E> List<E> copy(List<E> source, List<E> target)
	{
		target.addAll(source);
		return target;
	}

	public static <K, V> Map<K, V> copy(Map<K, V> source, Map<K, V> target)
	{
		target.putAll(source);
		return target;
	}

	public static <E> Set<E> copy(Set<E> source, Set<E> target)
	{
		target.addAll(source);
		return target;
	}

	public static String getCatalogFromMember(Member member)
	{
		if (member != null)
		{
			String catalog = member.catalog();

			if (Tools.isNullOrEmpty(catalog))
			{
				MemberMeta meta = member.getClass().getAnnotation(MemberMeta.class);

				if (meta != null)
				{
					catalog = meta.catalog();

					if (catalog.length() == 0 && meta.follow())
					{
						catalog = Utils.getCatalogFromPackage(member.getClass());
					}

					catalog = Tools.isNullOrEmpty(catalog) ? null : catalog;
				}
			}

			return catalog;
		}
		else
		{
			return null;
		}
	}

	public static String getCatalogFromPackage(Class<?> cls)
	{
		if (cls != null)
		{
			String pkg = cls.getPackage().getName();
			int idx = pkg.lastIndexOf('.');
			if (idx < 0)
			{
				return "";
			}
			pkg = pkg.substring(0, idx);
			return pkg.substring(pkg.lastIndexOf('.') + 1);
		}
		else
		{
			return null;
		}
	}

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

			if (meta != null && meta.raw())
			{
				if (Tools.notNullOrEmpty(meta.value()))
				{
					if (Tools.notNullOrWhite(meta.value()))
					{
						return sql.expr(meta.value());
					}
					else
					{
						return null;
					}
				}
				else
				{
					return getDataParameterFromField(sql, field);
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

	public static String getDataLabelFromField(Field field)
	{
		if (field != null)
		{
			String alias = getDataAliasFromField(field);
			return alias != null ? alias : field.getName();
		}
		else
		{
			return null;
		}
	}

	public static Expression getDataParameterFromField(SQL sql, Field field)
	{
		if (field != null)
		{
			DataMeta meta = field.getAnnotation(DataMeta.class);

			if (meta != null && Tools.notNullOrEmpty(meta.alias()))
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

	public static Expression getDataValueExpressionFromField(SQL sql, Field field)
	{
		if (field != null)
		{
			DataMeta meta = field.getAnnotation(DataMeta.class);

			if (meta != null && meta.raw())
			{
				if (Tools.notNullOrEmpty(meta.value()))
				{
					if (Tools.notNullOrWhite(meta.value()))
					{
						return sql.expr(meta.value());
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
		else
		{
			return null;
		}
	}

	public static Map<String, Object> getFieldNameMapByMeta(Class<?> cls, Map<String, Object> map)
	{
		if (cls == null || cls == Object.class)
		{
			return map;
		}

		if (map == null)
		{
			map = new LinkedHashMap<String, Object>();
		}

		map = getFieldNameMapByMeta(cls.getSuperclass(), map);

		DataMeta meta = null;

		for (Field field : cls.getDeclaredFields())
		{
			if ((meta = field.getAnnotation(DataMeta.class)) != null //
					&& meta.raw())
			{
				map.put(field.getName(), getDataLabelFromField(field));
			}
		}

		return map;
	}

	public static Map<String, Object> getFieldNameMapByMeta(Class<?> cls, Map<String, Object> map, Set<String> labels)
	{
		if (cls == null || cls == Object.class || labels == null || labels.size() == 0)
		{
			return map;
		}

		if (map == null)
		{
			map = new LinkedHashMap<String, Object>();
		}

		map = getFieldNameMapByMeta(cls.getSuperclass(), map, labels);

		DataMeta meta = null;

		for (Field field : cls.getDeclaredFields())
		{
			if ((meta = field.getAnnotation(DataMeta.class)) != null //
					&& labels.contains(meta.alias()))
			{
				map.put(field.getName(), getDataLabelFromField(field));
			}
		}

		return map;
	}

	public static Map<String, Object> getFieldNameMapByMetaFully(Class<?> cls, Map<String, Object> map)
	{
		if (cls == null || cls == Object.class)
		{
			return map;
		}

		if (map == null)
		{
			map = new LinkedHashMap<String, Object>();
		}

		map = getFieldNameMapByMetaFully(cls.getSuperclass(), map);

		for (Field field : cls.getDeclaredFields())
		{
			if (field.getAnnotation(DataMeta.class) != null)
			{
				map.put(field.getName(), getDataLabelFromField(field));
			}
		}

		return map;
	}

	/**
	 * Get label/field map defined by DataMeta of the given class.
	 * 
	 * @param cls
	 * @param map
	 * @return
	 */
	public static Map<String, Field> getLabelFieldMapByMeta(Class<?> cls, Map<String, Field> map)
	{
		if (cls != null)
		{
			if (map == null)
			{
				map = new HashMap<String, Field>();
			}

			map = getLabelFieldMapByMeta(cls.getSuperclass(), map);

			for (Field field : cls.getDeclaredFields())
			{
				if (field.getAnnotation(DataMeta.class) != null)
				{
					map.put(getDataLabelFromField(field), field);
				}
			}
		}

		return map;
	}

	/**
	 * Get label/field map defined by DataMeta of the given class within a given
	 * labels set.
	 * 
	 * @param cls
	 * @param labels
	 * @param map
	 * @return
	 */
	public static Map<String, Field> getLabelFieldMapByMeta(Class<?> cls, Set<String> labels, Map<String, Field> map)
	{
		if (cls != null)
		{
			if (map == null)
			{
				map = new HashMap<String, Field>();
			}

			map = getLabelFieldMapByMeta(cls.getSuperclass(), labels, map);

			String label = null;
			for (Field field : cls.getDeclaredFields())
			{
				if (field.getAnnotation(DataMeta.class) != null)
				{
					label = getDataLabelFromField(field);
					if (labels.contains(label))
					{
						map.put(label, field);
					}
				}
			}
		}

		return map;
	}

	public static String getLabelOfExpression(Expression expr)
	{
		if (expr == null)
		{
			return null;
		}

		if (expr instanceof Label)
		{
			return ((Label) expr).label();
		}

		if (expr instanceof Alias)
		{
			String alias = ((Alias) expr).alias();
			if (alias != null)
			{
				return alias;
			}
		}

		return getNameOfExpression(expr);
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
			String name = getNameFromMeta(cls);

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
			String name = getNameFromMeta(field);

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

	public static String getNameFromMeta(Class<?> cls)
	{
		NameMeta meta = cls.getAnnotation(NameMeta.class);
		return meta == null ? null : meta.name();
	}

	public static String getNameFromMeta(Field field)
	{
		NameMeta meta = field.getAnnotation(NameMeta.class);
		return meta == null ? null : meta.name();
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

	public static String getNameOfExpression(Expression expr)
	{
		if (expr == null)
		{
			return null;
		}

		if (expr instanceof Named)
		{
			return ((Named) expr).name();
		}
		else
		{
			return expr.toString();
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

	public static void main(String[] args) throws SQLException
	{

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
			((Expression) obj).toStringExpress(buf);
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

	public static StringBuilder outputExprInScope(StringBuilder buf, Expression expr)
	{
		if (expr instanceof Items)
		{
			buf.append('(');
		}
		outputExpr(buf, expr);
		if (expr instanceof Items)
		{
			buf.append(')');
		}
		return buf;
	}

	public static List<Object> reflectConditions(List<Object> reflects, Condition cond)
	{
		if (!(cond instanceof ReflectiveCondition))
		{
			return reflects;
		}

		if (reflects == null)
		{
			reflects = new LinkedList<Object>();
		}

		if (cond instanceof AtomicCondition)
		{
			reflects.add(cond);
		}
		else if (cond instanceof LogicalCondition)
		{
			List<Object> refls = ((LogicalCondition) cond).reflects();

			if (refls != null)
			{
				for (Object ref : refls)
				{
					if (ref instanceof AtomicCondition)
					{
						reflects.add(ref);
					}
					else if (ref instanceof ComposableCondition)
					{
						reflects.add(reflectConditions(new LinkedList<Object>(), (ComposableCondition) ref));
					}
					else
					{
						reflects.add(ref);
					}
				}
			}
		}

		return reflects;
	}

	@SuppressWarnings("unchecked")
	public static Condition restoreConditions(Provider p, List<Object> conds)
	{
		Condition c = null, d = null;

		String logic = null;

		for (Object o : conds)
		{
			if (o == null)
			{
				continue;
			}
			else if (o instanceof String)
			{
				logic = (String) o;
			}
			else if (logic != null)
			{
				if (o instanceof Condition)
				{
					d = (Condition) o;
				}
				else if (o instanceof List)
				{
					d = restoreConditions(p, (List<Object>) o);
				}
				else
				{
					d = null;
				}

				if (d != null)
				{
					if (c != null)
					{
						if (LogicalCondition.AND.equalsIgnoreCase(logic))
						{
							c = ((ComposableCondition) c).and(d);
						}
						else if (LogicalCondition.OR.equalsIgnoreCase(logic))
						{
							c = ((ComposableCondition) c).or(d);
						}
					}
					else if (LogicalCondition.NOT.equalsIgnoreCase(logic))
					{
						c = p.provideLogicalCondition().not(d);
					}
					else
					{
						c = d;
					}
				}

				logic = null;
			}
			else if (c == null)
			{
				c = (ComposableCondition) o;
			}
		}

		return c;
	}
}
