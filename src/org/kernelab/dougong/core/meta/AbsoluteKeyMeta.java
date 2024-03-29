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
public @interface AbsoluteKeyMeta
{
	/**
	 * Indicate whether this AbsoluteKey column is generating value on insert or
	 * not. Default is true.
	 */
	public boolean generate() default true;
}
