package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;

public abstract class AbstractComposableCondition implements ComposableCondition
{
	public LogicalCondition and(Condition cond)
	{
		return this.provideLogicalCondition().set(this).and(cond);
	}

	public LogicalCondition or(Condition cond)
	{
		return this.provideLogicalCondition().set(this).or(cond);
	}

	protected abstract LogicalCondition provideLogicalCondition();
}
