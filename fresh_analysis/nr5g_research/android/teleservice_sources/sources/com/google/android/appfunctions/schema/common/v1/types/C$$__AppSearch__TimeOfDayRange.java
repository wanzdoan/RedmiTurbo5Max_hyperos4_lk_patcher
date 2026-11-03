package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__TimeOfDayRange, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TimeOfDayRange implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TimeOfDayRange timeOfDayRange) {
        GenericDocument.Builder builder = new GenericDocument.Builder(timeOfDayRange.getNamespace(), timeOfDayRange.getId(), "com.google.android.appfunctions.schema.common.v1.types.TimeOfDayRange");
        timeOfDayRange.getStartTime();
        timeOfDayRange.getEndTime();
        return builder.build();
    }
}
