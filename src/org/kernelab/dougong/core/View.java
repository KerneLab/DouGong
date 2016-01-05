package org.kernelab.dougong.core;

import java.util.Map;

import org.kernelab.dougong.core.dml.AllColumns;

/**
 * The interface which could be selected from or joined.
 */
public interface View extends Text, Alias, Providable
{
	public View alias(String alias);

	public AllColumns all();

	/**
	 * Return the <name, Column> map.
	 * 
	 * @return
	 */
	public Map<String, Column> columns();

	/**
	 * Get the text of this object as a view which could be selected from. Alias
	 * name should be followed if given. Typically, subquery should be
	 * surrounded with brackets.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringViewed(StringBuilder buffer);
}
