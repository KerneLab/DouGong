package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InsertMeta
{
	/**
	 * The value parameter name if specified.<br />
	 * This param has lower priority than value.
	 */
	public String param() default "";

	/**
	 * The value expression if specified.<br/>
	 * This param has higher priority than param.
	 */
	public String value() default "";
}
