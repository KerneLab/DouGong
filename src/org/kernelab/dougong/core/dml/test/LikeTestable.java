package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.cond.LikeCondition;

public interface LikeTestable
{
	public LikeCondition like(Expression pattern);

	public LikeCondition notLike(Expression pattern);
}
