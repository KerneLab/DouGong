package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;

public interface EqualityTestable
{
	public ComparisonCondition eq(Expression expr);

	public ComparisonCondition ne(Expression expr);
}
