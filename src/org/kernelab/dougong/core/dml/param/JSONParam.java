package org.kernelab.dougong.core.dml.param;

import org.kernelab.basis.JSON;

public interface JSONParam extends Param<JSON>
{
	public Param<?> $(String name);
}
