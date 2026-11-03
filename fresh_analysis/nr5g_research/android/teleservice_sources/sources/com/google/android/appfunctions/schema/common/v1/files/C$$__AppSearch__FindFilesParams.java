package com.google.android.appfunctions.schema.common.v1.files;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.files.$$__AppSearch__FindFilesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindFilesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindFilesParams findFilesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findFilesParams.getNamespace(), findFilesParams.getId(), "com.google.android.appfunctions.schema.common.v1.files.FindFilesParams");
        String query = findFilesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String fileType = findFilesParams.getFileType();
        if (fileType != null) {
            builder.setPropertyString("fileType", fileType);
        }
        builder.setPropertyLong("maxCount", findFilesParams.getMaxCount());
        return builder.build();
    }
}
