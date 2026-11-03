package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__Folder, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Folder implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Folder folder) {
        GenericDocument.Builder builder = new GenericDocument.Builder(folder.getNamespace(), folder.getId(), "com.google.android.appfunctions.schema.common.v1.notes.Folder");
        String name = folder.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        return builder.build();
    }
}
