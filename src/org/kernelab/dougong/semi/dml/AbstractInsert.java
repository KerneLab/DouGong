package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Source;
import org.kernelab.dougong.core.util.Utils;

public class AbstractInsert extends AbstractProvidable implements Insert
{
	protected Insertable	target;

	protected Column[]		columns;

	protected Source		source;

	protected Expression[]	values;

	public AbstractInsert into(Insertable target, Column... columns)
	{
		this.target = target;
		this.columns = columns;
		return this;
	}

	protected void textOfColumns(StringBuilder buffer)
	{
		if (columns != null)
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
				buffer.append(column.name());
			}

			buffer.append(')');
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("INSERT INTO");
	}

	protected void textOfSource(StringBuilder buffer)
	{
		if (source != null)
		{
			source.toStringSource(buffer);
		}
	}

	protected void textOfTarget(StringBuilder buffer)
	{
		buffer.append(' ');
		target.toStringInsertable(buffer);
	}

	protected void textOfValues(StringBuilder buffer)
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
