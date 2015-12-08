package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Schema;

public interface Function extends SingleItem, Sortable, Schema, Providable
{
	public Expression[] args();

	public Function call(Expression... args);
}
