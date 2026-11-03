package com.android.internal.telephony;

import android.telephony.ClientRequestStats;
import com.android.telephony.Rlog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ClientWakelockAccountant {
    public static final String LOG_TAG = "ClientWakelockAccountant: ";
    public ClientRequestStats mRequestStats = new ClientRequestStats();
    public ArrayList<RilWakelockInfo> mPendingRilWakelocks = new ArrayList<>();

    public ClientWakelockAccountant(String str) {
        this.mRequestStats.setCallingPackage(str);
    }

    public void startAttributingWakelock(int i, int i2, int i3, long j) {
        RilWakelockInfo rilWakelockInfo = new RilWakelockInfo(i, i2, i3, j);
        synchronized (this.mPendingRilWakelocks) {
            this.mPendingRilWakelocks.add(rilWakelockInfo);
        }
    }

    public void stopAttributingWakelock(int i, int i2, long j) {
        RilWakelockInfo rilWakelockInfoRemovePendingWakelock = removePendingWakelock(i, i2);
        if (rilWakelockInfoRemovePendingWakelock != null) {
            completeRequest(rilWakelockInfoRemovePendingWakelock, j);
        }
    }

    public void stopAllPendingRequests(long j) {
        synchronized (this.mPendingRilWakelocks) {
            try {
                ArrayList<RilWakelockInfo> arrayList = this.mPendingRilWakelocks;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    RilWakelockInfo rilWakelockInfo = arrayList.get(i);
                    i++;
                    completeRequest(rilWakelockInfo, j);
                }
                this.mPendingRilWakelocks.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void changeConcurrentRequests(int i, long j) {
        synchronized (this.mPendingRilWakelocks) {
            try {
                ArrayList<RilWakelockInfo> arrayList = this.mPendingRilWakelocks;
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    RilWakelockInfo rilWakelockInfo = arrayList.get(i2);
                    i2++;
                    rilWakelockInfo.updateConcurrentRequests(i, j);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void completeRequest(RilWakelockInfo rilWakelockInfo, long j) {
        rilWakelockInfo.setResponseTime(j);
        synchronized (this.mRequestStats) {
            this.mRequestStats.addCompletedWakelockTime(rilWakelockInfo.getWakelockTimeAttributedToClient());
            this.mRequestStats.incrementCompletedRequestsCount();
            this.mRequestStats.updateRequestHistograms(rilWakelockInfo.getRilRequestSent(), (int) rilWakelockInfo.getWakelockTimeAttributedToClient());
        }
    }

    public int getPendingRequestCount() {
        return this.mPendingRilWakelocks.size();
    }

    public synchronized long updatePendingRequestWakelockTime(long j) {
        long wakelockTimeAttributedToClient;
        synchronized (this.mPendingRilWakelocks) {
            try {
                ArrayList<RilWakelockInfo> arrayList = this.mPendingRilWakelocks;
                int size = arrayList.size();
                wakelockTimeAttributedToClient = 0;
                int i = 0;
                while (i < size) {
                    RilWakelockInfo rilWakelockInfo = arrayList.get(i);
                    i++;
                    RilWakelockInfo rilWakelockInfo2 = rilWakelockInfo;
                    rilWakelockInfo2.updateTime(j);
                    wakelockTimeAttributedToClient += rilWakelockInfo2.getWakelockTimeAttributedToClient();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        synchronized (this.mRequestStats) {
            this.mRequestStats.setPendingRequestsCount(getPendingRequestCount());
            this.mRequestStats.setPendingRequestsWakelockTime(wakelockTimeAttributedToClient);
        }
        return wakelockTimeAttributedToClient;
    }

    private RilWakelockInfo removePendingWakelock(int i, int i2) {
        RilWakelockInfo rilWakelockInfo;
        synchronized (this.mPendingRilWakelocks) {
            try {
                ArrayList<RilWakelockInfo> arrayList = this.mPendingRilWakelocks;
                int size = arrayList.size();
                rilWakelockInfo = null;
                int i3 = 0;
                while (i3 < size) {
                    RilWakelockInfo rilWakelockInfo2 = arrayList.get(i3);
                    i3++;
                    RilWakelockInfo rilWakelockInfo3 = rilWakelockInfo2;
                    if (rilWakelockInfo3.getTokenNumber() == i2 && rilWakelockInfo3.getRilRequestSent() == i) {
                        rilWakelockInfo = rilWakelockInfo3;
                    }
                }
                if (rilWakelockInfo != null) {
                    this.mPendingRilWakelocks.remove(rilWakelockInfo);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (rilWakelockInfo == null) {
            Rlog.w(LOG_TAG, "Looking for Request<" + i + "," + i2 + "> in " + this.mPendingRilWakelocks);
        }
        return rilWakelockInfo;
    }

    public String toString() {
        return "ClientWakelockAccountant{mRequestStats=" + this.mRequestStats + ", mPendingRilWakelocks=" + this.mPendingRilWakelocks + '}';
    }
}
