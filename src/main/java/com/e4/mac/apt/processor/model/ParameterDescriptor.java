package com.e4.mac.apt.processor.model;

public class ParameterDescriptor extends ElementDescriptor {

    private static final long serialVersionUID = 1L;

    String paramType;

    public ParameterDescriptor(String name) {
        super(name);
        this.annotationType = AnnotationType.PARAMETER;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamType() {
        return paramType;
    }

}
