package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Item;

public interface OrderableItem extends Item
{
	/**
	 * Return a new OrderableItem object in ascending order.
	 * 
	 * @return
	 */
	public OrderableItem ascend();

	/**
	 * true if and only if this item is in ascending order otherwise descend.
	 * 
	 * @return
	 */
	public boolean ascending();

	/**
	 * Return a new OrderableItem object in descending order.
	 * 
	 * @return
	 */
	public OrderableItem descend();

	/**
	 * Get the text with order.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringOrdered(StringBuilder buffer);
}
