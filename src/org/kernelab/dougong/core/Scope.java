package org.kernelab.dougong.core;

public interface Scope extends Text
{
	/**
	 * Get the text of the object as scope.
	 */
	public StringBuilder toStringScoped(StringBuilder buffer);
}
