package com.hyper.internal.telephony;

import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IHyperGsmCdmaPhone;

/* JADX INFO: loaded from: classes.dex */
public class HyperGsmCdmaPhoneImpl implements IHyperGsmCdmaPhone {
    private String LOG_TAG = "HyperGsmCdmaPhoneImpl";
    private GsmCdmaPhone mPhone;
    private int mPhoneId;

    public HyperGsmCdmaPhoneImpl(GsmCdmaPhone phone) {
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
        this.LOG_TAG += "/" + this.mPhoneId;
    }
}
