package org.kernelab.dougong.core.dml.param;

import org.kernelab.basis.JSON.JSAN;

public interface JSANParam extends Param<JSAN>
{
	public Param<?> $(int index);
}
