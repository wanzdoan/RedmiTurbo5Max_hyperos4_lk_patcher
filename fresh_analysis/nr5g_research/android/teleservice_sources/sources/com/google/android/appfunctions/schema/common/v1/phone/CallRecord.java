package com.google.android.appfunctions.schema.common.v1.phone;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Phone.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CallRecord {
    public abstract String getCallType();

    public abstract String getContactId();

    public abstract Long getDurationMillis();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getPhoneNumber();

    public abstract String getSipAddress();

    public abstract DateTime getStartTime();

    public abstract Boolean isVideoCall();
}
