package org.kernelab.dougong.semi.dml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractEntity;

public class AbstractSubquery extends AbstractEntity implements Subquery
{
	private Select select;

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

	@Override
	public AllItems all()
	{
		return this.provider().provideAllItems(this);
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

	@Override
	public RangeCondition between(Expression from, Expression to)
	{
		return provider().provideRangeCondition().between(this, from, to);
	}

	@Override
	protected AbstractSubquery clone()
	{
		AbstractSubquery sq = null;
		try
		{
			sq = (AbstractSubquery) this.newInstance();
			sq.select(this.select().as(null));
			sq.provider(this.provider());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sq;
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
		return provider().provideComparisonCondition().eq(this, expr);
	}

	@Override
	protected Column getColumnByField(Field field)
	{
		String name = Utils.getNameFromField(field);
		return provider().provideColumn(this, name, field);
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
	public List<Item> items()
	{
		List<Item> raw = this.select().items();

		List<Item> items = new ArrayList<Item>(raw.size());

		for (Item item : raw)
		{
			items.add(this.provider().provideReference(this, item.label()));
		}

		return items;
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

	@Override
	public Map<String, Item> referItems()
	{
		return select().referItems();
	}

	@Override
	public List<Item> resolveItems()
	{
		return this.select().resolveItems();
	}

	@Override
	public RegexpLikeCondition rLike(Expression pattern)
	{
		return provider().provideRegexpCondition().rLike(this, pattern);
	}

	@Override
	public Select select()
	{
		return select;
	}

	@Override
	public AbstractSubquery select(Select select)
	{
		this.select = select;
		return this;
	}

	@Override
	public Result toLower()
	{
		return provider().provideToLowerCase(this);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return this.select().toString(buffer);
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return this.select().toStringDeletable(buffer);
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return this.select().toStringExpress(buffer);
	}

	@Override
	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		return this.select().toStringInsertable(buffer);
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return this.select().toStringScoped(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.select().toStringExpress(buffer), this);
	}

	@Override
	public StringBuilder toStringSourceOfBody(StringBuilder buffer)
	{
		return this.select().toStringSourceOfBody(buffer);
	}

	@Override
	public StringBuilder toStringSourceOfWith(StringBuilder buffer)
	{
		return this.select().toStringSourceOfWith(buffer);
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return this.select().toStringUpdatable(buffer);
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		if (this.with() != null)
		{
			return this.provider().provideOutputWithableAliased(buffer, this);
		}
		else
		{
			buffer.append('(');
			this.toString(buffer);
			buffer.append(')');
			return this.provider().provideOutputAlias(buffer, this);
		}
	}

	@Override
	public StringBuilder toStringWith(StringBuilder buffer)
	{
		buffer.append('(');
		this.toString(buffer);
		buffer.append(')');
		return buffer;
	}

	@Override
	public Result toUpper()
	{
		return provider().provideToUpperCase(this);
	}

	@Override
	public WithDefinition with()
	{
		return this.select().with();
	}

	@Override
	public AbstractSubquery with(WithDefinition define)
	{
		this.select().with(define);
		return this;
	}
}
