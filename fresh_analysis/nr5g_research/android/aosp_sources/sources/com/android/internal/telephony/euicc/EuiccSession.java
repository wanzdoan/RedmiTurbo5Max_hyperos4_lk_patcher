package com.android.internal.telephony.euicc;

import android.content.Context;
import android.util.ArraySet;
import com.android.internal.telephony.uicc.euicc.apdu.ApduSender;
import com.android.telephony.Rlog;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class EuiccSession {
    public static final String DOWNLOAD = "DOWNLOAD";
    private static EuiccSession sInstance;
    private final Context mContext;
    private final Set mSessions = new ArraySet();
    private final Set mApduSenders = new ArraySet();

    public static synchronized EuiccSession get(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new EuiccSession(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private boolean optimizeApduSender() {
        return this.mContext.getResources().getBoolean(17892116);
    }

    public void startSession(String str) {
        if (optimizeApduSender()) {
            Rlog.i("EuiccSession", "startSession: " + str);
            synchronized (this) {
                this.mSessions.add(str);
            }
        }
    }

    public boolean hasSession() {
        boolean zHasSessionInternal = hasSessionInternal();
        Rlog.i("EuiccSession", "hasSession: " + zHasSessionInternal);
        return zHasSessionInternal;
    }

    private boolean hasSessionInternal() {
        boolean z;
        synchronized (this) {
            z = !this.mSessions.isEmpty();
        }
        return z;
    }

    public void noteChannelOpen(ApduSender apduSender) {
        Rlog.i("EuiccSession", "noteChannelOpen: " + apduSender);
        synchronized (this) {
            try {
                if (hasSessionInternal()) {
                    this.mApduSenders.add(apduSender);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void endSession(String str) {
        Rlog.i("EuiccSession", "endSession: " + str);
        endSessionInternal(str);
    }

    public void endAllSessions() {
        Rlog.i("EuiccSession", "endAllSessions");
        endSessionInternal(null);
    }

    private void endSessionInternal(String str) {
        ApduSender[] apduSenderArr = new ApduSender[0];
        synchronized (this) {
            try {
                if (removeOrClear(this.mSessions, str) && !hasSessionInternal()) {
                    apduSenderArr = (ApduSender[]) this.mApduSenders.toArray(apduSenderArr);
                    this.mApduSenders.clear();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        for (ApduSender apduSender : apduSenderArr) {
            apduSender.closeAnyOpenChannel();
        }
    }

    private static boolean removeOrClear(Set set, String str) {
        if (str == null) {
            boolean z = !set.isEmpty();
            set.clear();
            return z;
        }
        return set.remove(str);
    }

    public EuiccSession(Context context) {
        this.mContext = context;
    }
}
