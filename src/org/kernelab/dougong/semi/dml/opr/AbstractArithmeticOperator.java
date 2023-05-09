package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.ModuloOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.NegativeOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.dml.opr.Result;

public class AbstractArithmeticOperator extends AbstractOperator
		implements PlusOperator, MinusOperator, NegativeOperator, MultiplyOperator, DivideOperator, ModuloOperator
{
	private String operator;

	public AbstractArithmeticOperator(String operator)
	{
		this.operator = operator;
	}

	@Override
	public Result operate(Expression operand)
	{
		return new AbstractUnaryResult(operator(), operand).provider(provider());
	}

	@Override
	public Result operate(Expression operand1, Expression operand2)
	{
		return new AbstractBinaryResult(operator(), operand1, operand2).provider(provider());
	}

	public String operator()
	{
		return operator;
	}
}
