package org.kernelab.dougong.maria;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.AbstractColumn;

public class MariaColumn extends AbstractColumn
{
	public MariaColumn(View view, String name, Field field)
	{
		super(view, name, field);
	}

	@Override
	protected MariaColumn replicate()
	{
		return (MariaColumn) new MariaColumn(view(), name(), field()).replicateOrderOf(this);
	}
}
