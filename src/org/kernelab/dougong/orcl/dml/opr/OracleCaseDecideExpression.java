package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseDecideExpression;

public class OracleCaseDecideExpression extends AbstractCaseDecideExpression
{
	private Provider	provider;

	public OracleCaseDecideExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}

	@Override
	protected OracleCaseDecideExpression replicate()
	{
		return new OracleCaseDecideExpression(this.provider());
	}
}
