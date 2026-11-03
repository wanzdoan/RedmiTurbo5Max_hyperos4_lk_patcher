package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.os.WorkSource;
import com.android.internal.telephony.util.LinkPropertiesUtils;
import com.android.telephony.Rlog;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/* JADX INFO: loaded from: classes.dex */
public class RILRequest {
    Object[] mArguments;
    String mClientId;
    RILRequest mNext;
    public int mRequest;
    public Message mResult;
    public int mSerial;
    long mStartTimeMs;
    int mWakeLockType;
    WorkSource mWorkSource;
    static Random sRandom = new Random();
    static AtomicInteger sNextSerial = new AtomicInteger(0);
    private static Object sPoolSync = new Object();
    private static RILRequest sPool = null;
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

    private static RILRequest obtain(int i, Message message) {
        RILRequest rILRequest;
        synchronized (sPoolSync) {
            try {
                rILRequest = sPool;
                if (rILRequest != null) {
                    sPool = rILRequest.mNext;
                    rILRequest.mNext = null;
                    sPoolSize--;
                } else {
                    rILRequest = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (rILRequest == null) {
            rILRequest = new RILRequest();
        }
        rILRequest.mSerial = sNextSerial.getAndUpdate(new IntUnaryOperator() { // from class: com.android.internal.telephony.RILRequest$$ExternalSyntheticLambda0
            @Override // java.util.function.IntUnaryOperator
            public final int applyAsInt(int i2) {
                return RILRequest.$r8$lambda$WdDGG1XFKJ3D5ymBP8qlhCtQE5s(i2);
            }
        });
        rILRequest.mRequest = i;
        rILRequest.mResult = message;
        rILRequest.mWakeLockType = -1;
        rILRequest.mWorkSource = null;
        rILRequest.mStartTimeMs = SystemClock.elapsedRealtime();
        if (message == null || message.getTarget() != null) {
            return rILRequest;
        }
        throw new NullPointerException("Message target must not be null");
    }

    public static /* synthetic */ int $r8$lambda$WdDGG1XFKJ3D5ymBP8qlhCtQE5s(int i) {
        return (i + 1) % Integer.MAX_VALUE;
    }

    public static RILRequest obtain(int i, Message message, WorkSource workSource) {
        RILRequest rILRequestObtain = obtain(i, message);
        if (workSource != null) {
            rILRequestObtain.mWorkSource = workSource;
            rILRequestObtain.mClientId = rILRequestObtain.getWorkSourceClientId();
            return rILRequestObtain;
        }
        Rlog.e("RilRequest", "null workSource " + i);
        return rILRequestObtain;
    }

    public static RILRequest obtain(int i, Message message, WorkSource workSource, Object... objArr) {
        RILRequest rILRequestObtain = obtain(i, message, workSource);
        rILRequestObtain.mArguments = objArr;
        return rILRequestObtain;
    }

    public String getWorkSourceClientId() {
        WorkSource workSource = this.mWorkSource;
        if (workSource != null && !workSource.isEmpty()) {
            int size = this.mWorkSource.size();
            WorkSource workSource2 = this.mWorkSource;
            if (size > 0) {
                return workSource2.getUid(0) + ":" + this.mWorkSource.getPackageName(0);
            }
            List workChains = workSource2.getWorkChains();
            if (workChains != null && !workChains.isEmpty()) {
                return ((WorkSource.WorkChain) workChains.get(0)).toString();
            }
        }
        return null;
    }

    void release() {
        synchronized (sPoolSync) {
            try {
                int i = sPoolSize;
                if (i < 4) {
                    this.mNext = sPool;
                    sPool = this;
                    sPoolSize = i + 1;
                    this.mResult = null;
                    int i2 = this.mWakeLockType;
                    if (i2 != -1 && i2 == 0) {
                        Rlog.e("RilRequest", "RILRequest releasing with held wake lock: " + serialString());
                    }
                    this.mArguments = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private RILRequest() {
    }

    static void resetSerial() {
        sNextSerial.set(sRandom.nextInt(Integer.MAX_VALUE));
    }

    public String serialString() {
        StringBuilder sb = new StringBuilder(8);
        String string = Integer.toString(this.mSerial % LinkPropertiesUtils.MAX_MTU);
        sb.append('[');
        int length = string.length();
        for (int i = 0; i < 4 - length; i++) {
            sb.append('0');
        }
        sb.append(string);
        sb.append(']');
        return sb.toString();
    }

    void onError(int i, Object obj) {
        CommandException commandExceptionFromRilErrno = CommandException.fromRilErrno(i);
        Message message = this.mResult;
        Rlog.d("RilRequest", serialString() + "< " + RILUtils.requestToString(this.mRequest) + " error: " + commandExceptionFromRilErrno + " ret=" + RIL.retToString(this.mRequest, obj) + " result=" + message);
        if (message == null || message.getTarget() == null) {
            return;
        }
        AsyncResult.forMessage(message, obj, commandExceptionFromRilErrno);
        message.sendToTarget();
    }

    public String toString() {
        return serialString() + ": " + RILUtils.requestToString(this.mRequest);
    }
}
