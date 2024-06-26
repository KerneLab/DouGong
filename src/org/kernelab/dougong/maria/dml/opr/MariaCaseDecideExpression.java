package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseDecideExpression;

public class MariaCaseDecideExpression extends AbstractCaseDecideExpression
{
	private Provider provider;

	public MariaCaseDecideExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MariaCaseDecideExpression newInstance()
	{
		return new MariaCaseDecideExpression(this.provider());
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}
}
