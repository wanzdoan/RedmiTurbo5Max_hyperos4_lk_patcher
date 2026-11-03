package com.hyper.internal.telephony.imsphone;

import android.content.Context;
import com.android.internal.telephony.IHyperImsPhone;
import com.android.internal.telephony.imsphone.ImsPhone;

/* JADX INFO: loaded from: classes.dex */
public class HyperImsPhoneImpl implements IHyperImsPhone {
    protected String LOG_TAG = "HyperImsPhoneImpl";
    private Context mContext;
    private ImsPhone mPhone;
    private int mPhoneId;

    public HyperImsPhoneImpl(ImsPhone phone) {
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
        this.LOG_TAG += "/" + this.mPhone.getPhoneId();
        this.mContext = this.mPhone.getContext();
    }
}
