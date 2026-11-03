package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__Alarm, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Alarm implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Alarm alarm) {
        GenericDocument.Builder builder = new GenericDocument.Builder(alarm.getNamespace(), alarm.getId(), "com.google.android.appfunctions.schema.common.v1.clock.Alarm");
        String label = alarm.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        alarm.getTime();
        String alarmStatus = alarm.getAlarmStatus();
        if (alarmStatus != null) {
            builder.setPropertyString("alarmStatus", alarmStatus);
        }
        alarm.getDayPattern();
        return builder.build();
    }
}
