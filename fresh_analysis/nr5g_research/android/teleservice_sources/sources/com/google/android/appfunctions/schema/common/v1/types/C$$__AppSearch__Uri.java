package com.google.android.appfunctions.schema.common.v1.types;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.types.$$__AppSearch__Uri, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Uri implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Uri uri) {
        GenericDocument.Builder builder = new GenericDocument.Builder(uri.getNamespace(), uri.getId(), "com.google.android.appfunctions.schema.common.v1.types.Uri");
        String uri2 = uri.getUri();
        if (uri2 != null) {
            builder.setPropertyString("uri", uri2);
        }
        return builder.build();
    }
}
