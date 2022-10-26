package org.kernelab.dougong.core.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;

public interface AtomicCondition extends ComposableCondition, ReflectiveCondition
{
	/**
	 * Get an operand in this condition at the certain position.
	 * 
	 * @param pos
	 * @return
	 */
	public Expression operand(int pos);

	/**
	 * Set the operand to this condition at the certain position.
	 * 
	 * @param pos
	 * @param opr
	 */
	public ReflectiveCondition operand(int pos, Expression opr);

	/**
	 * Get operands in this condition excluding any operator.
	 * 
	 * @return
	 */
	public List<Expression> operands();
}
