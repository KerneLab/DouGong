package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.PrimaryKey;

public abstract class AbstractPrimaryKey extends AbstractKey implements PrimaryKey
{
	public AbstractPrimaryKey(View view, Column... columns)
	{
		super(view, columns);
	}
}
