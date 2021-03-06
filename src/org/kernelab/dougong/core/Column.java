package org.kernelab.dougong.core;

import java.lang.reflect.Field;

import org.kernelab.basis.Castable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Sortable;

public interface Column extends Named, Item, Sortable, Castable
{
	public Column as(String alias);

	/**
	 * Get the field of class which represents the table containing this column.
	 * 
	 * @return
	 */
	public Field field();

	/**
	 * Determine whether this column is using by a join. The Default value is
	 * false.
	 * 
	 * @return
	 */
	public boolean isUsingByJoin();

	/**
	 * Get the string of this Column including the table alias if specified but
	 * excluding the column alias. The table alias would disappear when the
	 * table alias is not specified. It might also disappear when this column is
	 * using by a join in some SQL language.
	 */
	public StringBuilder toString(StringBuilder buffer);

	/**
	 * Get the string of this Column including the table alias if specified and
	 * including the column alias. The table alias would disappear when the
	 * table alias is not specified. It might also disappear when this column is
	 * using by a join in some SQL language.
	 */
	public StringBuilder toStringSelected(StringBuilder buffer);

	/**
	 * Specify whether this column is using by a join or not. The Default value
	 * is false.
	 * 
	 * @param using
	 * @return The column object itself.
	 */
	public Column usingByJoin(boolean using);

	/**
	 * Get the View object which this Column belongs to.
	 * 
	 * @return
	 */
	public View view();
}
