package com.android.internal.telephony;

import android.os.SystemClock;
import android.util.LongArrayQueue;

/* JADX INFO: loaded from: classes.dex */
public class SlidingWindowEventCounter {
    private final int mNumOccurrences;
    private final LongArrayQueue mTimestampQueueMillis;
    private final long mWindowSizeMillis;

    public SlidingWindowEventCounter(long j, int i) {
        if (j < 0) {
            Phone$$ExternalSyntheticBUOutline0.m("windowSizeMillis must be greater or equal to 0");
            throw null;
        }
        if (i <= 1) {
            Phone$$ExternalSyntheticBUOutline0.m("numOccurrences must be greater than 1");
            throw null;
        }
        this.mWindowSizeMillis = j;
        this.mNumOccurrences = i;
        this.mTimestampQueueMillis = new LongArrayQueue(i);
    }

    public synchronized boolean addOccurrence() {
        return addOccurrence(SystemClock.elapsedRealtime());
    }

    public synchronized boolean addOccurrence(long j) {
        try {
            this.mTimestampQueueMillis.addLast(j);
            if (this.mTimestampQueueMillis.size() > this.mNumOccurrences) {
                this.mTimestampQueueMillis.removeFirst();
            }
        } catch (Throwable th) {
            throw th;
        }
        return isInWindow();
    }

    public synchronized boolean isInWindow() {
        return this.mTimestampQueueMillis.size() == this.mNumOccurrences && this.mTimestampQueueMillis.peekFirst() + this.mWindowSizeMillis > this.mTimestampQueueMillis.peekLast();
    }

    int getQueuedNumOccurrences() {
        return this.mTimestampQueueMillis.size();
    }

    public synchronized long getWindowSizeMillis() {
        return this.mWindowSizeMillis;
    }

    public synchronized int getNumOccurrences() {
        return this.mNumOccurrences;
    }

    public String getFrequencyString() {
        long j = this.mWindowSizeMillis;
        int i = this.mNumOccurrences;
        if (j >= 1000) {
            return i + " times within " + (this.mWindowSizeMillis / 1000) + " seconds";
        }
        return i + " times within " + this.mWindowSizeMillis + "ms";
    }

    public String toString() {
        return String.format("SlidingWindowEventCounter=[windowSizeMillis=" + this.mWindowSizeMillis + ", numOccurrences=" + this.mNumOccurrences + ", timestampQueueMillis=" + this.mTimestampQueueMillis + "]", new Object[0]);
    }
}
