package com.xiaomi.onetrack.c;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class a {
    private static String b = "onetrack_broadcast";
    private static Handler c;

    public static void a() {
        if (c == null) {
            synchronized (a.class) {
                try {
                    if (c == null) {
                        Log.d("BroadcastExecutor", "initIfNeeded : " + Thread.currentThread().getId());
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
        Log.d("BroadcastExecutor", "BroadcastExecutor : " + Thread.currentThread().getId());
        a();
        c.post(runnable);
    }
}
