package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.PrimaryKey;

public abstract class AbstractPrimaryKey extends AbstractKey implements PrimaryKey
{
	public AbstractPrimaryKey(Entity entity, Column... columns)
	{
		super(entity, columns);
	}
}
