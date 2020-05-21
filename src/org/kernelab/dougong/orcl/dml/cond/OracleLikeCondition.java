package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;

public class OracleLikeCondition extends AbstractLikeCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}
}
