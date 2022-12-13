package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MemberMeta
{
	/**
	 * The catalog name which would leading this schema name.<br />
	 * Empty means the catalog name not specified by this attribution.<br />
	 * Default is empty.
	 */
	public String catalog() default "";

	/**
	 * The schema name which would leading this member name.<br />
	 * Empty means the schema name not specified by this attribution.<br />
	 * Default is empty.
	 */
	public String schema() default "";

	/**
	 * Indicate whether this member class follows its package which would be the
	 * catalog and schema name leading the member name.<br />
	 * This attribution would be taken effect only when none catalog or schema
	 * name had been specified explicitly.<br />
	 * Default is false.
	 */
	public boolean follow() default false;
}
