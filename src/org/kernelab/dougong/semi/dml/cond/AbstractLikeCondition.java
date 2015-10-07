package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.dml.cond.LikeCondition;

public abstract class AbstractLikeCondition extends AbstractNegatableCondition implements LikeCondition
{
	protected Item		item;

	protected String	pattern;

	public AbstractLikeCondition like(Item item, String pattern)
	{
		return this.set(item, pattern);
	}

	public AbstractLikeCondition set(Item item, String pattern)
	{
		this.item = item;
		this.pattern = pattern;
		return this;
	}
}
