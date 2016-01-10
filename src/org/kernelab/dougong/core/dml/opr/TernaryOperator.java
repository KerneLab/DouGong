package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.Expression;

public interface TernaryOperator
{
	public Result operate(Expression operand1, Expression operand2, Expression operand3);
}
