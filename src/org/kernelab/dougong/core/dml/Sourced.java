package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface Sourced
{
	/**
	 * Get the view to be filtered.
	 * 
	 * @return
	 */
	public View from();

	/**
	 * Set the view to be filtered.
	 * 
	 * @param view
	 * @return
	 */
	public Sourced from(View view);
}
