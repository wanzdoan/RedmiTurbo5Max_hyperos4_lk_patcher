package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateSetters.kt */
/* JADX INFO: loaded from: classes.dex */
public final class AdjustNumericDeviceStateItemByPercentageParams {
    private final String id;
    private final List itemizationKeys;
    private final String key;
    private final String namespace;
    private final int percentageAdjustment;
    private final Boolean requestInitiatedWhileUnlocked;

    public AdjustNumericDeviceStateItemByPercentageParams(String str, String str2, String str3, List list, int i, Boolean bool) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.itemizationKeys = list;
        this.percentageAdjustment = i;
        this.requestInitiatedWhileUnlocked = bool;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ AdjustNumericDeviceStateItemByPercentageParams(String str, String str2, String str3, List list, int i, Boolean bool, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i2 & 1) != 0 ? "" : str;
        str2 = (i2 & 2) != 0 ? "" : str2;
        if ((i2 & 8) != 0) {
            list = Collections.EMPTY_LIST;
            list.getClass();
        }
        this(str, str2, str3, list, i, (i2 & 32) != 0 ? null : bool);
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

    public final int getPercentageAdjustment() {
        return this.percentageAdjustment;
    }

    public final Boolean getRequestInitiatedWhileUnlocked() {
        return this.requestInitiatedWhileUnlocked;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AdjustNumericDeviceStateItemByPercentageParams)) {
            return false;
        }
        AdjustNumericDeviceStateItemByPercentageParams adjustNumericDeviceStateItemByPercentageParams = (AdjustNumericDeviceStateItemByPercentageParams) obj;
        return Intrinsics.areEqual(this.key, adjustNumericDeviceStateItemByPercentageParams.key) && Intrinsics.areEqual(this.itemizationKeys, adjustNumericDeviceStateItemByPercentageParams.itemizationKeys) && this.percentageAdjustment == adjustNumericDeviceStateItemByPercentageParams.percentageAdjustment && Intrinsics.areEqual(this.requestInitiatedWhileUnlocked, adjustNumericDeviceStateItemByPercentageParams.requestInitiatedWhileUnlocked);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.itemizationKeys, Integer.valueOf(this.percentageAdjustment), this.requestInitiatedWhileUnlocked);
    }
}
