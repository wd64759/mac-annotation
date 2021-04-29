package com.e4.maclient.apt.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

public class MacProcessorRegistery {

    private static List<MacAbstractProcessor> macAnnotationProcessors = new ArrayList<>();
    private static boolean init;

    public static void initProcessors(ProcessingEnvironment procEnv) {
        if(!init) {
            synchronized(macAnnotationProcessors) {
                if(init) {
                    return;
                }
                init = true;
                macAnnotationProcessors.add(AvailabilitySPIProcessor.build(procEnv));
                macAnnotationProcessors.add(LatencySPIProcessor.build(procEnv));
                macAnnotationProcessors.add(CountedProcessor.build(procEnv));
                macAnnotationProcessors.add(TagProcessor.build(procEnv));
            }
        }
    }

    public static final List<MacAbstractProcessor> getProcessors() {
        return macAnnotationProcessors;
    }
    
}
