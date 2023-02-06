package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.LongParam;

public class AbstractLongParam extends AbstractParam<Long> implements LongParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractLongParam(String name, Long value)
	{
		super(name, value);
	}

	@Override
	protected AbstractLongParam replicate()
	{
		return this.provider().provideProvider(new AbstractLongParam(name(), value()));
	}

	@Override
	public AbstractLongParam set(Long value)
	{
		return this.replicate().value(value);
	}
}
