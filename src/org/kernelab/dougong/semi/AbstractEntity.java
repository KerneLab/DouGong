package org.kernelab.dougong.semi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractEntity extends AbstractView implements Entity
{
	public static ForeignKey findForeignKey(Entity entity, String foreignKey, Entity reference)
	{
		try
		{
			return (ForeignKey) entity.getClass().getDeclaredMethod(foreignKey, reference.getClass()).invoke(entity,
					reference);
		}
		catch (Exception e)
		{
			return null;
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

	public ForeignKey foreignKey(String name, Entity reference)
	{
		return findForeignKey(this, name, reference);
	}

	protected Column getColumnByField(Field field)
	{
		String name = Utils.getNameFromField(field);

		Column column = Tools.as(referItems().get(name), Column.class);

		if (column == null)
		{
			column = provider().provideColumn(this, name, field);
		}

		return column;
	}

	public Map<Column, Expression> getColumnDefaultExpressions()
	{
		Map<Column, Expression> meta = new LinkedHashMap<Column, Expression>();

		SQL sql = this.provider().provideSQL();

		for (Field field : this.getColumnFields())
		{
			Expression value = Utils.getDataExpressionFromField(sql, field);

			if (value != null)
			{
				meta.put(this.getColumnByField(field), value);
			}
		}

		return meta;
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
					referItems().put(col.name(), col);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
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
