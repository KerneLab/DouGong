package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractKey extends AbstractProvidable implements Key
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
}
