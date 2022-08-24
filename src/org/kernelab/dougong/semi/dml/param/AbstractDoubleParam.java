package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.DoubleParam;

public class AbstractDoubleParam extends AbstractParam<Double> implements DoubleParam
{
	public AbstractDoubleParam(String name, Double value)
	{
		super(name, value);
	}

	@Override
	protected AbstractDoubleParam replicate()
	{
		return this.provider().provideProvider(new AbstractDoubleParam(name(), value()));
	}
	
	@Override
	public AbstractDoubleParam set(Double value)
	{
		return this.replicate().value(value);
	}
}
