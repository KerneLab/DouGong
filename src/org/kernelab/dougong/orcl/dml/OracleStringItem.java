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

	@Override
	public byte getNullsPosition()
	{
		return nulls;
	}

	@Override
	public OracleSortable nullsFirst()
	{
		this.nulls = OracleSortable.NULLS_FIRST;
		return this;
	}

	@Override
	public OracleSortable nullsLast()
	{
		this.nulls = OracleSortable.NULLS_LAST;
		return this;
	}
}
