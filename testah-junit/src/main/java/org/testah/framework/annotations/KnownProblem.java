package org.testah.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testah.client.enums.TypeOfKnown;


/**
 * The Interface KnownProblem.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface KnownProblem {

    /**
     * Linked ids.
     *
     * @return the string[]
     */
    String[] linkedIds() default {};

    /**
     * Description.
     *
     * @return the string
     */
    String description() default "";

    /**
     * Type of known.
     *
     * @return the type of known
     */
    TypeOfKnown typeOfKnown() default TypeOfKnown.DEFECT;

}
