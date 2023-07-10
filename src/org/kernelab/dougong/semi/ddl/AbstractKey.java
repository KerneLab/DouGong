package org.kernelab.dougong.semi.ddl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractProvidable;

public abstract class AbstractKey extends AbstractProvidable implements Key
{
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
	public boolean contains(Column... columns)
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

	protected Set<Column> getColumnSet()
	{
		if (columnSet == null)
		{
			columnSet = Tools.setOfArray(new HashSet<Column>(), this.columns());
		}
		return columnSet;
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
