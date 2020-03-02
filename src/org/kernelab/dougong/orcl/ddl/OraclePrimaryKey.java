package org.kernelab.dougong.orcl.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.semi.ddl.AbstractPrimaryKey;

public class OraclePrimaryKey extends AbstractPrimaryKey
{
	public OraclePrimaryKey(Entity entity, Column[] columns)
	{
		super(entity, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return buffer;
	}
}
