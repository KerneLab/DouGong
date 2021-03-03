package org.kernelab.dougong.semi.dml.param;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.AbstractItem;

public abstract class AbstractParam<E> extends AbstractItem implements Param<E>, Providable
{
	private Provider	provider;

	private String		name;

	private E			value;

	public AbstractParam(String name, E value)
	{
		this.name(name);
		this.value(value);
	}

	@Override
	public boolean given()
	{
		return got();
	}

	@Override
	public boolean got()
	{
		return this.value != null;
	}

	@Override
	public String name()
	{
		return name;
	}

	protected <T extends AbstractParam<E>> T name(String name)
	{
		this.name = name;
		return Tools.cast(this);
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	public Providable provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected abstract AbstractParam<E> replicate();

	@Override
	public String toString()
	{
		return this.provider().provideParameterExpression(this.name());
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
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}

	@Override
	public E value()
	{
		return value;
	}

	protected <T extends AbstractParam<E>> T value(E value)
	{
		this.value = value;
		return Tools.cast(this);
	}
}
