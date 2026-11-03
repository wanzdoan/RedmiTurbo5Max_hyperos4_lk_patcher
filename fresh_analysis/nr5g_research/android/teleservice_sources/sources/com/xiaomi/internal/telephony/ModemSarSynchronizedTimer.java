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

    public ModemSarSynchronizedTimer(String str, long j, Runnable runnable) {
        this.mName = str;
        this.mTimeout = j;
        this.mHandler = runnable;
        this.mRemainingTime = j;
    }

    public synchronized void start() {
        if (this.isRunning) {
            ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] is already running");
            return;
        }
        TimerTask timerTask = new TimerTask() { // from class: com.xiaomi.internal.telephony.ModemSarSynchronizedTimer.1
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
        Timer timer = new Timer();
        this.isRunning = true;
        ModemSarLog.d(LOG_TAG, "Start Timer[" + this.mName + "]!");
        timer.schedule(timerTask, this.mRemainingTime);
        this.mTask = timerTask;
        this.mTimer = timer;
    }

    public synchronized void stop() {
        if (!this.isRunning && this.mTimer == null && this.mTask == null) {
            ModemSarLog.d(LOG_TAG, "Timer[" + this.mName + "] is not Running!");
            return;
        }
        TimerTask timerTask = this.mTask;
        if (timerTask != null) {
            timerTask.cancel();
        }
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer.purge();
        }
        this.isRunning = false;
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! RemainingTime = " + this.mRemainingTime + "ms");
        long jScheduledExecutionTime = this.mTask.scheduledExecutionTime() - System.currentTimeMillis();
        this.mRemainingTime = jScheduledExecutionTime;
        if (jScheduledExecutionTime < 0) {
            this.mRemainingTime = 0L;
        }
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! currentTimeMillis = " + System.currentTimeMillis() + "ms");
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! scheduledExecutionTime = " + this.mTask.scheduledExecutionTime() + "ms");
        ModemSarLog.d(LOG_TAG, "Stop Timer[" + this.mName + "]! RemainingTime = " + this.mRemainingTime + "ms");
        this.mTimer = null;
        this.mTask = null;
        synchronized (this) {
            notifyAll();
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
