package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.cond.LikeCondition;

public abstract class AbstractLikeCondition extends AbstractNegatableCondition implements LikeCondition
{
	protected Expression	expr;

	protected String		pattern;

	public AbstractLikeCondition like(Expression expr, String pattern)
	{
		return this.set(expr, pattern);
	}

	public AbstractLikeCondition set(Expression expr, String pattern)
	{
		this.expr = expr;
		this.pattern = pattern;
		return this;
	}
}
