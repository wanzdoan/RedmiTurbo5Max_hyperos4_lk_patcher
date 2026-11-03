package com.google.android.appfunctions.schema.common.v1.video;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.video.$$__AppSearch__FindVideosParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindVideosParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindVideosParams findVideosParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findVideosParams.getNamespace(), findVideosParams.getId(), "com.google.android.appfunctions.schema.common.v1.video.FindVideosParams");
        String query = findVideosParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findVideosParams.getMaxCount());
        return builder.build();
    }
}
