package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.test.NegativeSemiTestable;

public class AbstractNegativeSemiTestable extends AbstractExpression implements NegativeSemiTestable, Providable
{
	private Provider	provider;

	private Expression	expression;

	public AbstractNegativeSemiTestable(Expression expression)
	{
		this.expression(expression);
	}

	protected Expression expression()
	{
		return expression;
	}

	protected AbstractNegativeSemiTestable expression(Expression expression)
	{
		this.expression = expression;
		return this;
	}

	@Deprecated
	@Override
	public NegativeSemiTestable not()
	{
		return this;
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public Providable provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected AbstractExpression replicate()
	{
		return provider().provideProvider(new AbstractNegativeSemiTestable(this.expression()));
	}

	@Deprecated
	@Override
	public List<Item> resolveItems()
	{
		return null;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return toStringExpress(buffer);
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		this.expression().toStringExpress(buffer);
		buffer.append(" NOT");
		return buffer;
	}

	@Deprecated
	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toStringExpress(buffer);
	}
}
