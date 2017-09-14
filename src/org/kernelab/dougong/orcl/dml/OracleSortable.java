package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.dml.Sortable;

public interface OracleSortable extends Sortable
{
	public static final byte	NULLS_NORMAL	= 0;

	public static final byte	NULLS_LAST		= 1;

	public static final byte	NULLS_FIRST		= 2;

	public static final String	NULLS_LAST_OPR	= "NULLS LAST";

	public static final String	NULLS_FIRST_OPR	= "NULLS FIRST";

	public byte getNullsPosition();

	public OracleSortable nullsFirst();

	public OracleSortable nullsLast();
}
