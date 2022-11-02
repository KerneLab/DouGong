package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractItems extends AbstractExpression implements Items, Providable
{
	private Provider		provider;

	private Expression[]	list;

	private String			alias	= null;

	public AbstractItems()
	{
	}

	public String alias()
	{
		return alias;
	}

	public AbstractItems alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	public AbstractItems as(String alias)
	{
		return this.replicate().alias(alias);
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : Tools.substr(this.toStringExpress(new StringBuilder()), 0, 30);
	}

	public Expression[] list()
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

	public List<Item> resolveItems()
	{
		List<Item> list = new LinkedList<Item>();

		for (Expression expr : list())
		{
			list.addAll(expr.resolveItems());
		}

		return list;
	}

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

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
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
				Utils.outputExprInScope(buffer, expr);
			}
		}
		return buffer;
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
