package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.SingleItem;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;

public abstract class AbstractComparisonCondition extends AbstractComposableCondition implements ComparisonCondition
{
	protected SingleItem	leftItem;

	protected SingleItem	rightItem;

	protected String		compType;

	public AbstractComparisonCondition eq(SingleItem a, SingleItem b)
	{
		this.compType = EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition ge(SingleItem a, SingleItem b)
	{
		this.compType = GREATER_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition gt(SingleItem a, SingleItem b)
	{
		this.compType = GREATER_THAN;
		return this.set(a, b);
	}

	public AbstractComparisonCondition le(SingleItem a, SingleItem b)
	{
		this.compType = LESS_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition lt(SingleItem a, SingleItem b)
	{
		this.compType = LESS_THAN;
		return this.set(a, b);
	}

	public AbstractComparisonCondition ne(SingleItem a, SingleItem b)
	{
		this.compType = NOT_EQUALS;
		return this.set(a, b);
	}

	public AbstractComparisonCondition set(SingleItem x, SingleItem y)
	{
		this.leftItem = x;
		this.rightItem = y;
		return this;
	}
}
