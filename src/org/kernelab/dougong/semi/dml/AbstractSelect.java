package org.kernelab.dougong.semi.dml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public abstract class AbstractSelect extends AbstractFilterable implements Select
{
	private String					alias		= null;

	private boolean					distinct	= false;

	private Expression[]			items		= null;

	private List<View>				froms		= new ArrayList<View>();

	private List<Join>				joins		= new ArrayList<Join>();

	private Expression[]			groupBy		= null;

	private Condition				having		= null;

	private List<AbstractSetopr>	setopr		= new LinkedList<AbstractSetopr>();

	private Expression[]			orderBy		= null;

	public String alias()
	{
		return alias;
	}

	public AbstractSelect alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractSelect as(String alias)
	{
		try
		{
			AbstractSelect select = (AbstractSelect) this.clone();

			select.alias(alias);

			return Tools.cast(select);
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}

	public RangeCondition between(Expression from, Expression to)
	{
		return this.provideRangeCondition().between(this, from, to);
	}

	protected boolean distinct()
	{
		return distinct;
	}

	public AbstractSelect distinct(boolean distinct)
	{
		this.distinct = distinct;
		return this;
	}

	public ComparisonCondition eq(Expression expr)
	{
		return this.provideComparisonCondition().eq(this, expr);
	}

	/**
	 * Get the first view specified by from.
	 * 
	 * @return
	 */
	@Override
	public View from()
	{
		return froms().isEmpty() ? null : froms().get(0);
	}

	/**
	 * Add a view to the views' set.
	 * 
	 * @param view
	 * @return
	 */
	@Override
	public AbstractSelect from(View view)
	{
		froms().add(view);
		return this;
	}

	protected List<View> froms()
	{
		return froms;
	}

	public AbstractSelect fullJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.FULL_JOIN, view, view.alias()).using(using));
		return this;
	}

	public AbstractSelect fullJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.FULL_JOIN, view, view.alias()).on(on));
		return this;
	}

	public ComparisonCondition ge(Expression expr)
	{
		return this.provideComparisonCondition().eq(this, expr);
	}

	protected View getLastFrom()
	{
		return froms().isEmpty() ? null : froms().get(froms().size() - 1);
	}

	protected Join getLastJoin()
	{
		return joins().isEmpty() ? null : joins().get(joins().size() - 1);
	}

	protected Expression[] groupBy()
	{
		return groupBy;
	}

	public AbstractSelect groupBy(Expression... exprs)
	{
		this.groupBy = exprs;
		return this;
	}

	public ComparisonCondition gt(Expression expr)
	{
		return this.provideComparisonCondition().gt(this, expr);
	}

	protected Condition having()
	{
		return having;
	}

	public AbstractSelect having(Condition condition)
	{
		this.having = condition;
		return this;
	}

	public MembershipCondition in(Scope scope)
	{
		return this.provideMembershipCondition().in(this, scope);
	}

	public AbstractSelect intersect(Select select)
	{
		setopr().add(new AbstractSetopr().setopr(Setopr.INTERSECT, select));
		return this;
	}

	public NullCondition isNotNull()
	{
		return (NullCondition) this.provideNullCondition().isNull(this).not();
	}

	public NullCondition isNull()
	{
		return this.provideNullCondition().isNull(this);
	}

	public AbstractSelect join(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).using(using));
		return this;
	}

	public AbstractSelect join(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).on(on));
		return this;
	}

	protected List<Join> joins()
	{
		return joins;
	}

	public ComparisonCondition le(Expression expr)
	{
		return this.provideComparisonCondition().le(this, expr);
	}

	public AbstractSelect leftJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.LEFT_JOIN, view, view.alias()).using(using));
		return this;
	}

	public AbstractSelect leftJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.LEFT_JOIN, view, view.alias()).on(on));
		return this;
	}

	public LikeCondition like(String pattern)
	{
		return this.provideLikeCondition().like(this, pattern);
	}

	public ComparisonCondition lt(Expression expr)
	{
		return this.provideComparisonCondition().lt(this, expr);
	}

	public AbstractSelect minus(Select select)
	{
		setopr().add(new AbstractSetopr().setopr(Setopr.MINUS, select));
		return this;
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

	protected Expression[] orderBy()
	{
		return orderBy;
	}

	public AbstractSelect orderBy(Expression... exprs)
	{
		this.orderBy = exprs;
		return this;
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

	@Override
	public AbstractSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	protected RangeCondition provideRangeCondition()
	{
		return this.provider().provideRangeCondition();
	}

	public AbstractSelect rightJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.RIGHT_JOIN, view, view.alias()).using(using));
		return this;
	}

	public AbstractSelect rightJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.RIGHT_JOIN, view, view.alias()).on(on));
		return this;
	}

	protected Expression[] select()
	{
		return items;
	}

	public AbstractSelect select(Expression... exprs)
	{
		this.items = exprs;
		return this;
	}

	protected List<AbstractSetopr> setopr()
	{
		return setopr;
	}

	protected void textOfAbstractSetopr(StringBuilder buffer)
	{
		for (AbstractSetopr opr : setopr())
		{
			if (opr != null)
			{
				opr.toString(buffer);
			}
		}
	}

	@Override
	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM");

		boolean first = true;

		for (View fr : froms())
		{
			if (fr != null)
			{
				if (first)
				{
					buffer.append(' ');
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				fr.toStringViewed(buffer);
			}
		}
	}

	protected void textOfGroup(StringBuilder buffer)
	{
		if (groupBy() != null && groupBy().length > 0)
		{
			buffer.append(" GROUP BY");

			boolean first = true;

			for (Expression expr : groupBy())
			{
				if (first)
				{
					buffer.append(' ');
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
			}
		}
	}

	protected void textOfHaving(StringBuilder buffer)
	{
		if (having() != null)
		{
			buffer.append(" HAVING ");
			having().toString(buffer);
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("SELECT");

		if (distinct())
		{
			buffer.append(" DISTINCT");
		}
	}

	protected void textOfItems(StringBuilder buffer)
	{
		boolean first = true;

		for (Expression expr : select())
		{
			if (expr != null)
			{
				if (first)
				{
					buffer.append(' ');
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				expr.toStringSelected(buffer);
			}
		}
	}

	protected void textOfJoin(StringBuilder buffer)
	{
		for (Join join : joins())
		{
			if (join != null)
			{
				buffer.append(' ');
				join.toString(buffer);
			}
		}
	}

	protected void textOfOrder(StringBuilder buffer)
	{
		if (orderBy() != null && orderBy().length > 0)
		{
			buffer.append(" ORDER BY");

			boolean first = true;

			for (Expression expr : orderBy())
			{
				if (first)
				{
					buffer.append(' ');
					first = false;
				}
				else
				{
					buffer.append(',');
				}

				if (expr instanceof Sortable)
				{
					((Sortable) expr).toStringOrdered(buffer);
				}
				else
				{
					Utils.outputExpr(buffer, expr);
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfItems(buffer);
		this.textOfFrom(buffer);
		this.textOfJoin(buffer);
		this.textOfWhere(buffer);
		this.textOfGroup(buffer);
		this.textOfHaving(buffer);
		this.textOfAbstractSetopr(buffer);
		this.textOfOrder(buffer);
		return buffer;
	}

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		buffer.append('(');
		this.toStringScoped(buffer);
		buffer.append(')');
		return buffer;
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfItems(buffer);
		this.textOfFrom(buffer);
		this.textOfJoin(buffer);
		this.textOfWhere(buffer);
		this.textOfGroup(buffer);
		this.textOfHaving(buffer);
		this.textOfAbstractSetopr(buffer);
		return buffer;
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.toStringExpressed(buffer), this);
	}

	public AbstractSelect union(Select select)
	{
		setopr().add(new AbstractSetopr().setopr(Setopr.UNION, select));
		return this;
	}

	public AbstractSelect unionAll(Select select)
	{
		setopr().add(new AbstractSetopr().setopr(Setopr.UNION_ALL, select));
		return this;
	}

	@Override
	public AbstractSelect where(Condition condition)
	{
		super.where(condition);
		return this;
	}
}
