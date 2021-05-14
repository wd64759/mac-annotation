package com.e4.mac.apt.processor.model;

public class ModuleDescriptor extends ElementDescriptor {

    public ModuleDescriptor(String name) {
        super(name);
        this.annotationType = AnnotationType.MODULE;
    }

    private static final long serialVersionUID = 1L;
    
}
