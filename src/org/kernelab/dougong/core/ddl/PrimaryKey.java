package org.kernelab.dougong.core.ddl;

import org.kernelab.dougong.core.dml.Condition;

public interface PrimaryKey extends DDL, Key
{
	public Condition queryCondition();
}
