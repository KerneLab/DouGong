package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.NullCondition;

public abstract class AbstractNullCondition extends AbstractNegatableCondition implements NullCondition
{
	protected Expression expr;

	@Override
	public Expression operand(int pos)
	{
		return pos == 0 ? this.expr : null;
	}

	@Override
	public AbstractNullCondition operand(int pos, Expression opr)
	{
		if (pos == 0)
		{
			this.expr = opr;
		}
		return this;
	}

	@Override
	public int operands()
	{
		return 1;
	}

	public AbstractNullCondition isNull(Expression expr)
	{
		this.expr = expr;
		return this;
	}
}
