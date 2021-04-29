package com.e4.maclient.apt.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.e4.maclient.annotation.AvailabilitySPI;
import com.e4.maclient.apt.processor.model.AnnotationDescriptor;
import com.e4.maclient.apt.processor.model.MethodDescriptor;
import com.e4.maclient.apt.processor.model.Pair;

public class AvailabilitySPIProcessor extends MacAbstractProcessor {

    private AvailabilitySPIProcessor() {}

    @Override
    public String getAnnotationName() {
        return AvailabilitySPI.class.getCanonicalName();
    }

    @Override
    public void process(TypeElement annotation, Element element) {
        MethodDescriptor mDescriptor = processMethodAnnotation(annotation, element);
        AvailabilitySPI liveSPI = element.getAnnotation(AvailabilitySPI.class);
        AnnotationDescriptor annotationDescriptor = new AnnotationDescriptor(AvailabilitySPI.class.getCanonicalName());
        annotationDescriptor.setValue(new Pair<String>("value", liveSPI.value()));
        mDescriptor.addAnnotation(annotationDescriptor);
        
    }

    public static AvailabilitySPIProcessor build(ProcessingEnvironment procEnv) {
        AvailabilitySPIProcessor spi = new AvailabilitySPIProcessor();
        spi.init(procEnv);
        return spi;
    }
    
}
