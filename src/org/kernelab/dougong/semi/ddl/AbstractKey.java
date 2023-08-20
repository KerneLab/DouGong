package org.kernelab.dougong.semi.ddl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.basis.WrappedLinkedHashSet;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.meta.MetaContext;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractColumn;
import org.kernelab.dougong.semi.AbstractProvidable;

public abstract class AbstractKey extends AbstractProvidable implements Key
{
	public static boolean equals(Key a, Key b)
	{
		if (a.entity() == null || b.entity() == null)
		{
			return false;
		}
		else if (a.entity().getClass() != b.entity().getClass())
		{
			return false;
		}

		if (!AbstractColumn.equals(a.columns(), b.columns()))
		{
			return false;
		}

		return true;
	}

	public static int hashCode(Entity entity, Column[] columns)
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (entity == null ? 0 : entity.getClass().hashCode());
		result = prime * result + AbstractColumn.hashCode(columns);
		return result;
	}

	public static int hashCode(Key key)
	{
		if (key == null)
		{
			return 0;
		}
		else
		{
			return hashCode(key.entity(), key.columns());
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
		Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass(), null);

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

	/**
	 * Map the keys in the map to the target columns according to the source
	 * columns.
	 * 
	 * @param map
	 * @param source
	 * @param target
	 * @return
	 */
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

	private Entity		entity;

	private Column[]	columns;

	private Set<Column>	columnSet	= null;

	public AbstractKey(Entity entity, Column... columns)
	{
		this.entity = entity;
		this.columns = columns;
	}

	@Override
	public Column[] columns()
	{
		return columns;
	}

	@Override
	public boolean containsAll(Column... columns)
	{
		Set<Column> s = this.getColumnSet();

		for (Column c : columns)
		{
			if (!s.contains(c))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public Entity entity()
	{
		return entity;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj == null)
		{
			return false;
		}

		if (!(obj instanceof Key))
		{
			return false;
		}

		return equals(this, (Key) obj);
	}

	@Override
	public Column[] excludeColumns(Column... excludes)
	{
		if (excludes == null)
		{
			return null;
		}
		if (excludes.length == 0)
		{
			return this.columns();
		}
		Set<Column> cols = this.getColumnSet();
		for (Column c : excludes)
		{
			cols.remove(c);
		}
		return cols.toArray(new Column[0]);
	}

	protected Set<Column> getColumnSet()
	{
		if (columnSet == null)
		{
			columnSet = Tools.setOfArray(new WrappedLinkedHashSet<Column>(MetaContext.COLUMN_EQUAL), this.columns());
		}
		return columnSet;
	}

	@Override
	public Column[] getColumnsOf(Entity entity)
	{
		if (entity().getClass() != entity.getClass())
		{
			return null;
		}

		Column[] defs = columns();
		Column[] cols = new Column[defs.length];
		for (int i = 0; i < defs.length; i++)
		{
			cols[i] = (Column) entity.item(defs[i].name());
		}
		return cols;
	}

	@Override
	public int hashCode()
	{
		return hashCode(this);
	}

	protected Condition queryCondition(Column... columns)
	{
		ComposableCondition c = null;

		Expression param = null;

		for (Column column : columns)
		{
			param = provider().provideParameter(Utils.getDataLabelFromField(column.field()));

			if (c == null)
			{
				c = column.eq(param);
			}
			else
			{
				c = c.and(column.eq(param));
			}
		}

		return c;
	}
}
