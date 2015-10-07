package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.cond.NegatableCondition;

public abstract class AbstractNegatableCondition extends AbstractComposableCondition implements NegatableCondition
{
	protected boolean	not	= false;

	public AbstractNegatableCondition not()
	{
		not = true;
		return this;
	}
}
