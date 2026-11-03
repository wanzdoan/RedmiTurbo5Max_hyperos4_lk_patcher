package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__LatLng, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__LatLng implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(LatLng latLng) {
        GenericDocument.Builder builder = new GenericDocument.Builder(latLng.getNamespace(), latLng.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.LatLng");
        builder.setPropertyDouble("latitude", latLng.getLatitude());
        builder.setPropertyDouble("longitude", latLng.getLongitude());
        return builder.build();
    }
}
