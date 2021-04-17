package org.kernelab.dougong.core;

import org.kernelab.basis.Castable;

public interface Providable extends Castable
{
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
	public Providable provider(Provider provider);
}
