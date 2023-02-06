package org.kernelab.dougong.semi.dml.param;

import org.kernelab.basis.JSON.JSAN;
import org.kernelab.dougong.core.dml.param.JSANParam;
import org.kernelab.dougong.core.dml.param.Param;

public class AbstractJSANParam extends AbstractParam<JSAN> implements JSANParam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractJSANParam(String name, JSAN value)
	{
		super(name, value);
	}

	@Override
	public Param<?> $(int index)
	{
		return this.provider().provideParameterByValue(this.name() + "." + index,
				this.value() != null ? this.value().attr(index) : null);
	}

	@Override
	protected AbstractJSANParam replicate()
	{
		return this.provider().provideProvider(new AbstractJSANParam(name(), value()));
	}

	@Override
	public AbstractJSANParam set(JSAN value)
	{
		return this.replicate().value(value);
	}
}
