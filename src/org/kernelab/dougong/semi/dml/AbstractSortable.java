package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Sortable;

public abstract class AbstractSortable extends AbstractItem implements Sortable
{
	private boolean	order	= true;

	private Boolean	nulls	= null;

	@Override
	public AbstractSortable ascend()
	{
		return ascend(true);
	}

	@Override
	public AbstractSortable ascend(boolean ascend)
	{
		this.order = ascend;
		return this;
	}

	@Override
	public boolean ascending()
	{
		return order;
	}

	@Override
	public AbstractSortable descend()
	{
		return ascend(false);
	}

	@Override
	public Sortable nullsFirst()
	{
		return nullsPosition(NULLS_FIRST);
	}

	@Override
	public Sortable nullsLast()
	{
		return nullsPosition(NULLS_LAST);
	}

	@Override
	public Boolean nullsPosition()
	{
		return nulls;
	}

	@Override
	public Sortable nullsPosition(Boolean first)
	{
		this.nulls = first;
		return this;
	}

	protected abstract AbstractSortable replicate();

	@Override
	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.provider().provideOutputOrder(toString(buffer), this);
	}
}
