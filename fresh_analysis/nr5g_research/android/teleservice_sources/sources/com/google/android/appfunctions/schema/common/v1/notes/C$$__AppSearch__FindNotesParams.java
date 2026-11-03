package com.google.android.appfunctions.schema.common.v1.notes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.notes.$$__AppSearch__FindNotesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindNotesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindNotesParams findNotesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findNotesParams.getNamespace(), findNotesParams.getId(), "com.google.android.appfunctions.schema.common.v1.notes.FindNotesParams");
        String query = findNotesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        findNotesParams.getStartDate();
        findNotesParams.getEndDate();
        builder.setPropertyLong("maxCount", findNotesParams.getMaxCount());
        return builder.build();
    }
}
