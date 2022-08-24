package org.kernelab.dougong.semi.dml.param;

import java.sql.Timestamp;

import org.kernelab.dougong.core.dml.param.TimestampParam;

public class AbstractTimestampParam extends AbstractParam<Timestamp> implements TimestampParam
{
	public AbstractTimestampParam(String name, Timestamp value)
	{
		super(name, value);
	}

	@Override
	protected AbstractTimestampParam replicate()
	{
		return this.provider().provideProvider(new AbstractTimestampParam(name(), value()));
	}

	@Override
	public AbstractTimestampParam set(Timestamp value)
	{
		return this.replicate().value(value);
	}
}
