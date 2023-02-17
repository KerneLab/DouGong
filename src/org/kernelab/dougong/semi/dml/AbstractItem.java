package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.dml.Item;

public abstract class AbstractItem extends AbstractExpression implements Item
{
	private String	alias	= null;

	private boolean	using	= false;

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
	public AbstractItem alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	@Override
	public AbstractItem as(String alias)
	{
		return this.replicate().alias(alias);
	}

	@Override
	public boolean isUsingByJoin()
	{
		return using;
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : Tools.substr(this.toStringExpress(new StringBuilder()), 0, 30);
	}

	/**
	 * Return a new Object which has the most same properties to this Object.
	 * <br />
	 * Typically, the alias name should not be considered.
	 * 
	 * @return
	 */
	@Override
	protected abstract AbstractItem replicate();

	@Override
	public List<Item> resolveItems()
	{
		return listOf((Item) this);
	}

	@Override
	public AbstractItem usingByJoin(boolean using)
	{
		this.using = using;
		return this;
	}
}
