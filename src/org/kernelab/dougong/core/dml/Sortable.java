package org.kernelab.dougong.core.dml;

public interface Sortable extends Item
{
	public static final Boolean	NULLS_NORMAL	= null;

	public static final Boolean	NULLS_LAST		= true;

	public static final Boolean	NULLS_FIRST		= false;

	public static final String	NULLS_LAST_OPR	= "NULLS LAST";

	public static final String	NULLS_FIRST_OPR	= "NULLS FIRST";

	/**
	 * Return a Sortable object in ascending order.
	 * 
	 * @return
	 */
	public Sortable ascend();

	/**
	 * Return a Sortable object according to the given order.
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
	 * Return a Sortable object in descending order.
	 * 
	 * @return
	 */
	public Sortable descend();

	/**
	 * Return a Sortable object with nullsFirst.
	 * 
	 * @return
	 */
	public Sortable nullsFirst();

	/**
	 * Return a Sortable object with nullsLast.
	 * 
	 * @return
	 */
	public Sortable nullsLast();

	/**
	 * true if this expr is ordered with nullLast, false if this expr is ordered
	 * with nullFirst, null means did not specify the nulls position.
	 * 
	 * @return
	 */
	public Boolean nullsPosition();

	/**
	 * Return a Sortable object with given nulls position.
	 * 
	 * @return
	 * @see {@link Sortable#nullsPosition()}
	 */
	public Sortable nullsPosition(Boolean first);

	/**
	 * Get the text with order.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringOrdered(StringBuilder buffer);
}
