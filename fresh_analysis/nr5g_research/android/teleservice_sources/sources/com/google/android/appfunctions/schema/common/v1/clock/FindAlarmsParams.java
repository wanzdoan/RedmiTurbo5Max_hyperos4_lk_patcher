package com.google.android.appfunctions.schema.common.v1.clock;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;

/* JADX INFO: compiled from: Clock.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindAlarmsParams {
    public abstract String getAlarmStatus();

    public abstract DayPattern getDayPattern();

    public abstract DateTime getEndTime();

    public abstract String getId();

    public abstract String getLabel();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getSortingOrder();

    public abstract DateTime getStartTime();
}
