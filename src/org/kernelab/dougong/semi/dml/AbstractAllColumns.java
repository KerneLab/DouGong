package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllColumns;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractAllColumns extends AbstractReplicable implements AllColumns
{
	private View	view;

	public AbstractAllColumns(View view)
	{
		this.view = view;
	}

	public RangeCondition between(Expression from, Expression to)
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

	public ComparisonCondition le(Expression expr)
	{
		return null;
	}

	public LikeCondition like(String pattern)
	{
		return null;
	}

	public ComparisonCondition lt(Expression expr)
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

	public MembershipCondition notIn(Scope scope)
	{
		return null;
	}

	public LikeCondition notLike(String pattern)
	{
		return null;
	}

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public View view()
	{
		return view;
	}
}