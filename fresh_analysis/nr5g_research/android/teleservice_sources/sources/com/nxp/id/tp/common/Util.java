package com.nxp.id.tp.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public abstract class Util {
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final Random RNG = new SecureRandom();

    public static List wrapInList(Object... objArr) {
        ArrayList arrayList = new ArrayList(objArr.length);
        for (Object obj : objArr) {
            arrayList.add(obj);
        }
        return arrayList;
    }
}
