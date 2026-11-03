package com.google.android.appfunctions.schema.common.v1.photos;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.photos.$$__AppSearch__SetMediaItemFilterParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__SetMediaItemFilterParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(SetMediaItemFilterParams setMediaItemFilterParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(setMediaItemFilterParams.getNamespace(), setMediaItemFilterParams.getId(), "com.google.android.appfunctions.schema.common.v1.photos.SetMediaItemFilterParams");
        String mediaItemId = setMediaItemFilterParams.getMediaItemId();
        if (mediaItemId != null) {
            builder.setPropertyString("mediaItemId", mediaItemId);
        }
        setMediaItemFilterParams.getMediaItemUri();
        String filter = setMediaItemFilterParams.getFilter();
        if (filter != null) {
            builder.setPropertyString("filter", filter);
        }
        return builder.build();
    }
}
