package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;

public interface Reference extends Column
{
	@Override
	public Reference as(String alias);
}
