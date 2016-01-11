package org.kernelab.dougong.core.dml;

public interface Updatable
{
	/**
	 * Get the text of this object as a update target. Normally, group, having
	 * and set operate clause should not appear in the text if the object is a
	 * subquery.
	 */
	public StringBuilder toStringUpdatable(StringBuilder buffer);
}
