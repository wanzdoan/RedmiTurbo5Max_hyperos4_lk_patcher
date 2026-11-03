package com.google.android.appfunctions.schema.common.v1.video;

/* JADX INFO: compiled from: Video.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class TimedText {
    public abstract double getConfidence();

    public abstract long getDurationMills();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract long getStartTimeMillis();

    public abstract String getText();
}
