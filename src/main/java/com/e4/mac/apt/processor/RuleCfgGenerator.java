package com.e4.mac.apt.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.e4.mac.apt.processor.model.AttributeDescriptor;
import com.e4.mac.apt.processor.model.ClassDescriptor;
import com.e4.mac.apt.processor.model.ElementDescriptor;
import com.e4.mac.apt.processor.model.MethodDescriptor;
import com.e4.mac.apt.processor.model.ModuleDescriptor;
import com.e4.mac.apt.processor.model.ParameterDescriptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class RuleCfgGenerator {
    public static final String CFG_FILE = "mac/rule_cfg.json";
    private static ModuleDescriptor root = new ModuleDescriptor("root");
    private static Set<MethodDescriptor> methodSet = new HashSet<>();
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory.of(ElementDescriptor.class).registerSubtype(ModuleDescriptor.class)
                            .registerSubtype(ClassDescriptor.class).registerSubtype(MethodDescriptor.class)
                            .registerSubtype(AttributeDescriptor.class).registerSubtype(ParameterDescriptor.class))
            .disableHtmlEscaping().create();

    public static ClassDescriptor getClassDescriptor(String fullName) {
        Optional<ElementDescriptor> clazzDescriptor = root.getChildren().stream()
                .filter(t -> t.getName().equals(fullName)).findAny();
        if (clazzDescriptor.isPresent()) {
            return (ClassDescriptor) clazzDescriptor.get();
        }

        ClassDescriptor cd = new ClassDescriptor(fullName);
        root.addChild(cd);
        return cd;
    }

    public static MethodDescriptor getMethodDescriptor(String fullName) {
        synchronized (methodSet) {
            Optional<MethodDescriptor> mDescriptor = methodSet.stream().filter(t -> t.getName().equals(fullName))
                    .findAny();
            if (mDescriptor.isPresent()) {
                return mDescriptor.get();
            }

            MethodDescriptor m = new MethodDescriptor(fullName);
            ClassDescriptor cDescriptor = RuleCfgGenerator.getClassDescriptor(m.getClazzName());
            cDescriptor.addChild(m);
            methodSet.add(m);
            return m;
        }
    }

    public static String buildCfg() {
        return gson.toJson(root);
    }

    public static ModuleDescriptor fromCfg(String json) {
        return gson.fromJson(json, ModuleDescriptor.class);
    }

    public static final ModuleDescriptor getCfg() {
        return root;
    }

    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("/mnt/d/code/e4/temp/a.json")));
        ModuleDescriptor md = fromCfg(json);
        System.out.println(gson.toJson(md));
    }
}
