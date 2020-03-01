package org.kernelab.dougong.semi.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractKey extends AbstractProvidable implements Key
{
	private Column[]	columns;

	private View		view;

	public AbstractKey(View view, Column... columns)
	{
		this.view = view;
		this.columns = columns;
	}

	public Column[] columns()
	{
		return columns;
	}

	public View view()
	{
		return view;
	}
}
