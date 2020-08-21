package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.ResultSetMetaData;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TypeMeta
{
	public static final int	NO_NULLS			= ResultSetMetaData.columnNoNulls;

	public static final int	NULLABLE			= ResultSetMetaData.columnNullable;

	public static final int	NULLABLE_UNKNOWN	= ResultSetMetaData.columnNullableUnknown;

	/**
	 * The type name of the data.
	 * 
	 * @return
	 */
	public String type() default "";

	/**
	 * The precision of the data.
	 * 
	 * @return
	 */
	public int precision() default 0;

	/**
	 * The scale of the data.
	 * 
	 * @return
	 */
	public int scale() default 0;

	/**
	 * The nullability of the data.
	 * 
	 * @return
	 */
	public int nullable() default NULLABLE_UNKNOWN;
}
