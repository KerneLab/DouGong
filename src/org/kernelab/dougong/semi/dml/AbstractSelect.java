package org.kernelab.dougong.semi.dml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Aliases;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.dml.test.NegativeSemiTestable;
import org.kernelab.dougong.core.util.AccessGather;
import org.kernelab.dougong.core.util.AccessGather.Access;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractSelect extends AbstractJoinable implements Select
{
	private String				alias		= null;

	private boolean				distinct	= false;

	private Expression[]		select		= null;

	private List<Pivot>			pivots		= new ArrayList<Pivot>();

	private Condition			startWith	= null;

	private Condition			connectBy	= null;

	private boolean				nocycle		= false;

	private Expression[]		groupBy		= null;

	private Condition			having		= null;

	private List<Setopr>		setopr		= new LinkedList<Setopr>();

	private Expression[]		orderBy		= null;

	private List<Item>			items		= new LinkedList<Item>();

	private Map<String, Item>	itemsMap	= null;

	private Expression			skip		= null;

	private Expression			rows		= null;

	private String				hint		= null;

	private WithDefinition		with		= null;

	private Set<String>			usingLabels	= null;

	@Override
	public Reference $(String refer)
	{
		return ref(refer);
	}

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
	public AbstractSelect alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	@Override
	public AllItems all()
	{
		return this.provider().provideAllItems(this);
	}

	@Override
	public AbstractSelect anti()
	{
		super.anti();
		return this;
	}

	@Override
	public AbstractSelect antiJoin(View view, Condition on)
	{
		super.antiJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect antiJoin(View view, ForeignKey rels)
	{
		return antiJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect antiJoin(View view, Item... using)
	{
		super.antiJoin(view, using);
		return this;
	}

	@Override
	public AbstractSelect as(String alias)
	{
		try
		{
			AbstractSelect select = this.clone();
			select.alias(alias);
			return select;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public RangeCondition between(Expression from, Expression to)
	{
		return provider().provideRangeCondition().between(this, from, to);
	}

	@Override
	protected AbstractSelect clone() throws CloneNotSupportedException
	{
		AbstractSelect clone = (AbstractSelect) super.clone();

		if (this.select != null)
		{
			clone.select = Utils.copy(this.select);
		}

		if (this.pivots != null)
		{
			clone.pivots = Utils.copy(this.pivots, new ArrayList<Pivot>());
		}

		if (this.groupBy != null)
		{
			clone.groupBy = Utils.copy(this.groupBy);
		}

		if (this.setopr != null)
		{
			clone.setopr = Utils.copy(this.setopr, new LinkedList<Setopr>());
		}

		if (this.orderBy != null)
		{
			clone.orderBy = Utils.copy(this.orderBy);
		}

		clone.items = null;
		clone.itemsMap = null;
		clone.usingLabels = null;

		return clone;
	}

	protected Condition connectBy()
	{
		return connectBy;
	}

	@Override
	public AbstractSelect connectBy(Condition connectBy)
	{
		this.connectBy = connectBy;
		return this;
	}

	@Override
	public AbstractSelect cross()
	{
		super.cross();
		return this;
	}

	@Override
	public AbstractSelect crossJoin(View view, Condition on)
	{
		super.crossJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect crossJoin(View view, ForeignKey rels)
	{
		return crossJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect crossJoin(View view, Item... using)
	{
		super.crossJoin(view, using);
		return this;
	}

	protected boolean distinct()
	{
		return distinct;
	}

	@Override
	public AbstractSelect distinct(boolean distinct)
	{
		this.distinct = distinct;
		return this;
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
	public AbstractSelect except(Select select)
	{
		setopr().add(provider().provideSetopr().setopr(Setopr.EXCEPT, select));
		return this;
	}

	/**
	 * Fill the vacancy aliases of each selected Column object with its
	 * {@link Field#getName()} if the NameMeta's name was specified but
	 * different to its field name. All those unsatisfied items will be skipped
	 * in this filling.
	 * 
	 * @return this Select object
	 */
	public AbstractSelect fillAliasByField()
	{
		Column column = null;
		Field field = null;

		for (Item item : this.items())
		{
			column = Tools.as(item, Column.class);

			if (column != null && column.alias() == null && (field = column.field()) != null)
			{
				String name = Utils.getNameFromMeta(field);

				if (Tools.notNullOrEmpty(name) && !Tools.equals(name, field.getName()))
				{
					column.alias(field.getName());
				}
			}
		}

		return this;
	}

	/**
	 * Fill the vacancy aliases of each selected Column object with
	 * {@link org.kernelab.dougong.core.meta.DataMeta#alias()}. All those
	 * unsatisfied items will be skipped in this filling.
	 * 
	 * @return this Select object
	 */
	public AbstractSelect fillAliasByMeta()
	{
		Column column = null;

		for (Item item : this.items())
		{
			column = Tools.as(item, Column.class);

			if (column != null && column.alias() == null)
			{
				column.alias(Utils.getDataAliasFromField(column.field()));
			}
		}

		return this;
	}

	@Override
	public AbstractSelect from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractSelect full()
	{
		super.full();
		return this;
	}

	@Override
	public AbstractSelect fullJoin(View view, Condition on)
	{
		super.fullJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect fullJoin(View view, Item... using)
	{
		super.fullJoin(view, using);
		return this;
	}

	@Override
	public ComparisonCondition ge(Expression expr)
	{
		return provider().provideComparisonCondition().eq(this, expr);
	}

	@Override
	protected View getLastFrom()
	{
		return froms().isEmpty() ? null : froms().get(froms().size() - 1);
	}

	@Override
	protected Join getLastJoin()
	{
		return joins().isEmpty() ? null : joins().get(joins().size() - 1);
	}

	protected Expression[] groupBy()
	{
		return groupBy;
	}

	@Override
	public AbstractSelect groupBy(Expression... exprs)
	{
		this.groupBy = exprs;
		return this;
	}

	@Override
	public ComparisonCondition gt(Expression expr)
	{
		return provider().provideComparisonCondition().gt(this, expr);
	}

	protected Condition having()
	{
		return having;
	}

	@Override
	public AbstractSelect having(Condition condition)
	{
		this.having = condition;
		return this;
	}

	protected String hint()
	{
		return hint;
	}

	@Override
	public AbstractSelect hint(String hint)
	{
		this.hint = hint;
		return this;
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
	public AbstractSelect inner()
	{
		super.inner();
		return this;
	}

	@Override
	public AbstractSelect innerJoin(View view, Condition on)
	{
		super.innerJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect innerJoin(View view, Item... using)
	{
		super.innerJoin(view, using);
		return this;
	}

	@Override
	public <T extends Insertable> Insert insert(T target, Column... columns)
	{
		return this.provider().provideInsert().into(target).columns(columns).select(this);
	}

	@Override
	public AbstractSelect intersect(Select select)
	{
		setopr().add(provider().provideSetopr().setopr(Setopr.INTERSECT, select));
		return this;
	}

	@Override
	public boolean isJoinUsing(String label)
	{
		return this.usingLabels != null && this.usingLabels.contains(label);
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

	public boolean isSetopr()
	{
		return this.setopr() != null && !this.setopr().isEmpty();
	}

	@Override
	public boolean isUsingByJoin()
	{
		return false;
	}

	@Override
	public Item item(String name)
	{
		return referItems().get(name);
	}

	@Override
	public List<Item> items()
	{
		if (this.items == null)
		{
			this.items = new LinkedList<Item>();

			Expression[] exprs = this.selects();

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

				for (Item item : items)
				{
					if (item != null)
					{
						if (item.isUsingByJoin())
						{
							if (!using.contains(item.label()))
							{
								this.items.add(item);
								using.add(item.label());
							}
						}
						else
						{
							this.items.add(item);
						}
					}
				}
			}
		}
		return this.items;
	}

	@Override
	public AbstractSelect join(View view, Condition on)
	{
		super.join(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect join(View view, ForeignKey rels)
	{
		return join(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect join(View view, Item... using)
	{
		super.join(view, using);
		return this;
	}

	@Override
	public Select joins(List<Join> joins)
	{
		super.joins(joins);
		return this;
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
	public void joinUsing(String... labels)
	{
		if (labels != null && labels.length > 0)
		{
			if (this.usingLabels == null)
			{
				this.usingLabels = new LinkedHashSet<String>();
			}
			for (String label : labels)
			{
				this.usingLabels.add(label);
			}
		}
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : Tools.substr(toStringExpress(new StringBuilder()), 0, 30);
	}

	@Override
	public ComparisonCondition le(Expression expr)
	{
		return provider().provideComparisonCondition().le(this, expr);
	}

	@Override
	public AbstractSelect left()
	{
		super.left();
		return this;
	}

	@Override
	public AbstractSelect leftJoin(View view, Condition on)
	{
		super.leftJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect leftJoin(View view, Item... using)
	{
		super.leftJoin(view, using);
		return this;
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
	public AbstractSelect limit(Expression rows)
	{
		this.rows = rows;
		return this;
	}

	@Override
	public AbstractSelect limit(Expression skip, Expression rows)
	{
		this.skip = skip;
		this.rows = rows;
		return this;
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
	public Result modulo(Expression operand)
	{
		return provider().provideModuloOperator().operate(this, operand);
	}

	@Override
	public Result multiply(Expression operand)
	{
		return provider().provideMultiplyOperator().operate(this, operand);
	}

	@Override
	public AbstractSelect natural()
	{
		super.natural();
		return this;
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

	protected boolean nocycle()
	{
		return nocycle;
	}

	@Override
	public AbstractSelect nocycle(boolean nocycle)
	{
		this.nocycle = nocycle;
		return this;
	}

	@Override
	public NegativeSemiTestable not()
	{
		return provider().provideNegativeSemiTestable(this);
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
	public AbstractSelect offset(Expression skip)
	{
		this.skip = skip;
		return this;
	}

	protected Expression[] orderBy()
	{
		return orderBy;
	}

	@Override
	public AbstractSelect orderBy(Expression... exprs)
	{
		this.orderBy = exprs;
		return this;
	}

	@Override
	public AbstractSelect outer()
	{
		super.outer();
		return this;
	}

	public Pivot pivot(Function... aggs)
	{
		return this.provider().providePivot().pivotOn(this).pivotAggs(aggs);
	}

	@Override
	public Result plus(Expression operand)
	{
		return provider().providePlusOperator().operate(this, operand);
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

	@Override
	public AbstractSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	public AbstractSelect recursive(boolean recursive)
	{
		super.recursive(recursive);
		return this;
	}

	@Override
	public Reference ref(String refer)
	{
		if (this.with() != null)
		{
			Reference ref = this.with().ref(refer);
			if (ref != null)
			{
				return ref;
			}
		}

		Item item = item(refer);
		if (item instanceof Reference)
		{
			return (Reference) item;
		}
		else if (item != null)
		{
			return provider().provideReference(this, item.label());
		}
		else
		{
			return null;
		}
	}

	protected List<Item> refer(View view, List<Item> items)
	{
		List<Item> list = new LinkedList<Item>();

		for (Item item : items)
		{
			list.add(provider().provideReference(view, item.label()));
		}

		return list;
	}

	@Override
	public Map<String, Item> referItems()
	{
		if (this.itemsMap == null)
		{
			this.itemsMap = new LinkedHashMap<String, Item>();

			if (selects() != null)
			{
				for (Expression expr : selects())
				{
					if (expr instanceof AllItems)
					{ // AllItems
						AllItems all = (AllItems) expr;

						if (all.view() == null)
						{
							for (View from : froms())
							{
								for (Entry<String, Item> entry : from.referItems().entrySet())
								{
									this.itemsMap.put(entry.getKey(),
											provider().provideReference(this, entry.getValue().label()));
								}
							}
							for (Join join : joins())
							{
								if (join.viewSelectable())
								{
									for (Entry<String, Item> entry : join.view().referItems().entrySet())
									{
										this.itemsMap.put(entry.getKey(),
												provider().provideReference(this, entry.getValue().label()));
									}
								}
							}
						}
						else
						{
							for (Entry<String, Item> entry : all.view().referItems().entrySet())
							{
								this.itemsMap.put(entry.getKey(),
										provider().provideReference(this, entry.getValue().label()));
							}
						}
					}
					else if (expr instanceof Aliases && ((Aliases) expr).aliases() != null)
					{ // Multi-Return-Columns Function
						for (String alias : ((Aliases) expr).aliases())
						{
							this.itemsMap.put(alias, provider().provideReference(this, alias));
						}
					}
					else if (expr instanceof Items)
					{ // Items list
						if (((Items) expr).list() != null)
						{
							Reference ref = null;

							for (Expression exp : ((Items) expr).list())
							{
								ref = provider().provideReference(this, Utils.getLabelOfExpression(exp));
								this.itemsMap.put(ref.name(), ref);
							}
						}
					}
					else
					{ // Single expression
						Reference ref = provider().provideReference(this, Utils.getLabelOfExpression(expr));
						this.itemsMap.put(ref.name(), ref);
					}
				}
			}
		}
		return this.itemsMap;
	}

	@Override
	public List<Item> resolveItems()
	{
		return Tools.listOfArray(new ArrayList<Item>(), this);
	}

	protected List<Item> resolveItemsFromViews()
	{
		List<Item> items = new LinkedList<Item>();

		Set<String> usingLabels = new HashSet<String>();

		for (View view : this.froms())
		{
			if (view instanceof Withable)
			{
				items.addAll(refer(view, AbstractWithsable.resolveAllItems(view)));
			}
			else
			{
				for (Item item : view.items())
				{
					if (item instanceof Column)
					{
						if (((Column) item).isPseudo())
						{
							continue;
						}
					}

					if (usingLabels.contains(item.label()))
					{
						continue;
					}

					items.add(provider().provideReference(view, item.label()));

					if (item.isUsingByJoin())
					{
						usingLabels.add(item.label());
					}
				}
			}
		}

		for (Join join : joins())
		{
			if (join.viewSelectable())
			{
				View view = join.view();
				if (view instanceof Withable)
				{
					items.addAll(refer(view, AbstractWithsable.resolveAllItems(view)));
				}
				else
				{
					for (Item item : view.items())
					{
						if (item instanceof Column)
						{
							if (((Column) item).isPseudo())
							{
								continue;
							}
						}

						if (usingLabels.contains(item.label()))
						{
							continue;
						}

						items.add(provider().provideReference(view, item.label()));

						if (item.isUsingByJoin())
						{
							usingLabels.add(item.label());
						}
					}
				}
			}
		}

		return items;
	}

	@Override
	public AbstractSelect right()
	{
		super.right();
		return this;
	}

	@Override
	public AbstractSelect rightJoin(View view, Condition on)
	{
		super.rightJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect rightJoin(View view, Item... using)
	{
		super.rightJoin(view, using);
		return this;
	}

	@Override
	public RegexpLikeCondition rLike(Expression pattern)
	{
		return provider().provideRegexpCondition().rLike(this, pattern);
	}

	/**
	 * The expression which indicates the rows should be returned at most.<br />
	 * Returns {@code null} which means not be specified.
	 */
	protected Expression rows()
	{
		return rows;
	}

	@Override
	public AbstractSelect select(Expression... exprs)
	{
		this.select = exprs;
		this.items = null;
		this.itemsMap = null;
		return this;
	}

	@Override
	public AbstractSelect selectOver(Expression... exprs)
	{
		Map<String, Item> ups = new LinkedHashMap<String, Item>();
		for (Item item : this.resolveItemsFromViews())
		{
			ups.put(Utils.getLabelOfExpression(item), item);
		}

		Map<String, Expression> ovs = new LinkedHashMap<String, Expression>();
		for (Expression expr : exprs)
		{
			for (String label : Utils.getLabelsOfExpression(expr))
			{
				ovs.put(label, expr);
			}
		}

		List<Expression> sels = new LinkedList<Expression>();
		Set<String> add = new HashSet<String>();
		for (Entry<String, Item> entry : ups.entrySet())
		{
			if (!ovs.containsKey(entry.getKey()))
			{
				sels.add(entry.getValue());
			}
			else
			{
				Expression exp = ovs.get(entry.getKey());
				String[] lbs = Utils.getLabelsOfExpression(exp);
				boolean added = false;
				for (String l : lbs)
				{
					if (add.contains(l))
					{
						added = true;
						break;
					}
				}
				if (!added)
				{
					sels.add(exp);
					for (String l : lbs)
					{
						add.add(l);
					}
				}
			}
		}

		for (String a : add)
		{
			ovs.remove(a);
		}

		for (Expression ov : new LinkedHashSet<Expression>(ovs.values()))
		{
			sels.add(ov);
		}

		this.select = sels.toArray(new Expression[0]);
		this.items = null;
		this.itemsMap = null;
		return this;
	}

	protected Expression[] selects()
	{
		return select;
	}

	@Override
	public AbstractSelect semi()
	{
		super.semi();
		return this;
	}

	@Override
	public AbstractSelect semiJoin(View view, Condition on)
	{
		super.semiJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractSelect semiJoin(View view, ForeignKey rels)
	{
		return semiJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractSelect semiJoin(View view, Item... using)
	{
		super.semiJoin(view, using);
		return this;
	}

	public List<Setopr> setopr()
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

	protected Condition startWith()
	{
		return startWith;
	}

	@Override
	public AbstractSelect startWith(Condition startWith)
	{
		this.startWith = startWith;
		return this;
	}

	protected void textOfConnectBy(StringBuilder buffer)
	{
		if (connectBy() != null)
		{
			if (startWith() != null)
			{
				buffer.append(" START WITH ");

				startWith().toString(buffer);
			}

			buffer.append(" CONNECT BY ");

			if (nocycle())
			{
				buffer.append("NOCYCLE ");
			}

			connectBy().toString(buffer);
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
		if (having() != null && !having().isEmpty())
		{
			buffer.append(" HAVING ");
			having().toString(buffer);
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("SELECT");
	}

	protected void textOfHint(StringBuilder buffer)
	{
		String hint = this.provider().provideHint(this.hint());
		if (hint != null)
		{
			buffer.append(' ').append(hint);
		}
	}

	protected void textOfItems(StringBuilder buffer)
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

	protected void textOfSetopr(StringBuilder buffer)
	{
		for (Setopr opr : setopr())
		{
			if (opr != null)
			{
				opr.toString(buffer);
			}
		}
	}

	protected void textOfSetoprScoped(StringBuilder buffer)
	{
		for (Setopr opr : setopr())
		{
			if (opr != null)
			{
				opr.toStringScoped(buffer);
			}
		}
	}

	protected void textOfUnique(StringBuilder buffer)
	{
		if (distinct())
		{
			buffer.append(" DISTINCT");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T to(Class<T> cls)
	{
		if (Tools.isSubClass(cls, Subquery.class))
		{
			T t = (T) provider().provideSubquery((Class<? extends Subquery>) cls, this);
			if (t != null)
			{
				return t;
			}
		}
		return super.to(cls);
	}

	@Override
	public <T extends Subquery> T to(T subquery)
	{
		provider().provideProvider(subquery);
		subquery.select(this);
		return subquery;
	}

	@Override
	public Result toLower()
	{
		return provider().provideToLowerCase(this);
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	protected void toString(AbstractSelect select, StringBuilder buffer)
	{
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfConnectBy(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfOrder(buffer);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		if (select.isSetopr())
		{
			buffer.append('(');
		}
		this.toString(select, buffer);
		if (select.isSetopr())
		{
			buffer.append(')');
		}
		select.textOfSetopr(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return toStringUpdatable(buffer);
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		buffer.append('(');
		this.toStringScoped(buffer);
		buffer.append(')');
		return buffer;
	}

	@Override
	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		buffer.append('(');
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfWhere(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		buffer.append(')');
		return buffer;
	}

	protected void toStringScoped(AbstractSelect select, StringBuilder buffer)
	{
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfConnectBy(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		if (select.isSetopr())
		{
			buffer.append('(');
		}
		this.toStringScoped(select, buffer);
		if (select.isSetopr())
		{
			buffer.append(')');
		}
		select.textOfSetoprScoped(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.toStringExpress(buffer), this);
	}

	protected void toStringSourceOfBody(AbstractSelect select, StringBuilder buffer)
	{
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfConnectBy(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfOrder(buffer);
	}

	@Override
	public StringBuilder toStringSourceOfBody(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		if (select.isSetopr())
		{
			buffer.append('(');
		}
		this.toStringSourceOfBody(select, buffer);
		if (select.isSetopr())
		{
			buffer.append(')');
		}
		select.textOfSetopr(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringSourceOfWith(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		select.textOfWith(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		AbstractSelect select = prepare();
		buffer.append('(');
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfWhere(buffer);
		select.textOfOrder(buffer);
		buffer.append(')');
		return buffer;
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
	public AbstractSelect union(Select select)
	{
		setopr().add(provider().provideSetopr().setopr(Setopr.UNION, select));
		return this;
	}

	@Override
	public AbstractSelect unionAll(Select select)
	{
		setopr().add(provider().provideSetopr().setopr(Setopr.UNION_ALL, select));
		return this;
	}

	@Override
	public AbstractSelect where(Condition cond)
	{
		super.where(cond);
		AccessGather.gather(this, Access.TYPE_WHERE, cond);
		return this;
	}

	@Override
	public WithDefinition with()
	{
		return this.with;
	}

	@Override
	public AbstractSelect with(WithDefinition define)
	{
		this.with = define;
		return this;
	}

	@Override
	public AbstractSelect withs(List<WithDefinition> withs)
	{
		super.withs(withs);
		return this;
	}
}
