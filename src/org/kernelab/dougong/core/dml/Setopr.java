package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Relation;

/**
 * The relation of set operation.
 */
public interface Setopr extends Relation
{
	public static final byte		UNION			= 0;

	public static final byte		INTERSECT		= 1;

	public static final byte		EXCEPT			= 2;

	public static final byte		UNION_ALL		= 3;

	public static final byte		INTERSECT_ALL	= 4;

	public static final byte		EXCEPT_ALL		= 5;

	public static final String[]	OPRS			= new String[] { "UNION", "INTERSECT", "EXCEPT", "UNION ALL",
			"INTERSECT ALL", "EXCEPT ALL" };

	public Select select();

	public Setopr setopr(byte type, Select select);

	public StringBuilder toStringScoped(StringBuilder buffer);

	public byte type();
}
