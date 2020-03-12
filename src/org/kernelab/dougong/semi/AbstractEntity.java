package org.kernelab.dougong.semi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractEntity extends AbstractView implements Entity
{
	public static ForeignKey findForeignKeyBetweenEntitys(String name, Entity a, Entity b)
	{
		try
		{
			Method method = a.getClass().getDeclaredMethod(name, b.getClass());
			return (ForeignKey) method.invoke(a, b);
		}
		catch (Exception e)
		{
			try
			{
				Method method = b.getClass().getDeclaredMethod(name, a.getClass());
				return (ForeignKey) method.invoke(b, a);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static Set<Column> getColumns(Entity entity)
	{
		Set<Column> columns = new HashSet<Column>();

		for (Field field : entity.getClass().getDeclaredFields())
		{
			if (isColumn(field))
			{
				try
				{
					columns.add((Column) Tools.access(entity, field));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return columns;
	}

	public static Column[] getColumnsArray(Entity entity)
	{
		Set<Column> columns = getColumns(entity);
		return columns.toArray(new Column[columns.size()]);
	}

	public static boolean isColumn(Field field)
	{
		int mod = field.getModifiers();

		try
		{
			if (Tools.isSubClass(field.getType(), Column.class) //
					&& Modifier.isPublic(mod) //
					&& !Modifier.isStatic(mod) //
					&& !Modifier.isFinal(mod) //
			)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isForeignKey(Method method)
	{
		int mod = method.getModifiers();

		try
		{
			if (Tools.isSubClass(method.getReturnType(), ForeignKey.class) //
					&& Modifier.isPublic(mod) //
					&& !Modifier.isStatic(mod) //
					&& method.getAnnotation(ForeignKeyMeta.class) != null //
					&& method.getParameterTypes().length == 1 //
			) //
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isForeignKey(Method method, Entity entity)
	{
		if (isForeignKey(method))
		{
			return method.getParameterTypes()[0].isInstance(entity);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Get a map which contains columns against corresponding values of in the
	 * object.
	 * 
	 * @param object
	 * @param columns
	 * @return
	 */
	public static Map<Column, Object> mapObjectValuesOfColumns(Object object, Column... columns)
	{
		Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass());

		Map<Column, Object> map = new HashMap<Column, Object>();

		for (Column column : columns)
		{
			try
			{
				map.put(column, Tools.access(object, fields.get(Utils.getDataLabelFromField(column.field()))));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return map;
	}

	public static Map<Column, Object> mapSourceToTargetColumns(Map<Column, Object> map, Column[] source,
			Column[] target)
	{
		Map<Column, Object> res = new HashMap<Column, Object>();
		for (int i = 0; i < target.length; i++)
		{
			res.put(target[i], map.get(source[i]));
		}
		return res;
	}

	public static <T> Map<Column, Object> mapValuesToReference(T object, ForeignKey key)
	{
		if (object.getClass().getAnnotation(EntityMeta.class).entity().isInstance(key.reference().entity()))
		{ // object is related to referrer entity
			return mapObjectValuesOfColumns(object, key.reference().columns());
		}
		else
		{ // object is related to reference entity
			return mapSourceToTargetColumns(mapObjectValuesOfColumns(object, key.columns()), key.columns(),
					key.reference().columns());
		}
	}

	public static <T> Map<Column, Object> mapValuesToReferrer(T object, ForeignKey key)
	{
		if (object.getClass().getAnnotation(EntityMeta.class).entity().isInstance(key.entity()))
		{ // object is related to referrer entity
			return mapObjectValuesOfColumns(object, key.columns());
		}
		else
		{ // object is related to reference entity
			return mapSourceToTargetColumns(mapObjectValuesOfColumns(object, key.reference().columns()),
					key.reference().columns(), key.columns());
		}
	}

	protected ForeignKey foreignKey(Entity ref, Column... columns)
	{
		PrimaryKey pk = ref.primaryKey();

		if (pk == null)
		{
			return null;
		}
		else
		{
			return this.provider().provideForeignKey(pk, this, columns);
		}
	}

	protected ForeignKey foreignKey(String name, Entity ref)
	{
		try
		{
			return (ForeignKey) this.getClass().getDeclaredMethod(name, ref.getClass()).invoke(this, ref);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	protected Column getColumnByField(Field field)
	{
		String name = Utils.getNameFromField(field);

		Column column = Tools.as(itemsMap().get(name), Column.class);

		if (column == null)
		{
			column = provider().provideColumn(this, name, field);
		}

		return column;
	}

	protected List<Field> getColumnFields()
	{
		List<Field> fields = new LinkedList<Field>();

		for (Field field : this.getClass().getFields())
		{
			if (isColumn(field))
			{
				fields.add(field);
			}
		}

		return fields;
	}

	protected void initColumns()
	{
		Column col = null;

		for (Field field : this.getColumnFields())
		{
			try
			{
				if (field.get(this) == null)
				{
					col = this.getColumnByField(field);
					field.set(this, col);
					items().add(col);
					itemsMap().put(col.name(), col);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public <T> Map<Column, Object> mapValuesToReference(T object, String foreignKey, Entity that)
	{
		return mapValuesToReference(object, findForeignKeyBetweenEntitys(foreignKey, this, that));
	}

	public <T> Map<Column, Object> mapValuesToReferrer(T object, String foreignKey, Entity that)
	{
		return mapValuesToReferrer(object, findForeignKeyBetweenEntitys(foreignKey, this, that));
	}

	public PrimaryKey primaryKey()
	{
		TreeMap<Integer, Column> keys = new TreeMap<Integer, Column>();

		for (Field field : this.getColumnFields())
		{
			PrimaryKeyMeta meta = field.getAnnotation(PrimaryKeyMeta.class);
			if (meta != null)
			{
				keys.put(meta.ordinal(), this.getColumnByField(field));
			}
		}

		if (keys.isEmpty())
		{
			return null;
		}
		else
		{
			return provider().providePrimaryKey(this, keys.values().toArray(new Column[keys.size()]));
		}
	}

	@Override
	public AbstractView provider(Provider provider)
	{
		super.provider(provider);
		this.initColumns();
		return this;
	}
}
