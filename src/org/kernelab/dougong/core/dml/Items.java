package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;

public interface Items extends Expression, Scope
{
	public Expression[] list();

	public Items list(Expression... items);
}
