package com.e4.mac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface AvailabilitySPI {
    /**
     * SPI name: ${service_name} + ".live"
     * available values : service/process/job 
     * @return
     */
    String value() default "service";
}
