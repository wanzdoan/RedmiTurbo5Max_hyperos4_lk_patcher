package com.google.android.appfunctions.schema.common.v1.calendar;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import java.util.List;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.calendar.$$__AppSearch__FindEventsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindEventsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindEventsParams findEventsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findEventsParams.getNamespace(), findEventsParams.getId(), "com.google.android.appfunctions.schema.common.v1.calendar.FindEventsParams");
        findEventsParams.getStartDate();
        findEventsParams.getEndDate();
        String query = findEventsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        List attendeeIds = findEventsParams.getAttendeeIds();
        if (attendeeIds != null) {
            builder.setPropertyString("attendeeIds", (String[]) attendeeIds.toArray(new String[0]));
        }
        builder.setPropertyLong("maxCount", findEventsParams.getMaxCount());
        String orderBy = findEventsParams.getOrderBy();
        if (orderBy != null) {
            builder.setPropertyString("orderBy", orderBy);
        }
        return builder.build();
    }
}
