package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Text;

public interface Source extends Text
{
	/**
	 * Get the text of the object as a data source of Insert.
	 */
	public StringBuilder toStringSource(StringBuilder buffer);
}
