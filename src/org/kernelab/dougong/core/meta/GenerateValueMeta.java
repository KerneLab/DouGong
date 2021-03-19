package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GenerateValueMeta
{
	/**
	 * Do not generate value.
	 */
	public static final short	NONE		= 0;

	/**
	 * Generate value depending on DataMeta.value expression.
	 */
	public static final short	AUTO		= 1;

	/**
	 * Generate value by table auto increment column.<br />
	 * The IDENTITY column and its correspond value will be skipped in insert
	 * statement.<br />
	 * If there is a GenerateValueMeta(IDENTITY) column, all the rest
	 * GenerateValueMeta column will be ignored.
	 */
	public static final short	IDENTITY	= 2;

	public short strategy() default AUTO;
}
