package org.kernelab.dougong.semi.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractComparisonCondition extends AbstractComposableCondition implements ComparisonCondition
{
	protected Expression	leftExpr;

	protected Expression	rightExpr;

	protected String		compType;

	protected String		groupQual;

	@Override
	public AbstractComparisonCondition all()
	{
		this.groupQual = ALL;
		return this;
	}

	@Override
	public AbstractComparisonCondition any()
	{
		this.groupQual = ANY;
		return this;
	}

	@Override
	public AbstractComparisonCondition eq(Expression a, Expression b)
	{
		this.compType = EQUALS;
		return this.set(a, b);
	}

	@Override
	public AbstractComparisonCondition ge(Expression a, Expression b)
	{
		this.compType = GREATER_EQUALS;
		return this.set(a, b);
	}

	@Override
	public AbstractComparisonCondition gt(Expression a, Expression b)
	{
		this.compType = GREATER_THAN;
		return this.set(a, b);
	}

	@Override
	public AbstractComparisonCondition le(Expression a, Expression b)
	{
		this.compType = LESS_EQUALS;
		return this.set(a, b);
	}

	@Override
	public AbstractComparisonCondition lt(Expression a, Expression b)
	{
		this.compType = LESS_THAN;
		return this.set(a, b);
	}

	@Override
	public AbstractComparisonCondition ne(Expression a, Expression b)
	{
		this.compType = NOT_EQUALS;
		return this.set(a, b);
	}

	@Override
	public Expression operand(int pos)
	{
		switch (pos)
		{
			case 0:
				return this.leftExpr;
			case 1:
				return this.rightExpr;
			default:
				return null;
		}
	}

	@Override
	public AbstractComparisonCondition operand(int pos, Expression opr)
	{
		switch (pos)
		{
			case 0:
				this.leftExpr = opr;
				break;
			case 1:
				this.rightExpr = opr;
				break;
			default:
				break;
		}
		return this;
	}

	@Override
	public List<Expression> operands()
	{
		return Utils.arrayList(this.leftExpr, this.rightExpr);
	}

	public AbstractComparisonCondition set(Expression x, Expression y)
	{
		this.leftExpr = x;
		this.rightExpr = y;
		return this;
	}

	@Override
	public AbstractComparisonCondition some()
	{
		this.groupQual = SOME;
		return this;
	}
}
