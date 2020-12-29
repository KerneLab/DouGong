package org.kernelab.dougong.semi.dml.cond;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;

public abstract class AbstractLogicalCondition extends AbstractCondition implements LogicalCondition
{
	protected List<Object> conds = new LinkedList<Object>();

	public AbstractLogicalCondition and(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			if (!conds.isEmpty())
			{
				conds.add(AND);
			}
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition and(Condition cond)
	{
		return and(true, cond);
	}

	public boolean isEmpty()
	{
		return conds.isEmpty();
	}

	public AbstractLogicalCondition not(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			conds.add(NOT);
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition not(Condition cond)
	{
		return not(true, cond);
	}

	public AbstractLogicalCondition or(boolean when, Condition cond)
	{
		if (when && cond != null)
		{
			if (!conds.isEmpty())
			{
				conds.add(OR);
			}
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition or(Condition cond)
	{
		return or(true, cond);
	}

	public AbstractLogicalCondition set(Condition cond)
	{
		conds.clear();
		return this.and(true, cond);
	}
}
