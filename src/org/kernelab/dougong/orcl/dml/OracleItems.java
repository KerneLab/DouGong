package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.semi.dml.AbstractItems;

public class OracleItems extends AbstractItems
{
	private Provider	provider;

	public OracleItems(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider.provideMembershipCondition();
	}
}
