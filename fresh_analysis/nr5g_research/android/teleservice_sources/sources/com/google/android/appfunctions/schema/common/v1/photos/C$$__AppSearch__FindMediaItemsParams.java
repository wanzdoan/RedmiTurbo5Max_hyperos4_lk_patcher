package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__FindMediaItemsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindMediaItemsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindMediaItemsParams findMediaItemsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findMediaItemsParams.getNamespace(), findMediaItemsParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.FindMediaItemsParams");
        findMediaItemsParams.getStartTime();
        findMediaItemsParams.getEndTime();
        String query = findMediaItemsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String location = findMediaItemsParams.getLocation();
        if (location != null) {
            builder.setPropertyString("location", location);
        }
        Boolean starred = findMediaItemsParams.getStarred();
        if (starred != null) {
            builder.setPropertyBoolean("starred", starred.booleanValue());
        }
        String albumId = findMediaItemsParams.getAlbumId();
        if (albumId != null) {
            builder.setPropertyString("albumId", albumId);
        }
        builder.setPropertyLong("maxCount", findMediaItemsParams.getMaxCount());
        return builder.build();
    }
}
