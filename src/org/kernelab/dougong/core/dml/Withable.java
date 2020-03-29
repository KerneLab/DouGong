package org.kernelab.dougong.core.dml;

public interface Withable extends Alias
{
	public String withName();

	public Withable withName(String name);

	public StringBuilder toStringWith(StringBuilder buffer);
}
