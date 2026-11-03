package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__CreateTimerParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateTimerParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateTimerParams createTimerParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createTimerParams.getNamespace(), createTimerParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.CreateTimerParams");
        String label = createTimerParams.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        builder.setPropertyLong("originalDurationMillis", createTimerParams.getOriginalDurationMillis());
        String timerState = createTimerParams.getTimerState();
        if (timerState != null) {
            builder.setPropertyString("timerState", timerState);
        }
        String externalId = createTimerParams.getExternalId();
        if (externalId != null) {
            builder.setPropertyString("externalId", externalId);
        }
        return builder.build();
    }
}
