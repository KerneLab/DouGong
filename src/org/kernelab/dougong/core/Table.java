package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Insertable;

public interface Table extends Named, View, Entity, Member, Partitioned, Insertable
{
	@Override
	public Table alias(String alias);

	public <T extends Table> T catalog(String catalog);

	public <T extends Table> T name(String name);

	public <T extends Table> T schema(String schema);

	/**
	 * Get the formal table name including catalog and schema with boundary
	 * mark.
	 * 
	 * @return the formal table name including catalog and schema with boundary
	 *         mark.
	 */
	public String toString();
}
