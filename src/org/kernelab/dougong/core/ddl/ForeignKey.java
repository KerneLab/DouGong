package org.kernelab.dougong.core.ddl;

public interface ForeignKey extends DDL, Key
{
	public PrimaryKey reference();
}
