package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__CreateBookmarkParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__CreateBookmarkParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(CreateBookmarkParams createBookmarkParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(createBookmarkParams.getNamespace(), createBookmarkParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.CreateBookmarkParams");
        String name = createBookmarkParams.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        createBookmarkParams.getUrl();
        return builder.build();
    }
}
