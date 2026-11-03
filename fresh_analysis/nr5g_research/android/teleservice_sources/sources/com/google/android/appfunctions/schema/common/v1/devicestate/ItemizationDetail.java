package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
public final class ItemizationDetail {
    private final String id;
    private final String key;
    private final String namespace;
    private final String value;

    public ItemizationDetail(String str, String str2, String str3, String str4) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        str4.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.value = str4;
    }

    public /* synthetic */ ItemizationDetail(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, str3, str4);
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

    public final String getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ItemizationDetail)) {
            return false;
        }
        ItemizationDetail itemizationDetail = (ItemizationDetail) obj;
        return Intrinsics.areEqual(this.key, itemizationDetail.key) && Intrinsics.areEqual(this.value, itemizationDetail.value);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }
}
