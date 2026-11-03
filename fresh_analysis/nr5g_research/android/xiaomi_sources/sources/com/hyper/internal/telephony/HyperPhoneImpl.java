package com.hyper.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.IHyperPhone;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class HyperPhoneImpl implements IHyperPhone {
    private String LOG_TAG = "HyperPhoneImpl";
    protected final Context mContext;
    private Phone mPhone;

    public HyperPhoneImpl(Phone phone) {
        this.mPhone = phone;
        this.LOG_TAG += "/" + this.mPhone.getPhoneId();
        this.mContext = this.mPhone.getContext();
        initOnce(phone);
    }

    private void initOnce(Phone phone) {
    }
}
