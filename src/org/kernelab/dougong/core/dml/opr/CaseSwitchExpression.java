package org.kernelab.dougong.core.dml.opr;

import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Expression;

public interface CaseSwitchExpression extends CaseExpression
{
	public Expression caseValue();

	public CaseSwitchExpression caseValue(Expression cas);

	public CaseSwitchExpression els(Expression expr);

	/**
	 * Add when branch to this case expression.
	 */
	public CaseSwitchExpression when(Expression when, Expression then);

	public List<Relation<Expression, Expression>> whens();
}
