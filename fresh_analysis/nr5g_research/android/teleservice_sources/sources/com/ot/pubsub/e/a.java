package com.ot.pubsub.e;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static final String a = "BroadcastExecutor";
    private static String b = "pub_broadcast";
    private static Handler c;

    public static void a() {
        if (c == null) {
            synchronized (a.class) {
                try {
                    if (c == null) {
                        Log.d(a, "initIfNeeded : " + Thread.currentThread().getId());
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
        Log.d(a, "BroadcastExecutor : " + Thread.currentThread().getId());
        a();
        c.post(runnable);
    }
}
