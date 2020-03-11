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
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.Company;
import org.kernelab.dougong.demo.Config;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.Department;

public abstract class AbstractEntity extends AbstractView implements Entity
{
	public static ForeignKey findForeignKeyBetweenEntitys(Entity a, Entity b)
	{
		for (Method method : a.getClass().getDeclaredMethods())
		{
			if (isForeignKey(method, b))
			{
				try
				{
					return (ForeignKey) method.invoke(a, b);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		for (Method method : b.getClass().getDeclaredMethods())
		{
			if (isForeignKey(method, a))
			{
				try
				{
					return (ForeignKey) method.invoke(b, a);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static ForeignKey findForeignKeyBetweenEntitys(String name, Entity a, Entity b)
	{
		if (Tools.notNullOrEmpty(name))
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
		else
		{
			return findForeignKeyBetweenEntitys(a, b);
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

	public static void main(String[] args)
	{
		// TODO
		Company compObj = new Company();
		compObj.setId("1");
		compObj.setName("Cm.1");

		Department deptObj = new Department();
		deptObj.setCompId("1");
		deptObj.setId("a");
		deptObj.setName("Dep.A");

		DEPT dep = Config.SQL.view(DEPT.class);
		COMP com = Config.SQL.view(COMP.class);

		Tools.debug(dep.mapValues(compObj, dep.FRN_DEPT(com)));
	}

	/**
	 * Get a map which contains columns against corresponding values of in the
	 * object.
	 * 
	 * @param object
	 * @param columns
	 * @return
	 */
	public static Map<Column, Object> mapObjectValuesToColumns(Object object, Column... columns)
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

	public static Map<Column, Object> mapObjectValuesToColumns(Provider provider, Object object, Entity entity,
			String foreignKey, Column... columns)
	{
		EntityMeta entityMeta = object.getClass().getAnnotation(EntityMeta.class);

		if (entityMeta.entity().isInstance(entity))
		{ // The given entity is related to the object
			return mapObjectValuesToColumns(object, columns);
		}
		else
		{
			Entity mate = provider.provideView(entityMeta.entity());

			ForeignKey key = findForeignKeyBetweenEntitys(foreignKey, entity, mate);

			if (key.entity() == entity)
			{
				return mapValuesToColumns(mapObjectValuesToColumns(object, key.reference().columns()),
						key.reference().columns(), key.columns());
			}
			else
			{
				return mapValuesToColumns(mapObjectValuesToColumns(object, key.columns()), key.columns(),
						key.reference().columns());
			}
		}
	}

	public static Map<Column, Object> mapValuesToColumns(Map<Column, Object> map, Column[] source, Column[] target)
	{
		Map<Column, Object> res = new HashMap<Column, Object>();
		for (int i = 0; i < target.length; i++)
		{
			res.put(target[i], map.get(source[i]));
		}
		return res;
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

	public Map<Column, Object> mapValues(Object object)
	{
		return mapValues(object, "");
	}

	public Map<Column, Object> mapValues(Object object, Column... columns)
	{
		return mapObjectValuesToColumns(provider(), object, this, "", columns);
	}

	public Map<Column, Object> mapValues(Object object, Key key)
	{
		return mapValues(object, key.columns());
	}

	public Map<Column, Object> mapValues(Object object, String foreignKey)
	{
		Class<? extends Entity> entityClass = object.getClass().getAnnotation(EntityMeta.class).entity();

		if (entityClass.isInstance(this))
		{
			return mapObjectValuesToColumns(object, getColumnsArray(this));
		}
		else
		{
			return mapValues(object,
					findForeignKeyBetweenEntitys(foreignKey, this, provider().provideView(entityClass)));
		}
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
