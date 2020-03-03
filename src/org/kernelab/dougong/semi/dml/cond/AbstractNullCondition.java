package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.NullCondition;

public abstract class AbstractNullCondition extends AbstractNegatableCondition implements NullCondition
{
	protected Expression	expr;

	public AbstractNullCondition isNull(Expression expr)
	{
		this.expr = expr;
		return this;
	}
}
