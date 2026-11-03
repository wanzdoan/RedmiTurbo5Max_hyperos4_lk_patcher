package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__UpdateAlarmParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateAlarmParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateAlarmParams updateAlarmParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateAlarmParams.getNamespace(), updateAlarmParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.UpdateAlarmParams");
        String alarmId = updateAlarmParams.getAlarmId();
        if (alarmId != null) {
            builder.setPropertyString("alarmId", alarmId);
        }
        updateAlarmParams.getLabel();
        updateAlarmParams.getTime();
        updateAlarmParams.getAlarmStatus();
        updateAlarmParams.getDayPattern();
        return builder.build();
    }
}
