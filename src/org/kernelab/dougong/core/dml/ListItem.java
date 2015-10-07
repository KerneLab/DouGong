package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public interface ListItem extends Item, Scope
{
	public MembershipCondition in(Scope scope);

	public ListItem list(Object... items);

	public MembershipCondition notIn(Scope scope);
}
