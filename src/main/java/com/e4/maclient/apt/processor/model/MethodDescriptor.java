package com.e4.maclient.apt.processor.model;

import java.util.ArrayList;
import java.util.List;

public class MethodDescriptor extends ElementDescriptor {

    private static final long serialVersionUID = 1L;
    List<ParameterDescriptor> params;
    String returnType;

    public MethodDescriptor(String name) {
        super(name);
        this.params = new ArrayList<>();
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public List<ParameterDescriptor> getParams() {
        return this.params;
    }

    public void addParam(ParameterDescriptor param) {
        this.params.add(param);
    }
}