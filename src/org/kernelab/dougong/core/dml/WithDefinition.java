package org.kernelab.dougong.core.dml;

public interface WithDefinition
{
	public String name();

	public WithDefinition as(Withable select);

	public String[] columns();

	public Reference ref(String refer);

	public Withable select();
}
