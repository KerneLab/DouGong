package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.ByteParam;

public class AbstractByteParam extends AbstractParam<Byte> implements ByteParam
{
	public AbstractByteParam(String name, Byte value)
	{
		super(name, value);
	}

	@Override
	protected AbstractParam<Byte> replicate()
	{
		return new AbstractByteParam(this.name(), this.value());
	}
}
