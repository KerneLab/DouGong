package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractExistsCondition;

public class MariaExistsCondition extends AbstractExistsCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}
}
