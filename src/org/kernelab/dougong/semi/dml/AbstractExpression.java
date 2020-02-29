package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.JointOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.semi.AbstractCastable;

public abstract class AbstractExpression extends AbstractCastable implements Expression
{
	public RangeCondition between(Expression from, Expression to)
	{
		return this.provideRangeCondition().between(this, from, to);
	}

	public Result divide(Expression operand)
	{
		return this.provideDivideOperator().operate(this, operand);
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

	public Result joint(Expression... operands)
	{
		Expression[] exprs = new Expression[1 + (operands == null ? 0 : operands.length)];

		exprs[0] = this;

		if (operands != null)
		{
			System.arraycopy(operands, 0, exprs, 1, operands.length);
		}

		return this.provideJointOperator().operate(exprs);
	}

	public ComparisonCondition le(Expression expr)
	{
		return this.provideComparisonCondition().le(this, expr);
	}

	public LikeCondition like(Expression pattern)
	{
		return this.provideLikeCondition().like(this, pattern);
	}

	public ComparisonCondition lt(Expression expr)
	{
		return this.provideComparisonCondition().lt(this, expr);
	}

	public Result minus(Expression operand)
	{
		return this.provideMinusOperator().operate(this, operand);
	}

	public Result multiply(Expression operand)
	{
		return this.provideMultiplyOperator().operate(this, operand);
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

	public LikeCondition notLike(Expression pattern)
	{
		return (LikeCondition) this.provideLikeCondition().like(this, pattern).not();
	}

	public Result plus(Expression operand)
	{
		return this.providePlusOperator().operate(this, operand);
	}

	protected abstract ComparisonCondition provideComparisonCondition();

	protected abstract DivideOperator provideDivideOperator();

	protected abstract JointOperator provideJointOperator();

	protected abstract LikeCondition provideLikeCondition();

	protected abstract MembershipCondition provideMembershipCondition();

	protected abstract MinusOperator provideMinusOperator();

	protected abstract MultiplyOperator provideMultiplyOperator();

	protected abstract NullCondition provideNullCondition();

	protected abstract PlusOperator providePlusOperator();

	protected abstract RangeCondition provideRangeCondition();

	protected abstract AbstractExpression replicate();
}
