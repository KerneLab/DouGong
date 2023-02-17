package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Relation;
import org.kernelab.dougong.core.View;

public interface Join extends Relation
{
	public static final byte		INNER_JOIN	= 0;

	public static final byte		LEFT_JOIN	= 1;

	public static final byte		RIGHT_JOIN	= 2;

	public static final byte		FULL_JOIN	= 3;

	public static final byte		CROSS_JOIN	= 4;

	public static final byte		SEMI_JOIN	= 5;

	public static final byte		ANTI_JOIN	= 6;

	public static final String		NATURAL		= "NATURAL";

	public static final String[]	JOINS		= new String[] { "INNER", "LEFT", "RIGHT", "FULL", "CROSS", "SEMI",
			"ANTI" };

	public Join join(View leading, Join former, boolean natural, byte type, View view, String alias);

	public boolean natural();

	public Condition on();

	public Join on(Condition condition);

	/**
	 * Spread items that using by join to this and former join object.
	 * 
	 * @param items
	 * @return
	 */
	public Join spread(Item... items);

	public byte type();

	public Item[] using();

	public Join using(Item... items);

	public View view();
}
