package com.e4.maclient.apt;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MacProcessor extends AbstractProcessor {

    private TypeElement countedElem;
    private TypeElement timedElem;

    private boolean macDisabled = false;

    private Messager messager;
    private Types typeUtils;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        if (System.getProperty("mac.disable") != null) {
            macDisabled = true;
            return;
        }

        this.messager = procEnv.getMessager();
        this.typeUtils = procEnv.getTypeUtils();
        this.elementUtils = procEnv.getElementUtils();

        this.countedElem = this.elementUtils.getTypeElement("com.e4.maclient.annotation.Counted");
        this.timedElem = this.elementUtils.getTypeElement("com.e4.maclient.annotation.Timed");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (macDisabled)
            return false;
        if (!roundEnv.processingOver() && !roundEnv.errorRaised()) {
            processRound(annotations, roundEnv);
        }
        return false;
    }

    private void processRound(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                processElement(element);
            }
        });
    }

    /**
     * main entry of parsing the annotation
     * @param element
     */
    private void processElement(Element element) {
        element.getAnnotationMirrors().forEach(annotation -> {
            Element annotationType = annotation.getAnnotationType().asElement();
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            if (annotationType.equals(this.countedElem)) {
                processCountedElement(element, ElementFilter.methodsIn(enclosedElements));
            } else if (annotationType.equals(this.timedElem)) {
                processTimedElement(element, ElementFilter.methodsIn(enclosedElements));
            }
        });
    }

    private void processTimedElement(Element element, List<ExecutableElement> methodsIn) {
        methodsIn.forEach(methodElement->{
            String methodName = methodElement.getSimpleName().toString();
            methodElement.getParameters().forEach(varElem->{
                varElem.asType().toString();
            });
        });
        printMessage(Kind.NOTE, "processTimedElement annotation", element, null);
    }

    /**
     * process counted method
     */
    private void processCountedElement(Element element, List<ExecutableElement> methodsIn) {
        
        printMessage(Kind.NOTE, "processCountedElement annotation", element, null);
    }

    private void printMessage(Kind kind, String message, Element element, AnnotationMirror annotation) {
        this.messager.printMessage(kind, message, element, annotation);
    }

    /**
     * Get literal name of a element
     * 
     * @param element
     * @return
     */
    public String getElementName(TypeElement element) {
        return getElementName(element, element.getSimpleName().toString());
    }

    public String getElementName(TypeElement element, String name) {
        Element enclosingElement = element.getEnclosingElement();
        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = (PackageElement) enclosingElement;
            if (pkg.isUnnamed()) {
                return name;
            }
            return String.format("%s.%s", pkg.getQualifiedName(), name);
        }
        TypeElement te = (TypeElement) enclosingElement;
        return getElementName(te, te.getSimpleName() + "$" + name);
    }

}
