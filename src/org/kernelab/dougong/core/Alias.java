package org.kernelab.dougong.core;

public interface Alias extends Cloneable
{
	/**
	 * Get the alias name.<br />
	 * Could be null which means none alias specified.
	 * 
	 * @return
	 */
	public String alias();

	/**
	 * Specify the alias name.
	 * 
	 * @param alias
	 * @return This object itself.
	 */
	public Alias alias(String alias);
}
