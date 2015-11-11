package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Filterable;

public abstract class AbstractFilterable extends AbstractProvidable implements Filterable
{
	protected Condition	where	= null;

	public Condition where()
	{
		return where;
	}

	public Filterable where(Condition cond)
	{
		this.where = cond;
		return this;
	}
}
