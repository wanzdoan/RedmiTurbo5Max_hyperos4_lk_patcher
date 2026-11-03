package com.google.android.appfunctions.schema.common.v1.fitness;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.fitness.$$__AppSearch__ExerciseGoal, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ExerciseGoal implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ExerciseGoal exerciseGoal) {
        GenericDocument.Builder builder = new GenericDocument.Builder(exerciseGoal.getNamespace(), exerciseGoal.getId(), "com.google.android.appfunctions.schema.common.v1.fitness.ExerciseGoal");
        Long durationMillis = exerciseGoal.getDurationMillis();
        if (durationMillis != null) {
            builder.setPropertyLong("durationMillis", durationMillis.longValue());
        }
        Long distanceMeters = exerciseGoal.getDistanceMeters();
        if (distanceMeters != null) {
            builder.setPropertyLong("distanceMeters", distanceMeters.longValue());
        }
        Long calories = exerciseGoal.getCalories();
        if (calories != null) {
            builder.setPropertyLong("calories", calories.longValue());
        }
        return builder.build();
    }
}
