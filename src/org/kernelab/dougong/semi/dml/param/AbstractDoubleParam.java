package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.DoubleParam;

public class AbstractDoubleParam extends AbstractParam<Double> implements DoubleParam
{
	public AbstractDoubleParam(String name, Double value)
	{
		super(name, value);
	}

	@Override
	protected AbstractParam<Double> replicate()
	{
		return new AbstractDoubleParam(this.name(), this.value());
	}
}
