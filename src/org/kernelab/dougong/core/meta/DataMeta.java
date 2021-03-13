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
	 * This item will be ignored in insert if not empty but white.
	 */
	public String value() default "";

	/**
	 * Indicate whether this member need to be serialized, for example in JSON.
	 * <br />
	 * This item would be false in case its field is a "virtual" member and its
	 * real value refers to the member in another object. So that this field is
	 * no need to be serialized because the reference object should already be
	 * serialized.
	 */
	public boolean serialize() default true;

	/**
	 * Indicate whether this member is a raw column of a table. Typically, a
	 * column from join table should be marked as not raw. If the column is not
	 * raw, the column will be skipped during selecting object directly
	 * according to this class rather than a select statement. The non-raw
	 * column will also be skipped during inserting to the table.
	 */
	public boolean raw() default true;

	/**
	 * Indicate the expression of this item on being selected. If this
	 * expression is not empty, it will take place of NameMeta as the select
	 * expression.
	 */
	public String select() default "";
}
