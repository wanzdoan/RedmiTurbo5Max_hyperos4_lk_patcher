package miuix.pickerwidget.internal.util.async;

import android.os.HandlerThread;
import android.os.Looper;
import android.util.ArrayMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WorkerThreads {
    private static final Map sWorkers = new ArrayMap();

    class ThreadWrapper {
        int refCount = 1;
        final HandlerThread thread;

        ThreadWrapper(String str) {
            HandlerThread handlerThread = new HandlerThread(str);
            this.thread = handlerThread;
            handlerThread.start();
        }
    }

    public static synchronized Looper acquireWorker(String str) {
        ThreadWrapper threadWrapper;
        try {
            Map map = sWorkers;
            threadWrapper = (ThreadWrapper) map.get(str);
            if (threadWrapper == null) {
                threadWrapper = new ThreadWrapper(str);
                map.put(str, threadWrapper);
            } else {
                threadWrapper.refCount++;
            }
        } catch (Throwable th) {
            throw th;
        }
        return threadWrapper.thread.getLooper();
    }

    public static synchronized void releaseWorker(String str) {
        Map map = sWorkers;
        ThreadWrapper threadWrapper = (ThreadWrapper) map.get(str);
        if (threadWrapper != null) {
            int i = threadWrapper.refCount - 1;
            threadWrapper.refCount = i;
            if (i == 0) {
                map.remove(str);
                threadWrapper.thread.quitSafely();
            }
        }
    }
}
