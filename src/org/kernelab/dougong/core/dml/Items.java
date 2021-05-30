package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Scope;

public interface Items extends Expression, Scope, Item
{
	/**
	 * Get a copy of this Items with the given alias.
	 * 
	 * @param alias
	 * @return
	 */
	public Items as(String alias);

	public Expression[] list();

	public Items list(Expression... items);
}
