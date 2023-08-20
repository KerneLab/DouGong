package org.kernelab.dougong.core.util;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Filter;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.Row;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.PriorExpression;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.cond.AtomicCondition;

public class Recursor
{
	public static class CycleDetectedException extends SQLException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6604465561461711402L;

		public CycleDetectedException(String message)
		{
			super(message);
		}
	}

	public static interface Resulter
	{
		public Row result(Row row, LinkedList<Row> path, boolean isLeaf);
	}

	public static <R extends Map<String, ?>> String path(List<R> path, String key, String joint)
	{
		if (path == null || key == null || joint == null)
		{
			return null;
		}

		StringBuilder buff = new StringBuilder();

		for (Map<String, ?> node : path)
		{
			buff.append(joint);
			buff.append(node.get(key));
		}

		return buff.toString();
	}

	private Provider				provider;

	private Primitive				view;

	private Condition				startWith;

	private Condition				connectBy;

	private Mapper<Select, Select>	selectMapper;

	private boolean					nocycle		= false;

	private Expression[]			selects;

	private Map<String, String>		selectItems;

	private Filter<Row>				where;

	private Resulter				resulter;

	private Select					union;

	private boolean					distinct	= false;

	protected Condition connectBy()
	{
		return connectBy;
	}

	public Recursor connectBy(Condition connectBy)
	{
		this.connectBy = connectBy;
		return this;
	}

	public Recursor distinct()
	{
		this.distinct = true;
		return this;
	}

	protected Map<String, String> getSelectItems()
	{
		if (this.selectItems == null)
		{
			this.selectItems = new HashMap<String, String>();

			if (this.selects != null)
			{
				for (Expression expr : this.selects)
				{
					if (expr instanceof AllItems)
					{
						for (Item i : ((AllItems) expr).resolveItems())
						{
							this.selectItems.put(i.toString(), Utils.getLabelOfExpression(i));
						}
					}
					else
					{
						this.selectItems.put(expr.toString(), Utils.getLabelOfExpression(expr));
					}
				}
			}
		}
		return this.selectItems;
	}

	protected boolean isDistinct()
	{
		return distinct;
	}

	protected boolean isNocycle()
	{
		return nocycle;
	}

	public Recursor nocycle()
	{
		this.nocycle = true;
		return this;
	}

	protected Provider provider()
	{
		return provider;
	}

	public Recursor provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	public Canal<?, Row> query(SQLKit kit, Map<String, Object> params) throws SQLException
	{
		Select init = this.select(this.view.where(this.startWith)).select(selects);
		Select recur = this.select(this.view.where(this.connectBy)).select(selects);

		Mapper<Select, Select> mapper = selectMapper();
		if (mapper != null)
		{
			try
			{
				init = mapper.map(init);
				recur = mapper.map(recur);
			}
			catch (Exception e)
			{
				throw new SQLException("Could not convert recursive select", e);
			}
		}

		Condition cond = this.connectBy();
		List<Object> refls = Utils.reflectConditions(new LinkedList<Object>(), cond);
		Set<Column> extra = new HashSet<Column>();
		Set<String> prior = new LinkedHashSet<String>();
		replace(refls, extra, prior);
		String[] priors = prior.toArray(new String[0]);
		Set<String> extraNames = new HashSet<String>();
		if (!extra.isEmpty())
		{
			List<Expression> sel = new LinkedList<Expression>(init.items());
			sel.addAll(extra);
			Expression[] selects = sel.toArray(new Expression[0]);
			init.select(selects);
			recur.select(selects);
			for (Column ext : extra)
			{
				extraNames.add(ext.label());
			}
		}

		Collection<Row> res = kit.execute(init.toString(), params).getRows(new LinkedList<Row>(), Row.class);

		List<Row> recurs = new LinkedList<Row>();

		query(kit, params, res, new LinkedList<Row>(), recur.toString(), priors, new HashSet<Row>(), extraNames,
				recurs);

		Canal<?, Row> result = Canal.of(recurs);

		if (this.union() != null)
		{
			String[] labels = Canal.of(init.items()).map(new Mapper<Item, String>()
			{
				@Override
				public String map(Item el) throws Exception
				{
					return el.label();
				}
			}).collect().toArray(new String[0]);

			int i = 0;
			for (Item item : this.union().items())
			{
				if (item.alias() == null)
				{
					item.alias(labels[i]);
				}
				i++;
			}

			result = result.union(
					Canal.of(kit.execute(this.union().toString(), params).getRows(new LinkedList<Row>(), Row.class)));
		}

		if (this.isDistinct())
		{
			result = result.distinct();
		}

		return result;
	}

	protected void query(SQLKit kit, Map<String, Object> params, Collection<Row> rows, LinkedList<Row> path,
			String select, String[] priors, Set<Row> nodes, Set<String> extra, List<Row> results) throws SQLException
	{
		Row node = null, result = null;
		for (Row row : rows)
		{
			node = row.newRow(priors);

			if (nodes.contains(node))
			{
				if (this.isNocycle())
				{
					continue;
				}
				else
				{
					throw new CycleDetectedException(node.toString());
				}
			}
			else
			{
				nodes.add(node);
			}

			path.add(row.exclude(extra));

			Collection<Row> subs = kit.execute(select, Row.of(params).set(node)).getRows(new LinkedList<Row>(),
					Row.class);

			try
			{
				result = this.where() == null ? row : (this.where().filter(row) ? row : null);
			}
			catch (Exception e)
			{
				throw new SQLException("Could not filter recursive query result row", e);
			}

			result = result == null || this.result() == null ? result
					: this.result().result(result, path, subs.isEmpty());

			if (result != null)
			{
				results.add(result);
			}

			query(kit, params, subs, path, select, priors, nodes, extra, results);

			nodes.remove(node);
			path.removeLast();
		}
	}

	@SuppressWarnings("unchecked")
	protected void replace(List<Object> conds, Set<Column> extra, Set<String> priors) throws SQLException
	{
		Map<String, String> map = this.getSelectItems();

		for (Object o : conds)
		{
			if (o instanceof Condition)
			{
				if (o instanceof AtomicCondition)
				{
					AtomicCondition a = (AtomicCondition) o;
					for (int i = 0; i < a.operands(); i++)
					{
						PriorExpression pe = Tools.as(a.operand(i), PriorExpression.class);
						if (pe != null)
						{
							Column c = Tools.as(pe.expression(), Column.class);
							if (c != null)
							{
								String param = null;
								if (map.containsKey(c.toString()))
								{
									param = map.get(c.toString());
								}
								else
								{
									param = Utils.getLabelOfExpression(c);
									extra.add(c);
								}
								priors.add(param);
								a.operand(i, provider().provideParameter(param));
							}
							else
							{
								throw new SQLException("Non column prior expression " + pe + " occurred in condition");
							}
						}
					}
				}
				else
				{
					throw new SQLException("Non atomic condition " + o.toString() + " occurred in reflect list");
				}
			}
			else if (o instanceof List)
			{
				replace((List<Object>) o, extra, priors);
			}
		}
	}

	protected Resulter result()
	{
		return resulter;
	}

	public Recursor result(Resulter resulter)
	{
		this.resulter = resulter;
		return this;
	}

	public Recursor select(Expression... selects)
	{
		this.selects = selects;
		this.selectItems = null;
		return this;
	}

	protected Select select(Primitive view)
	{
		return view.select(this.selects);
	}

	protected Mapper<Select, Select> selectMapper()
	{
		return selectMapper;
	}

	public Recursor selectMapper(Mapper<Select, Select> selectMapper)
	{
		this.selectMapper = selectMapper;
		return this;
	}

	protected Condition startWith()
	{
		return startWith;
	}

	public Recursor startWith(Condition startWith)
	{
		this.startWith = startWith;
		return this;
	}

	protected Select union()
	{
		return union;
	}

	public Recursor union(Select union)
	{
		this.union = union;
		return this;
	}

	public Primitive view()
	{
		return view;
	}

	public Recursor view(Primitive view)
	{
		this.view = view;
		return this;
	}

	protected Filter<Row> where()
	{
		return where;
	}

	public Recursor where(Filter<Row> where)
	{
		this.where = where;
		return this;
	}
}
