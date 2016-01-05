package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;

public interface AllColumns extends Expression
{
	public static final String	ALL_COLUMNS	= "*";

	/**
	 * Get the string of this AllColumns object including the table alias if
	 * specified. The table alias would disappear when the table alias is not
	 * specified.
	 */
	public StringBuilder toString(StringBuilder buffer);

	public View view();
}
