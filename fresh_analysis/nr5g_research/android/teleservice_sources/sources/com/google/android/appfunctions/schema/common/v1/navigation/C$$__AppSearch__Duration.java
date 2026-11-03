package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__Duration, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Duration implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Duration duration) {
        GenericDocument.Builder builder = new GenericDocument.Builder(duration.getNamespace(), duration.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.Duration");
        Long durationSeconds = duration.getDurationSeconds();
        if (durationSeconds != null) {
            builder.setPropertyLong("durationSeconds", durationSeconds.longValue());
        }
        String durationDisplayString = duration.getDurationDisplayString();
        if (durationDisplayString != null) {
            builder.setPropertyString("durationDisplayString", durationDisplayString);
        }
        return builder.build();
    }
}
