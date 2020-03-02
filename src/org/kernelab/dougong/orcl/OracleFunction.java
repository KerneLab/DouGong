package org.kernelab.dougong.orcl;

import org.kernelab.dougong.orcl.dml.OracleSortable;
import org.kernelab.dougong.semi.AbstractFunction;

public class OracleFunction extends AbstractFunction implements OracleSortable
{
	private byte nulls = OracleSortable.NULLS_NORMAL;

	public byte getNullsPosition()
	{
		return nulls;
	}

	public OracleSortable nullsFirst()
	{
		this.nulls = OracleSortable.NULLS_FIRST;
		return this;
	}

	public OracleSortable nullsLast()
	{
		this.nulls = OracleSortable.NULLS_LAST;
		return this;
	}
}
