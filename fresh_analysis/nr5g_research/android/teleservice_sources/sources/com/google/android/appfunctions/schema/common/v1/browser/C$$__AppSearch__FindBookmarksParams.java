package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__FindBookmarksParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindBookmarksParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindBookmarksParams findBookmarksParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findBookmarksParams.getNamespace(), findBookmarksParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.FindBookmarksParams");
        String query = findBookmarksParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        findBookmarksParams.getStartDate();
        findBookmarksParams.getEndDate();
        builder.setPropertyLong("maxCount", findBookmarksParams.getMaxCount());
        return builder.build();
    }
}
