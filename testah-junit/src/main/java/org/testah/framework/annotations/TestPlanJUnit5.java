package org.testah.framework.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testah.client.enums.TestType;
import org.testah.framework.testPlan.Junit5TestPlanSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface TestPlan.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.TYPE, ElementType.METHOD})
@ExtendWith(Junit5TestPlanSupport.class)
public @interface TestPlanJUnit5 {

    /**
     * Id.
     *
     * @return the int
     */
    int id()

            default -1;

    /**
     * Name.
     *
     * @return the string
     */
    String name()

            default "";

    /**
     * Description.
     *
     * @return the string
     */
    String description()

            default "";

    /**
     * Related links.
     *
     * @return the string[]
     */
    String[] relatedLinks() default {};

    /**
     * Related ids.
     *
     * @return the string[]
     */
    String[] relatedIds() default {};

    /**
     * Tags.
     *
     * @return the string[]
     */
    String[] tags() default {};

    /**
     * Test type.
     *
     * @return the test type
     */
    TestType testType() default TestType.AUTOMATED;

    /**
     * Components.
     *
     * @return the string[]
     */
    String[] components() default {};

    /**
     * Platforms.
     *
     * @return the string[]
     */
    String[] platforms() default {};

    /**
     * Devices.
     *
     * @return the string[]
     */
    String[] devices() default {};

    /**
     * Run types.
     *
     * @return the string[]
     */
    String[] runTypes() default {};

    /**
     * Owner.
     *
     * @return the string
     */
    String owner() default "";

}
