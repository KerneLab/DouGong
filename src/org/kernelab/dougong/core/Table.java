package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Insertable;

public interface Table extends Named, View, Entity, Member, Partitioned, Insertable
{
	@Override
	public Table alias(String alias);

	public <T extends Table> T as(String alias);

	/**
	 * Get the formal table name including catalog and schema name with boundary
	 * mark.
	 * 
	 * @return
	 */
	public String getFullName();

	public <T extends Table> T name(String name);
}
