package com.google.android.appfunctions.schema.common.v1.email;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.email.$$__AppSearch__FindAccountsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindAccountsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindAccountsParams findAccountsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findAccountsParams.getNamespace(), findAccountsParams.getId(), "com.google.android.appfunctions.schema.common.v1.email.FindAccountsParams");
        String query = findAccountsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findAccountsParams.getMaxCount());
        return builder.build();
    }
}
