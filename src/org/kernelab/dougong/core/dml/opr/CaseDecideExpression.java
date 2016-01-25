package org.kernelab.dougong.core.dml.opr;

import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.Condition;

public interface CaseDecideExpression extends CaseExpression
{
	public CaseDecideExpression els(Expression expr);

	/**
	 * Add when branch to this case expression.
	 */
	public CaseDecideExpression when(Condition cond, Expression then);

	public List<Relation<Condition, Expression>> whens();
}
