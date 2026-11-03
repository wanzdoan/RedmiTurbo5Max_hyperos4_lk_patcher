package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class PerScreenDeviceStates {
    private final String description;
    private final List deviceStateItems;
    private final String id;
    private final String intentUri;
    private final String namespace;

    public PerScreenDeviceStates(String str, String str2, String str3, String str4, List list) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        this.namespace = str;
        this.id = str2;
        this.description = str3;
        this.intentUri = str4;
        this.deviceStateItems = list;
    }

    public /* synthetic */ PerScreenDeviceStates(String str, String str2, String str3, String str4, List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? CollectionsKt.emptyList() : list);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getIntentUri() {
        return this.intentUri;
    }

    public final List getDeviceStateItems() {
        return this.deviceStateItems;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PerScreenDeviceStates)) {
            return false;
        }
        PerScreenDeviceStates perScreenDeviceStates = (PerScreenDeviceStates) obj;
        return Intrinsics.areEqual(this.description, perScreenDeviceStates.description) && Intrinsics.areEqual(this.intentUri, perScreenDeviceStates.intentUri) && Intrinsics.areEqual(this.deviceStateItems, perScreenDeviceStates.deviceStateItems);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.intentUri, this.deviceStateItems);
    }
}
