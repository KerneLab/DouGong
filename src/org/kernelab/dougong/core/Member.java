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

	/**
	 * Get the expression of this member object according to given level.
	 * 
	 * @param level
	 * @return if level is 0, only the name will be returned; if level is 1 then
	 *         schema.name will be returned; if level is 2 then
	 *         catalog.schema.name will be returned. If level is negative then
	 *         components will be returned as many as possible.
	 */
	public String toString(int level);

	/**
	 * Output the expression of this member object according to given level.
	 * 
	 * @param buffer
	 * @param level
	 * @return if level is 0, only the name will be output; if level is 1 then
	 *         schema.name will be output; if level is 2 then
	 *         catalog.schema.name will be output. If level is negative then
	 *         components will be output as many as possible.
	 */
	public StringBuilder toString(StringBuilder buffer, int level);
}
