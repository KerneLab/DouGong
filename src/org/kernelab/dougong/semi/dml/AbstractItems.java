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

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
	public AbstractItems alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	@Override
	public AbstractItems as(String alias)
	{
		return this.replicate().alias(alias);
	}

	@Override
	public boolean isUsingByJoin()
	{
		return false;
	}

	@Override
	public String label()
	{
		return alias() != null ? alias() : Tools.substr(this.toStringExpress(new StringBuilder()), 0, 30);
	}

	@Override
	public Expression[] list()
	{
		return list;
	}

	@Override
	public AbstractItems list(Expression... expr)
	{
		this.list = expr;
		return this;
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractItems provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected abstract AbstractItems replicate();

	@Override
	public List<Item> resolveItems()
	{
		List<Item> list = new LinkedList<Item>();

		for (Expression expr : list())
		{
			list.addAll(expr.resolveItems());
		}

		return list;
	}

	@Override
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

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
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

	@Override
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

	@Override
	public Item usingByJoin(boolean using)
	{
		return this;
	}
}
