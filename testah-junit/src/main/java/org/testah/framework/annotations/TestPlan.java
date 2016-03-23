package org.testah.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testah.framework.enums.TestType;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TestPlan {

	String uuid()

	default "";

	String name()

	default "";

	String description()

	default "";

	String[]relatedLinks() default {};

	String[]relatedIds() default {};

	String[]tags() default {};

	TestType testType() default TestType.AUTOMATED;

	String[]components() default {};

	String[]runTypes() default {};

}
