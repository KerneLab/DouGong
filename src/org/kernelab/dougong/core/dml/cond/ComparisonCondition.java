package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.SingleItem;

public interface ComparisonCondition extends ComposableCondition
{
	public static final String	EQUALS			= "=";

	public static final String	NOT_EQUALS		= "!=";

	public static final String	GREATER_EQUALS	= ">=";

	public static final String	GREATER_THAN	= ">";

	public static final String	LESS_EQUALS		= "<=";

	public static final String	LESS_THAN		= "<";

	public ComparisonCondition eq(SingleItem a, SingleItem b);

	public ComparisonCondition ge(SingleItem a, SingleItem b);

	public ComparisonCondition gt(SingleItem a, SingleItem b);

	public ComparisonCondition le(SingleItem a, SingleItem b);

	public ComparisonCondition lt(SingleItem a, SingleItem b);

	public ComparisonCondition ne(SingleItem a, SingleItem b);
}
