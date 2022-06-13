package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.LongParam;

public class AbstractLongParam extends AbstractParam<Long> implements LongParam
{
	public AbstractLongParam(String name, Long value)
	{
		super(name, value);
	}

	@Override
	protected AbstractLongParam replicate()
	{
		return this.provider().provideProvider(new AbstractLongParam(name(), value()));
	}
}
