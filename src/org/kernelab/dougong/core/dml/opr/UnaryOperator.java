package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.Expression;

public interface UnaryOperator
{
	public Result operate(Expression operand);
}
