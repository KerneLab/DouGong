package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.semi.dml.opr.AbstractBinaryResult;

public class OracleBinaryResult extends AbstractBinaryResult
{
	public OracleBinaryResult(String operator, Expression operand1, Expression operand2)
	{
		super(operator, operand1, operand2);
	}

	@Override
	protected OracleBinaryResult replicate()
	{
		return (OracleBinaryResult) new OracleBinaryResult(operator(), operand1(), operand2()).provider(provider());
	}
}
