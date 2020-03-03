package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Expression;

public interface WindowFunction extends Function
{
	public Expression[] orderBy();

	public WindowFunction orderBy(Expression... orderBy);

	public Expression[] partitionBy();

	public WindowFunction partitionBy(Expression... partitionBy);

	public WindowFunction range(Object... between);

	public boolean rows();

	public WindowFunction rows(Object... between);
}
