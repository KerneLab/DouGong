package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.StringParam;

public class AbstractStringParam extends AbstractParam<String> implements StringParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	protected AbstractStringParam replicate()
	{
		return this.provider().provideProvider(new AbstractStringParam(name(), value()));
	}

	@Override
	public AbstractStringParam set(String value)
	{
		return this.replicate().value(value);
	}
}
