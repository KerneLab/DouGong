package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractStringItem;

public class OracleStringItem extends AbstractStringItem implements OracleSortable
{
	private byte nulls = OracleSortable.NULLS_NORMAL;

	public OracleStringItem(Provider provider)
	{
		super(provider);
	}

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
