package com.google.android.appfunctions.schema.common.v1.nearbysharing;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.nearbysharing.$$__AppSearch__FindNearbyDevicesParams, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__FindNearbyDevicesParams implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(FindNearbyDevicesParams findNearbyDevicesParams) {
        GenericDocument.Builder builder = new GenericDocument.Builder(findNearbyDevicesParams.getNamespace(), findNearbyDevicesParams.getId(), "com.google.android.appfunctions.schema.common.v1.nearbysharing.FindNearbyDevicesParams");
        String query = findNearbyDevicesParams.getQuery();
        if (query != null) {
            builder.setPropertyString("query", query);
        }
        String deviceType = findNearbyDevicesParams.getDeviceType();
        if (deviceType != null) {
            builder.setPropertyString("deviceType", deviceType);
        }
        Boolean boolIsMyDevice = findNearbyDevicesParams.isMyDevice();
        if (boolIsMyDevice != null) {
            builder.setPropertyBoolean("isMyDevice", boolIsMyDevice.booleanValue());
        }
        Long maxSearchTimeMillis = findNearbyDevicesParams.getMaxSearchTimeMillis();
        if (maxSearchTimeMillis != null) {
            builder.setPropertyLong("maxSearchTimeMillis", maxSearchTimeMillis.longValue());
        }
        builder.setPropertyLong("maxCount", findNearbyDevicesParams.getMaxCount());
        return builder.build();
    }
}
