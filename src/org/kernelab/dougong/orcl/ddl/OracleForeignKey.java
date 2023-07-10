package org.kernelab.dougong.orcl.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.semi.ddl.AbstractForeignKey;

public class OracleForeignKey extends AbstractForeignKey
{
	public OracleForeignKey(PrimaryKey reference, Entity entity, Column[] columns)
	{
		super(reference, entity, columns);
	}
}
