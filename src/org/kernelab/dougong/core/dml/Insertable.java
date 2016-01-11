package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Text;

public interface Insertable extends Text
{
	/**
	 * Get the text of the object as Insert target.
	 */
	public StringBuilder toStringInsertable(StringBuilder buffer);
}
