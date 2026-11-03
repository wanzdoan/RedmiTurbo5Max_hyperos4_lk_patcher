package com.google.android.appfunctions.schema.common.v1.phone;

/* JADX INFO: compiled from: Phone.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class MakeCallParams {
    public final Boolean shouldRecord;

    public abstract String getContactId();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getPhoneNumber();

    public abstract String getType();

    public abstract Boolean isSpeakerOn();

    public abstract Boolean isVideoCall();
}
