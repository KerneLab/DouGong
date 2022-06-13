package org.kernelab.dougong.core.dml.param;

public interface ObjectParam<E> extends Param<E>
{
	public Param<?> $(String name);
}
