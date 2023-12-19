package org.kernelab.dougong.core.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.dml.Condition;

public interface Key
{
	/**
	 * Get the columns in this key.
	 * 
	 * @return
	 */
	public Column[] columns();

	/**
	 * To determine whether the given columns are contained in this keys.
	 * 
	 * @param columns
	 * @return
	 */
	public boolean containsAll(Column... columns);

	/**
	 * Get the entity this key belongs to.
	 * 
	 * @return
	 */
	public Entity entity();

	/**
	 * Get a columns array that is in this key but not in the given columns'
	 * list.
	 * 
	 * @param excludes
	 * @return
	 */
	public Column[] excludeColumns(Column... excludes);

	/**
	 * Get the columns in the given Entity according the columns' name defined
	 * in this Key.
	 * 
	 * @param entity
	 * @return Column objects in the given Entity or null if the Entity was not
	 *         same to the Entity defined in this Key.
	 */
	public Column[] getColumnsOf(Entity entity);

	/**
	 * Get a zero-base index of the given column appears in this key. Return -2
	 * if the given column does not belong to the entity of this key. Or return
	 * -1 if not found in this key.
	 * 
	 * @param column
	 * @return
	 */
	public int indexOf(Column column);

	/**
	 * Get a typical query condition according to this key.
	 * 
	 * @return
	 */
	public Condition queryCondition();
}
