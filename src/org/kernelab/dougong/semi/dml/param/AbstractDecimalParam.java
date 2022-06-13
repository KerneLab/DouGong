package org.kernelab.dougong.semi.dml.param;

import java.math.BigDecimal;

import org.kernelab.dougong.core.dml.param.DecimalParam;

public class AbstractDecimalParam extends AbstractParam<BigDecimal> implements DecimalParam
{
	public AbstractDecimalParam(String name, BigDecimal value)
	{
		super(name, value);
	}

	@Override
	protected AbstractDecimalParam replicate()
	{
		return this.provider().provideProvider(new AbstractDecimalParam(name(), value()));
	}
}
