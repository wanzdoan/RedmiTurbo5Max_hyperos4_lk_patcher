package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__ShowRouteInMapParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__ShowRouteInMapParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(ShowRouteInMapParams showRouteInMapParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(showRouteInMapParams.getNamespace(), showRouteInMapParams.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.ShowRouteInMapParams");
        String routeToken = showRouteInMapParams.getRouteToken();
        if (routeToken != null) {
            builder.setPropertyString("routeToken", routeToken);
        }
        return builder.build();
    }
}
