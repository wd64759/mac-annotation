package com.e4.mac.annotation;

public @interface Checkpoint {
    String id() default "$0";
    MonitorType monitorType() default MonitorType.Record;
    String tracingKey() default "";
    String volumeExp() default "";
}

enum MonitorType {Record, Timer};
