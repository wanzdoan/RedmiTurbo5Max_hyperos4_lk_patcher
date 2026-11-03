package com.google.android.appfunctions.schema.common.v1.calendar;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.calendar.$$__AppSearch__CreateEventParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateEventParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateEventParams createEventParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createEventParams.getNamespace(), createEventParams.getId(), "com.google.android.appfunctions.schema.common.v1.calendar.CreateEventParams");
        String title = createEventParams.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String description = createEventParams.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        createEventParams.getStartDate();
        createEventParams.getEndDate();
        List attendeeIds = createEventParams.getAttendeeIds();
        if (attendeeIds != null) {
            builder.setPropertyString("attendeeIds", (String[]) attendeeIds.toArray(new String[0]));
        }
        Boolean allDay = createEventParams.getAllDay();
        if (allDay != null) {
            builder.setPropertyBoolean("allDay", allDay.booleanValue());
        }
        String location = createEventParams.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        String recurrenceSchedule = createEventParams.getRecurrenceSchedule();
        if (recurrenceSchedule != null) {
            builder.setPropertyString("recurrenceSchedule", recurrenceSchedule);
        }
        String eventStatus = createEventParams.getEventStatus();
        if (eventStatus != null) {
            builder.setPropertyString("eventStatus", eventStatus);
        }
        String externalId = createEventParams.getExternalId();
        if (externalId != null) {
            builder.setPropertyString("externalId", externalId);
        }
        return builder.build();
    }
}
