package com.android.internal.telephony;

import android.os.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class LocalLog {
    private final Deque mLog;
    private final int mMaxLines;
    private final boolean mUseLocalTimestamps;

    public LocalLog(int i) {
        this(i, true);
    }

    public LocalLog(int i, boolean z) {
        int iMax = Math.max(0, i);
        this.mMaxLines = iMax;
        this.mLog = new ArrayDeque(iMax);
        this.mUseLocalTimestamps = z;
    }

    public void log(String str) {
        String str2;
        if (this.mMaxLines <= 0) {
            return;
        }
        if (this.mUseLocalTimestamps) {
            str2 = LocalDateTime.now() + " - " + str;
        } else {
            str2 = Duration.ofMillis(SystemClock.elapsedRealtime()) + " / " + Instant.now() + " - " + str;
        }
        append(str2);
    }

    private synchronized void append(String str) {
        while (true) {
            try {
                int size = this.mLog.size();
                int i = this.mMaxLines;
                Deque deque = this.mLog;
                if (size >= i) {
                    deque.remove();
                } else {
                    deque.add(str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dump(printWriter);
    }

    public synchronized void dump(PrintWriter printWriter) {
        dump("", printWriter);
    }

    public synchronized void dump(String str, PrintWriter printWriter) {
        Iterator it = this.mLog.iterator();
        while (it.hasNext()) {
            printWriter.printf("%s%s\n", str, it.next());
        }
    }
}
