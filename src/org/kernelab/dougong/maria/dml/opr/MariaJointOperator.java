package org.kernelab.dougong.maria.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.JointOperator;
import org.kernelab.dougong.semi.dml.opr.AbstractOperator;

public class MariaJointOperator extends AbstractOperator implements JointOperator
{
	@Override
	public MariaJointResult operate(Expression... operands)
	{
		return (MariaJointResult) new MariaJointResult(",", operands).provider(provider());
	}
}
