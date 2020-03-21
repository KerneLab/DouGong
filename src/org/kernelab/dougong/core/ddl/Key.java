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
	 * To judge whether the given columns are all contained in this key.
	 * 
	 * @param columns
	 * @return
	 */
	public boolean contains(Column... columns);

	/**
	 * Get the entity this key belongs to.
	 * 
	 * @return
	 */
	public Entity entity();

	/**
	 * Get a typical query condition according to this key.
	 * 
	 * @return
	 */
	public Condition queryCondition();
}
