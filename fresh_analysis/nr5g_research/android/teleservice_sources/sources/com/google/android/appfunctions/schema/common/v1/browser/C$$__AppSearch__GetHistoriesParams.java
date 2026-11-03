package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__GetHistoriesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__GetHistoriesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(GetHistoriesParams getHistoriesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(getHistoriesParams.getNamespace(), getHistoriesParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.GetHistoriesParams");
        getHistoriesParams.getStartTime();
        getHistoriesParams.getEndTime();
        String query = getHistoriesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        builder.setPropertyLong("maxCount", getHistoriesParams.getMaxCount());
        return builder.build();
    }
}
