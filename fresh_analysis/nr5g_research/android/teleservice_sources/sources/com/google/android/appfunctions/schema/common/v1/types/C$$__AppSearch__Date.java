package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__Date, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Date implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Date date) {
        GenericDocument.Builder builder = new GenericDocument.Builder(date.getNamespace(), date.getId(), "com.google.android.appfunctions.schema.common.v1.types.Date");
        builder.setPropertyLong("year", date.getYear());
        builder.setPropertyLong("month", date.getMonth());
        builder.setPropertyLong("day", date.getDay());
        return builder.build();
    }
}
