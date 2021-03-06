package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Expression;

public interface MembershipCondition extends ComposableCondition, NegatableCondition, BinaryCondition
{
	public MembershipCondition in(Expression expr, Scope scope);
}
