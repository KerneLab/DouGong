package org.kernelab.dougong.semi.dml.param;

import java.sql.Date;

import org.kernelab.dougong.core.dml.param.DateParam;

public class AbstractDateParam extends AbstractParam<Date> implements DateParam
{
	public AbstractDateParam(String name, Date value)
	{
		super(name, value);
	}

	@Override
	protected AbstractDateParam replicate()
	{
		return this.provider().provideProvider(new AbstractDateParam(name(), value()));
	}

	@Override
	public AbstractDateParam set(Date value)
	{
		return this.replicate().value(value);
	}
}
