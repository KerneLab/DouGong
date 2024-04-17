package org.kernelab.dougong.semi.dml.opr;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractCaseDecideExpression extends AbstractCaseExpression implements CaseDecideExpression
{
	private List<Relation<Condition, Expression>> whens = new LinkedList<Relation<Condition, Expression>>();

	@Override
	public AbstractCaseDecideExpression els(Expression expr)
	{
		return (AbstractCaseDecideExpression) super.els(expr);
	}

	@Override
	protected AbstractCaseDecideExpression replicate()
	{
		AbstractCaseDecideExpression clone = (AbstractCaseDecideExpression) super.replicate();
		clone.whens(this.whens());
		return clone;
	}

	@Override
	protected void textOfWhen(StringBuilder buffer)
	{
		for (Relation<Condition, Expression> pair : whens())
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

	@Override
	public AbstractCaseDecideExpression when(Condition cond, Expression then)
	{
		whens().add(new Relation<Condition, Expression>(cond, then));
		return this;
	}

	@Override
	public List<Relation<Condition, Expression>> whens()
	{
		return whens;
	}

	protected AbstractCaseDecideExpression whens(List<Relation<Condition, Expression>> whens)
	{
		this.whens().clear();
		this.whens().addAll(whens);
		return this;
	}
}
