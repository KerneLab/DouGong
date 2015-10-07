package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Item;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public interface SingleItem extends Item
{
	public RangeCondition between(Item from, Item to);

	public ComparisonCondition eq(SingleItem item);

	public ComparisonCondition ge(SingleItem item);

	public ComparisonCondition gt(SingleItem item);

	public MembershipCondition in(Scope scope);

	public NullCondition isNotNull();

	public NullCondition isNull();

	public ComparisonCondition le(SingleItem item);

	public LikeCondition like(String pattern);

	public ComparisonCondition lt(SingleItem item);

	public ComparisonCondition ne(SingleItem item);

	public RangeCondition notBetween(Item from, Item to);

	public MembershipCondition notIn(Scope scope);

	public LikeCondition notLike(String pattern);
}
