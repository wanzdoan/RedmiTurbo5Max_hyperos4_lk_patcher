package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__LocationQuery, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__LocationQuery implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(LocationQuery locationQuery) {
        GenericDocument.Builder builder = new GenericDocument.Builder(locationQuery.getNamespace(), locationQuery.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.LocationQuery");
        String query = locationQuery.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String userLocation = locationQuery.getUserLocation();
        if (userLocation != null) {
            builder.setPropertyString("userLocation", userLocation);
        }
        return builder.build();
    }
}
