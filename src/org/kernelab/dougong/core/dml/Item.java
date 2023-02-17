package org.kernelab.dougong.core.dml;

/**
 * Item is an interface which stand for a single item that can be selected,
 * tested and aliased.
 *
 */
public interface Item extends Expression, Alias, Label
{
	@Override
	public Item alias(String alias);

	/**
	 * Get a copy of this Item with the given alias.
	 * 
	 * @param alias
	 * @return
	 */
	public Item as(String alias);

	/**
	 * Determine whether this column is using by a join. The Default value is
	 * false.
	 * 
	 * @return
	 */
	public boolean isUsingByJoin();

	/**
	 * Specify whether this item is using by a join or not. The Default value is
	 * false.
	 * 
	 * @param using
	 * @return The item object itself.
	 */
	public Item usingByJoin(boolean using);
}
