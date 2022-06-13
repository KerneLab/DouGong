package org.kernelab.dougong.semi.dml.param;

import java.util.Map;

import org.kernelab.dougong.core.dml.param.MapParam;
import org.kernelab.dougong.core.dml.param.Param;

public class AbstractMapParam<K, V> extends AbstractParam<Map<K, V>> implements MapParam<K, V>
{
	public AbstractMapParam(String name, Map<K, V> value)
	{
		super(name, value);
	}

	@Override
	public Param<?> $(String name)
	{
		return this.provider().provideParameterByValue(this.name() + "." + name,
				this.value() != null ? this.value().get(name) : null);
	}

	@Override
	protected AbstractMapParam<K, V> replicate()
	{
		return this.provider().provideProvider(new AbstractMapParam<K, V>(name(), value()));
	}
}
