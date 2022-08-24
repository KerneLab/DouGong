package org.kernelab.dougong.semi.dml.param;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.dml.param.ObjectParam;
import org.kernelab.dougong.core.dml.param.Param;

public class AbstractObjectParam<E> extends AbstractParam<E> implements ObjectParam<E>
{
	public AbstractObjectParam(String name, E value)
	{
		super(name, value);
	}

	@Override
	public Param<?> $(String name)
	{
		Object value = null;

		if (name != null && this.value() != null)
		{
			try
			{
				value = Tools.access(this.value(), name, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return this.provider().provideParameterByValue(this.name() + "." + name, value);
	}

	@Override
	protected AbstractObjectParam<E> replicate()
	{
		return this.provider().provideProvider(new AbstractObjectParam<E>(name(), value()));
	}

	@Override
	public AbstractObjectParam<E> set(E value)
	{
		return this.replicate().value(value);
	}
}
