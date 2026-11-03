package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__ResumeExerciseParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ResumeExerciseParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ResumeExerciseParams resumeExerciseParams) {
        return new GenericDocument.Builder(resumeExerciseParams.getNamespace(), resumeExerciseParams.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.ResumeExerciseParams").build();
    }
}
