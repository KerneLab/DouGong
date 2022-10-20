package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;

public class AbstractPriorExpression extends AbstractItem implements Providable
{
	public static final String	PRIOR	= "PRIOR";

	private Provider			provider;

	private Expression			expression;

	public AbstractPriorExpression(Expression expression)
	{
		this.expression(expression);
	}

	public Expression expression()
	{
		return expression;
	}

	public AbstractPriorExpression expression(Expression expression)
	{
		this.expression = expression;
		return this;
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	public AbstractPriorExpression provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected AbstractPriorExpression replicate()
	{
		return provider().provideProvider(new AbstractPriorExpression(this.expression()));
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(PRIOR);
		buffer.append(' ');
		return Utils.outputExpr(buffer, this.expression());
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}
}
