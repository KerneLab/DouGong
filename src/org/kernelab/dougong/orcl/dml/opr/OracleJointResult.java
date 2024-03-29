package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.semi.dml.opr.AbstractVarinaryResult;

public class OracleJointResult extends AbstractVarinaryResult
{
	public OracleJointResult(String operator, Expression[] operands)
	{
		super(operator, operands);
	}

	@Override
	protected OracleJointResult newInstance()
	{
		return this.provider().provideProvider(new OracleJointResult(operator(), operands()));
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append('(');
		super.toString(buffer);
		buffer.append(')');
		return buffer;
	}
}
