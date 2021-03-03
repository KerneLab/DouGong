package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.StringParam;

public class AbstractStringParam extends AbstractParam<String> implements StringParam
{
	public AbstractStringParam(String name, String value)
	{
		super(name, value);
	}

	@Override
	public boolean given()
	{
		return super.given() && this.value().length() > 0;
	}

	@Override
	protected AbstractParam<String> replicate()
	{
		return this.provider().provideProvider(new AbstractStringParam(name(), value()));
	}
}
