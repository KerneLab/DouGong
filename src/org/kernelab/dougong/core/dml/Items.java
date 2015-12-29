package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.test.EqualityTestable;
import org.kernelab.dougong.core.dml.test.MembershipTestable;

public interface Items extends Expression, Scope, Alias, EqualityTestable, MembershipTestable
{
	public Items list(Expression... items);
}
