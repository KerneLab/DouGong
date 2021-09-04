package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.semi.AbstractView;

public final class ViewSelf extends AbstractView
{
	public static final ViewSelf SELF = new ViewSelf();

	@Override
	public AllItems all()
	{
		return null;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return null;
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return null;
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return null;
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		return null;
	}
}
