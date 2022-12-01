package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Aliases;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Sortable;

public interface Function extends Named, Item, Aliases, Sortable, Member, Providable
{
	@Override
	public Function alias(String alias);

	@Override
	public Function aliases(String... aliases);

	public Expression[] args();

	@Override
	public Function as(String alias);

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
	 * To indicate whether this function is a pseudo or not. Generally, the name
	 * of pseudo function must NOT be surrounded with quotes. Empty brackets
	 * should NOT appear after the function name.
	 * 
	 * @return
	 */
	public boolean isPseudo();
}
