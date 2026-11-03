package com.google.android.appfunctions.schema.common.v1.calendar;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.calendar.$$__AppSearch__UpdateEventParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateEventParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateEventParams updateEventParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateEventParams.getNamespace(), updateEventParams.getId(), "com.google.android.appfunctions.schema.common.v1.calendar.UpdateEventParams");
        String eventId = updateEventParams.getEventId();
        if (eventId != null) {
            builder.setPropertyString("eventId", eventId);
        }
        updateEventParams.getTitle();
        updateEventParams.getDescription();
        updateEventParams.getStartDate();
        updateEventParams.getEndDate();
        updateEventParams.getAttendeeIds();
        updateEventParams.getAllDay();
        updateEventParams.getLocation();
        updateEventParams.getRecurrenceSchedule();
        updateEventParams.getEventStatus();
        return builder.build();
    }
}
