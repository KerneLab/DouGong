package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractRangeCondition extends AbstractNegatableCondition implements RangeCondition
{
	protected Item	item;

	protected Item	from;

	protected Item	to;

	public AbstractRangeCondition between(Item item, Item from, Item to)
	{
		this.item = item;
		this.from = from;
		this.to = to;
		return this;
	}
}
