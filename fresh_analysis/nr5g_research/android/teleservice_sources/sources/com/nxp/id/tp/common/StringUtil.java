package com.nxp.id.tp.common;

import java.util.Arrays;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class StringUtil {
    public static final String BAR = repeat('-', 80);

    public static String repeat(char c, int i) {
        if (i < 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Negative lengths are not allowed");
            return null;
        }
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        Arrays.fill(cArr, c);
        return new String(cArr);
    }
}
