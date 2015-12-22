package org.kernelab.dougong.semi.dml;

public abstract class AbstractReplicable
{
	/**
	 * Return a new Object which has the most same properties to this Object.<br />
	 * Typically, the alias name is not considered.
	 * 
	 * @return
	 */
	protected abstract AbstractReplicable replicate();
}
