package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__CreateFolderParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateFolderParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateFolderParams createFolderParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createFolderParams.getNamespace(), createFolderParams.getId(), "com.google.android.appfunctions.schema.common.v1.notes.CreateFolderParams");
        String name = createFolderParams.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        return builder.build();
    }
}
