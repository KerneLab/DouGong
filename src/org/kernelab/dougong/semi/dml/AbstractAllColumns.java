package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllColumns;

public abstract class AbstractAllColumns extends AbstractReplicable implements AllColumns
{
	private View	view;

	public AbstractAllColumns(View view)
	{
		this.view = view;
	}

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public View view()
	{
		return view;
	}
}
