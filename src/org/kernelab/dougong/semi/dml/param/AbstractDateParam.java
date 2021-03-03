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
	protected AbstractParam<Date> replicate()
	{
		return new AbstractDateParam(this.name(), this.value());
	}
}
