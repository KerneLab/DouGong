package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ExistsCondition;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public abstract class AbstractExistsCondition extends AbstractComposableCondition implements ExistsCondition
{
	protected AbstractSelect select;

	public Expression $_1()
	{
		return select;
	}

	public AbstractExistsCondition exists(AbstractSelect select)
	{
		this.select = select;
		return this;
	}
}
