package org.kernelab.dougong.core;

import java.util.List;
import java.util.Map;

import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Deletable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Updatable;

/**
 * The interface which could be selected from or joined.
 */
public interface View extends Text, Alias, Updatable, Deletable, Providable
{
	/**
	 * Get the Item associated with the given name in this View. <br />
	 * This method is alias of {@code ref(String)}.
	 * 
	 * @param refer
	 *            The reference name of the Item.
	 * @return The reference.
	 * @see View#ref(String)
	 */
	public Reference $(String refer);

	public View alias(String alias);

	/**
	 * Get an AllItems object from this View which stand for all columns in this
	 * View.
	 * 
	 * @return AllItems object from this View.
	 */
	public AllItems all();

	/**
	 * Get the Item associated with the given name in this View.
	 * 
	 * @param refer
	 *            The reference name of the Item.
	 * @return The reference.
	 */
	public Reference ref(String refer);

	/**
	 * Get the real items on this view which including the items represented by
	 * {@code *}.
	 * 
	 * @return
	 */
	public List<Item> items();

	/**
	 * Return a <name, Item> map to describe the items could be selected from
	 * this View. The key of map must be the reference name of the item which
	 * might be alias name or its own name.
	 * 
	 * @return The items map.
	 */
	public Map<String, Item> referItems();

	/**
	 * Get the text of this object as a view which could be selected from. Alias
	 * name should be followed if given. Typically, subquery should be
	 * surrounded with brackets. Notice that if this view was referenced by with
	 * clause, this method must output the "with name" together with alias.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringViewed(StringBuilder buffer);
}
