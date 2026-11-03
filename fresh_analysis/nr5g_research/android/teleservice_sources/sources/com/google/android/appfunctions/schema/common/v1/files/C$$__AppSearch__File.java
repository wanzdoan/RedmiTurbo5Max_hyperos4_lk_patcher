package com.google.android.appfunctions.schema.common.v1.files;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.files.$$__AppSearch__File, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__File implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(File file) {
        GenericDocument.Builder builder = new GenericDocument.Builder(file.getNamespace(), file.getId(), "com.google.android.appfunctions.schema.common.v1.files.File");
        String name = file.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        file.getDateCreated();
        return builder.build();
    }
}
