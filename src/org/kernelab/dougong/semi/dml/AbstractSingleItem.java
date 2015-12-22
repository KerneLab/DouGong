package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.SingleItem;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractSingleItem extends AbstractReplicable implements SingleItem
{
	private String	alias	= null;

	public String alias()
	{
		return alias;
	}

	public AbstractSingleItem alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractSingleItem as(String alias)
	{
		return this.replicate().alias(alias);
	}

	public RangeCondition between(Item from, Item to)
	{
		return this.provideRangeCondition().between(this, from, to);
	}

	public ComparisonCondition eq(SingleItem item)
	{
		return this.provideComparisonCondition().eq(this, item);
	}

	public ComparisonCondition ge(SingleItem item)
	{
		return this.provideComparisonCondition().eq(this, item);
	}

	public ComparisonCondition gt(SingleItem item)
	{
		return this.provideComparisonCondition().gt(this, item);
	}

	public MembershipCondition in(Scope scope)
	{
		return this.provideMembershipCondition().in(this, scope);
	}

	public NullCondition isNotNull()
	{
		return (NullCondition) this.provideNullCondition().isNull(this).not();
	}

	public NullCondition isNull()
	{
		return this.provideNullCondition().isNull(this);
	}

	public ComparisonCondition le(SingleItem item)
	{
		return this.provideComparisonCondition().le(this, item);
	}

	public LikeCondition like(String pattern)
	{
		return this.provideLikeCondition().like(this, pattern);
	}

	public ComparisonCondition lt(SingleItem item)
	{
		return this.provideComparisonCondition().lt(this, item);
	}

	public ComparisonCondition ne(SingleItem item)
	{
		return this.provideComparisonCondition().ne(this, item);
	}

	public RangeCondition notBetween(Item from, Item to)
	{
		return (RangeCondition) this.provideRangeCondition().between(this, from, to).not();
	}

	public MembershipCondition notIn(Scope scope)
	{
		return (MembershipCondition) this.provideMembershipCondition().in(this, scope).not();
	}

	public LikeCondition notLike(String pattern)
	{
		return (LikeCondition) this.provideLikeCondition().like(this, pattern).not();
	}

	protected abstract ComparisonCondition provideComparisonCondition();

	protected abstract LikeCondition provideLikeCondition();

	protected abstract MembershipCondition provideMembershipCondition();

	protected abstract NullCondition provideNullCondition();

	protected abstract RangeCondition provideRangeCondition();

	protected abstract AbstractSingleItem replicate();
}
