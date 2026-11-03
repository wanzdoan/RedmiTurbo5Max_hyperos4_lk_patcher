package com.google.android.appfunctions.schema.common.v1.calendar;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Calendar.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindEventsParams {
    public abstract List getAttendeeIds();

    public abstract DateTime getEndDate();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract String getNamespace();

    public abstract String getOrderBy();

    public abstract String getQuery();

    public abstract DateTime getStartDate();
}
