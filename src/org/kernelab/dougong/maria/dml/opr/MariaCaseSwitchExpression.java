package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseSwitchExpression;

public class MariaCaseSwitchExpression extends AbstractCaseSwitchExpression
{
	private Provider provider;

	public MariaCaseSwitchExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MariaCaseSwitchExpression newInstance()
	{
		return new MariaCaseSwitchExpression(this.provider());
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}
}
