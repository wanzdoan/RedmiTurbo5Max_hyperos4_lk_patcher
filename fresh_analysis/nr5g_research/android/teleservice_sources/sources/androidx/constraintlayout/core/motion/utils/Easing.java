package androidx.constraintlayout.core.motion.utils;

import miuix.animation.FolmeEase;

/* JADX INFO: loaded from: classes.dex */
public class Easing {
    String str = "identity";
    static Easing sDefault = new Easing();
    public static String[] NAMED_EASING = {"standard", FolmeEase.ACCELERATE, FolmeEase.DECELERATE, FolmeEase.LINEAR};

    public String toString() {
        return this.str;
    }
}
