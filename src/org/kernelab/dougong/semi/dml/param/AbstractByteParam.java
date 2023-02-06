package org.kernelab.dougong.semi.dml.param;

import org.kernelab.dougong.core.dml.param.ByteParam;

public class AbstractByteParam extends AbstractParam<Byte> implements ByteParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractByteParam(String name, Byte value)
	{
		super(name, value);
	}

	@Override
	protected AbstractByteParam replicate()
	{
		return this.provider().provideProvider(new AbstractByteParam(name(), value()));
	}

	@Override
	public AbstractByteParam set(Byte value)
	{
		return this.replicate().value(value);
	}
}
