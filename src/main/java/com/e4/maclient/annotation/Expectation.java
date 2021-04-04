package com.e4.maclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Expectation {
    int volatility() default 50;
    String dataRange() default "";
    Severity severity() default Severity.Warning;
    String timewindow() default "";
}
enum Severity {Alert, Warning, Info};