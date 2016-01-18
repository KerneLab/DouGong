package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;

public interface Reference extends Column
{
	public Reference as(String alias);

	public Expression refer();

	public Reference usingByJoin(boolean using);
}
