package org.testah.framework.annotations;

import org.testah.client.enums.TestType;

import java.lang.annotation.*;

/**
 * The Interface TestCase.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestCase {

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
    TestType testType() default TestType.DEFAULT;

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

}
