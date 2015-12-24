package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public interface Items extends Expression, Scope, Alias
{
	public MembershipCondition in(Scope scope);

	public Items list(Expression... items);

	public MembershipCondition notIn(Scope scope);
}
