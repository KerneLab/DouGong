package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface NullCondition extends ComposableCondition, NegatableCondition, UnaryCondition
{
	public NullCondition isNull(Expression expr);
}
