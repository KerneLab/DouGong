package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;

public abstract class AbstractComposableCondition implements ComposableCondition
{
	public boolean isEmpty()
	{
		return false;
	}

	public ComposableCondition and(boolean when, Condition cond)
	{
		if (when)
		{
			return this.provideLogicalCondition().set(this).and(true, cond);
		}
		else
		{
			return this;
		}
	}

	public ComposableCondition and(Condition cond)
	{
		return and(true, cond);
	}

	public ComposableCondition or(boolean when, Condition cond)
	{
		if (when)
		{
			return this.provideLogicalCondition().set(this).or(true, cond);
		}
		else
		{
			return this;
		}
	}

	public ComposableCondition or(Condition cond)
	{
		return or(true, cond);
	}

	protected abstract LogicalCondition provideLogicalCondition();
}
