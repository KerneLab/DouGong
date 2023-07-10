package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.dml.test.NegativeSemiTestable;

public abstract class AbstractAllItems implements AllItems
{
	private View view;

	public AbstractAllItems(View view)
	{
		this.view = view;
	}

	@Override
	public RangeCondition between(Expression from, Expression to)
	{
		return null;
	}

	@Override
	public Result divide(Expression operand)
	{
		return null;
	}

	@Override
	public ComparisonCondition eq(Expression expr)
	{
		return null;
	}

	@Override
	public ComparisonCondition ge(Expression expr)
	{
		return null;
	}

	@Override
	public ComparisonCondition gt(Expression expr)
	{
		return null;
	}

	@Override
	public LikeCondition iLike(Expression pattern)
	{
		return null;
	}

	@Override
	public LikeCondition iLike(Expression pattern, Expression escape)
	{
		return null;
	}

	@Override
	public MembershipCondition in(Scope scope)
	{
		return null;
	}

	@Override
	public NullCondition isNotNull()
	{
		return null;
	}

	@Override
	public NullCondition isNull()
	{
		return null;
	}

	@Override
	public Result joint(Expression... operands)
	{
		return null;
	}

	@Override
	public ComparisonCondition le(Expression expr)
	{
		return null;
	}

	@Override
	public LikeCondition like(Expression pattern)
	{
		return null;
	}

	@Override
	public LikeCondition like(Expression pattern, Expression escape)
	{
		return null;
	}

	@Override
	public ComparisonCondition lt(Expression expr)
	{
		return null;
	}

	@Override
	public Result minus(Expression operand)
	{
		return null;
	}

	@Override
	public Result modulo(Expression operand)
	{
		return null;
	}

	@Override
	public Result multiply(Expression operand)
	{
		return null;
	}

	@Override
	public ComparisonCondition ne(Expression expr)
	{
		return null;
	}

	@Override
	public Result negative()
	{
		return null;
	}

	@Deprecated
	@Override
	public NegativeSemiTestable not()
	{
		return null;
	}

	@Override
	public RangeCondition notBetween(Expression from, Expression to)
	{
		return null;
	}

	@Override
	public LikeCondition notILike(Expression pattern)
	{
		return null;
	}

	@Override
	public LikeCondition notILike(Expression pattern, Expression escape)
	{
		return null;
	}

	@Override
	public MembershipCondition notIn(Scope scope)
	{
		return null;
	}

	@Override
	public LikeCondition notLike(Expression pattern)
	{
		return null;
	}

	@Override
	public LikeCondition notLike(Expression pattern, Expression escape)
	{
		return null;
	}

	@Override
	public RegexpLikeCondition notRLike(Expression pattern)
	{
		return null;
	}

	@Override
	public Result plus(Expression operand)
	{
		return null;
	}

	protected Provider provider()
	{
		return view().provider();
	}

	@Override
	public List<Item> resolveItems()
	{
		if (view() instanceof Withable)
		{
			return AbstractWithsable.resolveAllItems(view());
		}

		List<Item> items = new LinkedList<Item>();

		for (Item item : view().items())
		{
			if (item instanceof Column)
			{
				if (((Column) item).isPseudo())
				{
					continue;
				}
			}
			items.add(item);
		}

		return items;
	}

	@Override
	public RegexpLikeCondition rLike(Expression pattern)
	{
		return null;
	}

	@Override
	public Result toLower()
	{
		return null;
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public Result toUpper()
	{
		return null;
	}

	@Override
	public View view()
	{
		return view;
	}
}
