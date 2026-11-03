package com.hyper.internal.telephony;

import android.os.Handler;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IHyperGsmCdmaCallTracker;

/* JADX INFO: loaded from: classes.dex */
public class HyperGsmCdmaCallTrackerImpl extends Handler implements IHyperGsmCdmaCallTracker {
    protected static final String LOG_TAG = "HyperGsmCdmaCallTrackerImpl";
    private GsmCdmaCallTracker mGsmCdmaCallTracker;

    public HyperGsmCdmaCallTrackerImpl(GsmCdmaCallTracker callTracker) {
        this.mGsmCdmaCallTracker = callTracker;
    }

    public GsmCdmaPhone getPhone() {
        return this.mGsmCdmaCallTracker.getPhone();
    }
}
