package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.StringParam;

public class AbstractStringParam extends AbstractParam<String> implements StringParam
{
	public AbstractStringParam(String name, String value)
	{
		super(name, value);
	}

	@Override
	protected AbstractParam<String> replicate()
	{
		return new AbstractStringParam(this.name(), this.value());
	}
}
