package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.WindowFunction;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.util.Utils;

public class AbstractWindowFunction extends AbstractFunction implements WindowFunction
{
	private Expression[]	partitionBy;

	private Expression[]	orderBy;

	private boolean			rows	= true;

	private Object[]		between;

	@Override
	public AbstractWindowFunction alias(String alias)
	{
		return (AbstractWindowFunction) super.alias(alias);
	}

	@Override
	public AbstractWindowFunction aliases(String... aliases)
	{
		return (AbstractWindowFunction) super.aliases(aliases);
	}

	@Override
	public AbstractWindowFunction as(String alias)
	{
		return (AbstractWindowFunction) super.as(alias);
	}

	@Override
	public AbstractWindowFunction as(String... aliases)
	{
		return (AbstractWindowFunction) super.as(aliases);
	}

	public Object[] between()
	{
		return between;
	}

	protected AbstractWindowFunction between(Object[] between)
	{
		this.between = between;
		return this;
	}

	@Override
	public AbstractWindowFunction call(Expression... args)
	{
		return (AbstractWindowFunction) super.call(args);
	}

	protected boolean isRows()
	{
		return rows;
	}

	protected boolean isWindowed()
	{
		return (partitionBy() != null && partitionBy().length > 0) //
				|| (orderBy() != null && orderBy().length > 0) //
				|| (between() != null && between().length > 0);
	}

	@Override
	public Expression[] orderBy()
	{
		return orderBy;
	}

	@Override
	public AbstractWindowFunction orderBy(Expression... exprs)
	{
		this.orderBy = exprs;
		return this;
	}

	@Override
	public Expression[] partitionBy()
	{
		return partitionBy;
	}

	@Override
	public AbstractWindowFunction partitionBy(Expression... exprs)
	{
		this.partitionBy = exprs;
		return this;
	}

	public AbstractWindowFunction range()
	{
		this.rows = false;
		return this;
	}

	@Override
	public AbstractWindowFunction range(Object... between)
	{
		this.rows = false;
		this.between = between;
		return this;
	}

	@Override
	protected AbstractWindowFunction replicate()
	{
		return super.replicate() //
				.to(AbstractWindowFunction.class) //
				.partitionBy(partitionBy()) //
				.orderBy(orderBy()) //
				.rows(isRows()) //
				.between(between());
	}

	@Override
	public boolean rows()
	{
		return rows;
	}

	protected AbstractWindowFunction rows(boolean isRows)
	{
		this.rows = isRows;
		return this;
	}

	@Override
	public AbstractWindowFunction rows(Object... between)
	{
		this.rows = true;
		this.between = between;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		super.toString(buffer);

		if (this.isWindowed())
		{
			buffer.append(" OVER(");

			boolean wrote = false;

			if (partitionBy() != null && partitionBy().length > 0)
			{
				if (wrote)
				{
					buffer.append(' ');
				}

				buffer.append("PARTITION BY ");

				boolean first = true;

				for (Expression expr : partitionBy())
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

				wrote = true;
			}

			if (orderBy() != null && orderBy().length > 0)
			{
				if (wrote)
				{
					buffer.append(' ');
				}

				buffer.append("ORDER BY ");

				boolean first = true;

				for (Expression expr : orderBy())
				{
					if (first)
					{
						first = false;
					}
					else
					{
						buffer.append(',');
					}

					if (expr instanceof Sortable)
					{
						((Sortable) expr).toStringOrdered(buffer);
					}
					else
					{
						Utils.outputExpr(buffer, expr);
					}
				}

				wrote = true;
			}

			if (between() != null && between().length > 0)
			{
				if (wrote)
				{
					buffer.append(' ');
				}

				if (isRows())
				{
					buffer.append("ROWS ");
				}
				else
				{
					buffer.append("RANGE ");
				}

				if (between().length > 1)
				{
					buffer.append("BETWEEN ");
				}

				Utils.outputExpr(buffer, between()[0]);

				if (between().length > 1)
				{
					buffer.append(" AND ");
					Utils.outputExpr(buffer, between()[1]);
				}

				wrote = true;
			}

			buffer.append(')');
		}

		return buffer;
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.provider().provideOutputOrder(this.toString(buffer), this);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}
}
