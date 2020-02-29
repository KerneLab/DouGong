package org.kernelab.dougong.core;

public interface Castable
{
	/**
	 * Cast this object to given class.<br />
	 * Return null if could not be cast.
	 * 
	 * @param cls
	 * @return
	 */
	public <T> T as(Class<T> cls);
}
