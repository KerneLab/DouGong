package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Text;

public interface Source extends Text
{
	/**
	 * Get the text of the object as a data source of Insert without WITH
	 * clause.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringSourceOfBody(StringBuilder buffer);

	/**
	 * Get the WITH clause text of the object as a data source of Insert.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringSourceOfWith(StringBuilder buffer);
}
