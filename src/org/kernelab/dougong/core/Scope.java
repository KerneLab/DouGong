package org.kernelab.dougong.core;

public interface Scope extends Text
{
	/**
	 * Get the text of the object as scope without the surrounding brackets.
	 */
	public StringBuilder toStringScoped(StringBuilder buffer);
}
