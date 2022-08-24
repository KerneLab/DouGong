package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.FloatParam;

public class AbstractFloatParam extends AbstractParam<Float> implements FloatParam
{
	public AbstractFloatParam(String name, Float value)
	{
		super(name, value);
	}

	@Override
	protected AbstractFloatParam replicate()
	{
		return this.provider().provideProvider(new AbstractFloatParam(name(), value()));
	}

	@Override
	public AbstractFloatParam set(Float value)
	{
		return this.replicate().value(value);
	}
}
