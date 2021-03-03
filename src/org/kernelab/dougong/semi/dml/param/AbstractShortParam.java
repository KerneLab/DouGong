package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.ShortParam;

public class AbstractShortParam extends AbstractParam<Short> implements ShortParam
{
	public AbstractShortParam(String name, Short value)
	{
		super(name, value);
	}

	@Override
	protected AbstractParam<Short> replicate()
	{
		return new AbstractShortParam(this.name(), this.value());
	}
}
