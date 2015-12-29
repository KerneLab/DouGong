package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractRangeCondition extends AbstractNegatableCondition implements RangeCondition
{
	protected Expression	expr;

	protected Expression	from;

	protected Expression	to;

	public AbstractRangeCondition between(Expression item, Expression from, Expression to)
	{
		this.expr = item;
		this.from = from;
		this.to = to;
		return this;
	}
}
