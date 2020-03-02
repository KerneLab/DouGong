package org.kernelab.dougong.maria.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.semi.ddl.AbstractPrimaryKey;

public class MariaPrimaryKey extends AbstractPrimaryKey
{
	public MariaPrimaryKey(Entity entity, Column[] columns)
	{
		super(entity, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return buffer;
	}
}
