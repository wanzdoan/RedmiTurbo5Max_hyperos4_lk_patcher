package com.xiaomi.onetrack.h.d;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.xiaomi.onetrack.h.a.a;

/* JADX INFO: loaded from: classes2.dex */
public abstract class b {
    private static String b = "ot_monitor_executor";
    private static Handler c;

    private static void a() {
        if (c == null) {
            synchronized (b.class) {
                try {
                    if (c == null) {
                        HandlerThread handlerThread = new HandlerThread(b);
                        handlerThread.start();
                        c = new Handler(handlerThread.getLooper());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static void a(Runnable runnable) {
        try {
            a();
            c.post(runnable);
        } catch (Exception e) {
            Log.e("OTMonitorExecutor", "execute failed,", e);
        }
    }

    public static void a(String str, String str2, String str3, String str4, int i) {
        if (a.a().a(str, str2)) {
            a(new c(str, str3, str4, i, str2));
        }
    }
}
