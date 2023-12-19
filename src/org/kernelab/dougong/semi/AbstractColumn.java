package org.kernelab.dougong.semi;

import java.lang.reflect.Field;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public abstract class AbstractColumn extends AbstractSortable implements Column
{
	public static boolean equals(Column a, Column b)
	{
		return a != null && b != null //
				&& a.view().getClass() == b.view().getClass() //
				&& Tools.equals(a.getMetaName(), b.getMetaName());
	}

	public static boolean equals(Column[] a, Column[] b)
	{
		if (a == null || b == null)
		{
			return false;
		}

		int len = a.length;
		if (b.length != len)
		{
			return false;
		}

		for (int i = 0; i < len; i++)
		{
			if (!equals(a[i], b[i]))
			{
				return false;
			}
		}

		return true;
	}

	public static int hashCode(Column c)
	{
		return c == null ? 0 : (c.view().getClass().hashCode() * 31 + c.getMetaName().hashCode());
	}

	public static int hashCode(Column[] cols)
	{
		if (cols == null)
		{
			return 0;
		}
		else
		{
			final int prime = 31;
			int result = 1;
			for (Column c : cols)
			{
				result = result * prime + (c == null ? 0 : c.getMetaName().hashCode());
			}
			return result;
		}
	}

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
		return this.replicate() //
				.to(AbstractColumn.class) //
				.alias(alias);
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
	public String getMetaName()
	{
		if (field() != null)
		{
			return field().getName();
		}
		else
		{
			return name();
		}
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
