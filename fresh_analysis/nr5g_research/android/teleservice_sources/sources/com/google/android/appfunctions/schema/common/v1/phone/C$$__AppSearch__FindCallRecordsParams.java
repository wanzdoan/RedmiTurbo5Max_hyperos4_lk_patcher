package com.google.android.appfunctions.schema.common.v1.phone;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.phone.$$__AppSearch__FindCallRecordsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindCallRecordsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindCallRecordsParams findCallRecordsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findCallRecordsParams.getNamespace(), findCallRecordsParams.getId(), "com.google.android.appfunctions.schema.common.v1.phone.FindCallRecordsParams");
        String query = findCallRecordsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String contactId = findCallRecordsParams.getContactId();
        if (contactId != null) {
            builder.setPropertyString("contactId", contactId);
        }
        findCallRecordsParams.getStartTime();
        findCallRecordsParams.getEndTime();
        String callType = findCallRecordsParams.getCallType();
        if (callType != null) {
            builder.setPropertyString("callType", callType);
        }
        builder.setPropertyLong("maxCount", findCallRecordsParams.getMaxCount());
        return builder.build();
    }
}
