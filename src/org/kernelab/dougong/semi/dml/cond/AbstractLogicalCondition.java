package org.kernelab.dougong.semi.dml.cond;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;

public abstract class AbstractLogicalCondition extends AbstractCondition implements LogicalCondition
{
	protected List<Object> conds = new LinkedList<Object>();

	@Override
	public AbstractLogicalCondition and(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			if (!this.conds.isEmpty())
			{
				this.conds.add(AND);
			}
			this.conds.add(cond);
		}
		return this;
	}

	@Override
	public AbstractLogicalCondition and(Condition cond)
	{
		return and(true, cond);
	}

	public List<Object> getConditions()
	{
		return this.conds;
	}

	@Override
	public boolean isEmpty()
	{
		return this.conds.isEmpty();
	}

	@Override
	public AbstractLogicalCondition not(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			this.conds.add(NOT);
			this.conds.add(cond);
		}
		return this;
	}

	@Override
	public AbstractLogicalCondition not(Condition cond)
	{
		return not(true, cond);
	}

	@Override
	public AbstractLogicalCondition or(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			if (!this.conds.isEmpty())
			{
				this.conds.add(OR);
			}
			this.conds.add(cond);
		}
		return this;
	}

	@Override
	public AbstractLogicalCondition or(Condition cond)
	{
		return or(true, cond);
	}

	@Override
	public List<Object> reflects()
	{
		return this.conds;
	}

	@Override
	public AbstractLogicalCondition set(Condition cond)
	{
		this.conds.clear();
		return this.and(true, cond);
	}
}
