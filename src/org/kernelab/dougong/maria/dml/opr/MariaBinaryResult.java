package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.semi.dml.opr.AbstractBinaryResult;

public class MariaBinaryResult extends AbstractBinaryResult
{
	public MariaBinaryResult(String operator, Expression operand1, Expression operand2)
	{
		super(operator, operand1, operand2);
	}

	@Override
	protected MariaBinaryResult replicate()
	{
		return (MariaBinaryResult) new MariaBinaryResult(operator(), operand1(), operand2()).provider(provider());
	}
}
