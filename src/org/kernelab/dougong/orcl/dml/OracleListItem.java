package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.semi.dml.AbstractListItem;

public class OracleListItem extends AbstractListItem
{
	private Provider	provider;

	public OracleListItem(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider.provideMembershipCondition();
	}
}
