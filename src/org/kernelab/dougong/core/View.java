package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Deletable;
import org.kernelab.dougong.core.dml.Updatable;

/**
 * The interface which could be selected from or joined.
 */
public interface View extends Text, Alias, Entity, Updatable, Deletable, Providable
{
	public View alias(String alias);

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
