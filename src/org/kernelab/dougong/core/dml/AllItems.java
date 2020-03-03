package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface AllItems extends Expression
{
	public static final String ALL_COLUMNS = "*";

	/**
	 * Get the string of this AllItems object including the table alias if
	 * specified. The table alias would disappear when the table alias is not
	 * specified.
	 */
	public StringBuilder toString(StringBuilder buffer);

	/**
	 * The view which this AllItems refers to.
	 * 
	 * @return The view or null if indicates all views.
	 */
	public View view();
}
