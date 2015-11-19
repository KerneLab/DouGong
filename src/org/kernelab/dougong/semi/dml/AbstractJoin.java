package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Join;

public abstract class AbstractJoin implements Join
{
	private byte		type;

	private View		view;

	private Condition	on;

	private Column[]	using;

	public AbstractJoin join(byte type, View view, String alias, Column... using)
	{
		this.type = type;
		this.view = view.alias(alias);
		this.using = using;
		this.on = null;
		return this;
	}

	public AbstractJoin join(byte type, View view, String alias, Condition cond)
	{
		this.type = type;
		this.view = view.alias(alias);
		this.on = cond;
		this.using = null;
		return this;
	}

	protected Condition on()
	{
		return on;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.toStringJoinTable(buffer);

		if (this.on() != null)
		{
			this.toStringOnCondition(buffer);
		}
		else if (this.using() != null)
		{
			this.toStringUsingColumns(buffer);
		}

		return buffer;
	}

	protected abstract StringBuilder toStringJoinTable(StringBuilder buffer);

	protected abstract StringBuilder toStringOnCondition(StringBuilder buffer);

	protected abstract StringBuilder toStringUsingColumns(StringBuilder buffer);

	protected byte type()
	{
		return type;
	}

	protected Column[] using()
	{
		return using;
	}

	protected View view()
	{
		return view;
	}
}
