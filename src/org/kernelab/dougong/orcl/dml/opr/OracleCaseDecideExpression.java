package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractSortable;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseDecideExpression;

public class OracleCaseDecideExpression extends AbstractCaseDecideExpression
{
	private Provider provider;

	public OracleCaseDecideExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected AbstractSortable newInstance()
	{
		return new OracleCaseDecideExpression(this.provider());
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}
}
