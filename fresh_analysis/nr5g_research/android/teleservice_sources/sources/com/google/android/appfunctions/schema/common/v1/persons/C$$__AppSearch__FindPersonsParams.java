package com.google.android.appfunctions.schema.common.v1.persons;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.persons.$$__AppSearch__FindPersonsParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindPersonsParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindPersonsParams findPersonsParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findPersonsParams.getNamespace(), findPersonsParams.getId(), "com.google.android.appfunctions.schema.common.v1.persons.FindPersonsParams");
        String query = findPersonsParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", findPersonsParams.getMaxCount());
        return builder.build();
    }
}
