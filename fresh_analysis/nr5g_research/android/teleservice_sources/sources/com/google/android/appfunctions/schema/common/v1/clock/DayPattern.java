package com.google.android.appfunctions.schema.common.v1.clock;

import com.google.android.appfunctions.schema.common.v1.types.Date;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class DayPattern {
    public abstract Date getDate();

    public abstract String getId();

    public abstract String getNamespace();

    public abstract String getRecurrencePattern();
}
