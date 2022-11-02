package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface RegexpLikeCondition extends NegatableCondition, BinaryCondition
{
	public RegexpLikeCondition rLike(Expression expr, Expression pattern);
}
