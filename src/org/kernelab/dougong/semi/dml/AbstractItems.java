package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.dml.Items;

public abstract class AbstractItems extends AbstractExpression implements Items, Providable
{
	private Provider		provider;

	private Expression[]	list;

	public AbstractItems()
	{
	}

	protected Expression[] list()
	{
		return list;
	}

	public AbstractItems list(Expression... expr)
	{
		this.list = expr;
		return this;
	}
	
	public Provider provider()
	{
		return provider;
	}

	public AbstractItems provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	protected abstract AbstractItems replicate();

	public StringBuilder toString(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
			}
		}

		return buffer;
	}

	public StringBuilder toStringExpressed(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		if (list() != null)
		{
			boolean first = true;

			for (Expression expr : list())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
				Utils.outputAlias(this.provider(), buffer, expr);
			}
		}

		return buffer;
	}
}
