package org.kernelab.dougong.core.meta;

import org.kernelab.dougong.core.Entity;

public @interface JoinDefine
{
	public static final short	INNER_JOIN	= 0;

	public static final short	LEFT_JOIN	= 1;

	public static final short	RIGHT_JOIN	= 2;

	public static final short	FULL_JOIN	= 3;

	public Class<? extends Entity> entity();

	public String foreignKey();

	public short type() default INNER_JOIN;
}
