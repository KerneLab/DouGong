package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Aliases;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Sortable;

public interface Function extends Named, Item, Aliases, Sortable, Member, Providable
{
	public Function aliases(String... aliases);

	public Expression[] args();

	/**
	 * Get a copy of this Function with the given alias.
	 * 
	 * @param alias
	 * @return
	 */
	public Function as(String... aliases);

	/**
	 * Return a new instance of this function and set the arguments.
	 * 
	 * @param args
	 * @return
	 */
	public Function call(Expression... args);

	/**
	 * To indicate whether this function is a pseudo column or not. Generally,
	 * the name of pseudo column must NOT be surrounded with quotes. Empty
	 * brackets should NOT appear after the function name.
	 * 
	 * @return
	 */
	public boolean isPseudoColumn();
}
