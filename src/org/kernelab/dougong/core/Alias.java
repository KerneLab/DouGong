package org.kernelab.dougong.core;

public interface Alias extends Cloneable
{
	/**
	 * Get the alias name.<br />
	 * Could be null if not specified.
	 * 
	 * @return
	 */
	public String alias();

	/**
	 * Return this Object with the given alias name.
	 * 
	 * @param alias
	 * @return
	 */
	public Alias alias(String alias);

	/**
	 * Get the text with alias name.
	 * 
	 * @return
	 */
	public StringBuilder toStringAliased(StringBuilder buffer);
}
