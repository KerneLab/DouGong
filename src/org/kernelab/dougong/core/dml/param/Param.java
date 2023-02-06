package org.kernelab.dougong.core.dml.param;

import java.io.Serializable;

import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.test.Testable;

public interface Param<E> extends Item, Named, Testable, Serializable
{
	/**
	 * To test whether the value of this parameter is neither null nor empty.
	 * 
	 * @return
	 */
	public boolean given();

	/**
	 * To test whether the value of this parameter is null or not.
	 * 
	 * @return
	 */
	public boolean got();

	/**
	 * Clone a new Param object and set the value.
	 * 
	 * @param value
	 */
	public Param<E> set(E value);

	/**
	 * Get the value of the parameter.
	 * 
	 * @return
	 */
	public E value();
}
