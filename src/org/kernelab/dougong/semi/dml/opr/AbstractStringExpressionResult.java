package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.util.Utils;

public class AbstractStringExpressionResult extends AbstractResult implements Providable
{
	private Provider	provider;

	private String		expression;

	public AbstractStringExpressionResult(String expression)
	{
		this.expression = expression;
	}

	public String expression()
	{
		return expression;
	}

	@Override
	protected AbstractStringExpressionResult newInstance()
	{
		return provider().provideProvider(new AbstractStringExpressionResult(expression()));
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractStringExpressionResult provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return Utils.outputExpr(buffer, expression());
	}
}
