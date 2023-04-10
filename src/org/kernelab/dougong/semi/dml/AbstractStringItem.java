package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractStringItem extends AbstractSortable implements Providable, StringItem
{
	private Provider	provider;

	protected String	item;

	public AbstractStringItem(Provider provider)
	{
		this.provider(provider);
	}

	@Override
	public String getString()
	{
		return item;
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractStringItem provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected AbstractStringItem replicate()
	{
		return (AbstractStringItem) ((AbstractStringItem) provider().provideStringItem(this.getString()))
				.replicateOrderOf(this);
	}

	@Override
	public AbstractStringItem setString(String item)
	{
		this.item = item;
		return this;
	}

	@Override
	public String toString()
	{
		return this.getString() == null ? SQL.NULL : this.getString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return buffer.append(this.toString());
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}
}
