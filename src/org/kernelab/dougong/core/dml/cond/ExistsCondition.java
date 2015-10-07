package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.semi.dml.AbstractSelect;

public interface ExistsCondition extends ComposableCondition
{
	public ExistsCondition exists(AbstractSelect select);
}
