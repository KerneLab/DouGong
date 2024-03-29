package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.semi.dml.opr.AbstractVarinaryResult;

public class MariaJointResult extends AbstractVarinaryResult
{
	public MariaJointResult(String operator, Expression[] operands)
	{
		super(operator, operands);
	}

	@Override
	protected MariaJointResult newInstance()
	{
		return this.provider().provideProvider(new MariaJointResult(operator(), operands()));
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("CONCAT_WS('',");
		super.toString(buffer);
		return buffer.append(')');
	}
}
