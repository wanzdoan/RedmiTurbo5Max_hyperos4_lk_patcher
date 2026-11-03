package com.ot.pubsub.util;

import android.content.Context;
import android.os.UserManager;

/* JADX INFO: loaded from: classes.dex */
public class f {
    private static final String a = "FbeUtil";

    private f() {
    }

    public static Context a(Context context) {
        if (c(context)) {
            j.a(a, "getSafeContext return origin ctx");
            return context;
        }
        j.a(a, "getSafeContext , create the safe ctx");
        return context.createDeviceProtectedStorageContext();
    }

    public static boolean b(Context context) {
        return !c(context);
    }

    private static boolean c(Context context) {
        UserManager userManager = (UserManager) context.getSystemService("user");
        return userManager != null && userManager.isUserUnlocked();
    }
}
