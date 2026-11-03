package com.xiaomi.onetrack.util;

import android.content.Context;
import android.os.UserManager;

/* JADX INFO: loaded from: classes2.dex */
public abstract class k {
    public static Context a(Context context) {
        if (e(context)) {
            r.a("FbeUtil", "getSafeContext return origin ctx");
            return context;
        }
        r.a("FbeUtil", "getSafeContext , create the safe ctx");
        return context.createDeviceProtectedStorageContext();
    }

    public static boolean d(Context context) {
        return !e(context);
    }

    private static boolean e(Context context) {
        try {
            UserManager userManager = (UserManager) context.getSystemService("user");
            return userManager != null && userManager.isUserUnlocked();
        } catch (Exception e) {
            r.a("FbeUtil", "isUserUnlocked Exception: " + e.getMessage());
            return false;
        }
    }
}
