package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Expression;

/**
 * Item is an interface which stand for a single item that can be selected,
 * tested and aliased.
 *
 */
public interface Item extends Expression, Alias
{
	public Item alias(String alias);

	/**
	 * Get a copy of this Item with the given alias.
	 * 
	 * @param alias
	 * @return
	 */
	public Item as(String alias);
}
