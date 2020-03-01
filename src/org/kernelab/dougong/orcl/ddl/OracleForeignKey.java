package org.kernelab.dougong.orcl.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.semi.ddl.AbstractForeignKey;

public class OracleForeignKey extends AbstractForeignKey
{
	public OracleForeignKey(PrimaryKey reference, View view, Column[] columns)
	{
		super(reference, view, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
