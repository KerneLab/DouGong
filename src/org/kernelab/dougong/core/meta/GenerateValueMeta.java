package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenerateValueMeta
{
	/**
	 * Generate value depending on DataMeta.value expression.
	 */
	public static final short	AUTO		= 1;

	/**
	 * Generate value by table auto increment column.
	 */
	public static final short	IDENTITY	= 2;

	public short strategy() default AUTO;
}
