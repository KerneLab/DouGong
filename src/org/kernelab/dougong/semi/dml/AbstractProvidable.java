package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Provider;

public class AbstractProvidable
{
	private Provider	provider	= null;

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
