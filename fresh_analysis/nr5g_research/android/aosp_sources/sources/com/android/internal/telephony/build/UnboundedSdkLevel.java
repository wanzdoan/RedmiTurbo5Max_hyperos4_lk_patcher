package com.android.internal.telephony.build;

import android.os.Build;
import android.util.ArraySet;
import android.util.SparseArray;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class UnboundedSdkLevel {
    private static final SparseArray PREVIOUS_CODENAMES;
    private static final UnboundedSdkLevel sInstance;
    private final String mCodename;
    private final boolean mIsReleaseBuild;
    private final Set mKnownCodenames;
    private final int mSdkInt;

    public static boolean isAtLeast(String str) {
        return sInstance.isAtLeastInternal(str);
    }

    public static boolean isAtMost(String str) {
        return sInstance.isAtMostInternal(str);
    }

    static {
        Set set;
        SparseArray sparseArray = new SparseArray(4);
        PREVIOUS_CODENAMES = sparseArray;
        sparseArray.put(29, setOf("Q"));
        sparseArray.put(30, setOf("Q", "R"));
        sparseArray.put(31, setOf("Q", "R", "S"));
        sparseArray.put(32, setOf("Q", "R", "S", "Sv2"));
        int i = Build.VERSION.SDK_INT;
        String str = Build.VERSION.CODENAME;
        if (SdkLevel.isAtLeastT()) {
            set = Build.VERSION.KNOWN_CODENAMES;
        } else {
            set = (Set) sparseArray.get(i);
        }
        sInstance = new UnboundedSdkLevel(i, str, set);
    }

    private static Set setOf(String... strArr) {
        if (SdkLevel.isAtLeastR()) {
            return Set.of((Object[]) strArr);
        }
        ArraySet arraySet = new ArraySet(strArr.length);
        for (String str : strArr) {
            arraySet.add(str);
        }
        return arraySet;
    }

    UnboundedSdkLevel(int i, String str, Set<String> set) {
        this.mSdkInt = i;
        this.mCodename = str;
        this.mIsReleaseBuild = "REL".equals(str);
        this.mKnownCodenames = set;
    }

    boolean isAtLeastInternal(String str) {
        String strRemoveFingerprint = removeFingerprint(str);
        if (this.mIsReleaseBuild) {
            if (!isCodename(strRemoveFingerprint)) {
                return this.mSdkInt >= Integer.parseInt(strRemoveFingerprint);
            }
            if (!this.mKnownCodenames.contains(strRemoveFingerprint)) {
                return false;
            }
            UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Artifact with a known codename ", strRemoveFingerprint, " must be recompiled with a finalized integer version.");
            return false;
        }
        if (isCodename(strRemoveFingerprint)) {
            return this.mKnownCodenames.contains(strRemoveFingerprint);
        }
        return this.mSdkInt >= Integer.parseInt(strRemoveFingerprint);
    }

    boolean isAtMostInternal(String str) {
        String strRemoveFingerprint = removeFingerprint(str);
        if (this.mIsReleaseBuild) {
            if (!isCodename(strRemoveFingerprint)) {
                return this.mSdkInt <= Integer.parseInt(strRemoveFingerprint);
            }
            if (!this.mKnownCodenames.contains(strRemoveFingerprint)) {
                return true;
            }
            UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Artifact with a known codename ", strRemoveFingerprint, " must be recompiled with a finalized integer version.");
            return false;
        }
        if (isCodename(strRemoveFingerprint)) {
            return !this.mKnownCodenames.contains(strRemoveFingerprint) || this.mCodename.equals(strRemoveFingerprint);
        }
        return this.mSdkInt < Integer.parseInt(strRemoveFingerprint);
    }

    String removeFingerprint(String str) {
        int iIndexOf;
        return (!isCodename(str) || (iIndexOf = str.indexOf(46)) == -1) ? str : str.substring(0, iIndexOf);
    }

    private boolean isCodename(String str) {
        if (str.length() == 0) {
            throw new IllegalArgumentException();
        }
        return Character.isUpperCase(str.charAt(0));
    }
}
