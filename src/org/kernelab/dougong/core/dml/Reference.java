package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;

public interface Reference extends Column
{
	public Reference as(String alias);

	public Reference usingByJoin(boolean using);
}
