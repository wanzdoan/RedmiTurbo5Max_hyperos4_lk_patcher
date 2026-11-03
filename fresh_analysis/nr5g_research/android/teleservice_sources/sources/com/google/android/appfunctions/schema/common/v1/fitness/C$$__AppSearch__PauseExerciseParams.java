package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__PauseExerciseParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__PauseExerciseParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(PauseExerciseParams pauseExerciseParams) {
        return new GenericDocument.Builder(pauseExerciseParams.getNamespace(), pauseExerciseParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.PauseExerciseParams").build();
    }
}
