package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__StartExerciseParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__StartExerciseParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(StartExerciseParams startExerciseParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(startExerciseParams.getNamespace(), startExerciseParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.StartExerciseParams");
        String exerciseType = startExerciseParams.getExerciseType();
        if (exerciseType != null) {
            builder.setPropertyString("exerciseType", exerciseType);
        }
        startExerciseParams.getExerciseGoal();
        return builder.build();
    }
}
