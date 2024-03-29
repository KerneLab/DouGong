package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Source;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.util.Utils;

public class AbstractInsert extends AbstractHintable implements Insert
{
	protected Insertable						target;

	protected Column[]							columns;

	protected Source							source;

	protected Expression[]						values;

	protected List<Pair<Column, Expression>>	pairs;

	public Column[] columns()
	{
		if (this.columns == null && this.pairs != null)
		{
			this.columns = new Column[this.pairs.size()];
			int i = 0;
			for (Pair<Column, Expression> pair : this.pairs)
			{
				this.columns[i++] = pair.key;
			}
		}
		return this.columns;
	}

	@Override
	public AbstractInsert columns(Column... columns)
	{
		this.columns = columns;
		this.pairs = null;
		return this;
	}

	@Override
	public String hint()
	{
		return super.hint();
	}

	@Override
	public AbstractInsert hint(String hint)
	{
		super.hint(hint);
		return this;
	}

	public Insertable into()
	{
		return target;
	}

	@Override
	public AbstractInsert into(Insertable target)
	{
		this.target = target;
		return this;
	}

	@Override
	public AbstractInsert pair(Column column, Expression value)
	{
		if (this.pairs == null)
		{
			this.pairs = new LinkedList<Pair<Column, Expression>>();
			this.columns = null;
			this.values = null;
		}
		this.pairs.add(new Pair<Column, Expression>(column, value));
		return this;
	}

	public List<Pair<Column, Expression>> pairs()
	{
		if (this.pairs == null && this.columns != null)
		{
			if (this.source() != null)
			{
				AbstractSelect sel = Tools.as(this.select(), AbstractSelect.class);
				if (sel != null)
				{
					this.pairs = new LinkedList<Pair<Column, Expression>>();
					Item[] exprs = sel.listItems().toArray(new Item[0]);
					for (int i = 0; i < this.columns.length; i++)
					{
						this.pairs.add(new Pair<Column, Expression>(this.columns[i], exprs[i]));
					}
				}
			}
			else if (this.values != null)
			{
				this.pairs = new LinkedList<Pair<Column, Expression>>();
				for (int i = 0; i < this.columns.length; i++)
				{
					this.pairs.add(new Pair<Column, Expression>(this.columns[i], this.values[i]));
				}
			}
		}
		return pairs;
	}

	@Override
	public AbstractInsert pairs(Expression... columnValuePairs)
	{
		this.pairs = null;

		for (int i = 0; i < columnValuePairs.length; i += 2)
		{
			this.pair((Column) columnValuePairs[i], columnValuePairs[i + 1]);
		}

		return this;
	}

	public Select select()
	{
		if (this.source() == null)
		{
			return null;
		}
		else if (this.source() instanceof Select)
		{
			return (Select) this.source();
		}
		else if (this.source() instanceof Subquery)
		{
			return ((Subquery) this.source()).select();
		}
		else
		{
			return null;
		}
	}

	@Override
	public AbstractInsert select(Source source)
	{
		this.values = null;
		this.source = source;
		this.pairs = null;
		return this;
	}

	public Source source()
	{
		return source;
	}

	protected void textOfColumns(StringBuilder buffer)
	{
		if (columns() != null && columns().length > 0)
		{
			buffer.append(" (");

			boolean first = true;

			for (Column column : columns())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				this.provider().provideOutputColumnInsert(buffer, column);
			}

			buffer.append(')');
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("INSERT");
	}

	protected void textOfSourceBody(StringBuilder buffer)
	{
		if (this.source() != null)
		{
			this.source().toStringSourceOfBody(buffer);
		}
	}

	protected void textOfSourceWith(StringBuilder buffer)
	{
		if (this.source() != null)
		{
			this.source().toStringSourceOfWith(buffer);
		}
	}

	protected void textOfTarget(StringBuilder buffer)
	{
		buffer.append(" INTO ");
		this.into().toStringInsertable(buffer);
	}

	protected void textOfValues(StringBuilder buffer)
	{
		if (values() != null)
		{
			buffer.append(" VALUES (");

			boolean first = true;

			for (Expression value : values())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, value);
			}

			buffer.append(')');
		}
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfHint(buffer);
		this.textOfTarget(buffer);
		this.textOfColumns(buffer);
		if (values() != null)
		{
			this.textOfValues(buffer);
		}
		else if (this.source() != null)
		{
			buffer.append(' ');
			this.textOfSourceWith(buffer);
			this.textOfSourceBody(buffer);
		}
		return buffer;
	}

	public Expression[] values()
	{
		if (this.values == null && this.pairs != null)
		{
			Expression[] values = new Expression[this.pairs.size()];
			int i = 0;
			for (Pair<Column, Expression> pair : this.pairs)
			{
				values[i++] = pair.value;
			}
			if (this.source() == null)
			{
				this.values = values;
			}
			else if (this.source() instanceof Select)
			{
				Select sel = (Select) this.source();
				this.source = sel.as(sel.alias()).select(values);
			}
			else if (this.source() instanceof Subquery)
			{
				this.source = provider().provideSelect().from((Subquery) this.source()).select(values);
			}
			this.pairs = null;
		}
		return this.values;
	}

	@Override
	public AbstractInsert values(Expression... values)
	{
		this.source = null;
		this.values = values;
		this.pairs = null;
		return this;
	}
}
