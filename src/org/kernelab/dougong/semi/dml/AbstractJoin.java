package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Join;

public abstract class AbstractJoin implements Join
{
	public static final byte		INNER_JOIN	= 0;

	public static final byte		LEFT_JOIN	= 1;

	public static final byte		RIGHT_JOIN	= 2;

	public static final byte		FULL_JOIN	= 3;

	public static final String[]	JOINS		= new String[] { "INNER", "LEFT", "RIGHT", "FULL" };

	protected byte					type;

	protected View					view;

	protected Condition				cond;

	public AbstractJoin join(byte type, View view, String alias, Condition cond)
	{
		this.type = type;
		this.view = view.alias(alias);
		this.cond = cond;
		return this;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}
}
