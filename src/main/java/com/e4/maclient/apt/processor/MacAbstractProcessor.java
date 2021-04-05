package com.e4.maclient.apt.processor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public abstract class MacAbstractProcessor {

    protected Messager messager;
    protected Types typeUtils;
    protected Elements elementUtils;

    public void init(ProcessingEnvironment procEnv) {
        this.messager = procEnv.getMessager();
        this.typeUtils = procEnv.getTypeUtils();
        this.elementUtils = procEnv.getElementUtils();
    }

    protected void processMethodAnnotation(Element annotation, Element enclosingElement) {
        System.out.println(">>annotation:" + annotation.getSimpleName());
        System.out.println(">>element:" + getElementName(enclosingElement));
        System.out.println(">>element Type:" + enclosingElement.getKind());
        ExecutableElement executableElement = ExecutableElement.class.cast(enclosingElement);
        System.out.println(">>executableElement:" + executableElement.getParameters());
        System.out.println(">>return:" + executableElement.getReturnType());
        printMessage(Kind.NOTE, "processCountedElement annotation", enclosingElement, null);
    }

    public boolean isAcceptable(Element annotation) {
        return annotation.equals(elementUtils.getTypeElement(this.getAnnotationName()));
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
    public String getElementName(Element element) {
        return getElementName(element, element.getSimpleName().toString());
    }

    public String getElementName(Element element, String name) {
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

    public abstract String getAnnotationName();
    public abstract void process(Element annotation, Element element);
}
