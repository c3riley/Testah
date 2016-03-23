package org.testah.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testah.framework.enums.TypeOfKnown;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface KnownProblem {

    String[]linkedIds() default {};
    
    String description() default "";

    TypeOfKnown typeOfKnown() default TypeOfKnown.DEFECT;

}
