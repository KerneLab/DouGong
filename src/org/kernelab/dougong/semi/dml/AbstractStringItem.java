package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractStringItem extends AbstractItem implements Providable, StringItem
{
	private Provider	provider;

	protected String	item;

	private boolean		order;

	public AbstractStringItem(Provider provider)
	{
		this.provider(provider);
		this.order = true;
	}

	public AbstractStringItem ascend()
	{
		return ascend(true);
	}

	public AbstractStringItem ascend(boolean ascend)
	{
		this.order = ascend;
		return this;
	}

	public boolean ascending()
	{
		return order;
	}

	public AbstractStringItem descend()
	{
		return ascend(false);
	}

	public String getString()
	{
		return item;
	}

	public Provider provider()
	{
		return provider;
	}

	public AbstractStringItem provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	protected AbstractStringItem replicate()
	{
		return this;
	}

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

	public StringBuilder toString(StringBuilder buffer)
	{
		return buffer.append(this.toString());
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		this.provider().provideOutputOrder(toString(buffer), this);
		return buffer;
	}

	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}
}
