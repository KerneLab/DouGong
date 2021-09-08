package org.kernelab.dougong.core.dml;

public interface Withable extends Alias
{
	public StringBuilder toStringWith(StringBuilder buffer);

	public String[] withCols();

	public String withName();

	public Withable with(String name, String... cols);
}
