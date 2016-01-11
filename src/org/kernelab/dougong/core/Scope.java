package org.kernelab.dougong.core;

public interface Scope extends Text
{
	/**
	 * Get the text of the object as scope without the surrounding brackets
	 * which would be used in membership condition. Generally, the order clause
	 * is not required.
	 */
	public StringBuilder toStringScoped(StringBuilder buffer);
}
