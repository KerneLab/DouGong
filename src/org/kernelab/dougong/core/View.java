package org.kernelab.dougong.core;

public interface View extends Text, Alias
{
	public View alias(String alias);

	/**
	 * Get the SQL Provider.
	 * 
	 * @return
	 */
	public Provider provider();

	/**
	 * Specify a SQL Provider.
	 * 
	 * @param provider
	 * @return
	 */
	public View provider(Provider provider);
}
