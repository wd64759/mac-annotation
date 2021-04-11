package com.e4.maclient.apt.processor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.e4.maclient.apt.processor.model.ClassDescriptor;
import com.e4.maclient.apt.processor.model.ElementDescriptor;
import com.e4.maclient.apt.processor.model.MethodDescriptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RuleCfgGenerator {
    private static ElementDescriptor root = new ElementDescriptor("root");
    private static Set<MethodDescriptor> methodSet = new HashSet<>();
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static ClassDescriptor getClassDescriptor(String fullName) {
        Optional<ElementDescriptor> clazzDescriptor = root.getChildren().stream().filter(t->t.getName().equals(fullName)).findAny();
        if (clazzDescriptor.isPresent()) {
            return (ClassDescriptor) clazzDescriptor.get();
        }

        ClassDescriptor cd = new ClassDescriptor(fullName);
        root.addChild(cd);
        return cd;
    }

    public static MethodDescriptor getMethodDescriptor(String fullName) {
        synchronized(methodSet) {
            Optional<MethodDescriptor> mDescriptor = methodSet.stream().filter(t->t.getName().equals(fullName)).findAny();
            if(mDescriptor.isPresent()) {
                return mDescriptor.get();
            }

            MethodDescriptor m = new MethodDescriptor(fullName);
            ClassDescriptor cDescriptor = RuleCfgGenerator.getClassDescriptor(m.getClazzName());
            cDescriptor.addChild(m);
            methodSet.add(m);
            return m;
        }
    }

    public static void buildCfg() {
        System.out.println(gson.toJson(root));
    }
}
