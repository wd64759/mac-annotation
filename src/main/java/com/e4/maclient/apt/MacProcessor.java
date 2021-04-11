package com.e4.maclient.apt;

import java.io.OutputStreamWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import com.e4.maclient.apt.processor.MacProcessorRegistery;
import com.e4.maclient.apt.processor.RuleCfgGenerator;
import com.e4.maclient.apt.processor.model.ModuleDescriptor;

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
        // System.out.println("call>>" + annotations.stream().map(t ->
        // t.getSimpleName()).collect(Collectors.joining(">")));
        if (macDisabled)
            return false;
        if (!roundEnv.processingOver() && !roundEnv.errorRaised()) {
            processRound(annotations, roundEnv);
        }
        if (roundEnv.processingOver() && validateCfg(RuleCfgGenerator.getCfg())) {
            String configJson = RuleCfgGenerator.buildCfg();
            generateConfigFile(configJson);
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

    /**
     * validate annotation configuration structure
     * 
     * @param md
     * @return
     */
    protected boolean validateCfg(ModuleDescriptor md) {
        // TODO
        return true;
    }

    protected void generateConfigFile(String configJson) {
        Filer filer = processingEnv.getFiler();
        try {
            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", RuleCfgGenerator.CFG_FILE);
            OutputStreamWriter out = new OutputStreamWriter(fileObject.openOutputStream());
            out.write(configJson);
            out.close();
        } catch (Exception e) {
            log("fail to generate monitoring configuration file - " + e);
        }
    }

    private void log(String msg) {
        if (processingEnv.getOptions().containsKey("debug")) {
            processingEnv.getMessager().printMessage(Kind.NOTE, msg);
        }
    }

}
