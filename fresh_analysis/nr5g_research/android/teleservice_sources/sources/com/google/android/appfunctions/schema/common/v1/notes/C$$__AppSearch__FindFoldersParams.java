package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__FindFoldersParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindFoldersParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindFoldersParams findFoldersParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findFoldersParams.getNamespace(), findFoldersParams.getId(), "com.google.android.appfunctions.schema.common.v1.notes.FindFoldersParams");
        String query = findFoldersParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findFoldersParams.getMaxCount());
        return builder.build();
    }
}
