package org.kernelab.dougong.semi.ddl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.basis.WrappedLinkedHashMap;
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

	private Entity					entity;

	private Column[]				columns;

	private Map<Column, Integer>	columnMap	= null;

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
		Map<Column, Integer> m = this.getColumnMap();

		for (Column c : columns)
		{
			if (!m.containsKey(c))
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
		Set<Column> cols = new WrappedLinkedHashSet<Column>(MetaContext.COLUMN_BY_CLASS);
		cols.addAll(this.getColumnMap().keySet());
		for (Column c : excludes)
		{
			cols.remove(c);
		}
		return cols.toArray(new Column[0]);
	}

	protected Map<Column, Integer> getColumnMap()
	{
		if (columnMap == null)
		{
			columnMap = new WrappedLinkedHashMap<Column, Integer>(MetaContext.COLUMN_BY_CLASS);
			int i = 0;
			for (Column c : this.columns())
			{
				columnMap.put(c, i++);
			}
		}
		return columnMap;
	}

	@Override
	public Column[] getColumns(int... indexes)
	{
		Column[] cols = new Column[indexes.length];

		int idx = -1;
		for (int i = 0; i < indexes.length; i++)
		{
			if ((idx = indexes[i]) >= 0)
			{
				cols[i] = this.columns()[idx];
			}
		}

		return cols;
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
			try
			{
				cols[i] = Tools.access(entity, defs[i].getMetaName(), null);
			}
			catch (Exception e)
			{
				cols[i] = (Column) entity.item(defs[i].name());
			}
		}
		return cols;
	}

	@Override
	public boolean has(Column column)
	{
		return this.getColumnMap().containsKey(column);
	}

	@Override
	public int hashCode()
	{
		return hashCode(this);
	}

	@Override
	public int[] indexesOf(Column... columns)
	{
		int[] arr = new int[columns.length];

		int i = 0;
		for (Column c : columns)
		{
			arr[i++] = indexOf(c);
		}

		return arr;
	}

	@Override
	public int indexOf(Column column)
	{
		if (this.entity().getClass() != column.view().getClass())
		{
			return -2;
		}

		Integer index = this.getColumnMap().get(column);

		return index != null ? index : -1;
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
