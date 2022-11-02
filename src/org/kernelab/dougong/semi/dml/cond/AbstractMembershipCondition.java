package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.util.Utils;

public class AbstractMembershipCondition extends AbstractNegatableCondition implements MembershipCondition
{
	protected Expression	expr;

	protected Scope			scope;

	@Override
	public AbstractMembershipCondition in(Expression expr, Scope scope)
	{
		return this.set(expr, scope);
	}

	@Override
	public Expression operand(int pos)
	{
		switch (pos)
		{
			case 0:
				return this.expr;
			case 1:
				return this.scope;
			default:
				return null;
		}
	}

	@Override
	public AbstractMembershipCondition operand(int pos, Expression opr)
	{
		switch (pos)
		{
			case 0:
				this.expr = opr;
				break;
			case 1:
				this.scope = (Scope) opr;
				break;
			default:
				break;
		}
		return this;
	}

	@Override
	public int operands()
	{
		return 2;
	}

	public AbstractMembershipCondition set(Expression expr, Scope scope)
	{
		this.expr = expr;
		this.scope = scope;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		Utils.outputExprInScope(buffer, this.expr);
		if (this.not)
		{
			buffer.append(" " + LogicalCondition.NOT);
		}
		buffer.append(" IN (");
		this.scope.toStringScoped(buffer);
		return buffer.append(')');
	}
}
