package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface Filterable extends Sourced
{
	public Filterable from(View view);

	/**
	 * Get the filter condition.
	 * 
	 * @return
	 */
	public Condition where();

	/**
	 * Set the filter condition.
	 * 
	 * @param cond
	 * @return
	 */
	public Filterable where(Condition cond);
}
