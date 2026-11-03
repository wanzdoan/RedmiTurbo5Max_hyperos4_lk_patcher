package com.android.phone.streaming;

import android.os.SystemClock;
import android.util.Log;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class StreamingOptimizationState {
    private int mOptState = 0;
    private long mRoundStartTime = 0;
    private long mRoundEndTime = 0;
    private int mRoundCount = 0;
    private final Object mLock = new Object();
    private final StreamingSceneContext mSceneContext = new StreamingSceneContext();
    private final StreamingOptimizationAuxState mAuxState = new StreamingOptimizationAuxState();

    public StreamingSceneContext getSceneContext() {
        return this.mSceneContext;
    }

    public StreamingOptimizationAuxState getAuxState() {
        return this.mAuxState;
    }

    public int getOptState() {
        int i;
        synchronized (this.mLock) {
            i = this.mOptState;
        }
        return i;
    }

    public long getRoundStartTime() {
        long j;
        synchronized (this.mLock) {
            j = this.mRoundStartTime;
        }
        return j;
    }

    public long getRoundEndTime() {
        long j;
        synchronized (this.mLock) {
            j = this.mRoundEndTime;
        }
        return j;
    }

    public int getRoundCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mRoundCount;
        }
        return i;
    }

    public void resetRoundCount() {
        synchronized (this.mLock) {
            this.mRoundCount = 0;
            Log.d("StreamingOptState", "resetRoundCount");
        }
    }

    public void startNewRound(int i, int i2) {
        synchronized (this.mLock) {
            try {
                if (this.mOptState != 0) {
                    Log.e("StreamingOptState", "Invalid state transition: trying to start new round from state " + this.mOptState + ", networkScore=" + i + ", roundCount=" + this.mRoundCount);
                    return;
                }
                this.mOptState = 1;
                this.mRoundStartTime = SystemClock.elapsedRealtime();
                this.mRoundCount++;
                Log.i("StreamingOptState", "startNewRound: networkScore=" + i + ", pci=" + i2 + ", roundCount=" + this.mRoundCount);
                this.mAuxState.setLastNetworkScore(i);
                this.mAuxState.setChecking(true);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void switchToOpt2(int i) {
        synchronized (this.mLock) {
            try {
                if (this.mOptState != 1) {
                    Log.e("StreamingOptState", "Cannot switch to OPT2 from state: " + this.mOptState);
                    resetOptimizationState();
                    return;
                }
                this.mOptState = 2;
                Log.i("StreamingOptState", "switchToOpt2: networkScore=" + i);
                this.mAuxState.setLastNetworkScore(i);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void finishRound() {
        synchronized (this.mLock) {
            try {
                if (this.mOptState == 0) {
                    Log.e("StreamingOptState", "Cannot finish round from IDLE state");
                    return;
                }
                this.mOptState = 0;
                this.mRoundEndTime = SystemClock.elapsedRealtime();
                Log.i("StreamingOptState", "finishRound");
                this.mAuxState.setChecking(false);
                this.mAuxState.setLastNetworkScore(0);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void resetOptimizationState() {
        synchronized (this.mLock) {
            this.mOptState = 0;
            this.mRoundStartTime = 0L;
            this.mRoundEndTime = 0L;
            this.mRoundCount = 0;
            Log.i("StreamingOptState", "resetOptimizationState");
        }
        this.mAuxState.reset();
    }

    private static String optStateToString(int i) {
        if (i == 0) {
            return "IDLE";
        }
        if (i == 1) {
            return "OPT1";
        }
        if (i == 2) {
            return "OPT2";
        }
        return "UNKNOWN(" + i + ")";
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("[State]");
        synchronized (this.mLock) {
            printWriter.println("  optState=" + optStateToString(this.mOptState) + ", roundCount=" + this.mRoundCount + ", roundStart=" + this.mRoundStartTime + ", roundEnd=" + this.mRoundEndTime);
        }
        printWriter.println("[Scene]");
        this.mSceneContext.dump(printWriter);
        this.mAuxState.dump(printWriter);
    }
}
