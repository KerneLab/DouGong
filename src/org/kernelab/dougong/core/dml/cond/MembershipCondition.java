package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.Scope;

public interface MembershipCondition extends ComposableCondition, NegatableCondition
{
	public MembershipCondition in(Item item, Scope scope);
}
