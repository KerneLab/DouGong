package org.kernelab.dougong.core.dml;

public interface PriorExpression extends Expression
{
	public static final String PRIOR = "PRIOR";

	public Expression expression();
}
