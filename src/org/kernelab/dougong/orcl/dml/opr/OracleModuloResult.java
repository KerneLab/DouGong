package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.opr.AbstractBinaryResult;

public class OracleModuloResult extends AbstractBinaryResult
{
	public OracleModuloResult(String operator, Expression operand1, Expression operand2)
	{
		super(operator, operand1, operand2);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("MOD(");
		Utils.outputExpr(buffer, operand1());
		buffer.append(',');
		Utils.outputExpr(buffer, operand2());
		buffer.append(')');
		return buffer;
	}
}
