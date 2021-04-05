package com.e4.maclient.apt.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import com.e4.maclient.annotation.Counted;

public class CountedProcessor extends MacAbstractProcessor {

    private CountedProcessor(){}

    @Override
    public String getAnnotationName() {
        return Counted.class.getCanonicalName();
    }

    @Override
    public void process(Element annotation, Element element) {
        processMethodAnnotation(annotation, element);
    }

    public static CountedProcessor build(ProcessingEnvironment procEnv) {
        CountedProcessor cp = new CountedProcessor();
        cp.init(procEnv);
        return cp;
    }

}
