package org.kernelab.dougong.semi.dml.param;

import java.math.BigDecimal;

import org.kernelab.dougong.core.dml.param.DecimalParam;

public class AbstractDecimalParam extends AbstractParam<BigDecimal> implements DecimalParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractDecimalParam(String name, BigDecimal value)
	{
		super(name, value);
	}

	@Override
	protected AbstractDecimalParam replicate()
	{
		return this.provider().provideProvider(new AbstractDecimalParam(name(), value()));
	}

	@Override
	public AbstractDecimalParam set(BigDecimal value)
	{
		return this.replicate().value(value);
	}
}
