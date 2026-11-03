package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__DateTime, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DateTime implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DateTime dateTime) {
        GenericDocument.Builder builder = new GenericDocument.Builder(dateTime.getNamespace(), dateTime.getId(), "com.google.android.appfunctions.schema.common.v1.types.DateTime");
        String timeZone = dateTime.getTimeZone();
        if (timeZone != null) {
            builder.setPropertyString("timeZone", timeZone);
        }
        dateTime.getDate();
        dateTime.getTimeOfDay();
        return builder.build();
    }
}
