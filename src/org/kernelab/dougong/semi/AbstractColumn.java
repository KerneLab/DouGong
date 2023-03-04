package org.kernelab.dougong.semi;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public abstract class AbstractColumn extends AbstractSortable implements Column
{
	private View	view;

	private String	name;

	private String	alias;

	private Field	field;

	public AbstractColumn(View view, String name, Field field)
	{
		this.view = view;
		this.name = name;
		this.field = field;
		this.alias = null;
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
		AbstractColumn col = (AbstractColumn) this.replicate() //
				.name(this.name()) //
				.alias(alias);
		col.ascend(this.ascending());
		return col;
	}

	@Override
	public Column asFieldName()
	{
		if (this.field() == null)
		{
			return this;
		}
		return this.as(this.field().getName());
	}

	@Override
	public Field field()
	{
		return field;
	}

	@Override
	public boolean isPseudo()
	{
		Field field = this.field();
		if (field != null)
		{
			return field.getAnnotation(PseudoColumnMeta.class) != null;
		}
		return false;
	}

	@Override
	public boolean isUsingByJoin()
	{
		return this.view().isJoinUsing(this.label());
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : name();
	}

	@Override
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
	protected Provider provider()
	{
		return view().provider();
	}

	@Override
	protected abstract AbstractColumn replicate();

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return this.toStringExpress(buffer);
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return this.provider().provideOutputColumnExpress(buffer, this);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return this.provider().provideOutputColumnSelect(buffer, this);
	}

	@Override
	public View view()
	{
		return view;
	}
}
