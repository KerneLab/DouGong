package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Expression;

public interface WindowFunction extends Function
{
	@Override
	public WindowFunction alias(String alias);

	@Override
	public WindowFunction aliases(String... aliases);

	@Override
	public WindowFunction as(String alias);

	@Override
	public WindowFunction as(String... aliases);

	@Override
	public WindowFunction call(Expression... args);

	public Expression[] orderBy();

	public WindowFunction orderBy(Expression... orderBy);

	public Expression[] partitionBy();

	public WindowFunction partitionBy(Expression... partitionBy);

	public WindowFunction range(Object... between);

	public boolean rows();

	public WindowFunction rows(Object... between);
}
