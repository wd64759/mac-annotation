package com.e4.maclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Counted {
    String value() default "method.counted";
    boolean recordFailureOnly() default false;
    String[] extraTags() default {};
    String description() default "";
}
