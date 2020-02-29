package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMeta
{
	/**
	 * The alias of data.<br/>
	 * Which would be used to generate insert values' parameter if value is not
	 * specified.<br/>
	 * This alias would also be used as the alias of select items.
	 */
	public String alias() default "";

	/**
	 * The value expression.<br/>
	 * Which would be used to generate insert values' expression if alias is not
	 * specified.<br/>
	 */
	public String value() default "";
}
