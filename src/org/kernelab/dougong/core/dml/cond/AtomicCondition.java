package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface AtomicCondition extends ComposableCondition, ReflectiveCondition
{
	/**
	 * Get an operand in this condition at the certain (zero-based) position.
	 * 
	 * @param pos
	 * @return
	 */
	public Expression operand(int pos);

	/**
	 * Set the operand to this condition at the certain (zero-based) position.
	 * 
	 * @param pos
	 * @param opr
	 */
	public ReflectiveCondition operand(int pos, Expression opr);

	/**
	 * Get operands number in this condition excluding any operator.
	 * 
	 * @return
	 */
	public int operands();
}
