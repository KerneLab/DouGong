package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.dml.Expression;

public interface UnaryOperator
{
	public Result operate(Expression operand);
}
