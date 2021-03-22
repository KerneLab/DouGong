package org.kernelab.dougong.core.dml;

public interface Aliases extends Alias
{
	/**
	 * Get the alias names.<br />
	 * Could be null which means none alias specified.
	 * 
	 * @return
	 */
	public String[] aliases();

	/**
	 * Specify the alias names.<br />
	 * null or empty aliases array will be ignored.<br />
	 * If the aliases array has only one alias, this method should call
	 * {@link Alias#alias(String)}.
	 * 
	 * @param aliases
	 * @return This object itself.
	 */
	public Alias aliases(String... aliases);
}
