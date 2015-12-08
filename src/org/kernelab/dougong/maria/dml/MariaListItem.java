package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.semi.dml.AbstractListItem;

public class MariaListItem extends AbstractListItem
{
	private Provider	provider;

	public MariaListItem(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider.provideMembershipCondition();
	}
}
