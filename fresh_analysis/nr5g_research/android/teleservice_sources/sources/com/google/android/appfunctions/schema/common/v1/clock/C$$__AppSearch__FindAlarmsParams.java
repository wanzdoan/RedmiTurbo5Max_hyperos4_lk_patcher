package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__FindAlarmsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindAlarmsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindAlarmsParams findAlarmsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findAlarmsParams.getNamespace(), findAlarmsParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.FindAlarmsParams");
        findAlarmsParams.getStartTime();
        findAlarmsParams.getEndTime();
        String label = findAlarmsParams.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        String alarmStatus = findAlarmsParams.getAlarmStatus();
        if (alarmStatus != null) {
            builder.setPropertyString("alarmStatus", alarmStatus);
        }
        findAlarmsParams.getDayPattern();
        builder.setPropertyLong("maxCount", findAlarmsParams.getMaxCount());
        String sortingOrder = findAlarmsParams.getSortingOrder();
        if (sortingOrder != null) {
            builder.setPropertyString("sortingOrder", sortingOrder);
        }
        return builder.build();
    }
}
