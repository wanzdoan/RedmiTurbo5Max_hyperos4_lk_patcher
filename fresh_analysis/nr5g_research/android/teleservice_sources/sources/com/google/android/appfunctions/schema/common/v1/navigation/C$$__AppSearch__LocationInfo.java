package com.google.android.appfunctions.schema.common.v1.navigation;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.navigation.$$__AppSearch__LocationInfo, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__LocationInfo implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(LocationInfo locationInfo) {
        GenericDocument.Builder builder = new GenericDocument.Builder(locationInfo.getNamespace(), locationInfo.getId(), "com.google.android.appfunctions.schema.common.v1.navigation.LocationInfo");
        String address = locationInfo.getAddress();
        if (address != null) {
            builder.setPropertyString("address", address);
        }
        locationInfo.getLatLng();
        return builder.build();
    }
}
