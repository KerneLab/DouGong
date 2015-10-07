package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Item;

public interface LikeCondition extends ComposableCondition, NegatableCondition
{
	public LikeCondition like(Item item, String pattern);
}
