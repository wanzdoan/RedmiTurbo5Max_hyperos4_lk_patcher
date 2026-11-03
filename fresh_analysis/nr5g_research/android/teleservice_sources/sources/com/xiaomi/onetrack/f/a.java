package com.xiaomi.onetrack.f;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.xiaomi.onetrack.util.k;

/* JADX INFO: loaded from: classes2.dex */
public abstract class a {
    private static Context a = null;
    private static Context b = null;
    private static int c = 0;
    private static String d = null;
    private static String e = null;
    private static long f = 0;
    private static volatile boolean g = false;

    public static void a(Context context) {
        if (g) {
            return;
        }
        synchronized (a.class) {
            try {
                if (g) {
                    return;
                }
                a = context;
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(a.getPackageName(), 0);
                    c = packageInfo.versionCode;
                    d = packageInfo.versionName;
                    f = packageInfo.lastUpdateTime;
                    e = a.getPackageName();
                } catch (PackageManager.NameNotFoundException e2) {
                    e2.printStackTrace();
                }
                g = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static Context a() {
        if (k.d(a)) {
            Context context = b;
            if (context != null) {
                return context;
            }
            synchronized (a.class) {
                try {
                    if (b == null) {
                        b = k.a(a);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return b;
        }
        return a;
    }

    public static Context b() {
        return a;
    }

    public static String c() {
        return d;
    }

    public static int d() {
        return c;
    }

    public static String e() {
        return e;
    }

    public static long f() {
        return f;
    }
}
