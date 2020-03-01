package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;

public abstract class AbstractForeignKey extends AbstractKey implements ForeignKey
{
	private PrimaryKey reference;

	public AbstractForeignKey(PrimaryKey reference, View view, Column... columns)
	{
		super(view, columns);
		this.reference = reference;
	}

	public PrimaryKey reference()
	{
		return reference;
	}
}
