package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractMembershipCondition extends AbstractNegatableCondition implements MembershipCondition
{
	protected Expression	expr;

	protected Scope			scope;

	public Expression $_1()
	{
		return expr;
	}

	public Expression $_2()
	{
		return scope;
	}

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

	public StringBuilder toString(StringBuilder buffer)
	{
		Utils.outputExprInScope(buffer, this.expr);
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" IN (");
		this.scope.toStringScoped(buffer);
		return buffer.append(')');
	}
}
