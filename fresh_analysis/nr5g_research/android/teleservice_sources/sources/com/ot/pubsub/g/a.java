package com.ot.pubsub.g;

import android.os.Handler;
import android.os.HandlerThread;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static final String a = "DbExecutor";
    private static String b = "ot_pubsub_db";
    private static Handler c;

    public static void a() {
        if (c == null) {
            synchronized (a.class) {
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
        a();
        c.post(runnable);
    }
}
