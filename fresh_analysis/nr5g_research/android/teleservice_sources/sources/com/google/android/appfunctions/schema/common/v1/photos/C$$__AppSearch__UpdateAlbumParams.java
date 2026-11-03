package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__UpdateAlbumParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateAlbumParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateAlbumParams updateAlbumParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateAlbumParams.getNamespace(), updateAlbumParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.UpdateAlbumParams");
        String albumId = updateAlbumParams.getAlbumId();
        if (albumId != null) {
            builder.setPropertyString("albumId", albumId);
        }
        updateAlbumParams.getTitle();
        return builder.build();
    }
}
