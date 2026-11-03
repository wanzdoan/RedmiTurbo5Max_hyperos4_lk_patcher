package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__Bookmark, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Bookmark implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Bookmark bookmark) {
        GenericDocument.Builder builder = new GenericDocument.Builder(bookmark.getNamespace(), bookmark.getId(), "com.google.android.appfunctions.schema.common.v1.browser.Bookmark");
        String name = bookmark.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        bookmark.getUrl();
        return builder.build();
    }
}
