package com.google.android.appfunctions.schema.common.v1.phone;

/* JADX INFO: compiled from: Phone.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class AcceptCallParams {
    public final Boolean shouldRecord;

    public abstract String getId();

    public abstract String getNamespace();

    public abstract Boolean isSpeakerOn();

    public abstract Boolean isVideoCall();
}
