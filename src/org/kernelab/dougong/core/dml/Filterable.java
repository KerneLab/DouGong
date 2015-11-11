package org.kernelab.dougong.core.dml;

public interface Filterable
{
	public Condition where();

	public Filterable where(Condition cond);
}
