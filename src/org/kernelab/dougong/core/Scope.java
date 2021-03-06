package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Expression;

public interface Scope extends Text, Expression
{
	/**
	 * Get the text of the object as scope without the surrounding brackets
	 * which would be used in membership condition. Generally, the order clause
	 * is not required.
	 */
	public StringBuilder toStringScoped(StringBuilder buffer);
}
