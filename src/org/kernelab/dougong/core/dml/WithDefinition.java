package org.kernelab.dougong.core.dml;

public interface WithDefinition
{
	public String name();

	public WithDefinition as(Withable select);

	public String[] columns();

	public boolean isRecursive();

	public WithDefinition recursive();

	public Reference ref(String refer);

	public Withable select();
}
