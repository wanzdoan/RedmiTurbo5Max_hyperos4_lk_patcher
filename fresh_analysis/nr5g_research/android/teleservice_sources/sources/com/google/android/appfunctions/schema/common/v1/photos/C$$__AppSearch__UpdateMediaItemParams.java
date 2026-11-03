package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__UpdateMediaItemParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateMediaItemParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateMediaItemParams updateMediaItemParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateMediaItemParams.getNamespace(), updateMediaItemParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.UpdateMediaItemParams");
        String mediaItemId = updateMediaItemParams.getMediaItemId();
        if (mediaItemId != null) {
            builder.setPropertyString("mediaItemId", mediaItemId);
        }
        updateMediaItemParams.getTitle();
        updateMediaItemParams.getStarred();
        updateMediaItemParams.getAlbumId();
        return builder.build();
    }
}
