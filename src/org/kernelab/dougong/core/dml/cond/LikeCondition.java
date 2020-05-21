package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface LikeCondition extends ComposableCondition, NegatableCondition
{
	public LikeCondition like(Expression expr, Expression pattern, Expression escape);
}
