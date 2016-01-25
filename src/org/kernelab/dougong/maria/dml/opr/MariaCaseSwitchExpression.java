package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseSwitchExpression;

public class MariaCaseSwitchExpression extends AbstractCaseSwitchExpression
{
	private Provider	provider;

	public MariaCaseSwitchExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}

	@Override
	protected MariaCaseSwitchExpression replicate()
	{
		return new MariaCaseSwitchExpression(this.provider());
	}
}
