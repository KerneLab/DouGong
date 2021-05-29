package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
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
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractItems extends AbstractExpression implements Items, Providable
{
	private Provider		provider;

	private Expression[]	list;

	private String			alias	= null;

	public AbstractItems()
	{
	}

	public String alias()
	{
		return alias;
	}

	public AbstractItems alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractItems as(String alias)
	{
		return this.replicate().alias(alias);
	}

	public Expression[] list()
	{
		return list;
	}

	public AbstractItems list(Expression... expr)
	{
		this.list = expr;
		return this;
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

	public Provider provider()
	{
		return provider;
	}

	public AbstractItems provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

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

	protected abstract AbstractItems replicate();

	public List<Item> resolveItems()
	{
		List<Item> list = new LinkedList<Item>();

		for (Expression expr : list())
		{
			list.addAll(expr.resolveItems());
		}

		return list;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
			}
		}

		return buffer;
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExprInScope(buffer, expr);
			}
		}
		return buffer;
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
				Utils.outputAlias(this.provider(), buffer, expr);
			}
		}

		return buffer;
	}
}
