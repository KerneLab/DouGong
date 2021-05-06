package org.kernelab.dougong.semi;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public abstract class AbstractColumn extends AbstractSortable implements Column
{
	private View	view;

	private String	name;

	private String	alias;

	private Field	field;

	private boolean	using;

	public AbstractColumn(View view, String name, Field field)
	{
		this.view = view;
		this.name = name;
		this.field = field;
		this.alias = null;
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
		return (AbstractColumn) this.replicate() //
				.name(this.name()) //
				.alias(alias) //
				.usingByJoin(this.isUsingByJoin()) //
				.ascend(this.ascending()) //
		;
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
