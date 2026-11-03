package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class GetBatteryDeviceStateParams {
    private final String id;
    private final String namespace;
    private final Boolean requestInitiatedWhileUnlocked;

    public GetBatteryDeviceStateParams() {
        this(null, null, null, 7, null);
    }

    public GetBatteryDeviceStateParams(String str, String str2, Boolean bool) {
        str.getClass();
        str2.getClass();
        this.namespace = str;
        this.id = str2;
        this.requestInitiatedWhileUnlocked = bool;
    }

    public /* synthetic */ GetBatteryDeviceStateParams(String str, String str2, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? null : bool);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final Boolean getRequestInitiatedWhileUnlocked() {
        return this.requestInitiatedWhileUnlocked;
    }

    public boolean equals(Object obj) {
        return (obj instanceof GetBatteryDeviceStateParams) && Intrinsics.areEqual(this.requestInitiatedWhileUnlocked, ((GetBatteryDeviceStateParams) obj).requestInitiatedWhileUnlocked);
    }

    public int hashCode() {
        return Objects.hash(this.requestInitiatedWhileUnlocked);
    }
}
