package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;

public abstract class AbstractArithmeticOperator extends AbstractOperator implements PlusOperator, MinusOperator,
		MultiplyOperator, DivideOperator
{
	private String	operator;

	public AbstractArithmeticOperator(String operator)
	{
		this.operator = operator;
	}

	public String operator()
	{
		return operator;
	}
}
