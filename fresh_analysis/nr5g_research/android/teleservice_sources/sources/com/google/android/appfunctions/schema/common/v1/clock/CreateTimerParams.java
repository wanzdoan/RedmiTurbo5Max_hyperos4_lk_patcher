package com.google.android.appfunctions.schema.common.v1.clock;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreateTimerParams {
    public abstract String getExternalId();

    public abstract String getId();

    public abstract String getLabel();

    public abstract String getNamespace();

    public abstract long getOriginalDurationMillis();

    public abstract String getTimerState();
}
