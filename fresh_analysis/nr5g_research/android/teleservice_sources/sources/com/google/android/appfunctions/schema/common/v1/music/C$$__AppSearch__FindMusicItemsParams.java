package com.google.android.appfunctions.schema.common.v1.music;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.music.$$__AppSearch__FindMusicItemsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindMusicItemsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindMusicItemsParams findMusicItemsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findMusicItemsParams.getNamespace(), findMusicItemsParams.getId(), "com.google.android.appfunctions.schema.common.v1.music.FindMusicItemsParams");
        String query = findMusicItemsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String type = findMusicItemsParams.getType();
        if (type != null) {
            builder.setPropertyString("type", type);
        }
        builder.setPropertyLong("maxCount", findMusicItemsParams.getMaxCount());
        return builder.build();
    }
}
