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
public @interface OneToOneMeta
{
	public Class<?> model();

	public String key();

	/**
	 * Indicate whether this field is on the reference side or not.
	 * 
	 * @return
	 */
	public boolean referred();

	public boolean serialize() default true;
}
