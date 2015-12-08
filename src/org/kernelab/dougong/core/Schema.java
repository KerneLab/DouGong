package org.kernelab.dougong.core;

public interface Schema
{
	/**
	 * Indicate whether the name should leading with the package name of this
	 * class as schema name.
	 * 
	 * @return
	 */
	public boolean usingSchema();

	/**
	 * Specify whether the name should leading with the package name of this
	 * class as schema name.
	 * 
	 * @param using
	 * @return
	 */
	public Schema usingSchema(boolean using);
}
