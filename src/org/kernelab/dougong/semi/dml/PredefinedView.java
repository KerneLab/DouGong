package org.kernelab.dougong.semi.dml;

import java.util.Map;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;

public abstract class PredefinedView extends AbstractSubquery
{
	public PredefinedView()
	{
		super();
	}

	public abstract Map<String, Object> parameters();

	@Override
	public Select select()
	{
		return this.select(this.provider().provideSQL());
	}

	protected abstract Select select(SQL sql);
}
