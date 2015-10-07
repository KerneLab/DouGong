package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.dml.cond.NullCondition;

public abstract class AbstractNullCondition extends AbstractNegatableCondition implements NullCondition
{
	protected Item	item;

	public AbstractNullCondition isNull(Item item)
	{
		this.item = item;
		return this;
	}
}
