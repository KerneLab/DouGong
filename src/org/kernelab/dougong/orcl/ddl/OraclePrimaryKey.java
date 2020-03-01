package org.kernelab.dougong.orcl.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.ddl.AbstractPrimaryKey;

public class OraclePrimaryKey extends AbstractPrimaryKey
{
	public OraclePrimaryKey(View view, Column[] columns)
	{
		super(view, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return buffer;
	}
}
