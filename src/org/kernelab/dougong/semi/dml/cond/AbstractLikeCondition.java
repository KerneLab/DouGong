package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.LikeCondition;

public abstract class AbstractLikeCondition extends AbstractNegatableCondition implements LikeCondition
{
	public static final String	ESCAPE	= "\\";

	protected Expression		expr;

	protected Expression		pattern;

	protected Expression		escape;

	public AbstractLikeCondition like(Expression expr, Expression pattern, Expression escape)
	{
		return this.set(expr, pattern, escape);
	}

	public AbstractLikeCondition set(Expression expr, Expression pattern, Expression escape)
	{
		this.expr = expr;
		this.pattern = pattern;
		this.escape = escape;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toStringExpress(buffer);
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" LIKE ");
		this.pattern.toStringExpress(buffer);
		buffer.append(" ESCAPE ");
		(this.escape != null ? this.escape : provider().provideLikePatternDefaultEscape()).toStringExpress(buffer);
		return buffer;
	}
}
