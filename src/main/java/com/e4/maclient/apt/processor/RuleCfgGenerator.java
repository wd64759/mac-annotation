package com.e4.maclient.apt.processor;

import java.util.Optional;

import com.e4.maclient.apt.processor.model.ClassDescriptor;
import com.e4.maclient.apt.processor.model.ElementDescriptor;
import com.google.gson.Gson;

public class RuleCfgGenerator {
    private static ElementDescriptor root = new ElementDescriptor("root");
    private static Gson gson = new Gson();
    public static ClassDescriptor getClassDescriptor(String fullName) {
        Optional<ElementDescriptor> clazzDescriptor = root.getChildren().stream().filter(t->t.getName().equals(fullName)).findAny();
        if (clazzDescriptor.isPresent()) {
            return (ClassDescriptor) clazzDescriptor.get();
        }

        ClassDescriptor cd = new ClassDescriptor(fullName);
        root.addChild(cd);
        return cd;
    }
    public static void buildCfg() {
        System.out.println(gson.toJson(root));
    }
}
