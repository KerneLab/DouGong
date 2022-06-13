package org.kernelab.dougong.core.dml.param;

import java.util.Map;

public interface MapParam<K, V> extends Param<Map<K, V>>
{
	public Param<?> $(String name);
}
