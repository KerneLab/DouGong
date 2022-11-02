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
import org.kernelab.basis.JSON;
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
	public static class CycleDetectedException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7844178941583896393L;

		public CycleDetectedException(String message)
		{
			super(message);
		}
	}

	public static interface Resulter
	{
		public Row result(Row row, LinkedList<Row> path, boolean isLeaf);
	}

	private Provider			provider;

	private Primitive			view;

	private Condition			startWith;

	private Condition			connectBy;

	private boolean				nocycle	= false;

	private Expression[]		selects;

	private Map<String, String>	selectItems;

	private Resulter			resulter;

	private Select				union;

	protected Condition connectBy()
	{
		return connectBy;
	}

	public Recursor connectBy(Condition connectBy)
	{
		this.connectBy = connectBy;
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

	protected void query(SQLKit kit, Collection<Row> rows, LinkedList<Row> path, String select, String[] priors,
			Set<Row> nodes, List<Row> results) throws SQLException
	{
		Row node = null, result = null;
		for (Row row : rows)
		{
			node = row.gets(priors);

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

			path.add(row);

			Collection<Row> subs = kit.execute(select, node.to(JSON.class)).getRows(new LinkedList<Row>(), Row.class);

			result = this.result() == null ? row : this.result().result(row, path, subs.isEmpty());
			if (result != null)
			{
				results.add(result);
			}

			query(kit, subs, path, select, priors, nodes, results);

			nodes.remove(node);
			path.removeLast();
		}
	}

	public Canal<?, Row> query(SQLKit kit, JSON param) throws Exception
	{
		Condition cond = this.connectBy();

		List<Object> refls = Utils.reflectConditions(new LinkedList<Object>(), cond);
		Set<Column> extra = new HashSet<Column>();
		Set<String> prior = new LinkedHashSet<String>();
		replace(refls, extra, prior);
		String[] priors = prior.toArray(new String[0]);

		List<Expression> sel = Tools.listOfArray(new LinkedList<Expression>(), this.selects);
		sel.addAll(extra);
		Expression[] selects = sel.toArray(new Expression[0]);

		Select init = this.select(this.view.where(this.startWith)).select(selects);
		Select recur = this.select(this.view.where(this.connectBy)).select(selects);

		Collection<Row> res = kit.execute(init.toString(), param).getRows(new LinkedList<Row>(), Row.class);

		List<Row> recurs = new LinkedList<Row>();

		query(kit, res, new LinkedList<Row>(), recur.toString(), priors, new HashSet<Row>(), recurs);

		Canal<?, Row> result = Canal.of(recurs);

		if (this.union() == null)
		{
			return result;
		}

		int i = 0;
		for (Item item : this.union().items())
		{
			if (item.alias() == null)
			{
				item.alias(Utils.getLabelOfExpression(selects[i]));
			}
			i++;
		}
		return result
				.union(Canal.of(kit.execute(this.union().toString(), param).getRows(new LinkedList<Row>(), Row.class)));
	}

	@SuppressWarnings("unchecked")
	protected void replace(List<Object> conds, Set<Column> extra, Set<String> priors)
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
								throw new RuntimeException(
										"Non column prior expression " + pe + " occurred in condition");
							}
						}
					}
				}
				else
				{
					throw new RuntimeException("Non atomic condition " + o.toString() + " occurred in reflect list");
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

	protected Select select(Primitive view) throws Exception
	{
		return view.select(this.selects);
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

	protected Primitive view()
	{
		return view;
	}

	public Recursor view(Primitive view)
	{
		this.view = view;
		return this;
	}
}
