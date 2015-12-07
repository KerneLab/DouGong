package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.OrderableItem;
import org.kernelab.dougong.core.dml.SingleItem;

public interface Column extends SingleItem, OrderableItem
{
	/**
	 * Get a copy of this Column with the given alias.
	 * 
	 * @param alias
	 * @return
	 */
	public Column as(String alias);

	/**
	 * Determine whether this column is using by a join. The Default value is false.
	 * 
	 * @return
	 */
	public boolean isUsingByJoin();

	/**
	 * Get the name of this Column without the alias nor the view name.
	 * 
	 * @return
	 */
	public String name();

	/**
	 * Get the string of this Column including the table alias if specified but
	 * excluding the column alias. The table alias should disappear when this
	 * column is using by a join or the table alias is not specified.
	 */
	public StringBuilder toString(StringBuilder buffer);

	/**
	 * Get the string of this Column including the column alias.
	 */
	public StringBuilder toStringAliased(StringBuilder buffer);

	/**
	 * Specify whether this column is using by a join or not. The Default value is false.
	 * 
	 * @param using
	 * @return The column object itself.
	 */
	public Column usingByJoin(boolean using);

	/**
	 * Get the View object which this Column belongs to.
	 * 
	 * @return
	 */
	public View view();
}
