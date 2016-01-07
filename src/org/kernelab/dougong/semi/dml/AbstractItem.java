package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Item;

public abstract class AbstractItem extends AbstractExpression implements Item
{
	private String	alias	= null;

	public String alias()
	{
		return alias;
	}

	public AbstractItem alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractItem as(String alias)
	{
		return this.replicate().alias(alias);
	}

	protected abstract AbstractItem replicate();
}
