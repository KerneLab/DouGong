package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;

public interface GreaterLessTestable
{
	public ComparisonCondition ge(Expression expr);

	public ComparisonCondition gt(Expression expr);

	public ComparisonCondition le(Expression expr);

	public ComparisonCondition lt(Expression expr);
}
