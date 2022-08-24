package org.kernelab.dougong.semi.dml.param;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.core.dml.param.JSONParam;
import org.kernelab.dougong.core.dml.param.Param;

public class AbstractJSONParam extends AbstractParam<JSON> implements JSONParam
{
	public AbstractJSONParam(String name, JSON value)
	{
		super(name, value);
	}

	@Override
	public Param<?> $(String name)
	{
		return this.provider().provideParameterByValue(this.name() + "." + name,
				this.value() != null ? this.value().attr(name) : null);
	}

	@Override
	protected AbstractJSONParam replicate()
	{
		return this.provider().provideProvider(new AbstractJSONParam(name(), value()));
	}

	@Override
	public AbstractJSONParam set(JSON value)
	{
		return this.replicate().value(value);
	}
}
