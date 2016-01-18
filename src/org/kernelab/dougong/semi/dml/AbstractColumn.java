package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.util.Utils;

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

	protected Provider provider()
	{
		return view().provider();
	}

	protected abstract AbstractColumn replicate();

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.view().provider().provideOutputOrder(toString(buffer), this);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.view().provider(), this.toStringExpress(buffer), this);
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
