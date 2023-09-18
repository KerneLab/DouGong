package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;

public abstract class AbstractComposableCondition extends AbstractCondition implements ComposableCondition
{
	@Override
	public ComposableCondition and(boolean when, Condition cond)
	{
		if (when && cond != null && !cond.isEmpty())
		{
			return provider().provideLogicalCondition().set(this).and(true, cond);
		}
		else
		{
			return this;
		}
	}

	@Override
	public ComposableCondition and(Condition cond)
	{
		return and(true, cond);
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public ComposableCondition or(boolean when, Condition cond)
	{
		if (when && cond != null && !cond.isEmpty())
		{
			return provider().provideLogicalCondition().set(this).or(true, cond);
		}
		else
		{
			return this;
		}
	}

	@Override
	public ComposableCondition or(Condition cond)
	{
		return or(true, cond);
	}
}
