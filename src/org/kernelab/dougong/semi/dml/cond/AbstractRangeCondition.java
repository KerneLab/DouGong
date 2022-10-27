package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractRangeCondition extends AbstractNegatableCondition implements RangeCondition
{
	protected Expression	expr;

	protected Expression	from;

	protected Expression	to;

	@Override
	public AbstractRangeCondition between(Expression item, Expression from, Expression to)
	{
		this.expr = item;
		this.from = from;
		this.to = to;
		return this;
	}

	@Override
	public Expression operand(int pos)
	{
		switch (pos)
		{
			case 0:
				return this.expr;
			case 1:
				return this.from;
			case 2:
				return this.to;
			default:
				return null;
		}
	}

	@Override
	public AbstractRangeCondition operand(int pos, Expression opr)
	{
		switch (pos)
		{
			case 0:
				this.expr = opr;
				break;
			case 1:
				this.from = opr;
				break;
			case 2:
				this.to = opr;
				break;
			default:
				break;
		}
		return this;
	}

	@Override
	public int operands()
	{
		return 3;
	}
}
