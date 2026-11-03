package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateSetters.kt */
/* JADX INFO: loaded from: classes.dex */
public final class OffsetNumericDeviceStateItemByValueParams {
    private final String id;
    private final List itemizationKeys;
    private final String key;
    private final String namespace;
    private final Boolean requestInitiatedWhileUnlocked;
    private final double valueAdjustment;

    public OffsetNumericDeviceStateItemByValueParams(String str, String str2, String str3, List list, double d, Boolean bool) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.itemizationKeys = list;
        this.valueAdjustment = d;
        this.requestInitiatedWhileUnlocked = bool;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ OffsetNumericDeviceStateItemByValueParams(String str, String str2, String str3, List list, double d, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i & 1) != 0 ? "" : str;
        str2 = (i & 2) != 0 ? "" : str2;
        if ((i & 8) != 0) {
            list = Collections.EMPTY_LIST;
            list.getClass();
        }
        this(str, str2, str3, list, d, (i & 32) != 0 ? null : bool);
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

    public final double getValueAdjustment() {
        return this.valueAdjustment;
    }

    public final Boolean getRequestInitiatedWhileUnlocked() {
        return this.requestInitiatedWhileUnlocked;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OffsetNumericDeviceStateItemByValueParams)) {
            return false;
        }
        OffsetNumericDeviceStateItemByValueParams offsetNumericDeviceStateItemByValueParams = (OffsetNumericDeviceStateItemByValueParams) obj;
        return Intrinsics.areEqual(this.key, offsetNumericDeviceStateItemByValueParams.key) && Intrinsics.areEqual(this.itemizationKeys, offsetNumericDeviceStateItemByValueParams.itemizationKeys) && this.valueAdjustment == offsetNumericDeviceStateItemByValueParams.valueAdjustment && Intrinsics.areEqual(this.requestInitiatedWhileUnlocked, offsetNumericDeviceStateItemByValueParams.requestInitiatedWhileUnlocked);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.itemizationKeys, Double.valueOf(this.valueAdjustment), this.requestInitiatedWhileUnlocked);
    }
}
