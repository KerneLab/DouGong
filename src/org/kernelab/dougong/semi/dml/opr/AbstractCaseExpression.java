package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.CaseExpression;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public abstract class AbstractCaseExpression extends AbstractSortable implements CaseExpression
{
	private Expression els;

	@Override
	public AbstractCaseExpression alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@Override
	public AbstractCaseExpression as(String alias)
	{
		return (AbstractCaseExpression) super.as(alias);
	}

	@Override
	public Expression els()
	{
		return els;
	}

	@Override
	public AbstractCaseExpression els(Expression expr)
	{
		this.els = expr;
		return this;
	}

	@Override
	protected AbstractCaseExpression replicate()
	{
		AbstractCaseExpression clone = (AbstractCaseExpression) super.replicate();
		clone.els(this.els());
		return clone;
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("CASE");
	}

	protected void textOfTail(StringBuilder buffer)
	{
		if (els() != null)
		{
			buffer.append(" ELSE ");
			Utils.outputExpr(buffer, els());
		}
		buffer.append(" END");
	}

	protected abstract void textOfWhen(StringBuilder buffer);

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		textOfHead(buffer);
		textOfWhen(buffer);
		textOfTail(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.toStringExpress(buffer), this);
	}
}
