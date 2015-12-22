package org.kernelab.dougong.core;

public interface Table extends Named, View, Member
{
	public Table alias(String alias);

	public <T extends Table> T as(String alias);
}
