package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractMembershipCondition;

public class OracleMembershipCondition extends AbstractMembershipCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}
}
