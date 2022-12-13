package org.kernelab.dougong.core;

public interface Member extends Named
{
	/**
	 * Get the catalog name which should leading the schema name.
	 * 
	 * @return The catalog name, null or empty if the object should not leading
	 *         with a catalog name.
	 */
	public String catalog();

	/**
	 * Get the schema name which should leading the object name.
	 * 
	 * @return The schema name, null or empty if the object should not leading
	 *         with a schema name.
	 */
	public String schema();
}
