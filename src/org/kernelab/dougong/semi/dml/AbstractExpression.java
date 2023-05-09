package org.kernelab.dougong.semi.dml;

import java.util.ArrayList;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.semi.AbstractCastable;

public abstract class AbstractExpression extends AbstractCastable implements Expression
{
	@Override
	public RangeCondition between(Expression from, Expression to)
	{
		return provider().provideRangeCondition().between(this, from, to);
	}

	@Override
	public Result divide(Expression operand)
	{
		return provider().provideDivideOperator().operate(this, operand);
	}

	@Override
	public ComparisonCondition eq(Expression expr)
	{
		return provider().provideComparisonCondition().eq(this, expr);
	}

	@Override
	public ComparisonCondition ge(Expression expr)
	{
		return provider().provideComparisonCondition().ge(this, expr);
	}

	@Override
	public ComparisonCondition gt(Expression expr)
	{
		return provider().provideComparisonCondition().gt(this, expr);
	}

	@Override
	public LikeCondition iLike(Expression pattern)
	{
		return iLike(pattern, null);
	}

	@Override
	public LikeCondition iLike(Expression pattern, Expression escape)
	{
		return provider().provideLikeCondition().like(provider().provideToUpperCase(this),
				provider().provideToUpperCase(pattern), escape);
	}

	@Override
	public MembershipCondition in(Scope scope)
	{
		return provider().provideMembershipCondition().in(this, scope);
	}

	@Override
	public NullCondition isNotNull()
	{
		return (NullCondition) provider().provideNullCondition().isNull(this).not();
	}

	@Override
	public NullCondition isNull()
	{
		return provider().provideNullCondition().isNull(this);
	}

	@Override
	public Result joint(Expression... operands)
	{
		Expression[] exprs = new Expression[1 + (operands == null ? 0 : operands.length)];

		exprs[0] = this;

		if (operands != null)
		{
			System.arraycopy(operands, 0, exprs, 1, operands.length);
		}

		return provider().provideJointOperator().operate(exprs);
	}

	@Override
	public ComparisonCondition le(Expression expr)
	{
		return provider().provideComparisonCondition().le(this, expr);
	}

	@Override
	public LikeCondition like(Expression pattern)
	{
		return like(pattern, null);
	}

	@Override
	public LikeCondition like(Expression pattern, Expression escape)
	{
		return provider().provideLikeCondition().like(this, pattern, escape);
	}

	protected <T> List<T> listOf(T... es)
	{
		return Tools.listOfArray(new ArrayList<T>(es.length), es);
	}

	@Override
	public ComparisonCondition lt(Expression expr)
	{
		return provider().provideComparisonCondition().lt(this, expr);
	}

	@Override
	public Result minus(Expression operand)
	{
		return provider().provideMinusOperator().operate(this, operand);
	}

	@Override
	public Result modulo(Expression operand)
	{
		return provider().provideModuloOperator().operate(this, operand);
	}

	@Override
	public Result multiply(Expression operand)
	{
		return provider().provideMultiplyOperator().operate(this, operand);
	}

	@Override
	public ComparisonCondition ne(Expression expr)
	{
		return provider().provideComparisonCondition().ne(this, expr);
	}

	@Override
	public Result negative()
	{
		return provider().provideNegativeOperator().operate(this);
	}

	@Override
	public RangeCondition notBetween(Expression from, Expression to)
	{
		return (RangeCondition) provider().provideRangeCondition().between(this, from, to).not();
	}

	@Override
	public LikeCondition notILike(Expression pattern)
	{
		return notILike(pattern, null);
	}

	@Override
	public LikeCondition notILike(Expression pattern, Expression escape)
	{
		return (LikeCondition) provider().provideLikeCondition()
				.like(provider().provideToUpperCase(this), provider().provideToUpperCase(pattern), escape).not();
	}

	@Override
	public MembershipCondition notIn(Scope scope)
	{
		return (MembershipCondition) provider().provideMembershipCondition().in(this, scope).not();
	}

	@Override
	public LikeCondition notLike(Expression pattern)
	{
		return notLike(pattern, null);
	}

	@Override
	public LikeCondition notLike(Expression pattern, Expression escape)
	{
		return (LikeCondition) provider().provideLikeCondition().like(this, pattern, escape).not();
	}

	@Override
	public RegexpLikeCondition notRLike(Expression pattern)
	{
		return (RegexpLikeCondition) provider().provideRegexpCondition().rLike(this, pattern).not();
	}

	@Override
	public Result plus(Expression operand)
	{
		return provider().providePlusOperator().operate(this, operand);
	}

	protected abstract Provider provider();

	protected abstract AbstractExpression replicate();

	@Override
	public RegexpLikeCondition rLike(Expression pattern)
	{
		return provider().provideRegexpCondition().rLike(this, pattern);
	}

	@Override
	public Result toLower()
	{
		return provider().provideToLowerCase(this);
	}

	@Override
	public Result toUpper()
	{
		return provider().provideToUpperCase(this);
	}
}
