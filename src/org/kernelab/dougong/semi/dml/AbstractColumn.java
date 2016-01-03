package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractColumn extends AbstractItem implements Column
{
	private View	view;

	private String	name;

	private String	alias;

	private boolean	order;

	private boolean	using;

	public AbstractColumn(View view, String name)
	{
		this.view = view;
		this.name = name;
		this.alias = null;
		this.order = true;
		this.using = false;
	}

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
	public AbstractColumn alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	@Override
	public AbstractColumn as(String alias)
	{
		return this.replicate() //
				.name(this.name()) //
				.alias(alias) //
				.ascend(this.ascending()) //
				.usingByJoin(this.isUsingByJoin());
	}

	public AbstractColumn ascend()
	{
		return ascend(true);
	}

	public AbstractColumn ascend(boolean ascend)
	{
		this.order = ascend;
		return this;
	}

	public boolean ascending()
	{
		return order;
	}

	public AbstractColumn descend()
	{
		return ascend(false);
	}

	public boolean isUsingByJoin()
	{
		return using;
	}

	public String name()
	{
		return name;
	}

	public AbstractColumn name(String name)
	{
		this.name = name;
		return this;
	}

	@Override
	protected ComparisonCondition provideComparisonCondition()
	{
		return view().provider().provideComparisonCondition();
	}

	@Override
	protected LikeCondition provideLikeCondition()
	{
		return view().provider().provideLikeCondition();
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return view().provider().provideMembershipCondition();
	}

	@Override
	protected NullCondition provideNullCondition()
	{
		return view().provider().provideNullCondition();
	}

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return view().provider().provideRangeCondition();
	}

	protected abstract AbstractColumn replicate();

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.view().provider().provideOutputOrder(toString(buffer), this);
	}

	public AbstractColumn usingByJoin(boolean using)
	{
		this.using = using;
		return this;
	}

	public View view()
	{
		return view;
	}
}
