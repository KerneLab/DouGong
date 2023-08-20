package org.kernelab.dougong.semi.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Condition;

public abstract class AbstractPrimaryKey extends AbstractKey implements PrimaryKey
{
	public AbstractPrimaryKey(Entity entity, Column... columns)
	{
		super(entity, columns);
	}

	@Override
	public <T> Map<Column, Object> mapValues(T object)
	{
		return mapObjectValuesOfColumns(object, this.columns());
	}

	@Override
	public Condition queryCondition()
	{
		return queryCondition(columns());
	}
}
