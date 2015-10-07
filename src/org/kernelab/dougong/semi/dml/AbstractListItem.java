package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.dml.ListItem;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;

public abstract class AbstractListItem implements ListItem
{
	private Object[]	items;

	public AbstractListItem()
	{
	}

	public String alias()
	{
		return null;
	}

	public AbstractListItem alias(String alias)
	{
		return this;
	}

	public MembershipCondition in(Scope scope)
	{
		return this.provideMembershipCondition().in(this, scope);
	}

	public AbstractListItem list(Object... items)
	{
		this.items = items;
		return this;
	}

	public MembershipCondition notIn(Scope scope)
	{
		return (MembershipCondition) this.provideMembershipCondition().in(this, scope).not();
	}

	protected abstract MembershipCondition provideMembershipCondition();

	public StringBuilder toString(StringBuilder buffer)
	{
		if (items != null)
		{
			for (Object item : items)
			{
				if (buffer.length() > 0)
				{
					buffer.append(',');
				}
				Utils.text(buffer, item);
			}
		}

		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		if (items != null)
		{
			for (Object item : items)
			{
				if (buffer.length() > 0)
				{
					buffer.append(',');
				}
				Utils.textAliased(buffer, item);
			}
		}

		return buffer;
	}
}
