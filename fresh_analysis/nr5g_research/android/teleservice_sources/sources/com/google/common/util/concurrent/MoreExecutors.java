package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public abstract class MoreExecutors {

    class Application {
        Application() {
        }

        void addShutdownHook(Thread thread) {
            Runtime.getRuntime().addShutdownHook(thread);
        }
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }
}
