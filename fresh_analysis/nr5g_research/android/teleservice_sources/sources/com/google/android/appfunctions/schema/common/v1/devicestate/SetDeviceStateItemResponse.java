package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateSetters.kt */
/* JADX INFO: loaded from: classes.dex */
public final class SetDeviceStateItemResponse {
    private final String currentValue;
    private final String failureReason;
    private final String id;
    private final boolean isSuccessful;
    private final String namespace;

    public SetDeviceStateItemResponse(String str, String str2, boolean z, String str3, String str4) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        this.namespace = str;
        this.id = str2;
        this.isSuccessful = z;
        this.currentValue = str3;
        this.failureReason = str4;
    }

    public /* synthetic */ SetDeviceStateItemResponse(String str, String str2, boolean z, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, z, str3, (i & 16) != 0 ? null : str4);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final boolean isSuccessful() {
        return this.isSuccessful;
    }

    public final String getCurrentValue() {
        return this.currentValue;
    }

    public final String getFailureReason() {
        return this.failureReason;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SetDeviceStateItemResponse)) {
            return false;
        }
        SetDeviceStateItemResponse setDeviceStateItemResponse = (SetDeviceStateItemResponse) obj;
        return this.isSuccessful == setDeviceStateItemResponse.isSuccessful && Intrinsics.areEqual(this.currentValue, setDeviceStateItemResponse.currentValue) && Intrinsics.areEqual(this.failureReason, setDeviceStateItemResponse.failureReason);
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.isSuccessful), this.currentValue, this.failureReason);
    }
}
