package org.kernelab.dougong.semi.dml;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;

public abstract class PredeclaredView extends AbstractSubquery
{
	public PredeclaredView()
	{
		super();
	}

	public abstract JSON parameters();

	@Override
	public Select select()
	{
		return this.select(this.provider().provideSQL());
	}

	protected abstract Select select(SQL sql);
}
