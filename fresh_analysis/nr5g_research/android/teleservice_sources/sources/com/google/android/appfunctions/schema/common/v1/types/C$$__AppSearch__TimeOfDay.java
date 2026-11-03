package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__TimeOfDay, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TimeOfDay implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TimeOfDay timeOfDay) {
        GenericDocument.Builder builder = new GenericDocument.Builder(timeOfDay.getNamespace(), timeOfDay.getId(), "com.google.android.appfunctions.schema.common.v1.types.TimeOfDay");
        builder.setPropertyLong("hours", timeOfDay.getHours());
        builder.setPropertyLong("minutes", timeOfDay.getMinutes());
        builder.setPropertyLong("seconds", timeOfDay.getSeconds());
        builder.setPropertyLong("nanos", timeOfDay.getNanos());
        return builder.build();
    }
}
