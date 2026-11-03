package com.android.phone.streaming;

import android.os.SystemClock;
import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class StreamingOptimizationAuxState {
    private int mLastQoeReason = 0;
    private long mLastCoverageStateTime = 0;
    private boolean mIsStableCellEnabled = false;
    private boolean mIs5GDisabled = false;
    private boolean mIsChecking = false;
    private int mLastNetworkScore = 0;
    private long mLastCellId = Long.MIN_VALUE;
    private boolean mIsWaitingToRestore5G = false;
    private final ArrayList mCollectedScores = new ArrayList();
    private final ArrayList mCollectedTimestamps = new ArrayList();
    private boolean mIsCollectingScores = false;
    private boolean mStableCellDecided = false;
    private final Object mLock = new Object();

    public int getLastQoeReason() {
        int i;
        synchronized (this.mLock) {
            i = this.mLastQoeReason;
        }
        return i;
    }

    public void setLastQoeReason(int i) {
        synchronized (this.mLock) {
            this.mLastQoeReason = i;
            Log.d("StreamingOptAuxState", "setLastQoeReason: " + i);
        }
    }

    public long getCoverageStateTime() {
        long j;
        synchronized (this.mLock) {
            j = this.mLastCoverageStateTime;
        }
        return j;
    }

    public void setCoverageStateTime(long j) {
        synchronized (this.mLock) {
            this.mLastCoverageStateTime = j;
            Log.d("StreamingOptAuxState", "setCoverageStateTime: " + j);
        }
    }

    public boolean isStableCellEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsStableCellEnabled;
        }
        return z;
    }

    public void setStableCellEnabled(boolean z) {
        synchronized (this.mLock) {
            this.mIsStableCellEnabled = z;
            Log.d("StreamingOptAuxState", "setStableCellEnabled: " + z);
        }
    }

    public boolean is5GDisabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIs5GDisabled;
        }
        return z;
    }

    public void set5GDisabled(boolean z) {
        synchronized (this.mLock) {
            this.mIs5GDisabled = z;
            Log.d("StreamingOptAuxState", "set5GDisabled: " + z);
        }
    }

    public boolean isChecking() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsChecking;
        }
        return z;
    }

    public void setChecking(boolean z) {
        synchronized (this.mLock) {
            this.mIsChecking = z;
            Log.d("StreamingOptAuxState", "setChecking: " + z);
        }
    }

    public int getLastNetworkScore() {
        int i;
        synchronized (this.mLock) {
            i = this.mLastNetworkScore;
        }
        return i;
    }

    public void setLastNetworkScore(int i) {
        synchronized (this.mLock) {
            this.mLastNetworkScore = i;
            Log.d("StreamingOptAuxState", "setLastNetworkScore: " + i);
        }
    }

    public long getLastCellId() {
        long j;
        synchronized (this.mLock) {
            j = this.mLastCellId;
        }
        return j;
    }

    public void setLastCellId(long j) {
        synchronized (this.mLock) {
            this.mLastCellId = j;
            Log.d("StreamingOptAuxState", "setLastCellId: " + TelephonyUtils.pii(String.valueOf(j)));
        }
    }

    public boolean isWaitingToRestore5G() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsWaitingToRestore5G;
        }
        return z;
    }

    public void setWaitingToRestore5G(boolean z) {
        synchronized (this.mLock) {
            this.mIsWaitingToRestore5G = z;
            Log.d("StreamingOptAuxState", "setWaitingToRestore5G: " + z);
        }
    }

    public boolean isCollectingScores() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsCollectingScores;
        }
        return z;
    }

    public void setCollectingScores(boolean z) {
        synchronized (this.mLock) {
            this.mIsCollectingScores = z;
            Log.d("StreamingOptAuxState", "setCollectingScores: " + z);
        }
    }

    public void addCollectedScore(int i) {
        synchronized (this.mLock) {
            try {
                if (this.mCollectedScores.size() >= 200) {
                    Log.w("StreamingOptAuxState", "addCollectedScore: reached max capacity, dropping score=" + i);
                    return;
                }
                this.mCollectedScores.add(Integer.valueOf(i));
                this.mCollectedTimestamps.add(Long.valueOf(SystemClock.elapsedRealtime()));
                Log.d("StreamingOptAuxState", "addCollectedScore: " + i + ", total=" + this.mCollectedScores.size());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void clearCollectedScores() {
        synchronized (this.mLock) {
            this.mCollectedScores.clear();
            this.mCollectedTimestamps.clear();
            Log.d("StreamingOptAuxState", "clearCollectedScores");
        }
    }

    public List getCollectedScores() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mCollectedScores);
        }
        return arrayList;
    }

    public List getCollectedTimestamps() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mCollectedTimestamps);
        }
        return arrayList;
    }

    public boolean isStableCellDecided() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mStableCellDecided;
        }
        return z;
    }

    public void setStableCellDecided(boolean z) {
        synchronized (this.mLock) {
            this.mStableCellDecided = z;
            Log.d("StreamingOptAuxState", "setStableCellDecided: " + z);
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mLastQoeReason = 0;
            this.mLastCoverageStateTime = 0L;
            this.mIs5GDisabled = false;
            this.mLastNetworkScore = 0;
            this.mIsChecking = false;
            this.mIsStableCellEnabled = false;
            this.mLastCellId = Long.MIN_VALUE;
            this.mIsWaitingToRestore5G = false;
            this.mCollectedScores.clear();
            this.mCollectedTimestamps.clear();
            this.mIsCollectingScores = false;
            this.mStableCellDecided = false;
            Log.i("StreamingOptAuxState", "reset all aux states");
        }
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            try {
                printWriter.println("[AuxState]");
                printWriter.println("  lastScore=" + this.mLastNetworkScore + ", lastReason=" + this.mLastQoeReason);
                printWriter.println("  5GDisabled=" + this.mIs5GDisabled + ", checking=" + this.mIsChecking);
                StringBuilder sb = new StringBuilder("  stableCell=");
                sb.append(this.mIsStableCellEnabled ? "enabled" : "disabled");
                sb.append(", decided=");
                sb.append(this.mStableCellDecided);
                printWriter.println(sb.toString());
                printWriter.println("  lastCellId=" + TelephonyUtils.pii(String.valueOf(this.mLastCellId)) + ", waiting5G=" + this.mIsWaitingToRestore5G);
                printWriter.println("  collecting=" + this.mIsCollectingScores + ", scoreCount=" + this.mCollectedScores.size());
                if (!this.mCollectedScores.isEmpty()) {
                    int size = this.mCollectedScores.size();
                    printWriter.println("  recentScores=" + this.mCollectedScores.subList(Math.max(0, size - 3), size));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
