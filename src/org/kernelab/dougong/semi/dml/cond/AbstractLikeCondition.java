package org.kernelab.dougong.semi.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractLikeCondition extends AbstractNegatableCondition implements LikeCondition
{
	public static final String	ESCAPE	= "\\";

	protected Expression		expr;

	protected Expression		pattern;

	protected Expression		escape;

	@Override
	public AbstractLikeCondition like(Expression expr, Expression pattern, Expression escape)
	{
		return this.set(expr, pattern, escape);
	}

	@Override
	public Expression operand(int pos)
	{
		switch (pos)
		{
			case 0:
				return this.expr;
			case 1:
				return this.pattern;
			case 2:
				return this.escape;
			default:
				return null;
		}
	}

	@Override
	public AbstractLikeCondition operand(int pos, Expression opr)
	{
		switch (pos)
		{
			case 0:
				this.expr = opr;
				break;
			case 1:
				this.pattern = opr;
				break;
			case 2:
				this.escape = opr;
				break;
			default:
				break;
		}
		return this;
	}

	@Override
	public List<Expression> operands()
	{
		return Utils.arrayList(this.expr, this.pattern, this.escape);
	}

	public AbstractLikeCondition set(Expression expr, Expression pattern, Expression escape)
	{
		this.expr = expr;
		this.pattern = pattern;
		this.escape = escape;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toStringExpress(buffer);
		if (this.not)
		{
			buffer.append(" " + LogicalCondition.NOT);
		}
		buffer.append(" LIKE ");
		this.pattern.toStringExpress(buffer);
		buffer.append(" ESCAPE ");
		(this.escape != null ? this.escape : provider().provideLikePatternDefaultEscape()).toStringExpress(buffer);
		return buffer;
	}
}
