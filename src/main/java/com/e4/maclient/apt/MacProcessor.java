package com.e4.maclient.apt;

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

import com.e4.maclient.apt.processor.MacProcessorRegistery;
import com.e4.maclient.apt.processor.RuleCfgGenerator;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MacProcessor extends AbstractProcessor {

    private boolean macDisabled = false;

    @Override
    public synchronized void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        if (System.getProperty("mac.disable") != null) {
            macDisabled = true;
            return;
        }
        MacProcessorRegistery.initProcessors(procEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // System.out.println("call>>" + annotations.stream().map(t -> t.getSimpleName()).collect(Collectors.joining(">")));
        if (macDisabled)
            return false;
        if (!roundEnv.processingOver() && !roundEnv.errorRaised()) {
            processRound(annotations, roundEnv);
        }
        if(roundEnv.processingOver()) {
            RuleCfgGenerator.buildCfg();
        }
        return false;
    }

    private void processRound(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            MacProcessorRegistery.getProcessors().forEach(processor -> {
                if (processor.isAcceptable(annotation)) {
                    for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                        processor.process(annotation, element);
                    }
                }
            });
        });
    }

}
