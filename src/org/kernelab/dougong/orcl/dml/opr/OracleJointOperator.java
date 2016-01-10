package org.kernelab.dougong.orcl.dml.opr;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.opr.JointOperator;
import org.kernelab.dougong.semi.dml.opr.AbstractOperator;

public class OracleJointOperator extends AbstractOperator implements JointOperator
{
	public OracleJointResult operate(Expression... operands)
	{
		return (OracleJointResult) new OracleJointResult("||", operands).provider(provider());
	}
}
