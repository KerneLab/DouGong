package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;

public interface LikeTestable
{
	public LikeCondition iLike(Expression pattern);

	public LikeCondition iLike(Expression pattern, Expression escape);

	public LikeCondition like(Expression pattern);

	public LikeCondition like(Expression pattern, Expression escape);

	public LikeCondition notILike(Expression pattern);

	public LikeCondition notILike(Expression pattern, Expression escape);

	public LikeCondition notLike(Expression pattern);

	public LikeCondition notLike(Expression pattern, Expression escape);

	public RegexpLikeCondition notRLike(Expression pattern);

	public RegexpLikeCondition rLike(Expression pattern);
}
