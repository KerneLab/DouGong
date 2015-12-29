package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Expression;

public interface NullCondition extends ComposableCondition, NegatableCondition
{
	public NullCondition isNull(Expression expr);
}
