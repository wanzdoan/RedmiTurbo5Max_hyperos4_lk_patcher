package com.google.android.appfunctions.schema.common.v1.devicestate;

import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateItem {
    private final String hintText;
    private final String id;
    private final String jsonValue;
    private final String key;
    private final Long lastUpdatedEpochMillis;
    private final LocalizedString name;
    private final String namespace;
    private final String purpose;

    public DeviceStateItem(String str, String str2, String str3, String str4, LocalizedString localizedString, String str5, Long l, String str6) {
        str.getClass();
        str2.getClass();
        str4.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.purpose = str4;
        this.name = localizedString;
        this.jsonValue = str5;
        this.lastUpdatedEpochMillis = l;
        this.hintText = str6;
    }

    public /* synthetic */ DeviceStateItem(String str, String str2, String str3, String str4, LocalizedString localizedString, String str5, Long l, String str6, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? null : str3, str4, (i & 16) != 0 ? null : localizedString, (i & 32) != 0 ? null : str5, (i & 64) != 0 ? null : l, (i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) != 0 ? null : str6);
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

    public final String getPurpose() {
        return this.purpose;
    }

    public final LocalizedString getName() {
        return this.name;
    }

    public final String getJsonValue() {
        return this.jsonValue;
    }

    public final Long getLastUpdatedEpochMillis() {
        return this.lastUpdatedEpochMillis;
    }

    public final String getHintText() {
        return this.hintText;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceStateItem)) {
            return false;
        }
        DeviceStateItem deviceStateItem = (DeviceStateItem) obj;
        return Intrinsics.areEqual(this.key, deviceStateItem.key) && Intrinsics.areEqual(this.purpose, deviceStateItem.purpose) && Intrinsics.areEqual(this.name, deviceStateItem.name) && Intrinsics.areEqual(this.jsonValue, deviceStateItem.jsonValue) && Intrinsics.areEqual(this.lastUpdatedEpochMillis, deviceStateItem.lastUpdatedEpochMillis) && Intrinsics.areEqual(this.hintText, deviceStateItem.hintText);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.purpose, this.name, this.jsonValue, this.lastUpdatedEpochMillis, this.hintText);
    }
}
