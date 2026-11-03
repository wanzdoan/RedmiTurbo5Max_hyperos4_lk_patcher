package com.google.android.appfunctions.schema.common.v1.nearbysharing;

/* JADX INFO: compiled from: NearbySharing.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FindNearbyDevicesParams {
    public abstract String getDeviceType();

    public abstract String getId();

    public abstract int getMaxCount();

    public abstract Long getMaxSearchTimeMillis();

    public abstract String getNamespace();

    public abstract String getQuery();

    public abstract Boolean isMyDevice();
}
