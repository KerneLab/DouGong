package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;

public abstract class AbstractComparisonCondition extends AbstractComposableCondition implements ComparisonCondition
{
	protected Expression	leftExpr;

	protected Expression	rightExpr;

	protected String		compType;

	protected String		groupQual;

	public AbstractComparisonCondition all()
	{
		this.groupQual = ALL;
		return this;
	}

	public AbstractComparisonCondition any()
	{
		this.groupQual = ANY;
		return this;
	}

	public AbstractComparisonCondition eq(Expression a, Expression b)
	{
		this.compType = EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition ge(Expression a, Expression b)
	{
		this.compType = GREATER_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition gt(Expression a, Expression b)
	{
		this.compType = GREATER_THAN;
		return this.set(a, b);
	}

	public AbstractComparisonCondition le(Expression a, Expression b)
	{
		this.compType = LESS_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition lt(Expression a, Expression b)
	{
		this.compType = LESS_THAN;
		return this.set(a, b);
	}

	public AbstractComparisonCondition ne(Expression a, Expression b)
	{
		this.compType = NOT_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition set(Expression x, Expression y)
	{
		this.leftExpr = x;
		this.rightExpr = y;
		return this;
	}

	public AbstractComparisonCondition some()
	{
		this.groupQual = SOME;
		return this;
	}
}
