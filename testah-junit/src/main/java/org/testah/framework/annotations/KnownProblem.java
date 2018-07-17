package org.testah.framework.annotations;

import org.testah.client.enums.TypeOfKnown;

import java.lang.annotation.*;

/**
 * The Interface KnownProblem.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
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
