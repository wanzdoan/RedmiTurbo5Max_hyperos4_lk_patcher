package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class GetDeviceStateItemParams {
    private final String id;
    private final List itemizationKeys;
    private final String key;
    private final String namespace;

    public GetDeviceStateItemParams(String str, String str2, String str3, List list) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.itemizationKeys = list;
    }

    public /* synthetic */ GetDeviceStateItemParams(String str, String str2, String str3, List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, str3, (i & 8) != 0 ? CollectionsKt.emptyList() : list);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final String getKey() {
        return this.key;
    }

    public final List getItemizationKeys() {
        return this.itemizationKeys;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GetDeviceStateItemParams)) {
            return false;
        }
        GetDeviceStateItemParams getDeviceStateItemParams = (GetDeviceStateItemParams) obj;
        return Intrinsics.areEqual(this.key, getDeviceStateItemParams.key) && Intrinsics.areEqual(this.itemizationKeys, getDeviceStateItemParams.itemizationKeys);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.itemizationKeys);
    }
}
