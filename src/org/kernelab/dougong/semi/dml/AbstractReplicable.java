package org.kernelab.dougong.semi.dml;

public abstract class AbstractReplicable
{
	/**
	 * Return a new Object which has the same properties to this Object.
	 * 
	 * @return
	 */
	protected abstract AbstractReplicable replicate();
}
