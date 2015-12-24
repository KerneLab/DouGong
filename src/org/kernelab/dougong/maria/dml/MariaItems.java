package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.semi.dml.AbstractItems;

public class MariaItems extends AbstractItems
{
	private Provider	provider;

	public MariaItems(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider.provideMembershipCondition();
	}
}
