package com.e4.mac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Repeatable(TimedSet.class)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Timed {
    String value() default "";
    String[] extraTags() default {};
    boolean longTask() default false;
    double[] percentiles() default {};
    boolean histogram() default false;
    String description() default "";
}
