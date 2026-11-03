package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__UpdateTimerParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateTimerParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateTimerParams updateTimerParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateTimerParams.getNamespace(), updateTimerParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.UpdateTimerParams");
        String timerId = updateTimerParams.getTimerId();
        if (timerId != null) {
            builder.setPropertyString("timerId", timerId);
        }
        updateTimerParams.getLabel();
        updateTimerParams.getDurationMs();
        updateTimerParams.getTimerState();
        return builder.build();
    }
}
