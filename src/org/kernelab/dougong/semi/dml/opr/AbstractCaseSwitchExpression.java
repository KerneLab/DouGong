package org.kernelab.dougong.semi.dml.opr;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractCaseSwitchExpression extends AbstractCaseExpression implements CaseSwitchExpression
{
	private Expression								caseValue;

	private List<Relation<Expression, Expression>>	whens	= new LinkedList<Relation<Expression, Expression>>();

	@Override
	public AbstractCaseSwitchExpression as(String alias)
	{
		return ((AbstractCaseSwitchExpression) super.as(alias)) //
				.caseValue(this.caseValue()) //
				.whens(this.whens());
	}

	public Expression caseValue()
	{
		return caseValue;
	}

	public AbstractCaseSwitchExpression caseValue(Expression cas)
	{
		this.caseValue = cas;
		return this;
	}

	@Override
	public AbstractCaseSwitchExpression els(Expression expr)
	{
		return (AbstractCaseSwitchExpression) super.els(expr);
	}

	@Override
	protected void textOfHead(StringBuilder buffer)
	{
		super.textOfHead(buffer);
		buffer.append(' ');
		Utils.outputExpr(buffer, caseValue());
	}

	@Override
	protected void textOfWhen(StringBuilder buffer)
	{
		for (Relation<Expression, Expression> pair : whens())
		{
			if (pair != null)
			{
				buffer.append(" WHEN ");
				Utils.outputExpr(buffer, pair.getKey());
				buffer.append(" THEN ");
				Utils.outputExpr(buffer, pair.getValue());
			}
		}
	}

	public AbstractCaseSwitchExpression when(Expression when, Expression then)
	{
		whens().add(new Relation<Expression, Expression>(when, then));
		return this;
	}

	public List<Relation<Expression, Expression>> whens()
	{
		return whens;
	}

	protected AbstractCaseSwitchExpression whens(List<Relation<Expression, Expression>> whens)
	{
		this.whens().addAll(whens);
		return this;
	}
}
