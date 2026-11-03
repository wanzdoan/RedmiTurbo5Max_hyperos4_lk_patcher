package com.google.android.appfunctions.schema.common.v1.calendar;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.android.phone.settings.search.utils.SearchContract;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.calendar.$$__AppSearch__Event, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Event implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Event event) {
        GenericDocument.Builder builder = new GenericDocument.Builder(event.getNamespace(), event.getId(), "com.google.android.appfunctions.schema.common.v1.calendar.Event");
        String title = event.getTitle();
        if (title != null) {
            builder.setPropertyString(SearchContract.SearchResultColumn.COLUMN_TITLE, title);
        }
        String description = event.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        event.getStartDate();
        event.getEndDate();
        List attendeeIds = event.getAttendeeIds();
        if (attendeeIds != null) {
            builder.setPropertyString("attendeeIds", (String[]) attendeeIds.toArray(new String[0]));
        }
        Boolean allDay = event.getAllDay();
        if (allDay != null) {
            builder.setPropertyBoolean("allDay", allDay.booleanValue());
        }
        String location = event.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        String recurrenceSchedule = event.getRecurrenceSchedule();
        if (recurrenceSchedule != null) {
            builder.setPropertyString("recurrenceSchedule", recurrenceSchedule);
        }
        String eventStatus = event.getEventStatus();
        if (eventStatus != null) {
            builder.setPropertyString("eventStatus", eventStatus);
        }
        Boolean boolIsReadOnly = event.isReadOnly();
        if (boolIsReadOnly != null) {
            builder.setPropertyBoolean("isReadOnly", boolIsReadOnly.booleanValue());
        }
        Boolean boolIsInPublicCalendar = event.isInPublicCalendar();
        if (boolIsInPublicCalendar != null) {
            builder.setPropertyBoolean("isInPublicCalendar", boolIsInPublicCalendar.booleanValue());
        }
        Boolean boolIsOrganizer = event.isOrganizer();
        if (boolIsOrganizer != null) {
            builder.setPropertyBoolean("isOrganizer", boolIsOrganizer.booleanValue());
        }
        String selfAttendeeStatus = event.getSelfAttendeeStatus();
        if (selfAttendeeStatus != null) {
            builder.setPropertyString("selfAttendeeStatus", selfAttendeeStatus);
        }
        return builder.build();
    }
}
