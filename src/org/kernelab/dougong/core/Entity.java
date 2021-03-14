package org.kernelab.dougong.core;

import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;

/**
 * The interface in whose sub classes Column members, primary key and foreign
 * keys would be defined.
 */
public interface Entity extends View
{
	/**
	 * Return the AbsoluteKey of this view.
	 * 
	 * @return The AbsoluteKey or null if not defined.
	 */
	public AbsoluteKey absoluteKey();

	/**
	 * Find a foreign key of given name and refers to the reference.
	 * 
	 * @param name
	 * @param reference
	 * @return The foreign key or null if could not find one.
	 */
	public ForeignKey foreignKey(String name, Entity reference);

	/**
	 * Return the PrimaryKey of this view.
	 * 
	 * @return The PrimaryKey or null if not defined.
	 */
	public PrimaryKey primaryKey();
}
