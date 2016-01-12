package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Condition;

public interface ComposableCondition extends Condition
{
	public ComposableCondition and(boolean when, Condition cond);

	public ComposableCondition and(Condition cond);

	public ComposableCondition or(boolean when, Condition cond);

	public ComposableCondition or(Condition cond);
}
