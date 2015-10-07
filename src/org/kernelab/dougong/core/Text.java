package org.kernelab.dougong.core;

public interface Text
{
	/**
	 * Get the text of the object itself.<br />
	 * Normally without alias name.
	 * 
	 * @return
	 */
	public StringBuilder toString(StringBuilder buffer);
}
