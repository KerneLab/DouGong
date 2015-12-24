package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;

public interface MembershipCondition extends ComposableCondition, NegatableCondition
{
	public MembershipCondition in(Expression expr, Scope scope);
}
