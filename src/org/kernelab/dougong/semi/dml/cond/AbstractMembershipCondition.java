package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public abstract class AbstractMembershipCondition extends AbstractNegatableCondition implements MembershipCondition
{
	protected Item	item;

	protected Scope	scope;

	public AbstractMembershipCondition in(Item item, Scope scope)
	{
		return this.set(item, scope);
	}

	public AbstractMembershipCondition set(Item item, Scope scope)
	{
		this.item = item;
		this.scope = scope;
		return this;
	}
}
