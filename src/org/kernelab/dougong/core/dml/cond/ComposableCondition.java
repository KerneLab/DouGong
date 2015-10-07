package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Condition;

public interface ComposableCondition extends Condition
{
	public LogicalCondition and(Condition cond);

	public LogicalCondition or(Condition cond);
}
