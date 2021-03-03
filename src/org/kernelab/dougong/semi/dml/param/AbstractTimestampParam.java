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
	protected AbstractParam<Timestamp> replicate()
	{
		return new AbstractTimestampParam(this.name(), this.value());
	}
}
