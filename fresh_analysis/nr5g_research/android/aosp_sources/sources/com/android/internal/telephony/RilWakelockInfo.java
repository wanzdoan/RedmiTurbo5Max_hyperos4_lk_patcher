package com.android.internal.telephony;

import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class RilWakelockInfo {
    private final String LOG_TAG = RilWakelockInfo.class.getSimpleName();
    private int mConcurrentRequests;
    private long mLastAggregatedTime;
    private long mRequestTime;
    private long mResponseTime;
    private int mRilRequestSent;
    private int mTokenNumber;
    private long mWakelockTimeAttributedSoFar;

    public int getConcurrentRequests() {
        return this.mConcurrentRequests;
    }

    RilWakelockInfo(int i, int i2, int i3, long j) {
        int iValidateConcurrentRequests = validateConcurrentRequests(i3);
        this.mRilRequestSent = i;
        this.mTokenNumber = i2;
        this.mConcurrentRequests = iValidateConcurrentRequests;
        this.mRequestTime = j;
        this.mWakelockTimeAttributedSoFar = 0L;
        this.mLastAggregatedTime = j;
    }

    private int validateConcurrentRequests(int i) {
        if (i > 0) {
            return i;
        }
        if (!TelephonyUtils.IS_DEBUGGABLE) {
            return 1;
        }
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("concurrentRequests should always be greater than 0.");
        Rlog.e(this.LOG_TAG, illegalArgumentException.toString());
        throw illegalArgumentException;
    }

    int getTokenNumber() {
        return this.mTokenNumber;
    }

    int getRilRequestSent() {
        return this.mRilRequestSent;
    }

    void setResponseTime(long j) {
        updateTime(j);
        this.mResponseTime = j;
    }

    void updateConcurrentRequests(int i, long j) {
        int iValidateConcurrentRequests = validateConcurrentRequests(i);
        updateTime(j);
        this.mConcurrentRequests = iValidateConcurrentRequests;
    }

    synchronized void updateTime(long j) {
        this.mWakelockTimeAttributedSoFar += (j - this.mLastAggregatedTime) / ((long) this.mConcurrentRequests);
        this.mLastAggregatedTime = j;
    }

    long getWakelockTimeAttributedToClient() {
        return this.mWakelockTimeAttributedSoFar;
    }

    public String toString() {
        return "WakelockInfo{rilRequestSent=" + this.mRilRequestSent + ", tokenNumber=" + this.mTokenNumber + ", requestTime=" + this.mRequestTime + ", responseTime=" + this.mResponseTime + ", mWakelockTimeAttributed=" + this.mWakelockTimeAttributedSoFar + '}';
    }
}
