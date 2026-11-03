package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__DayPattern, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__DayPattern implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(DayPattern dayPattern) {
        GenericDocument.Builder builder = new GenericDocument.Builder(dayPattern.getNamespace(), dayPattern.getId(), "com.google.android.appfunctions.schema.common.v1.clock.DayPattern");
        dayPattern.getDate();
        String recurrencePattern = dayPattern.getRecurrencePattern();
        if (recurrencePattern != null) {
            builder.setPropertyString("recurrencePattern", recurrencePattern);
        }
        return builder.build();
    }
}
