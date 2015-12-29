package org.kernelab.dougong.core.dml;

public interface Sortable extends Item
{
	/**
	 * Return an Sortable object in ascending order.
	 * 
	 * @return
	 */
	public Sortable ascend();

	/**
	 * Return an Sortable object according to the given order.
	 * 
	 * @param ascend
	 *            true if in ascending order otherwise false.
	 * @return
	 */
	public Sortable ascend(boolean ascend);

	/**
	 * true if and only if this expr is in ascending order otherwise descend.
	 * 
	 * @return
	 */
	public boolean ascending();

	/**
	 * Return an Sortable object in descending order.
	 * 
	 * @return
	 */
	public Sortable descend();

	/**
	 * Get the text with order.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringOrdered(StringBuilder buffer);
}
