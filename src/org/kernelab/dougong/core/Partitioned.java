package org.kernelab.dougong.core;

public interface Partitioned
{
	/**
	 * Get the partition name of the object. Return {@code null} if not
	 * partitioned.
	 * 
	 * @return
	 */
	public String partition();
}
