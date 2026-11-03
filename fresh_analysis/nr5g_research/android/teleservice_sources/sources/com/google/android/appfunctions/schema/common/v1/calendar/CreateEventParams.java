package com.google.android.appfunctions.schema.common.v1.calendar;

import com.google.android.appfunctions.schema.common.v1.types.DateTime;
import java.util.List;

/* JADX INFO: compiled from: Calendar.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class CreateEventParams {
    public abstract Boolean getAllDay();

    public abstract List getAttendeeIds();

    public abstract String getDescription();

    public abstract DateTime getEndDate();

    public abstract String getEventStatus();

    public abstract String getExternalId();

    public abstract String getId();

    public abstract String getLocation();

    public abstract String getNamespace();

    public abstract String getRecurrenceSchedule();

    public abstract DateTime getStartDate();

    public abstract String getTitle();
}
