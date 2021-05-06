package com.e4.mac.apt.processor;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import com.e4.mac.apt.processor.model.MethodDescriptor;
import com.e4.mac.apt.processor.model.ParameterDescriptor;

public abstract class MacAbstractProcessor {

    protected Messager messager;
    protected Types typeUtils;
    protected Elements elementUtils;

    public void init(ProcessingEnvironment procEnv) {
        this.messager = procEnv.getMessager();
        this.typeUtils = procEnv.getTypeUtils();
        this.elementUtils = procEnv.getElementUtils();
    }

    protected MethodDescriptor processMethodAnnotation(TypeElement annotation, Element enclosingElement) {
        ExecutableElement executableElement = ExecutableElement.class.cast(enclosingElement);
        String methodPath = getElementName(enclosingElement);
        String returnType = executableElement.getReturnType().toString();

        // MethodDescriptor mDescriptor = new MethodDescriptor(methodPath);
        MethodDescriptor mDescriptor = RuleCfgGenerator.getMethodDescriptor(methodPath);
        mDescriptor.setReturnType(returnType);

        List<? extends VariableElement> params = executableElement.getParameters();
        params.forEach(param -> {
            String paramName = param.getSimpleName().toString();
            Element paramElement = ((DeclaredType) param.asType()).asElement();
            String paramType = paramElement.getSimpleName().toString();
            ParameterDescriptor pDescriptor = new ParameterDescriptor(paramName);
            pDescriptor.setParamType(paramType);
            mDescriptor.addParam(pDescriptor);

            paramElement.getAnnotationMirrors().stream().forEach(paramAnnotation -> {
                TypeElement paramAnnotationElement = (TypeElement) paramAnnotation;
                MacProcessorRegistery.getProcessors().forEach(processor -> {
                    if (processor.isAcceptable(paramAnnotationElement)) {
                        processor.process(paramAnnotationElement, paramElement);
                    }
                });
            });
            System.out.println(String.format("param name:%s, type:%s, annotations:%s", paramName, paramType,
                    param.getAnnotationMirrors()));
        });
        // System.out.println(">>return:" + executableElement.getReturnType());
        return mDescriptor;
    }

    protected void processParamAnnotation(TypeElement annotation, Element element) {
        printMessage(Kind.ERROR, "TODO - processParamAnnotation", element, null);
    }

    protected void processFieldAnnotation(TypeElement annotation, Element element) {
        printMessage(Kind.ERROR, "TODO - processFieldAnnotation", element, null);
    }

    protected void processConstructorAnnotation(TypeElement annotation, Element element) {
        printMessage(Kind.ERROR, "TODO - processConstructorAnnotation", element, null);
    }

    public boolean isAcceptable(Element annotation) {
        return annotation.equals(elementUtils.getTypeElement(this.getAnnotationName()));
    }

    protected void printMessage(Kind kind, String message, Element element, AnnotationMirror annotation) {
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

    public abstract void process(TypeElement annotation, Element element);
}
