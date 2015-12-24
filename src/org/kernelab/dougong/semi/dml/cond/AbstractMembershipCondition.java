package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public abstract class AbstractMembershipCondition extends AbstractNegatableCondition implements MembershipCondition
{
	protected Expression	expr;

	protected Scope			scope;

	public AbstractMembershipCondition in(Expression expr, Scope scope)
	{
		return this.set(expr, scope);
	}

	public AbstractMembershipCondition set(Expression expr, Scope scope)
	{
		this.expr = expr;
		this.scope = scope;
		return this;
	}
}
