package com.e4.mac.annotation;

import java.util.List;

public class Example {
    
    @Checkpoint(monitorType = MonitorType.Record, tracingKey = "$1")
    public Object processOrder(long orderID, Object context) {
        // business logic
        return null;
    }

    @Checkpoint(monitorType = MonitorType.Timer, volumeExp = "$1::size")
    @Expectation(volatility=20, severity = Severity.Alert)
    public Object processOrders(List<Long> orderID, Object context) {
        // business logic
        return null;
    }

}
