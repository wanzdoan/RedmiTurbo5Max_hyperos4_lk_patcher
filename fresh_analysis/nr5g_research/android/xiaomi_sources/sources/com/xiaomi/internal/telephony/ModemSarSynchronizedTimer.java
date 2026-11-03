package com.xiaomi.internal.telephony;

import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarSynchronizedTimer {
    private static final String LOG_TAG = "RadSynchronizedTimer";
    private Runnable mHandler;
    private String mName;
    private long mRemainingTime;
    private TimerTask mTask;
    private long mTimeout;
    private Timer mTimer = null;
    private boolean isRunning = false;

    public ModemSarSynchronizedTimer(String name, long timeout, Runnable handler) {
        this.mName = name;
        this.mTimeout = timeout;
        this.mHandler = handler;
        this.mRemainingTime = timeout;
    }

    public synchronized void start() {
        if (this.isRunning) {
            ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] is already running");
            return;
        }
        TimerTask task = new TimerTask() { // from class: com.xiaomi.internal.telephony.ModemSarSynchronizedTimer.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ModemSarLog.d(ModemSarSynchronizedTimer.LOG_TAG, "Timer[" + ModemSarSynchronizedTimer.this.mName + "] is timeout [" + ModemSarSynchronizedTimer.this.mRemainingTime + "ms]");
                ModemSarSynchronizedTimer.this.mHandler.run();
                ModemSarSynchronizedTimer.this.isRunning = false;
                synchronized (this) {
                    notifyAll();
                }
            }
        };
        Timer t = new Timer();
        this.isRunning = true;
        ModemSarLog.d(LOG_TAG, "Start Timer[" + this.mName + "]!");
        t.schedule(task, this.mRemainingTime);
        this.mTask = task;
        this.mTimer = t;
    }

    public synchronized void stop() {
        if (!this.isRunning && this.mTimer == null && this.mTask == null) {
            ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] is not Running!");
            return;
        }
        if (this.mTask != null) {
            this.mTask.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer.purge();
        }
        this.isRunning = false;
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! RemainingTime = " + this.mRemainingTime + "ms");
        this.mRemainingTime = this.mTask.scheduledExecutionTime() - System.currentTimeMillis();
        if (this.mRemainingTime < 0) {
            this.mRemainingTime = 0L;
        }
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! currentTimeMillis = " + System.currentTimeMillis() + "ms");
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! scheduledExecutionTime = " + this.mTask.scheduledExecutionTime() + "ms");
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! RemainingTime = " + this.mRemainingTime + "ms");
        this.mTimer = null;
        this.mTask = null;
        synchronized (this) {
            try {
                notifyAll();
            } catch (Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
    }

    public synchronized long getRemainingTime() {
        ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] getRemainingTime = " + this.mRemainingTime + "ms");
        return this.mRemainingTime;
    }

    public synchronized boolean isTimerRunning() {
        ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] isRunning = " + this.isRunning);
        return this.isRunning;
    }

    public synchronized void resetTimer() {
        ModemSarLog.d(LOG_TAG, "resetTimer");
        this.mRemainingTime = this.mTimeout;
        this.isRunning = false;
    }
}
