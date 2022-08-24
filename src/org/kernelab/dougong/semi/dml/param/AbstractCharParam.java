package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.CharParam;

public class AbstractCharParam extends AbstractParam<Character> implements CharParam
{
	public AbstractCharParam(String name, Character value)
	{
		super(name, value);
	}

	@Override
	protected AbstractCharParam replicate()
	{
		return this.provider().provideProvider(new AbstractCharParam(name(), value()));
	}

	@Override
	public AbstractCharParam set(Character value)
	{
		return this.replicate().value(value);
	}
}
