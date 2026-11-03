package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__ShowFitnessMetricParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ShowFitnessMetricParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ShowFitnessMetricParams showFitnessMetricParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(showFitnessMetricParams.getNamespace(), showFitnessMetricParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.ShowFitnessMetricParams");
        String metricType = showFitnessMetricParams.getMetricType();
        if (metricType != null) {
            builder.setPropertyString("metricType", metricType);
        }
        return builder.build();
    }
}
