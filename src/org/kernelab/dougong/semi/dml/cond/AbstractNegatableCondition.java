package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.cond.NegatableCondition;

public abstract class AbstractNegatableCondition extends AbstractComposableCondition implements NegatableCondition
{
	protected boolean not = false;

	@Override
	public AbstractNegatableCondition not()
	{
		this.not = true;
		return this;
	}
}
