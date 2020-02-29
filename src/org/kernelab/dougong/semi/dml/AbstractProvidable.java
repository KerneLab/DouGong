package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.AbstractCastable;

public class AbstractProvidable extends AbstractCastable implements Providable
{
	private Provider provider = null;

	public Provider provider()
	{
		return provider;
	}

	public AbstractProvidable provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}
}
