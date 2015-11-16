package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Filterable;

public abstract class AbstractFilterable extends AbstractProvidable implements Filterable
{
	private View		from	= null;

	private Condition	where	= null;

	public View from()
	{
		return from;
	}

	public AbstractFilterable from(View view)
	{
		this.from = view;
		return this;
	}

	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM ");
		from().toStringAliased(buffer);
	}

	protected void textOfWhere(StringBuilder buffer)
	{
		if (where() != null)
		{
			buffer.append(" WHERE ");
			where().toString(buffer);
		}
	}

	public Condition where()
	{
		return where;
	}

	public AbstractFilterable where(Condition cond)
	{
		this.where = cond;
		return this;
	}
}
