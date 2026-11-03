package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.os.WorkSource;
import com.android.telephony.Rlog;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/* JADX INFO: loaded from: classes.dex */
public class XmRILRequest {
    static final String LOG_TAG = "XmRilRequest";
    private static final int MAX_POOL_SIZE = 4;
    Object[] mArguments;
    String mClientId;
    XmRILRequest mNext;
    public int mRequest;
    public Message mResult;
    public int mSerial;
    long mStartTimeMs;
    int mWakeLockType;
    WorkSource mWorkSource;
    static Random sRandom = new Random();
    static AtomicInteger sNextSerial = new AtomicInteger(0);
    private static Object sPoolSync = new Object();
    private static XmRILRequest sPool = null;
    private static int sPoolSize = 0;

    public int getSerial() {
        return this.mSerial;
    }

    public int getRequest() {
        return this.mRequest;
    }

    public Message getResult() {
        return this.mResult;
    }

    private static XmRILRequest obtain(int request, Message result) {
        XmRILRequest rr = null;
        synchronized (sPoolSync) {
            if (sPool != null) {
                rr = sPool;
                sPool = rr.mNext;
                rr.mNext = null;
                sPoolSize--;
            }
        }
        if (rr == null) {
            rr = new XmRILRequest();
        }
        rr.mSerial = sNextSerial.getAndUpdate(new IntUnaryOperator() { // from class: com.android.internal.telephony.XmRILRequest$$ExternalSyntheticLambda0
            @Override // java.util.function.IntUnaryOperator
            public final int applyAsInt(int i) {
                return XmRILRequest.lambda$obtain$0(i);
            }
        });
        rr.mRequest = request;
        rr.mResult = result;
        rr.mWakeLockType = -1;
        rr.mWorkSource = null;
        rr.mStartTimeMs = SystemClock.elapsedRealtime();
        if (result != null && result.getTarget() == null) {
            throw new NullPointerException("Message target must not be null");
        }
        return rr;
    }

    static /* synthetic */ int lambda$obtain$0(int n) {
        return (n + 1) % Integer.MAX_VALUE;
    }

    public static XmRILRequest obtain(int request, Message result, WorkSource workSource) {
        XmRILRequest rr = obtain(request, result);
        if (workSource != null) {
            rr.mWorkSource = workSource;
            rr.mClientId = rr.getWorkSourceClientId();
        } else {
            Rlog.e(LOG_TAG, "null workSource " + request);
        }
        return rr;
    }

    public static XmRILRequest obtain(int request, Message result, WorkSource workSource, Object... args) {
        XmRILRequest rr = obtain(request, result, workSource);
        rr.mArguments = args;
        return rr;
    }

    public String getWorkSourceClientId() {
        if (this.mWorkSource == null || this.mWorkSource.isEmpty()) {
            return null;
        }
        if (this.mWorkSource.size() > 0) {
            return this.mWorkSource.getUid(0) + ":" + this.mWorkSource.getPackageName(0);
        }
        List<WorkSource.WorkChain> workChains = this.mWorkSource.getWorkChains();
        if (workChains == null || workChains.isEmpty()) {
            return null;
        }
        WorkSource.WorkChain workChain = workChains.get(0);
        return workChain.toString();
    }

    void release() {
        synchronized (sPoolSync) {
            if (sPoolSize < 4) {
                this.mNext = sPool;
                sPool = this;
                sPoolSize++;
                this.mResult = null;
                if (this.mWakeLockType != -1 && this.mWakeLockType == 0) {
                    Rlog.e(LOG_TAG, "XmRILRequest releasing with held wake lock: " + serialString());
                }
                this.mArguments = null;
            }
        }
    }

    private XmRILRequest() {
    }

    static void resetSerial() {
        sNextSerial.set(sRandom.nextInt(Integer.MAX_VALUE));
    }

    public String serialString() {
        StringBuilder sb = new StringBuilder(8);
        int adjustedSerial = this.mSerial % 10000;
        String sn = Integer.toString(adjustedSerial);
        sb.append('[');
        int s = sn.length();
        for (int i = 0; i < 4 - s; i++) {
            sb.append('0');
        }
        sb.append(sn);
        sb.append(']');
        return sb.toString();
    }

    void onError(int error, Object ret) {
        CommandException ex = CommandException.fromRilErrno(error);
        Message result = this.mResult;
        Rlog.d(LOG_TAG, serialString() + "< " + XmRILUtils.requestToString(this.mRequest) + " error: " + ex + " ret=" + XmRIL.retToString(this.mRequest, ret) + " result=" + result);
        if (result != null && result.getTarget() != null) {
            AsyncResult.forMessage(result, ret, ex);
            result.sendToTarget();
        }
    }

    public String toString() {
        return serialString() + ": " + XmRILUtils.requestToString(this.mRequest);
    }
}
