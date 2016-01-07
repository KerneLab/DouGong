package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractExpression extends AbstractReplicable implements Expression
{
	public RangeCondition between(Expression from, Expression to)
	{
		return this.provideRangeCondition().between(this, from, to);
	}

	public ComparisonCondition eq(Expression expr)
	{
		return this.provideComparisonCondition().eq(this, expr);
	}

	public ComparisonCondition ge(Expression expr)
	{
		return this.provideComparisonCondition().eq(this, expr);
	}

	public ComparisonCondition gt(Expression expr)
	{
		return this.provideComparisonCondition().gt(this, expr);
	}

	public MembershipCondition in(Scope scope)
	{
		return this.provideMembershipCondition().in(this, scope);
	}

	public NullCondition isNotNull()
	{
		return (NullCondition) this.provideNullCondition().isNull(this).not();
	}

	public NullCondition isNull()
	{
		return this.provideNullCondition().isNull(this);
	}

	public ComparisonCondition le(Expression expr)
	{
		return this.provideComparisonCondition().le(this, expr);
	}

	public LikeCondition like(String pattern)
	{
		return this.provideLikeCondition().like(this, pattern);
	}

	public ComparisonCondition lt(Expression expr)
	{
		return this.provideComparisonCondition().lt(this, expr);
	}

	public ComparisonCondition ne(Expression expr)
	{
		return this.provideComparisonCondition().ne(this, expr);
	}

	public RangeCondition notBetween(Expression from, Expression to)
	{
		return (RangeCondition) this.provideRangeCondition().between(this, from, to).not();
	}

	public MembershipCondition notIn(Scope scope)
	{
		return (MembershipCondition) this.provideMembershipCondition().in(this, scope).not();
	}

	public LikeCondition notLike(String pattern)
	{
		return (LikeCondition) this.provideLikeCondition().like(this, pattern).not();
	}

	protected abstract ComparisonCondition provideComparisonCondition();

	protected abstract LikeCondition provideLikeCondition();

	protected abstract MembershipCondition provideMembershipCondition();

	protected abstract NullCondition provideNullCondition();

	protected abstract RangeCondition provideRangeCondition();

	protected abstract AbstractExpression replicate();
}
