package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.ModuloOperator;
import org.kernelab.dougong.semi.dml.opr.AbstractOperator;

public class OracleModuloOperator extends AbstractOperator implements ModuloOperator
{
	@Override
	public OracleModuloResult operate(Expression operand1, Expression operand2)
	{
		return (OracleModuloResult) new OracleModuloResult(ArithmeticOperable.MODULO, operand1, operand2)
				.provider(provider());
	}
}
