package org.kernelab.dougong.core.dml;

public interface Withable extends Alias
{
	public StringBuilder toStringWith(StringBuilder buffer);

	public WithDefinition with();

	public Withable with(WithDefinition define);
}
