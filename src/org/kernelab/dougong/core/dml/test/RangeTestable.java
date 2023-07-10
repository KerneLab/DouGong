package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public interface RangeTestable
{
	public RangeCondition between(Expression from, Expression to);

	@Deprecated
	public RangeCondition notBetween(Expression from, Expression to);
}
