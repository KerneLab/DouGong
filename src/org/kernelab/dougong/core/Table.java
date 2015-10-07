package org.kernelab.dougong.core;

public interface Table extends View
{
	public Table alias(String alias);

	public <T extends Table> T as(String alias);

	/**
	 * Indicate whether the table name should begin with the package name of the
	 * Table class as schema name.
	 * 
	 * @return
	 */
	public boolean usingSchema();

	/**
	 * Specify whether the table name shoudl begin with the package name of the
	 * Table class as schema name.
	 * 
	 * @param using
	 * @return
	 */
	public Table usingSchema(boolean using);
}
