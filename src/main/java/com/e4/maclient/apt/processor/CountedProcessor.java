package com.e4.maclient.apt.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.e4.maclient.annotation.Counted;

public class CountedProcessor extends MacAbstractProcessor {

    private CountedProcessor(){}

    @Override
    public String getAnnotationName() {
        return Counted.class.getCanonicalName();
    }

    @Override
    public void process(TypeElement annotation, Element element) {
        String methodScratch = processMethodAnnotation(annotation, element);
        System.out.println("annotation:" + annotation.asType());
        System.out.println("methodScratch:" + methodScratch);
        System.out.println("description:" + element.getAnnotation(Counted.class));
    }

    public static CountedProcessor build(ProcessingEnvironment procEnv) {
        CountedProcessor cp = new CountedProcessor();
        cp.init(procEnv);
        return cp;
    }

}
