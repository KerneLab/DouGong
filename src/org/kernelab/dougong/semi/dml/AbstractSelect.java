package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;

public abstract class AbstractSelect extends AbstractFilterable implements Select, View
{
	private String					alias		= null;

	protected boolean				distinct	= false;

	protected Object[]				items		= null;

	protected List<View>			from		= new LinkedList<View>();

	protected List<Join>			joins		= new LinkedList<Join>();

	protected Object[]				groupBy		= null;

	protected Condition				having		= null;

	protected List<AbstractSetopr>	setopr		= new LinkedList<AbstractSetopr>();

	protected Object[]				orderBy		= null;

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

	public AbstractSelect distinct(boolean distinct)
	{
		this.distinct = distinct;
		return this;
	}

	public AbstractSelect from(View view)
	{
		this.from.add(view);
		return this;
	}

	public AbstractSelect fullJoin(View view, Condition on)
	{
		this.joins.add(provider().provideJoin().join(AbstractJoin.FULL_JOIN, view, view.alias(), on));
		return this;
	}

	public AbstractSelect groupBy(Object... items)
	{
		this.groupBy = items;
		return this;
	}

	public AbstractSelect having(Condition condition)
	{
		this.having = condition;
		return this;
	}

	public AbstractSelect intersect(Select select)
	{
		this.setopr.add(new AbstractSetopr().setopr(Setopr.INTERSECT, select));
		return this;
	}

	public AbstractSelect join(View view, Condition on)
	{
		this.joins.add(provider().provideJoin().join(AbstractJoin.INNER_JOIN, view, view.alias(), on));
		return this;
	}

	public AbstractSelect leftJoin(View view, Condition on)
	{
		this.joins.add(provider().provideJoin().join(AbstractJoin.LEFT_JOIN, view, view.alias(), on));
		return this;
	}

	public AbstractSelect minus(Select select)
	{
		this.setopr.add(new AbstractSetopr().setopr(Setopr.MINUS, select));
		return this;
	}

	public AbstractSelect orderBy(Object... items)
	{
		this.orderBy = items;
		return this;
	}

	@Override
	public AbstractSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	public AbstractSelect rightJoin(View view, Condition on)
	{
		this.joins.add(provider().provideJoin().join(AbstractJoin.RIGHT_JOIN, view, view.alias(), on));
		return this;
	}

	public AbstractSelect select(Object... items)
	{
		this.items = items;
		return this;
	}

	protected void textOfAbstractSetopr(StringBuilder buffer)
	{
		for (AbstractSetopr opr : setopr)
		{
			if (opr != null)
			{
				opr.toString(buffer);
			}
		}
	}

	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM");

		boolean first = true;

		for (View fr : from)
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
				fr.toStringAliased(buffer);
			}
		}
	}

	protected void textOfGroup(StringBuilder buffer)
	{
		if (groupBy != null && groupBy.length > 0)
		{
			buffer.append(" GROUP BY");

			boolean first = true;

			for (Object item : groupBy)
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
				Utils.text(buffer, item);
			}
		}
	}

	protected void textOfHaving(StringBuilder buffer)
	{
		if (having != null)
		{
			buffer.append(" HAVING ");
			having.toString(buffer);
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("SELECT");

		if (distinct)
		{
			buffer.append(" DISTINCT");
		}
	}

	protected void textOfItems(StringBuilder buffer)
	{
		boolean first = true;

		for (Object item : items)
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
				Utils.textAliased(buffer, item);
			}
		}
	}

	protected void textOfJoin(StringBuilder buffer)
	{
		for (Join join : joins)
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
		if (orderBy != null && orderBy.length > 0)
		{
			buffer.append(" ORDER BY");

			boolean first = true;

			for (Object item : orderBy)
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
				Utils.text(buffer, item);
			}
		}
	}

	protected void textOfWhere(StringBuilder buffer)
	{
		if (where() != null)
		{
			buffer.append(" WHERE ");
			where().toString(buffer);
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

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		// TODO Lang Spec
		String alias = this.alias();

		if (alias != null)
		{
			buffer.append('(');
		}

		this.toString(buffer);

		if (alias != null)
		{
			buffer.append(") ");
			buffer.append(alias);
		}

		return buffer;
	}

	public AbstractSelect union(Select select)
	{
		this.setopr.add(new AbstractSetopr().setopr(Setopr.UNION, select));
		return this;
	}

	public AbstractSelect unionAll(Select select)
	{
		this.setopr.add(new AbstractSetopr().setopr(Setopr.UNION_ALL, select));
		return this;
	}

	public AbstractSelect where(Condition condition)
	{
		super.where(condition);
		return this;
	}
}
