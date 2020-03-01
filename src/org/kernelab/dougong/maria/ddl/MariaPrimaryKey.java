package org.kernelab.dougong.maria.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.ddl.AbstractPrimaryKey;

public class MariaPrimaryKey extends AbstractPrimaryKey
{
	public MariaPrimaryKey(View view, Column[] columns)
	{
		super(view, columns);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		// TODO Auto-generated method stub
		return buffer;
	}
}
