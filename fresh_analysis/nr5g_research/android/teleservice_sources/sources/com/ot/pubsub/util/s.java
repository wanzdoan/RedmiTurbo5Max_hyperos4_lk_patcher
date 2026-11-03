package com.ot.pubsub.util;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class s {
    public static final String a = "\\.";
    public static final String b = ",";
    private static final String c = "com.ot.pubsub.util.s";

    public static boolean a(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean a(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean b(CharSequence charSequence) {
        return !a(charSequence);
    }

    public static List a(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String[] strArrSplit = str.split(str2);
            if (strArrSplit == null || strArrSplit.length <= 0) {
                return null;
            }
            return Arrays.asList(strArrSplit);
        } catch (Exception e) {
            j.b(c, "StringToList error: " + e.getMessage());
            return null;
        }
    }

    public static Set a(String str, String str2, String str3) {
        HashSet hashSet = new HashSet();
        try {
            String[] strArrSplit = null;
            String[] strArrSplit2 = TextUtils.isEmpty(str) ? null : str.split(str3);
            if (strArrSplit2 != null && strArrSplit2.length > 0) {
                hashSet.addAll(Arrays.asList(strArrSplit2));
            }
            if (!TextUtils.isEmpty(str2)) {
                strArrSplit = str2.split(str3);
            }
            if (strArrSplit != null && strArrSplit.length > 0) {
                hashSet.addAll(Arrays.asList(strArrSplit));
            }
            return hashSet;
        } catch (Exception e) {
            j.b(c, "mergeParams error: " + e.getMessage());
            return hashSet;
        }
    }
}
