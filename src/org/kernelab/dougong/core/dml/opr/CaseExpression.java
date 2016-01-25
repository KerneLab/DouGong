package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.Item;

public interface CaseExpression extends Item
{
	public CaseExpression alias(String alias);

	public CaseExpression as(String alias);

	/**
	 * Get the value of else clause. Could be {@code null} which means not be
	 * specified.
	 */
	public Expression els();

	/**
	 * Set the value of else clause.
	 */
	public CaseExpression els(Expression expr);
}
