package org.kernelab.dougong.core.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.dml.Condition;

public interface Key
{
	public Column[] columns();

	public Entity entity();

	public Condition queryCondition();
}