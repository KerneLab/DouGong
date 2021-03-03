package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.FloatParam;

public class AbstractFloatParam extends AbstractParam<Float> implements FloatParam
{
	public AbstractFloatParam(String name, Float value)
	{
		super(name, value);
	}

	@Override
	protected AbstractParam<Float> replicate()
	{
		return new AbstractFloatParam(this.name(), this.value());
	}
}
