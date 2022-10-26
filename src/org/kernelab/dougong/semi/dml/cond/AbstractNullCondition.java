package org.kernelab.dougong.semi.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.util.Utils;

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
	public List<Expression> operands()
	{
		return Utils.arrayList((Expression) this.expr);
	}

	public AbstractNullCondition isNull(Expression expr)
	{
		this.expr = expr;
		return this;
	}
}
