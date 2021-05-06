package com.e4.mac.apt.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.e4.mac.annotation.Counted;
import com.e4.mac.apt.processor.model.AnnotationDescriptor;
import com.e4.mac.apt.processor.model.MethodDescriptor;
import com.e4.mac.apt.processor.model.Pair;

public class CountedProcessor extends MacAbstractProcessor {

    private CountedProcessor(){}

    @Override
    public String getAnnotationName() {
        return Counted.class.getCanonicalName();
    }

    @Override
    public void process(TypeElement annotation, Element element) {
        MethodDescriptor mDescriptor = processMethodAnnotation(annotation, element);
        Counted counted = element.getAnnotation(Counted.class);
        AnnotationDescriptor annotationDescriptor = new AnnotationDescriptor(Counted.class.getCanonicalName());
        annotationDescriptor.setValue(new Pair<String>("value", counted.value()));
        annotationDescriptor.setValue(new Pair<String>("description", counted.description()));
        annotationDescriptor.setValue(new Pair<Boolean>("recordFailureOnly", counted.recordFailureOnly()));
        mDescriptor.addAnnotation(annotationDescriptor);
    }

    public static CountedProcessor build(ProcessingEnvironment procEnv) {
        CountedProcessor cp = new CountedProcessor();
        cp.init(procEnv);
        return cp;
    }

}
