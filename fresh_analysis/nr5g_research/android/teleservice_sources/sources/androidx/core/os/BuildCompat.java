package androidx.core.os;

import android.os.Build;
import android.os.ext.SdkExtensions;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import miuix.animation.controller.AnimState;

/* JADX INFO: compiled from: BuildCompat.kt */
/* JADX INFO: loaded from: classes.dex */
public final class BuildCompat {
    public static final int AD_SERVICES_EXTENSION_INT;
    public static final BuildCompat INSTANCE = new BuildCompat();
    public static final int R_EXTENSION_INT;
    public static final int S_EXTENSION_INT;
    public static final int T_EXTENSION_INT;

    private BuildCompat() {
    }

    private static final Integer isAtLeastPreReleaseCodename$codenameToInt(String str) {
        String upperCase = str.toUpperCase(Locale.ROOT);
        upperCase.getClass();
        return Intrinsics.areEqual(upperCase, "BAKLAVA") ? 0 : null;
    }

    public static final boolean isAtLeastPreReleaseCodename(String str, String str2) {
        str.getClass();
        str2.getClass();
        if (Intrinsics.areEqual("REL", str2)) {
            return false;
        }
        Integer numIsAtLeastPreReleaseCodename$codenameToInt = isAtLeastPreReleaseCodename$codenameToInt(str2);
        Integer numIsAtLeastPreReleaseCodename$codenameToInt2 = isAtLeastPreReleaseCodename$codenameToInt(str);
        if (numIsAtLeastPreReleaseCodename$codenameToInt != null && numIsAtLeastPreReleaseCodename$codenameToInt2 != null) {
            return numIsAtLeastPreReleaseCodename$codenameToInt.intValue() >= numIsAtLeastPreReleaseCodename$codenameToInt2.intValue();
        }
        if (numIsAtLeastPreReleaseCodename$codenameToInt != null || numIsAtLeastPreReleaseCodename$codenameToInt2 != null) {
            return numIsAtLeastPreReleaseCodename$codenameToInt != null;
        }
        Locale locale = Locale.ROOT;
        String upperCase = str2.toUpperCase(locale);
        upperCase.getClass();
        String upperCase2 = str.toUpperCase(locale);
        upperCase2.getClass();
        return upperCase.compareTo(upperCase2) >= 0;
    }

    public static final boolean isAtLeastB_1() {
        return Build.VERSION.SDK_INT_FULL >= 3600001;
    }

    static {
        Api30Impl api30Impl = Api30Impl.INSTANCE;
        R_EXTENSION_INT = api30Impl.getExtensionVersion(30);
        S_EXTENSION_INT = api30Impl.getExtensionVersion(31);
        T_EXTENSION_INT = api30Impl.getExtensionVersion(33);
        AD_SERVICES_EXTENSION_INT = api30Impl.getExtensionVersion(AnimState.VIEW_SIZE);
    }

    /* JADX INFO: compiled from: BuildCompat.kt */
    final class Api30Impl {
        public static final Api30Impl INSTANCE = new Api30Impl();

        private Api30Impl() {
        }

        public final int getExtensionVersion(int i) {
            return SdkExtensions.getExtensionVersion(i);
        }
    }
}
