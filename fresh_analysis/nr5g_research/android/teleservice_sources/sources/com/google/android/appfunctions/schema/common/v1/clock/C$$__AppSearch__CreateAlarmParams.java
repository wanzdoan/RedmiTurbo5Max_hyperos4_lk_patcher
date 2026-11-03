package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__CreateAlarmParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateAlarmParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateAlarmParams createAlarmParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createAlarmParams.getNamespace(), createAlarmParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.CreateAlarmParams");
        String label = createAlarmParams.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        createAlarmParams.getTime();
        String alarmStatus = createAlarmParams.getAlarmStatus();
        if (alarmStatus != null) {
            builder.setPropertyString("alarmStatus", alarmStatus);
        }
        createAlarmParams.getDayPattern();
        String externalId = createAlarmParams.getExternalId();
        if (externalId != null) {
            builder.setPropertyString("externalId", externalId);
        }
        return builder.build();
    }
}
