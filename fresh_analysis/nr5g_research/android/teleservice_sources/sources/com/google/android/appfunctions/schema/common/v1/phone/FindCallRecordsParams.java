package com.google.android.appfunctions.schema.common.v1.phone;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Phone.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindCallRecordsParams {
    public abstract String getCallType();

    public abstract String getContactId();

    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract DateTime getStartTime();
}
