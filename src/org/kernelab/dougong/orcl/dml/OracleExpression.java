package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.dml.Expression;
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
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractExpression;

public abstract class OracleExpression extends AbstractExpression implements Expression
{
	private OracleProvider provider;

	public OracleExpression(OracleProvider provider)
	{
		super();
		this.provider(provider);
	}

	@Override
	protected ComparisonCondition provideComparisonCondition()
	{
		return provider().provideComparisonCondition();
	}

	@Override
	protected DivideOperator provideDivideOperator()
	{
		return provider().provideDivideOperator();
	}

	@Override
	protected JointOperator provideJointOperator()
	{
		return provider().provideJointOperator();
	}

	@Override
	protected LikeCondition provideLikeCondition()
	{
		return provider().provideLikeCondition();
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return provider().provideMembershipCondition();
	}

	@Override
	protected MinusOperator provideMinusOperator()
	{
		return provider().provideMinusOperator();
	}

	@Override
	protected MultiplyOperator provideMultiplyOperator()
	{
		return provider().provideMultiplyOperator();
	}

	@Override
	protected NullCondition provideNullCondition()
	{
		return provider().provideNullCondition();
	}

	@Override
	protected PlusOperator providePlusOperator()
	{
		return provider().providePlusOperator();
	}

	protected OracleProvider provider()
	{
		return provider;
	}

	protected OracleExpression provider(OracleProvider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return provider().provideRangeCondition();
	}
}
