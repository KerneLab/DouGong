package org.kernelab.dougong.core.dml.test;

import org.kernelab.dougong.core.dml.cond.NullCondition;

public interface NullTestable
{
	public NullCondition isNotNull();

	public NullCondition isNull();
}
