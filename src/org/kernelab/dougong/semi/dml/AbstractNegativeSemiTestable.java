package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.test.NegativeSemiTestable;

public class AbstractNegativeSemiTestable extends AbstractExpression implements NegativeSemiTestable, Providable
{
	private Provider	provider;

	private Expression	expression;

	public AbstractNegativeSemiTestable(Expression expression)
	{
		this.expression(expression);
	}

	@Override
	public RangeCondition between(Expression from, Expression to)
	{
		return expression().notBetween(from, to);
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

	@Override
	public LikeCondition iLike(Expression pattern)
	{
		return expression().notILike(pattern);
	}

	@Override
	public LikeCondition iLike(Expression pattern, Expression escape)
	{
		return expression().notILike(pattern, escape);
	}

	@Override
	public MembershipCondition in(Scope scope)
	{
		return expression().notIn(scope);
	}

	@Override
	public LikeCondition like(Expression pattern)
	{
		return expression().notLike(pattern);
	}

	@Override
	public LikeCondition like(Expression pattern, Expression escape)
	{
		return expression().notLike(pattern, escape);
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
	public RegexpLikeCondition rLike(Expression pattern)
	{
		return expression().notRLike(pattern);
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
