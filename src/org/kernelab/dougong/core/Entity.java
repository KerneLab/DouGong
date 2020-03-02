package org.kernelab.dougong.core;

import org.kernelab.dougong.core.ddl.PrimaryKey;

/**
 * The interface in whose sub classes Column members would be defined.
 */
public interface Entity extends View
{
	/**
	 * Return the PrimaryKey of this view.
	 * 
	 * @return The PrimaryKey or null if not defined.
	 */
	public PrimaryKey primaryKey();
}
