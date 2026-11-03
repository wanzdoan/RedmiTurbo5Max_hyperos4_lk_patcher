package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__StopExerciseParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__StopExerciseParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(StopExerciseParams stopExerciseParams) {
        return new GenericDocument.Builder(stopExerciseParams.getNamespace(), stopExerciseParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.StopExerciseParams").build();
    }
}
