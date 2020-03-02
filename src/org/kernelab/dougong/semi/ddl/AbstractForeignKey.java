package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;

public abstract class AbstractForeignKey extends AbstractKey implements ForeignKey
{
	private PrimaryKey reference;

	public AbstractForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		super(entity, columns);
		this.reference = reference;
	}

	public ComposableCondition joinCondition()
	{
		Column[] columns = this.columns();

		Column[] refers = this.reference().columns();

		ComposableCondition c = null;

		for (int i = 0; i < columns.length; i++)
		{
			if (c == null)
			{
				c = columns[i].eq(refers[i]);
			}
			else
			{
				c = c.and(columns[i].eq(refers[i]));
			}
		}

		return c;
	}

	public PrimaryKey reference()
	{
		return reference;
	}
}
