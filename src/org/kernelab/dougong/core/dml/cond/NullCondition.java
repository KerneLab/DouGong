package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Item;

public interface NullCondition extends ComposableCondition, NegatableCondition
{
	public NullCondition isNull(Item item);
}
