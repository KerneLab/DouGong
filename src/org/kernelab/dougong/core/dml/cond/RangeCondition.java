package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface RangeCondition extends ComposableCondition, NegatableCondition, TernaryCondition
{
	public RangeCondition between(Expression expr, Expression from, Expression to);
}
