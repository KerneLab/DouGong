package org.kernelab.dougong.core;

import java.util.Map;

import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Deletable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Updatable;

/**
 * The interface which could be selected from or joined.
 */
public interface View extends Text, Alias, Updatable, Deletable, Providable
{
	public View alias(String alias);

	/**
	 * Get an AllItems object from this View which stand for all columns in
	 * this View.
	 * 
	 * @return AllItems object from this View.
	 */
	public AllItems all();

	/**
	 * Return a <name, Item> map to describe the items could be selected from
	 * this View. The key of map must be the label of the item which means alias
	 * name if given, otherwise its name.
	 * 
	 * @return The items map.
	 */
	public Map<String, Item> items();

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
