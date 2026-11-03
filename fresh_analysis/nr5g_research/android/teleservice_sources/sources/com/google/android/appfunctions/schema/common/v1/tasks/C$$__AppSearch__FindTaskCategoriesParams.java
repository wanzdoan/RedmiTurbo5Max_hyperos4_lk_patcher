package com.google.android.appfunctions.schema.common.v1.tasks;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.tasks.$$__AppSearch__FindTaskCategoriesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindTaskCategoriesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindTaskCategoriesParams findTaskCategoriesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findTaskCategoriesParams.getNamespace(), findTaskCategoriesParams.getId(), "com.google.android.appfunctions.schema.common.v1.tasks.FindTaskCategoriesParams");
        String query = findTaskCategoriesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findTaskCategoriesParams.getMaxCount());
        return builder.build();
    }
}
