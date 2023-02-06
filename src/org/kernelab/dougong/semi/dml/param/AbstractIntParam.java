package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.IntParam;

public class AbstractIntParam extends AbstractParam<Integer> implements IntParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractIntParam(String name, Integer value)
	{
		super(name, value);
	}

	@Override
	protected AbstractIntParam replicate()
	{
		return this.provider().provideProvider(new AbstractIntParam(name(), value()));
	}

	@Override
	public AbstractIntParam set(Integer value)
	{
		return this.replicate().value(value);
	}
}
