package org.testah.framework.annotations;

import java.lang.annotation.*;

/**
 * The Interface FailFast.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FailFast {

    /**
     * If failOnFirstException is true, the test execution will stop on first exception/error.
     * Otherwise, the test will run all steps, whether they pass or not.
     *
     * @return whether to stop test execution on first failure
     */
    boolean failOnFirstException() default true;
}
