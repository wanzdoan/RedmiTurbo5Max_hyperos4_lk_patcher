package com.google.android.appfunctions.schema.common.v1.calendar;

import com.google.android.appfunctions.schema.common.v1.types.SetBooleanField;
import com.google.android.appfunctions.schema.common.v1.types.SetDateTimeField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringListField;
import com.google.android.appfunctions.schema.common.v1.types.SetStringNullableField;

/* JADX INFO: compiled from: Calendar.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateEventParams {
    public abstract SetBooleanField getAllDay();

    public abstract SetStringListField getAttendeeIds();

    public abstract SetStringNullableField getDescription();

    public abstract SetDateTimeField getEndDate();

    public abstract String getEventId();

    public abstract SetStringField getEventStatus();

    public abstract String getId();

    public abstract SetStringNullableField getLocation();

    public abstract String getNamespace();

    public abstract SetStringNullableField getRecurrenceSchedule();

    public abstract SetDateTimeField getStartDate();

    public abstract SetStringField getTitle();
}
