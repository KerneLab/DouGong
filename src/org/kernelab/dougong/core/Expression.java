package org.kernelab.dougong.core;

public interface Expression extends Text
{
	/**
	 * Get the text of this object as an expression which could be computed and
	 * compared. Typically, without alias name but subquery should be surrounded
	 * with brackets.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringExpressed(StringBuilder buffer);

	/**
	 * Get the text of this object as an selected item which should followed by
	 * alias name.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringSelected(StringBuilder buffer);
}
