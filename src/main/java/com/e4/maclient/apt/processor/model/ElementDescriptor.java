package com.e4.maclient.apt.processor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementDescriptor implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected List<AnnotationDescriptor> annotations = new ArrayList<>();
    protected List<ElementDescriptor> children;
    protected ElementDescriptor parent;

    public ElementDescriptor(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void addChild(ElementDescriptor element) {
        this.children.add(element);
    }

    public List<ElementDescriptor> getChildren() {
        return this.children;
    }

    public void setParent(ElementDescriptor parent) {
        this.parent = parent;
    }

    public ElementDescriptor getParent() {
        return parent;
    }

}
