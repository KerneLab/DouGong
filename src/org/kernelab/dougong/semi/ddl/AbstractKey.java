package org.kernelab.dougong.semi.ddl;

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
	private Entity		entity;

	private Column[]	columns;

	public AbstractKey(Entity entity, Column... columns)
	{
		this.entity = entity;
		this.columns = columns;
	}

	public Column[] columns()
	{
		return columns;
	}

	public Entity entity()
	{
		return entity;
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
