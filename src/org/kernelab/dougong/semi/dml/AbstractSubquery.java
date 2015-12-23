package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;

public class AbstractSubquery extends AbstractView implements Subquery
{
	private Select	select;

	public AbstractSubquery()
	{
		super();
	}

	@Override
	public AbstractSubquery alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Subquery> T as(String alias)
	{
		AbstractSubquery sq = this.clone();
		if (sq != null)
		{
			sq.alias(alias);
		}
		return (T) sq;
	}

	@Override
	protected AbstractSubquery clone()
	{
		AbstractSubquery sq = null;
		try
		{
			sq = this.getClass().newInstance();
			sq.select(this.select());
			sq.provider(this.provider());
		}
		catch (Exception e)
		{
		}
		return sq;
	}

	public Select select()
	{
		return select;
	}

	public AbstractSubquery select(Select select)
	{
		this.select = select;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.select().toString(buffer);
		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		// TODO Lang Spec
		buffer.append('(');

		this.toString(buffer);

		buffer.append(')');

		String alias = this.alias();

		if (alias != null)
		{
			buffer.append(' ');
			buffer.append(alias);
		}

		return buffer;
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return this.select().toStringScoped(buffer);
	}
}
