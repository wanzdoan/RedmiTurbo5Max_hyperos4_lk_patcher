package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__FindTimersParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindTimersParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindTimersParams findTimersParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findTimersParams.getNamespace(), findTimersParams.getId(), "com.google.android.appfunctions.schema.common.v1.clock.FindTimersParams");
        String label = findTimersParams.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        String timerState = findTimersParams.getTimerState();
        if (timerState != null) {
            builder.setPropertyString("timerState", timerState);
        }
        builder.setPropertyLong("maxCount", findTimersParams.getMaxCount());
        String sortingOrder = findTimersParams.getSortingOrder();
        if (sortingOrder != null) {
            builder.setPropertyString("sortingOrder", sortingOrder);
        }
        return builder.build();
    }
}
