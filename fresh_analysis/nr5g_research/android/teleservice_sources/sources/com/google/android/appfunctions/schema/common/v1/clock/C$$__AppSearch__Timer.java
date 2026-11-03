package com.google.android.appfunctions.schema.common.v1.clock;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.clock.$$__AppSearch__Timer, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Timer implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Timer timer) {
        GenericDocument.Builder builder = new GenericDocument.Builder(timer.getNamespace(), timer.getId(), "com.google.android.appfunctions.schema.common.v1.clock.Timer");
        String label = timer.getLabel();
        if (label != null) {
            builder.setPropertyString("label", label);
        }
        builder.setPropertyLong("originalDurationMillis", timer.getOriginalDurationMillis());
        builder.setPropertyLong("durationMillis", timer.getDurationMillis());
        String timerState = timer.getTimerState();
        if (timerState != null) {
            builder.setPropertyString("timerState", timerState);
        }
        builder.setPropertyLong("remainingDurationMillis", timer.getRemainingDurationMillis());
        Long expireTimeEpochMillis = timer.getExpireTimeEpochMillis();
        if (expireTimeEpochMillis != null) {
            builder.setPropertyLong("expireTimeEpochMillis", expireTimeEpochMillis.longValue());
        }
        Boolean boolIsFiring = timer.isFiring();
        if (boolIsFiring != null) {
            builder.setPropertyBoolean("isFiring", boolIsFiring.booleanValue());
        }
        return builder.build();
    }
}
