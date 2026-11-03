package com.google.android.appfunctions.schema.common.v1.browser;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.browser.$$__AppSearch__ShowSiteParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ShowSiteParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ShowSiteParams showSiteParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(showSiteParams.getNamespace(), showSiteParams.getId(), "com.google.android.appfunctions.schema.common.v1.browser.ShowSiteParams");
        showSiteParams.getUrl();
        builder.setPropertyBoolean("shouldShowInNewTab", showSiteParams.shouldShowInNewTab);
        return builder.build();
    }
}
