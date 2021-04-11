package com.e4.maclient.apt.processor;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.e4.maclient.annotation.TagSet;
import com.e4.maclient.apt.processor.model.AnnotationDescriptor;
import com.e4.maclient.apt.processor.model.MethodDescriptor;
import com.e4.maclient.apt.processor.model.Pair;

public class TagProcessor extends MacAbstractProcessor {

    private TagProcessor(){}

    @Override
    public String getAnnotationName() {
        return TagSet.class.getCanonicalName();
    }

    @Override
    public void process(TypeElement annotation, Element element) {
        if(element.getKind().equals(ElementKind.METHOD)) {
            MethodDescriptor mDescriptor = this.processMethodAnnotation(annotation, element);
            AnnotationDescriptor ad = new AnnotationDescriptor(this.getAnnotationName());
            mDescriptor.addAnnotation(ad);
            
            TagSet tags = element.getAnnotation(TagSet.class);
            Arrays.asList(tags.value()).forEach(tag->{
                String[] annotationPair = tag.value().split("=");
                ad.setValue(new Pair<String>(annotationPair[0], annotationPair[1]));
            });
        }
        if(element.getKind().equals(ElementKind.PARAMETER)) {
            this.processParamAnnotation(annotation, element);
        }
    }

    public static TagProcessor build(ProcessingEnvironment procEnv) {
        TagProcessor tp = new TagProcessor();
        tp.init(procEnv);
        return tp;
    }
}
