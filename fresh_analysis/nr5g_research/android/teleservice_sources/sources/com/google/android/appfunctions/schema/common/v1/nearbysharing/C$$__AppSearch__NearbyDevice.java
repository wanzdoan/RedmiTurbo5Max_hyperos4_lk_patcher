package com.google.android.appfunctions.schema.common.v1.nearbysharing;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: com.google.android.appfunctions.schema.common.v1.nearbysharing.$$__AppSearch__NearbyDevice, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__NearbyDevice implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(NearbyDevice nearbyDevice) {
        GenericDocument.Builder builder = new GenericDocument.Builder(nearbyDevice.getNamespace(), nearbyDevice.getId(), "com.google.android.appfunctions.schema.common.v1.nearbysharing.NearbyDevice");
        String nearbyDeviceId = nearbyDevice.getNearbyDeviceId();
        if (nearbyDeviceId != null) {
            builder.setPropertyString("nearbyDeviceId", nearbyDeviceId);
        }
        String deviceName = nearbyDevice.getDeviceName();
        if (deviceName != null) {
            builder.setPropertyString("deviceName", deviceName);
        }
        String deviceType = nearbyDevice.getDeviceType();
        if (deviceType != null) {
            builder.setPropertyString("deviceType", deviceType);
        }
        Boolean boolIsMyDevice = nearbyDevice.isMyDevice();
        if (boolIsMyDevice != null) {
            builder.setPropertyBoolean("isMyDevice", boolIsMyDevice.booleanValue());
        }
        return builder.build();
    }
}
