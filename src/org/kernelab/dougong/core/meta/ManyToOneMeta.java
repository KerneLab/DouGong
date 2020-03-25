package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManyToOneMeta
{
	public Class<?> model();

	public String key();

	public boolean referred() default true;

	/**
	 * Indicate whether all kind of meta of the ONE including OneToMany will be
	 * setup or not. This ManyToOne field of a child object normally setup to
	 * its parent object and the parent no need to setup fully which means its
	 * OneToMany fields no need to be setup because this child is already exist.
	 * 
	 * @return true if the ONE's OneToMany fields need to be setup.
	 */
	public boolean fully() default false;
}
