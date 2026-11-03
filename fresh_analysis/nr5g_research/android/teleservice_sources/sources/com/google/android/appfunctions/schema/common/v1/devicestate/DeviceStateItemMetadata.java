package com.google.android.appfunctions.schema.common.v1.devicestate;

import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateItemMetadata {
    private final String defaultValue;
    private final String hintText;
    private final String id;
    private final String key;
    private final LocalizedString name;
    private final String namespace;
    private final String possibleValues;
    private final String purpose;
    private final String sensitivity;
    private final Boolean writable;

    public DeviceStateItemMetadata(String str, String str2, String str3, String str4, LocalizedString localizedString, String str5, Boolean bool, String str6, String str7, String str8) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        str4.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.purpose = str4;
        this.name = localizedString;
        this.sensitivity = str5;
        this.writable = bool;
        this.possibleValues = str6;
        this.hintText = str7;
        this.defaultValue = str8;
    }

    public /* synthetic */ DeviceStateItemMetadata(String str, String str2, String str3, String str4, LocalizedString localizedString, String str5, Boolean bool, String str6, String str7, String str8, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, str3, str4, (i & 16) != 0 ? null : localizedString, (i & 32) != 0 ? null : str5, (i & 64) != 0 ? null : bool, (i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) != 0 ? null : str6, (i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 0 ? null : str7, (i & 512) != 0 ? null : str8);
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

    public final String getSensitivity() {
        return this.sensitivity;
    }

    public final Boolean getWritable() {
        return this.writable;
    }

    public final String getPossibleValues() {
        return this.possibleValues;
    }

    public final String getHintText() {
        return this.hintText;
    }

    public final String getDefaultValue() {
        return this.defaultValue;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceStateItemMetadata)) {
            return false;
        }
        DeviceStateItemMetadata deviceStateItemMetadata = (DeviceStateItemMetadata) obj;
        return Intrinsics.areEqual(this.key, deviceStateItemMetadata.key) && Intrinsics.areEqual(this.purpose, deviceStateItemMetadata.purpose) && Intrinsics.areEqual(this.name, deviceStateItemMetadata.name) && Intrinsics.areEqual(this.sensitivity, deviceStateItemMetadata.sensitivity) && Intrinsics.areEqual(this.writable, deviceStateItemMetadata.writable) && Intrinsics.areEqual(this.possibleValues, deviceStateItemMetadata.possibleValues) && Intrinsics.areEqual(this.hintText, deviceStateItemMetadata.hintText) && Intrinsics.areEqual(this.defaultValue, deviceStateItemMetadata.defaultValue);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.purpose, this.name, this.sensitivity, this.writable, this.possibleValues, this.hintText, this.defaultValue);
    }
}
