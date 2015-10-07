package org.kernelab.dougong.semi.dml.cond;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;

public abstract class AbstractLogicalCondition implements LogicalCondition
{
	protected List<Object>	conds	= new LinkedList<Object>();

	public AbstractLogicalCondition and(Condition cond)
	{
		if (cond != null)
		{
			if (!conds.isEmpty())
			{
				conds.add(AND);
			}
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition not(Condition cond)
	{
		if (cond != null)
		{
			conds.add(NOT);
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition or(Condition cond)
	{
		if (cond != null)
		{
			if (!conds.isEmpty())
			{
				conds.add(OR);
			}
			conds.add(cond);
		}
		return this;
	}

	public AbstractLogicalCondition set(Condition cond)
	{
		conds.clear();
		return this.and(cond);
	}
}
