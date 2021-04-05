package com.e4.maclient.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.e4.maclient.apt.processor.CountedProcessor;
import com.e4.maclient.apt.processor.MacAbstractProcessor;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MacProcessor extends AbstractProcessor {

    private boolean macDisabled = false;

    // all elements marked with MAC annotations
    private List<MacAbstractProcessor> macAnnotationProcessors = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        if (System.getProperty("mac.disable") != null) {
            macDisabled = true;
            return;
        }
        this.macAnnotationProcessors.add(CountedProcessor.build(procEnv));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("call>>" + annotations.stream().map(t -> t.getSimpleName()).collect(Collectors.joining(">")));
        if (macDisabled)
            return false;
        if (!roundEnv.processingOver() && !roundEnv.errorRaised()) {
            processRound(annotations, roundEnv);
        }
        return false;
    }

    private void processRound(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            macAnnotationProcessors.forEach(processor -> {
                if (processor.isAcceptable(annotation)) {
                    for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                        processor.process(annotation, element);
                    }
                }
            });
        });
    }

}
