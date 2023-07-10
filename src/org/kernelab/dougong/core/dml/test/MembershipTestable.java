package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public interface MembershipTestable
{
	public MembershipCondition in(Scope scope);

	@Deprecated
	public MembershipCondition notIn(Scope scope);
}
