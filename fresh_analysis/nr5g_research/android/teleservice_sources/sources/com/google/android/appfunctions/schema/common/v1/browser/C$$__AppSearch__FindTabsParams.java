package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__FindTabsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindTabsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindTabsParams findTabsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findTabsParams.getNamespace(), findTabsParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.FindTabsParams");
        String query = findTabsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findTabsParams.getMaxCount());
        return builder.build();
    }
}
