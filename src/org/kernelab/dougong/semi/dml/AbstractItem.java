package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
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

public abstract class AbstractItem extends AbstractExpression implements Item
{
	private String alias = null;

	public String alias()
	{
		return alias;
	}

	public AbstractItem alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractItem as(String alias)
	{
		return this.replicate().alias(alias);
	}

	public String label()
	{
		return alias() != null ? alias() : Tools.substr(this.toStringExpress(new StringBuilder()), 0, 30);
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

	protected abstract Provider provider();

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return provider().provideRangeCondition();
	}

	@Override
	protected Result provideToLowerCase(Expression expr)
	{
		return this.provider().provideToLowerCase(expr);
	}

	@Override
	protected Result provideToUpperCase(Expression expr)
	{
		return this.provider().provideToUpperCase(expr);
	}

	/**
	 * Return a new Object which has the most same properties to this Object.
	 * <br />
	 * Typically, the alias name should not be considered.
	 * 
	 * @return
	 */
	protected abstract AbstractItem replicate();

	public List<Item> resolveItems()
	{
		return listOf((Item) this);
	}
}
