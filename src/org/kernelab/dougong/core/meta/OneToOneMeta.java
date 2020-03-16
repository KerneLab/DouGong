package org.kernelab.dougong.core.meta;

public @interface OneToOneMeta
{
	public Class<?> model();

	public String key();

	public boolean referred();
}
