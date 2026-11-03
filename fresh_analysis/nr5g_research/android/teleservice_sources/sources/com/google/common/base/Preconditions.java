package com.google.common.base;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class Preconditions {
    public static void checkArgument(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean z, Object obj) {
        if (!z) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static void checkArgument(boolean z, String str, char c) {
        if (z) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, Character.valueOf(c)));
    }

    public static void checkArgument(boolean z, String str, int i) {
        if (z) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, Integer.valueOf(i)));
    }

    public static void checkArgument(boolean z, String str, Object obj) {
        if (z) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, obj));
    }

    public static void checkArgument(boolean z, String str, int i, int i2) {
        if (z) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, Integer.valueOf(i), Integer.valueOf(i2)));
    }

    public static void checkArgument(boolean z, String str, Object obj, Object obj2) {
        if (z) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, obj, obj2));
    }

    public static void checkState(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean z, Object obj) {
        if (!z) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void checkState(boolean z, String str, int i) {
        if (z) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, Integer.valueOf(i)));
    }

    public static void checkState(boolean z, String str, Object obj) {
        if (z) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m(Strings.lenientFormat(str, obj));
    }

    public static int checkElementIndex(int i, int i2) {
        return checkElementIndex(i, i2, "index");
    }

    public static int checkElementIndex(int i, int i2, String str) {
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException(badElementIndex(i, i2, str));
        }
        return i;
    }

    private static String badElementIndex(int i, int i2, String str) {
        if (i < 0) {
            return Strings.lenientFormat("%s (%s) must not be negative", str, Integer.valueOf(i));
        }
        if (i2 < 0) {
            HexString$$ExternalSyntheticBUOutline1.m("negative size: ", i2);
            return null;
        }
        return Strings.lenientFormat("%s (%s) must be less than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static int checkPositionIndex(int i, int i2) {
        return checkPositionIndex(i, i2, "index");
    }

    public static int checkPositionIndex(int i, int i2, String str) {
        if (i < 0 || i > i2) {
            throw new IndexOutOfBoundsException(badPositionIndex(i, i2, str));
        }
        return i;
    }

    private static String badPositionIndex(int i, int i2, String str) {
        if (i < 0) {
            return Strings.lenientFormat("%s (%s) must not be negative", str, Integer.valueOf(i));
        }
        if (i2 < 0) {
            HexString$$ExternalSyntheticBUOutline1.m("negative size: ", i2);
            return null;
        }
        return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void checkPositionIndexes(int i, int i2, int i3) {
        if (i < 0 || i2 < i || i2 > i3) {
            throw new IndexOutOfBoundsException(badPositionIndexes(i, i2, i3));
        }
    }

    private static String badPositionIndexes(int i, int i2, int i3) {
        if (i < 0 || i > i3) {
            return badPositionIndex(i, i3, "start index");
        }
        if (i2 < 0 || i2 > i3) {
            return badPositionIndex(i2, i3, "end index");
        }
        return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", Integer.valueOf(i2), Integer.valueOf(i));
    }
}
