package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Sortable;

public interface Function extends Named, Item, Sortable, Member, Providable
{
	public Expression[] args();

	public Function call(Expression... args);
}
