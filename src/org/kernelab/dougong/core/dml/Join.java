package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Relation;
import org.kernelab.dougong.core.View;

public interface Join extends Relation
{
	public static final byte		INNER_JOIN	= 0;

	public static final byte		LEFT_JOIN	= 1;

	public static final byte		RIGHT_JOIN	= 2;

	public static final byte		FULL_JOIN	= 3;

	public static final String[]	JOINS		= new String[] { "INNER", "LEFT", "RIGHT", "FULL" };

	public Join join(View leading, Join former, byte type, View view, String alias);

	public Condition on();

	public Join on(Condition condition);

	public byte type();

	public Column[] using();

	public Join using(Column... columns);

	public View view();
}
