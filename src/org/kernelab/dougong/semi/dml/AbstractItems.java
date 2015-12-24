package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public abstract class AbstractItems implements Items
{
	private Expression[]	list;

	public AbstractItems()
	{
	}

	public String alias()
	{
		return null;
	}

	public AbstractItems alias(String alias)
	{
		return this;
	}

	public MembershipCondition in(Scope scope)
	{
		return this.provideMembershipCondition().in(this, scope);
	}

	protected Expression[] list()
	{
		return list;
	}

	public AbstractItems list(Expression... list)
	{
		this.list = list;
		return this;
	}

	public MembershipCondition notIn(Scope scope)
	{
		return (MembershipCondition) this.provideMembershipCondition().in(this, scope).not();
	}

	protected abstract MembershipCondition provideMembershipCondition();

	public StringBuilder toString(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.text(buffer, expr);
			}
		}

		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.textAliased(buffer, expr);
			}
		}

		return buffer;
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return toString(buffer);
	}
}
