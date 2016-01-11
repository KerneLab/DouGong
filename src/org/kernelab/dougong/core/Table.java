package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Insertable;

public interface Table extends Named, View, Member, Insertable
{
	public Table alias(String alias);

	public <T extends Table> T as(String alias);
}
