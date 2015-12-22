package org.kernelab.dougong.core;

public interface Member
{
	/**
	 * Get the schema name which should leading the object name.
	 * 
	 * @return The schema name, null or empty if the object should not leading
	 *         with a schema name.
	 */
	public String schema();
}
