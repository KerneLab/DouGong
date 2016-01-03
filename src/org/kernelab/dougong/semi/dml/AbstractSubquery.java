package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public class AbstractSubquery extends AbstractView implements Subquery
{
	private Select	select;

	public AbstractSubquery()
	{
		super();
	}

	@Override
	public AbstractSubquery alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Subquery> T as(String alias)
	{
		AbstractSubquery sq = this.clone();
		if (sq != null)
		{
			sq.alias(alias);
		}
		return (T) sq;
	}

	public RangeCondition between(Expression from, Expression to)
	{
		return this.provideRangeCondition().between(this, from, to);
	}

	@Override
	protected AbstractSubquery clone()
	{
		AbstractSubquery sq = null;
		try
		{
			sq = this.getClass().newInstance();
			sq.select(this.select());
			sq.provider(this.provider());
		}
		catch (Exception e)
		{
		}
		return sq;
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

	protected ComparisonCondition provideComparisonCondition()
	{
		return this.provider().provideComparisonCondition();
	}

	protected LikeCondition provideLikeCondition()
	{
		return this.provider().provideLikeCondition();
	}

	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider().provideMembershipCondition();
	}

	protected NullCondition provideNullCondition()
	{
		return this.provider().provideNullCondition();
	}

	protected RangeCondition provideRangeCondition()
	{
		return this.provider().provideRangeCondition();
	}

	public Select select()
	{
		return select;
	}

	public AbstractSubquery select(Select select)
	{
		this.select = select;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		return this.select().toString(buffer);
	}

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		return this.select().toStringExpressed(buffer);
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return this.select().toStringScoped(buffer);
	}

	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		buffer.append('(');
		this.toString(buffer);
		buffer.append(')');
		return this.provider().provideOutputAlias(buffer, this);
	}
}
