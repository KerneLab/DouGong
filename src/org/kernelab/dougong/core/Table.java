package org.kernelab.dougong.core;

public interface Table extends View, Schema
{
	public Table alias(String alias);

	public <T extends Table> T as(String alias);
}
