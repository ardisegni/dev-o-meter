package com.malam.payroll.devometer.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum ProgrammingLanguage {
    JAVA(Set.of("java", "properties")),
    JAVASCRIPT(Set.of("js")),
    TYPESCRIPT(Set.of("ts")),
    VUE(Set.of("vue"));

    private static final Map<String, ProgrammingLanguage> lookup;

    static {
        lookup = new HashMap<>();
        for (ProgrammingLanguage langEnum : ProgrammingLanguage.values()) {
            for (String extension : langEnum.extensions) {
                lookup.put(extension, langEnum);
            }
        }
    }

    private final Set<String> extensions;

    ProgrammingLanguage(Set<String> extensions) {
        this.extensions = extensions;
    }

    public static ProgrammingLanguage fromExtension(String extension) {
        return lookup.get(extension);
    }
}
