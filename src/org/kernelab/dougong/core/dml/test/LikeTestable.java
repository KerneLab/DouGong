package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.cond.LikeCondition;

public interface LikeTestable
{
	public LikeCondition like(String pattern);

	public LikeCondition notLike(String pattern);
}
