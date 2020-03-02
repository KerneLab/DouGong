package org.kernelab.dougong.maria.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.semi.ddl.AbstractForeignKey;

public class MariaForeignKey extends AbstractForeignKey
{
	public MariaForeignKey(PrimaryKey reference, Entity entity, Column[] columns)
	{
		super(reference, entity, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
