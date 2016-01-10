package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.Expression;

public interface VarinaryOperator
{
	public Result operate(Expression... operands);
}
