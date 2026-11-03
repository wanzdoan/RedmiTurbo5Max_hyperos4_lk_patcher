package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__UpdateFolderParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateFolderParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateFolderParams updateFolderParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateFolderParams.getNamespace(), updateFolderParams.getId(), "com.google.android.appfunctions.schema.common.v1.notes.UpdateFolderParams");
        String folderId = updateFolderParams.getFolderId();
        if (folderId != null) {
            builder.setPropertyString("folderId", folderId);
        }
        updateFolderParams.getName();
        return builder.build();
    }
}
