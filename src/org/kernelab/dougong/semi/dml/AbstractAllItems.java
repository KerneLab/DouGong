package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.opr.Result;

public abstract class AbstractAllItems implements AllItems
{
	private View view;

	public AbstractAllItems(View view)
	{
		this.view = view;
	}

	public RangeCondition between(Expression from, Expression to)
	{
		return null;
	}

	public Result divide(Expression operand)
	{
		return null;
	}

	public ComparisonCondition eq(Expression expr)
	{
		return null;
	}

	public ComparisonCondition ge(Expression expr)
	{
		return null;
	}

	public ComparisonCondition gt(Expression expr)
	{
		return null;
	}

	public LikeCondition iLike(Expression pattern)
	{
		return null;
	}

	public LikeCondition iLike(Expression pattern, Expression escape)
	{
		return null;
	}

	public MembershipCondition in(Scope scope)
	{
		return null;
	}

	public NullCondition isNotNull()
	{
		return null;
	}

	public NullCondition isNull()
	{
		return null;
	}

	public Result joint(Expression... operands)
	{
		return null;
	}

	public ComparisonCondition le(Expression expr)
	{
		return null;
	}

	public LikeCondition like(Expression pattern)
	{
		return null;
	}

	public LikeCondition like(Expression pattern, Expression escape)
	{
		return null;
	}

	public ComparisonCondition lt(Expression expr)
	{
		return null;
	}

	public Result minus(Expression operand)
	{
		return null;
	}

	public Result multiply(Expression operand)
	{
		return null;
	}

	public ComparisonCondition ne(Expression expr)
	{
		return null;
	}

	public RangeCondition notBetween(Expression from, Expression to)
	{
		return null;
	}

	public LikeCondition notILike(Expression pattern)
	{
		return null;
	}

	public LikeCondition notILike(Expression pattern, Expression escape)
	{
		return null;
	}

	public MembershipCondition notIn(Scope scope)
	{
		return null;
	}

	public LikeCondition notLike(Expression pattern)
	{
		return null;
	}

	public LikeCondition notLike(Expression pattern, Expression escape)
	{
		return null;
	}

	public Result plus(Expression operand)
	{
		return null;
	}

	public List<Item> resolveItems()
	{
		return view.items();
	}

	@Override
	public Result toLower()
	{
		return null;
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public Result toUpper()
	{
		return null;
	}

	public View view()
	{
		return view;
	}
}
