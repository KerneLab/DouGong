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
	protected OracleJointResult replicate()
	{
		return this.provider().provideProvider(
				(OracleJointResult) new OracleJointResult(operator(), operands()).replicateOrderOf(this));
	}
}
