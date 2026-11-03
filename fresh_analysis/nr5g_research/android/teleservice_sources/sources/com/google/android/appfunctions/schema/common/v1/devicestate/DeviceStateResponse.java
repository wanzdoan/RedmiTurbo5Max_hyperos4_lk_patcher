package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateResponse {
    private final String deviceLocale;
    private final String globalHintText;
    private final String id;
    private final String namespace;
    private final List perScreenDeviceStates;

    public DeviceStateResponse(String str, String str2, List list, String str3, String str4) {
        str.getClass();
        str2.getClass();
        list.getClass();
        str3.getClass();
        this.namespace = str;
        this.id = str2;
        this.perScreenDeviceStates = list;
        this.deviceLocale = str3;
        this.globalHintText = str4;
    }

    public /* synthetic */ DeviceStateResponse(String str, String str2, List list, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? CollectionsKt.emptyList() : list, str3, (i & 16) != 0 ? null : str4);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final List getPerScreenDeviceStates() {
        return this.perScreenDeviceStates;
    }

    public final String getDeviceLocale() {
        return this.deviceLocale;
    }

    public final String getGlobalHintText() {
        return this.globalHintText;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceStateResponse)) {
            return false;
        }
        DeviceStateResponse deviceStateResponse = (DeviceStateResponse) obj;
        return Intrinsics.areEqual(this.perScreenDeviceStates, deviceStateResponse.perScreenDeviceStates) && Intrinsics.areEqual(this.deviceLocale, deviceStateResponse.deviceLocale) && Intrinsics.areEqual(this.globalHintText, deviceStateResponse.globalHintText);
    }

    public int hashCode() {
        return Objects.hash(this.perScreenDeviceStates, this.deviceLocale, this.globalHintText);
    }
}
