package com.hyper.internal.telephony;

import android.content.Context;
import android.os.Handler;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IHyperServiceStateTracker;
import com.android.internal.telephony.ServiceStateTracker;

/* JADX INFO: loaded from: classes.dex */
public class HyperServiceStateTrackerImpl extends Handler implements IHyperServiceStateTracker {
    private String LOG_TAG = "HyperServiceStateTrackerImpl";
    private Context mContext;
    private GsmCdmaPhone mPhone;
    private ServiceStateTracker mSST;

    public HyperServiceStateTrackerImpl(ServiceStateTracker sst, GsmCdmaPhone phone) {
        this.mSST = sst;
        this.mPhone = phone;
        this.LOG_TAG += "/" + this.mPhone.getPhoneId();
        this.mContext = this.mPhone.getContext();
    }
}
