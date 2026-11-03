package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__UpdateBookmarkParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__UpdateBookmarkParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(UpdateBookmarkParams updateBookmarkParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(updateBookmarkParams.getNamespace(), updateBookmarkParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.UpdateBookmarkParams");
        String bookmarkId = updateBookmarkParams.getBookmarkId();
        if (bookmarkId != null) {
            builder.setPropertyString("bookmarkId", bookmarkId);
        }
        updateBookmarkParams.getName();
        updateBookmarkParams.getUrl();
        return builder.build();
    }
}
