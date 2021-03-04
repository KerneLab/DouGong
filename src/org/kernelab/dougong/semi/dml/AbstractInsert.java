package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Pair;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Source;
import org.kernelab.dougong.core.util.Utils;

public class AbstractInsert extends AbstractHintable implements Insert
{
	protected Insertable						target;

	protected Column[]							columns;

	protected Source							source;

	protected Expression[]						values;

	protected List<Pair<Column, Expression>>	pairs;

	protected Column[] columns()
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

	public AbstractInsert columns(Column... columns)
	{
		this.columns = columns;
		this.pairs = null;
		return this;
	}

	@Override
	public AbstractInsert hint(String hint)
	{
		super.hint(hint);
		return this;
	}

	public AbstractInsert into(Insertable target)
	{
		this.target = target;
		return this;
	}

	public AbstractInsert pair(Column column, Expression value)
	{
		if (this.pairs == null)
		{
			this.pairs = new LinkedList<Pair<Column, Expression>>();
			this.columns = null;
			this.values = null;
			this.source = null;
		}
		this.pairs.add(new Pair<Column, Expression>(column, value));
		return this;
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
				this.provider().provideOutputColumnReference(buffer, column);
			}

			buffer.append(')');
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("INSERT");
	}

	protected void textOfSource(StringBuilder buffer)
	{
		if (source != null)
		{
			buffer.append(' ');
			source.toStringSource(buffer);
		}
	}

	protected void textOfTarget(StringBuilder buffer)
	{
		buffer.append(" INTO ");
		target.toStringInsertable(buffer);
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
		else if (source != null)
		{
			this.textOfSource(buffer);
		}
		return buffer;
	}

	protected Expression[] values()
	{
		if (this.values == null && this.pairs != null)
		{
			this.values = new Expression[this.pairs.size()];
			int i = 0;
			for (Pair<Column, Expression> pair : this.pairs)
			{
				this.values[i++] = pair.value;
			}
		}
		return this.values;
	}

	public AbstractInsert values(Expression... values)
	{
		this.source = null;
		this.values = values;
		this.pairs = null;
		return this;
	}

	public AbstractInsert values(Source source)
	{
		this.values = null;
		this.source = source;
		this.pairs = null;
		return this;
	}
}
