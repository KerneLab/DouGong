package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Expression;

public interface ComparisonCondition extends GroupCondition, BinaryCondition
{
	public static final String	EQUALS			= "=";

	public static final String	NOT_EQUALS		= "!=";

	public static final String	GREATER_EQUALS	= ">=";

	public static final String	GREATER_THAN	= ">";

	public static final String	LESS_EQUALS		= "<=";

	public static final String	LESS_THAN		= "<";

	public ComparisonCondition all();

	public ComparisonCondition any();

	public ComparisonCondition eq(Expression a, Expression b);

	public ComparisonCondition ge(Expression a, Expression b);

	public ComparisonCondition gt(Expression a, Expression b);

	public ComparisonCondition le(Expression a, Expression b);

	public ComparisonCondition lt(Expression a, Expression b);

	public ComparisonCondition ne(Expression a, Expression b);

	public ComparisonCondition some();
}
