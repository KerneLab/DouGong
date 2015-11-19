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
	 * Get the name of this Column without the alias nor the view name.
	 * 
	 * @return
	 */
	public String name();

	/**
	 * Get the string of this Column including the table alias if specified but
	 * excluding the column alias.
	 */
	public StringBuilder toString(StringBuilder buffer);

	/**
	 * Get the string of this Column including the column alias.
	 */
	public StringBuilder toStringAliased(StringBuilder buffer);

	/**
	 * Get the View object which this Column belongs to.
	 * 
	 * @return
	 */
	public View view();
}
