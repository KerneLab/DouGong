package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Source;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractInsert extends AbstractProvidable implements Insert
{
	protected Insertable	target;

	protected Column[]		columns;

	protected Source		source;

	protected Expression[]	values;

	public Insert columns(Column... columns)
	{
		this.columns = columns;
		return this;
	}

	public AbstractInsert into(Insertable target)
	{
		this.target = target;
		return this;
	}

	public void textOfColumns(StringBuilder buffer)
	{
		if (columns != null && columns.length > 0)
		{
			buffer.append(" (");

			boolean first = true;

			for (Column column : columns)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, column);
			}

			buffer.append(')');
		}
	}

	public void textOfHead(StringBuilder buffer)
	{
		buffer.append("INSERT INTO");
	}

	public void textOfSource(StringBuilder buffer)
	{
		if (source != null)
		{
			buffer.append(' ');
			source.toStringSource(buffer);
		}
	}

	public void textOfTarget(StringBuilder buffer)
	{
		buffer.append(' ');
		target.toStringInsertable(buffer);
	}

	public void textOfValues(StringBuilder buffer)
	{
		if (values != null)
		{
			buffer.append(" VALUES (");

			boolean first = true;

			for (Expression value : values)
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
		this.textOfTarget(buffer);
		this.textOfColumns(buffer);
		if (values != null)
		{
			this.textOfValues(buffer);
		}
		else if (source != null)
		{
			this.textOfSource(buffer);
		}
		return buffer;
	}

	public AbstractInsert values(Expression... values)
	{
		this.source = null;
		this.values = values;
		return this;
	}

	public AbstractInsert values(Source source)
	{
		this.values = null;
		this.source = source;
		return this;
	}
}
