package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ForeignKeyMeta
{
	/**
	 * Deny the changes on reference.
	 */
	public static final byte	RESTRICT	= 1;

	/**
	 * Change the referrer's data cascade.
	 */
	public static final byte	CASCADE		= 2;

	/**
	 * The behavior on delete.
	 * 
	 * @return
	 */
	public byte onDelete() default RESTRICT;

	/**
	 * The behavior on update.
	 * 
	 * @return
	 */
	public byte onUpdate() default RESTRICT;
}
