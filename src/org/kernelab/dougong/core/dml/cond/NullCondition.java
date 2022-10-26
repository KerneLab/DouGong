package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface NullCondition extends NegatableCondition, UnaryCondition
{
	public NullCondition isNull(Expression expr);
}
