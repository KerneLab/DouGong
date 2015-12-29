package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.Providable;

public interface Function extends Named, Item, Sortable, Member, Providable
{
	public Expression[] args();

	public Function call(Expression... args);
}
