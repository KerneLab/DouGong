package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Table;

public abstract class AbstractTable extends AbstractView implements Table
{
	private boolean	usingSchema	= false;

	public AbstractTable()
	{
		super();
	}

	@Override
	public AbstractTable alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Table> T as(String alias)
	{
		AbstractTable table = this.clone();
		if (table != null)
		{
			table.alias(alias);
		}
		return (T) table;
	}

	@Override
	protected AbstractTable clone()
	{
		AbstractTable table = null;
		try
		{
			table = this.getClass().newInstance();
			table.usingSchema(this.usingSchema());
			table.provider(this.provider());
		}
		catch (Exception e)
		{
		}
		return table;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		return buffer.append(provider().provideTableName(this));
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		return buffer.append(provider().provideTableNameAliased(this));
	}

	public boolean usingSchema()
	{
		return usingSchema;
	}

	public AbstractTable usingSchema(boolean using)
	{
		this.usingSchema = using;
		return this;
	}
}
