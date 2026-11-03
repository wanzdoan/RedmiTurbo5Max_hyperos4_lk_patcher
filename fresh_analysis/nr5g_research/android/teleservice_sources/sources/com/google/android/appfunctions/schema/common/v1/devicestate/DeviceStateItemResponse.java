package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateItemResponse {
    private final DeviceStateItem deviceStateItem;
    private final String id;
    private final String namespace;

    public DeviceStateItemResponse(String str, String str2, DeviceStateItem deviceStateItem) {
        str.getClass();
        str2.getClass();
        deviceStateItem.getClass();
        this.namespace = str;
        this.id = str2;
        this.deviceStateItem = deviceStateItem;
    }

    public /* synthetic */ DeviceStateItemResponse(String str, String str2, DeviceStateItem deviceStateItem, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, deviceStateItem);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final DeviceStateItem getDeviceStateItem() {
        return this.deviceStateItem;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DeviceStateItemResponse) && Intrinsics.areEqual(this.deviceStateItem, ((DeviceStateItemResponse) obj).deviceStateItem);
    }

    public int hashCode() {
        return Objects.hash(this.deviceStateItem);
    }
}
