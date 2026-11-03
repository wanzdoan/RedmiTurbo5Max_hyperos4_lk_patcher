package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__RenderMapWithRouteImageParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__RenderMapWithRouteImageParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(RenderMapWithRouteImageParams renderMapWithRouteImageParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(renderMapWithRouteImageParams.getNamespace(), renderMapWithRouteImageParams.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.RenderMapWithRouteImageParams");
        String routeToken = renderMapWithRouteImageParams.getRouteToken();
        if (routeToken != null) {
            builder.setPropertyString("routeToken", routeToken);
        }
        Double widthPx = renderMapWithRouteImageParams.getWidthPx();
        if (widthPx != null) {
            builder.setPropertyDouble("widthPx", widthPx.doubleValue());
        }
        Double heightPx = renderMapWithRouteImageParams.getHeightPx();
        if (heightPx != null) {
            builder.setPropertyDouble("heightPx", heightPx.doubleValue());
        }
        return builder.build();
    }
}
