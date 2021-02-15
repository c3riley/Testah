package org.testah.framework.annotations;

import org.junit.jupiter.api.Test;
import org.testah.client.enums.TestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface TestCase.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
// Looks like you cannot have annotations @Test and @ParameterizedTest
// at the same time, see
// https://stackoverflow.com/questions/51867650/junit-5-no-parameterresolver-registered-for-parameter
// Therefore commenting out here
// @Test
@TestCase
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface TestCaseJUnit5 {

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
