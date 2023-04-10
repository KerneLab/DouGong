package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Sortable;

public abstract class AbstractSortable extends AbstractItem implements Sortable
{
	private boolean	order	= true;

	private Boolean	nulls	= null;

	@Override
	public AbstractSortable asc()
	{
		return this.replicate().ascend(true);
	}

	@Override
	public AbstractSortable ascend()
	{
		return this.asc();
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
	public AbstractSortable desc()
	{
		return this.replicate().ascend(false);
	}

	@Override
	public AbstractSortable descend()
	{
		return this.desc();
	}

	/**
	 * Return a new instance of this class with the attributes declared by this
	 * class.
	 * 
	 * @return
	 */
	protected abstract AbstractSortable newInstance();

	@Override
	public Sortable nullsFirst()
	{
		return this.replicate().nullsPosition(NULLS_FIRST);
	}

	@Override
	public Sortable nullsLast()
	{
		return this.replicate().nullsPosition(NULLS_LAST);
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

	@Override
	protected AbstractSortable replicate()
	{
		return this.newInstance().replicateOrderOf(this);
	}

	private AbstractSortable replicateOrderOf(Sortable sort)
	{
		this.ascend(sort.ascending()).nullsPosition(sort.nullsPosition());
		return this;
	}

	@Override
	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.provider().provideOutputOrder(toString(buffer), this);
	}
}
