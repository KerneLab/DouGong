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

	@Deprecated
	public LikeCondition notILike(Expression pattern);

	@Deprecated
	public LikeCondition notILike(Expression pattern, Expression escape);

	@Deprecated
	public LikeCondition notLike(Expression pattern);

	@Deprecated
	public LikeCondition notLike(Expression pattern, Expression escape);

	@Deprecated
	public RegexpLikeCondition notRLike(Expression pattern);

	public RegexpLikeCondition rLike(Expression pattern);
}
