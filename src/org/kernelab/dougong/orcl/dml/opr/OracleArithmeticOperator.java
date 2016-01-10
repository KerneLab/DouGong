package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.semi.dml.opr.AbstractArithmeticOperator;

public class OracleArithmeticOperator extends AbstractArithmeticOperator
{
	public OracleArithmeticOperator(String operator)
	{
		super(operator);
	}

	public Result operate(Expression operand1, Expression operand2)
	{
		return new OracleBinaryResult(operator(), operand1, operand2).provider(provider());
	}
}
