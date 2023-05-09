package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.AbstractCastable;

public class AbstractOperator extends AbstractCastable implements Providable
{
	private Provider provider;

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractOperator provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}
}
