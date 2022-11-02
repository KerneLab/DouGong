package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;

public abstract class AbstractRegexpLikeCondition extends AbstractNegatableCondition implements RegexpLikeCondition
{
	protected Expression	expr;

	protected Expression	pattern;

	@Override
	public Expression operand(int pos)
	{
		switch (pos)
		{
			case 0:
				return this.expr;
			case 1:
				return this.pattern;
			default:
				return null;
		}
	}

	@Override
	public AbstractRegexpLikeCondition operand(int pos, Expression opr)
	{
		switch (pos)
		{
			case 0:
				this.expr = opr;
				break;
			case 1:
				this.pattern = opr;
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

	@Override
	public AbstractRegexpLikeCondition rLike(Expression expr, Expression pattern)
	{
		return this.set(expr, pattern);
	}

	public AbstractRegexpLikeCondition set(Expression expr, Expression pattern)
	{
		this.expr = expr;
		this.pattern = pattern;
		return this;
	}
}
