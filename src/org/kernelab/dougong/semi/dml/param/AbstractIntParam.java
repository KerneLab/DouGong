package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.IntParam;

public class AbstractIntParam extends AbstractParam<Integer> implements IntParam
{
	public AbstractIntParam(String name, Integer value)
	{
		super(name, value);
	}

	@Override
	protected AbstractIntParam replicate()
	{
		return this.provider().provideProvider(new AbstractIntParam(name(), value()));
	}
}
