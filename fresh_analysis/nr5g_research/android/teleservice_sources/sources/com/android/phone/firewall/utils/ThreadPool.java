package com.android.phone.firewall.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public abstract class ThreadPool {
    private static ExecutorService sExecutor = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        sExecutor.execute(runnable);
    }
}
