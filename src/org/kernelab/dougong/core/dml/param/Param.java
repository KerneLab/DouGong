package org.kernelab.dougong.core.dml.param;

import org.kernelab.dougong.core.Named;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.test.Testable;

public interface Param<E> extends Item, Named, Testable
{
	/**
	 * To test whether the value of this parameter is null or not.
	 * 
	 * @return
	 */
	public boolean given();

	/**
	 * Get the value of the parameter.
	 * 
	 * @return
	 */
	public E value();
}
