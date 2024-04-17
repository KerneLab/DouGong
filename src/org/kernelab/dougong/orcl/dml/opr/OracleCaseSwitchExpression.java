package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractSortable;
import org.kernelab.dougong.semi.dml.opr.AbstractCaseSwitchExpression;

public class OracleCaseSwitchExpression extends AbstractCaseSwitchExpression
{
	private Provider provider;

	public OracleCaseSwitchExpression(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected AbstractSortable newInstance()
	{
		return new OracleCaseSwitchExpression(this.provider());
	}

	@Override
	protected Provider provider()
	{
		return provider;
	}
}
