package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.Expression;

public interface LikeCondition extends ComposableCondition, NegatableCondition
{
	public LikeCondition like(Expression expr, String pattern);
}
