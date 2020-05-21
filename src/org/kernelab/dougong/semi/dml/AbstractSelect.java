package org.kernelab.dougong.semi.dml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.Withable;
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

public abstract class AbstractSelect extends AbstractFilterable implements Select
{
	private String					alias		= null;

	private boolean					distinct	= false;

	private Expression[]			select		= null;

	private List<View>				froms		= new ArrayList<View>();

	private List<Join>				joins		= new ArrayList<Join>();

	private Expression[]			groupBy		= null;

	private Condition				having		= null;

	private List<AbstractSetopr>	setopr		= new LinkedList<AbstractSetopr>();

	private Expression[]			orderBy		= null;

	private List<Item>				items		= new LinkedList<Item>();

	private Map<String, Item>		itemsMap	= null;

	private Expression				skip		= null;

	private Expression				rows		= null;

	public String alias()
	{
		return alias;
	}

	public AbstractSelect alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AllItems all()
	{
		return this.provider().provideAllItems(this);
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

	public Result divide(Expression operand)
	{
		return this.provideDivideOperator().operate(this, operand);
	}

	public ComparisonCondition eq(Expression expr)
	{
		return this.provideComparisonCondition().eq(this, expr);
	}

	public AbstractSelect fillAliasByMeta()
	{
		for (Item item : this.items())
		{
			Column column = Tools.as(item, Column.class);

			if (column != null && column.alias() == null)
			{
				column.alias(Utils.getDataAliasFromField(column.field()));
			}
		}

		return this;
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
		if (view != null)
		{
			froms().add(view);
		}
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

	public AbstractSelect fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
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

	public AbstractSelect innerJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).using(using));
		return this;
	}

	public AbstractSelect innerJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).on(on));
		return this;
	}

	public AbstractSelect innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	public <T extends Insertable> Insert insert(T target, Column... columns)
	{
		return this.provider().provideInsert().into(target).columns(columns).values(this);
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

	public Item item(String refer)
	{
		return itemsMap().get(refer);
	}

	public List<Item> items()
	{
		return items;
	}

	public Map<String, Item> itemsMap()
	{
		if (itemsMap == null)
		{
			itemsMap = new LinkedHashMap<String, Item>();

			if (select() != null)
			{
				for (Expression expr : select())
				{
					if (expr instanceof AllItems)
					{
						// AllItems
						AllItems all = (AllItems) expr;

						if (all.view() == null)
						{
							for (View from : froms())
							{
								for (Entry<String, Item> entry : from.itemsMap().entrySet())
								{
									itemsMap.put(entry.getKey(), this.provideReference(this, entry.getValue()));
								}
							}
							for (Join join : joins())
							{
								for (Entry<String, Item> entry : join.view().itemsMap().entrySet())
								{
									itemsMap.put(entry.getKey(), this.provideReference(this, entry.getValue()));
								}
							}
						}
						else
						{
							for (Entry<String, Item> entry : all.view().itemsMap().entrySet())
							{
								itemsMap.put(entry.getKey(), this.provideReference(this, entry.getValue()));
							}
						}
					}
					else if (expr instanceof Items)
					{
						// Items list
						if (((Items) expr).list() != null)
						{
							Reference ref = null;

							for (Expression exp : ((Items) expr).list())
							{
								ref = this.provideReference(this, exp);
								itemsMap.put(ref.name(), ref);
							}
						}
					}
					else
					{
						// Single expression
						Reference ref = this.provideReference(this, expr);
						itemsMap.put(ref.name(), ref);
					}
				}
			}
		}
		return itemsMap;
	}

	protected List<Join> joins()
	{
		return joins;
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

	public String label()
	{
		return alias() != null ? alias() : Tools.substr(toStringExpress(new StringBuilder()), 0, 30);
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

	public AbstractSelect leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	public LikeCondition like(Expression pattern)
	{
		return like(pattern, null);
	}

	public LikeCondition like(Expression pattern, Expression escape)
	{
		return this.provideLikeCondition().like(this, pattern, escape);
	}

	public AbstractSelect limit(Expression skip, Expression rows)
	{
		this.skip = skip;
		this.rows = rows;
		return this;
	}

	public ComparisonCondition lt(Expression expr)
	{
		return this.provideComparisonCondition().lt(this, expr);
	}

	public Result minus(Expression operand)
	{
		return this.provideMinusOperator().operate(this, operand);
	}

	public AbstractSelect minus(Select select)
	{
		setopr().add(new AbstractSetopr().setopr(Setopr.MINUS, select));
		return this;
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
		return notLike(pattern, null);
	}

	public LikeCondition notLike(Expression pattern, Expression escape)
	{
		return (LikeCondition) this.provideLikeCondition().like(this, pattern, escape).not();
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

	public Result plus(Expression operand)
	{
		return this.providePlusOperator().operate(this, operand);
	}

	/**
	 * Prepare a Select object for converting to text. Generally, this Select
	 * would be returned directly. If the Select need to be wrapped, then a new
	 * Select object should be returned.
	 * 
	 * @return The Select object which would be used to convert to text.
	 */
	protected AbstractSelect prepare()
	{
		return this;
	}

	public PrimaryKey primaryKey()
	{
		return null;
	}

	protected ComparisonCondition provideComparisonCondition()
	{
		return this.provider().provideComparisonCondition();
	}

	protected DivideOperator provideDivideOperator()
	{
		return this.provider().provideDivideOperator();
	}

	protected JointOperator provideJointOperator()
	{
		return this.provider().provideJointOperator();
	}

	protected LikeCondition provideLikeCondition()
	{
		return this.provider().provideLikeCondition();
	}

	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider().provideMembershipCondition();
	}

	protected MinusOperator provideMinusOperator()
	{
		return this.provider().provideMinusOperator();
	}

	protected MultiplyOperator provideMultiplyOperator()
	{
		return this.provider().provideMultiplyOperator();
	}

	protected NullCondition provideNullCondition()
	{
		return this.provider().provideNullCondition();
	}

	protected PlusOperator providePlusOperator()
	{
		return this.provider().providePlusOperator();
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

	protected Reference provideReference(View view, Expression expr)
	{
		return this.provider().provideReference(view, expr);
	}

	protected List<Item> refer(View view, List<Item> items)
	{
		List<Item> list = new LinkedList<Item>();

		for (Item item : items)
		{
			list.add(provideReference(view, item));
		}

		return list;
	}

	public List<Item> resolveItems()
	{
		return Tools.listOfArray(new ArrayList<Item>(), this);
	}

	protected List<Item> resolveItemsFromViews()
	{
		List<Item> items = new LinkedList<Item>();

		for (View view : froms())
		{
			for (Item item : view.items())
			{
				items.add(provider().provideReference(view, item));
			}
		}

		for (Join join : joins())
		{
			View view = join.view();
			for (Item item : view.items())
			{
				items.add(provider().provideReference(view, item));
			}
		}

		return items;
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

	public AbstractSelect rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	/**
	 * The expression which indicates the rows should be returned at most.<br />
	 * Returns {@code null} which means not be specified.
	 */
	protected Expression rows()
	{
		return rows;
	}

	protected Expression[] select()
	{
		return select;
	}

	public AbstractSelect select(Expression... exprs)
	{
		this.select = exprs;

		items().clear();

		if (exprs != null && exprs.length > 0)
		{
			List<Item> items = new LinkedList<Item>();

			for (Expression expr : exprs)
			{
				if (expr instanceof AllItems)
				{
					AllItems all = (AllItems) expr;
					if (all.view() != null)
					{
						if (all.view() instanceof Entity)
						{
							items.addAll(all.resolveItems());
						}
						else
						{
							items.addAll(this.refer(all.view(), all.resolveItems()));
						}
					}
					else
					{
						items.addAll(this.resolveItemsFromViews());
					}
				}
				else
				{
					items.addAll(expr.resolveItems());
				}
			}

			Set<String> using = new HashSet<String>();

			Column col = null;
			for (Item item : items)
			{
				if ((col = Tools.as(item, Column.class)) != null)
				{
					if (col.isUsingByJoin())
					{
						if (!using.contains(col.name()))
						{
							items().add(col);
							using.add(col.name());
						}
					}
					else
					{
						items().add(col);
					}
				}
				else
				{
					items().add(item);
				}
			}
		}

		return this;
	}

	protected List<AbstractSetopr> setopr()
	{
		return setopr;
	}

	/**
	 * The expression which indicates the rows should be skipped in the result.
	 * <br />
	 * Returns {@code null} which means not be specified.
	 */
	protected Expression skip()
	{
		return skip;
	}

	public void textOfAbstractSetopr(StringBuilder buffer)
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
	public void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM");

		boolean first = true;

		for (View fr : froms())
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

	public void textOfGroup(StringBuilder buffer)
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

	public void textOfHaving(StringBuilder buffer)
	{
		if (having() != null && !having().isEmpty())
		{
			buffer.append(" HAVING ");
			having().toString(buffer);
		}
	}

	public void textOfHead(StringBuilder buffer)
	{
		buffer.append("SELECT");

		if (distinct())
		{
			buffer.append(" DISTINCT");
		}
	}

	public void textOfItems(StringBuilder buffer)
	{
		boolean first = true;

		for (Item item : items())
		{
			if (item != null)
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
				item.toStringSelected(buffer);
			}
		}
	}

	public void textOfJoin(StringBuilder buffer)
	{
		for (Join join : joins())
		{
			buffer.append(' ');
			join.toString(buffer);
		}
	}

	public void textOfOrder(StringBuilder buffer)
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
		AbstractSelect select = prepare();
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfAbstractSetopr(buffer);
		select.textOfOrder(buffer);
		return buffer;
	}

	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return toStringUpdatable(buffer);
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		buffer.append('(');
		this.toStringScoped(buffer);
		buffer.append(')');
		return buffer;
	}

	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		buffer.append('(');
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfWhere(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		buffer.append(')');
		return buffer;
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfAbstractSetopr(buffer);
		return buffer;
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.toStringExpress(buffer), this);
	}

	public StringBuilder toStringSource(StringBuilder buffer)
	{
		return this.toString(buffer);
	}

	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		buffer.append('(');
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfWhere(buffer);
		select.textOfOrder(buffer);
		buffer.append(')');
		return buffer;
	}

	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		buffer.append('(');
		this.toString(buffer);
		buffer.append(')');
		return this.provider().provideOutputAlias(buffer, this);
	}

	public StringBuilder toStringWith(StringBuilder buffer)
	{
		buffer.append('(');
		this.toString(buffer);
		buffer.append(')');
		return buffer;
	}

	public Subquery toSubquery(Class<? extends Subquery> cls)
	{
		return this.provider().provideSubquery(cls != null ? cls : AbstractSubquery.class, this);
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

	@Override
	public AbstractSelect with(List<Withable> with)
	{
		super.with(with);
		return this;
	}
}
