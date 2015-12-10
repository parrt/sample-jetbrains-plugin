package org.antlr.jetbrains.plugin;

import com.intellij.lang.Language;

public class SampleLanguage extends Language {
    public static final SampleLanguage INSTANCE = new SampleLanguage();

    private SampleLanguage() {
        super("STGroup");
    }
}
