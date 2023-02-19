package org.kernelab.dougong.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Pseudo Column will not be resolved by AllItems.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PseudoColumnMeta
{
}
