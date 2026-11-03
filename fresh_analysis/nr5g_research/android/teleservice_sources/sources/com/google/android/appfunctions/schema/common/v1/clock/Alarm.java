package com.google.android.appfunctions.schema.common.v1.clock;

import com.google.android.appfunctions.schema.common.v1.types.TimeOfDay;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class Alarm {
    public abstract String getAlarmStatus();

    public abstract DayPattern getDayPattern();

    public abstract String getId();

    public abstract String getLabel();

    public abstract String getNamespace();

    public abstract TimeOfDay getTime();
}
