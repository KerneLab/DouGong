package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Item;

public interface RangeCondition extends ComposableCondition, NegatableCondition
{
	public RangeCondition between(Item item, Item from, Item to);
}
