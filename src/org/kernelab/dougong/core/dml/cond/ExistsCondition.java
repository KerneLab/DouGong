package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.semi.dml.AbstractSelect;

public interface ExistsCondition extends ComposableCondition, UnaryCondition
{
	public ExistsCondition exists(AbstractSelect select);
}
