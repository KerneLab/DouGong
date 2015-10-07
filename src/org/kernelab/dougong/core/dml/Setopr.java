package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Relation;

/**
 * The relation of set operation.
 */
public interface Setopr extends Relation
{
	public static final byte		UNION_ALL	= 0;

	public static final byte		UNION		= 1;

	public static final byte		INTERSECT	= 2;

	public static final byte		MINUS		= 3;

	public static final String[]	OPRS		= new String[] { "UNION ALL", "UNION", "INTERSECT", "MINUS" };

	public Setopr setopr(byte type, Select select);
}
