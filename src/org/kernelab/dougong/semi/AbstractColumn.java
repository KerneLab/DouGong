package org.kernelab.dougong.semi;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractItem;

public abstract class AbstractColumn extends AbstractItem implements Column
{
	private View	view;

	private String	name;

	private String	alias;

	private Field	field;

	private boolean	order;

	private boolean	using;

	public AbstractColumn(View view, String name, Field field)
	{
		this.view = view;
		this.name = name;
		this.field = field;
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

	public Field field()
	{
		return field;
	}

	public boolean isUsingByJoin()
	{
		return using;
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : name();
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

	public StringBuilder toString(StringBuilder buffer)
	{
		return this.toStringExpress(buffer);
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return this.provider().provideOutputColumnExpress(buffer, this);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.provider().provideOutputOrder(toString(buffer), this);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return this.provider().provideOutputColumnSelect(buffer, this);
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
