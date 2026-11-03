package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__FindAlbumsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindAlbumsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindAlbumsParams findAlbumsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findAlbumsParams.getNamespace(), findAlbumsParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.FindAlbumsParams");
        String query = findAlbumsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findAlbumsParams.getMaxCount());
        return builder.build();
    }
}
