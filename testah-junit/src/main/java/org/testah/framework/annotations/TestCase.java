package org.testah.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testah.framework.enums.TestType;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestCase {

    String id()

    default "";

    String name()

    default "";

    String description()

    default "";

    String[] relatedLinks() default {};

    String[] relatedIds() default {};

    String[] tags() default {};

    TestType testType() default TestType.AUTOMATED;

    String[] components() default {};

    String[] platforms() default {};

    String[] devices() default {};

    String[] runTypes() default {};

}
