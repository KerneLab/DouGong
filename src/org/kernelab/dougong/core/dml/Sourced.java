package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface Sourced
{
	/**
	 * Get the object to be manipulated.
	 */
	public View from();

	/**
	 * Set the object to be manipulated.
	 */
	public Sourced from(View s);
}
