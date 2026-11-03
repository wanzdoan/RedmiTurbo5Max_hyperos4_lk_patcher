package com.google.android.appfunctions.schema.common.v1.video;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.video.$$__AppSearch__TimedText, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__TimedText implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(TimedText timedText) {
        GenericDocument.Builder builder = new GenericDocument.Builder(timedText.getNamespace(), timedText.getId(), "com.google.android.appfunctions.schema.common.v1.video.TimedText");
        builder.setPropertyLong("startTimeMillis", timedText.getStartTimeMillis());
        builder.setPropertyLong("durationMills", timedText.getDurationMills());
        builder.setPropertyDouble("confidence", timedText.getConfidence());
        String text = timedText.getText();
        if (text != null) {
            builder.setPropertyString("text", text);
        }
        return builder.build();
    }
}
