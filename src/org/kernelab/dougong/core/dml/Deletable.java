package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Text;

public interface Deletable extends Text
{
	/**
	 * Get the text of this object as a delete target. Normally, group, having
	 * and set operate clause should not appear in the text if the object is a
	 * subquery.
	 */
	public StringBuilder toStringDeletable(StringBuilder buffer);
}
