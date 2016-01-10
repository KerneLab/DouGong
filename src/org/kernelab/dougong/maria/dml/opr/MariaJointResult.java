package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.semi.dml.opr.AbstractVarinaryResult;

public class MariaJointResult extends AbstractVarinaryResult
{
	public MariaJointResult(String operator, Expression[] operands)
	{
		super(operator, operands);
	}

	@Override
	protected MariaJointResult replicate()
	{
		return (MariaJointResult) new MariaJointResult(operator(), operands()).provider(provider());
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("CONCAT(");
		super.toString(buffer);
		return buffer.append(')');
	}
}
