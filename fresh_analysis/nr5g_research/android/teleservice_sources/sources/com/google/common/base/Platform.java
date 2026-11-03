package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
abstract class Platform {
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    static boolean stringIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }
    }
}
