package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.ShortParam;

public class AbstractShortParam extends AbstractParam<Short> implements ShortParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractShortParam(String name, Short value)
	{
		super(name, value);
	}

	@Override
	protected AbstractShortParam replicate()
	{
		return this.provider().provideProvider(new AbstractShortParam(name(), value()));
	}

	@Override
	public AbstractShortParam set(Short value)
	{
		return this.replicate().value(value);
	}
}
