package com.e4.maclient.apt.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.e4.maclient.annotation.LatencySPI;
import com.e4.maclient.apt.processor.model.AnnotationDescriptor;
import com.e4.maclient.apt.processor.model.MethodDescriptor;
import com.e4.maclient.apt.processor.model.Pair;

public class LatencySPIProcessor extends MacAbstractProcessor {

    private LatencySPIProcessor() {}

    @Override
    public String getAnnotationName() {
        return LatencySPI.class.getCanonicalName();
    }

    @Override
    public void process(TypeElement annotation, Element element) {
        MethodDescriptor mDescriptor = processMethodAnnotation(annotation, element);
        LatencySPI spi = element.getAnnotation(LatencySPI.class);
        AnnotationDescriptor annotationDescriptor = new AnnotationDescriptor(LatencySPI.class.getCanonicalName());
        annotationDescriptor.setValue(new Pair<String>("value", spi.value()));
        mDescriptor.addAnnotation(annotationDescriptor);
    }

    public static LatencySPIProcessor build(ProcessingEnvironment procEnv) {
        LatencySPIProcessor spi = new LatencySPIProcessor();
        spi.init(procEnv);
        return spi;
    }
    
}
