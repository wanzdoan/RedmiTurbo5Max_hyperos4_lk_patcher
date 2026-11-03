package com.google.android.appfunctions.schema.common.v1.memory;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.memory.$$__AppSearch__FindMemoryItemsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindMemoryItemsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindMemoryItemsParams findMemoryItemsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findMemoryItemsParams.getNamespace(), findMemoryItemsParams.getId(), "com.google.android.appfunctions.schema.common.v1.memory.FindMemoryItemsParams");
        String query = findMemoryItemsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        findMemoryItemsParams.getUpdatedAfter();
        findMemoryItemsParams.getUpdatedBefore();
        Integer maxCount = findMemoryItemsParams.getMaxCount();
        if (maxCount != null) {
            builder.setPropertyLong("maxCount", maxCount.intValue());
        }
        String queryLocale = findMemoryItemsParams.getQueryLocale();
        if (queryLocale != null) {
            builder.setPropertyString("queryLocale", queryLocale);
        }
        return builder.build();
    }
}
