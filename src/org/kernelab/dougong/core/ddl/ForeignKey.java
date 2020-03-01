package org.kernelab.dougong.core.ddl;

import org.kernelab.dougong.core.dml.Condition;

public interface ForeignKey extends DDL, Key
{
	public Condition joinCondition();

	public PrimaryKey reference();
}
