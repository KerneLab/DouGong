package org.kernelab.dougong.orcl;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.AbstractColumn;

public class OracleColumn extends AbstractColumn
{
	public OracleColumn(View view, String name, Field field)
	{
		super(view, name, field);
	}

	@Override
	protected OracleColumn replicate()
	{
		return (OracleColumn) new OracleColumn(view(), name(), field()).replicateOrderOf(this);
	}
}
