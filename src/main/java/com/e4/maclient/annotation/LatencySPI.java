package com.e4.maclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface LatencySPI {
    /**
     * SPI name: ${service_name} + ".latency"
     * available values : service/process/job 
     * @return
     */
    String value() default "service";
}
