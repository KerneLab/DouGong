package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.semi.dml.opr.AbstractArithmeticOperator;

public class MariaArithmeticOperator extends AbstractArithmeticOperator
{
	public MariaArithmeticOperator(String operator)
	{
		super(operator);
	}

	public Result operate(Expression operand1, Expression operand2)
	{
		return new MariaBinaryResult(operator(), operand1, operand2).provider(provider());
	}
}
