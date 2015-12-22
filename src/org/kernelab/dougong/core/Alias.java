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
	 * Set the alias name.
	 * 
	 * @param alias
	 * @return This object itself.
	 */
	public Alias alias(String alias);

	/**
	 * Get the text with alias name.
	 * 
	 * @return
	 */
	public StringBuilder toStringAliased(StringBuilder buffer);
}
