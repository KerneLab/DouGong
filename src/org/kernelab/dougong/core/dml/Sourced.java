package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface Sourced
{
	/**
	 * Get the object to be filtered.
	 */
	public View from();

	/**
	 * Set the object to be filtered.
	 */
	public Sourced from(View s);
}
