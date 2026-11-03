package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__ShowExerciseParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ShowExerciseParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ShowExerciseParams showExerciseParams) {
        return new GenericDocument.Builder(showExerciseParams.getNamespace(), showExerciseParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.ShowExerciseParams").build();
    }
}
