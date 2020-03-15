package org.kernelab.dougong.core.meta;

import org.kernelab.dougong.core.Entity;

public @interface JoinDefine
{
	public static final short	INNER_JOIN	= 0;

	public static final short	LEFT_JOIN	= 1;

	public static final short	RIGHT_JOIN	= 2;

	public static final short	FULL_JOIN	= 3;

	/**
	 * The class of entity to be joined.
	 * 
	 * @return
	 */
	public Class<? extends Entity> entity();

	/**
	 * The name of foreign key.
	 * 
	 * @return
	 */
	public String key();

	/**
	 * Indicate whether the entity is on the reference side.
	 * 
	 * @return true if the join entity is on the reference side, otherwise
	 *         false.
	 */
	public boolean referred();

	/**
	 * The join type.
	 * 
	 * @return
	 */
	public short type() default INNER_JOIN;
}
