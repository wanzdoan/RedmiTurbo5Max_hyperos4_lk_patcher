package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateMetadataResponse {
    private final String deviceLocale;
    private final String globalHintText;
    private final String id;
    private final List itemizationTypes;
    private final String namespace;
    private final List perScreenMetadata;

    public DeviceStateMetadataResponse(String str, String str2, List list, String str3, String str4, List list2) {
        str.getClass();
        str2.getClass();
        list.getClass();
        str3.getClass();
        list2.getClass();
        this.namespace = str;
        this.id = str2;
        this.perScreenMetadata = list;
        this.deviceLocale = str3;
        this.globalHintText = str4;
        this.itemizationTypes = list2;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ DeviceStateMetadataResponse(String str, String str2, List list, String str3, String str4, List list2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i & 1) != 0 ? "" : str;
        str2 = (i & 2) != 0 ? "" : str2;
        if ((i & 4) != 0) {
            list = Collections.EMPTY_LIST;
            list.getClass();
        }
        str4 = (i & 16) != 0 ? null : str4;
        if ((i & 32) != 0) {
            list2 = Collections.EMPTY_LIST;
            list2.getClass();
        }
        this(str, str2, list, str3, str4, list2);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final List getPerScreenMetadata() {
        return this.perScreenMetadata;
    }

    public final String getDeviceLocale() {
        return this.deviceLocale;
    }

    public final String getGlobalHintText() {
        return this.globalHintText;
    }

    public final List getItemizationTypes() {
        return this.itemizationTypes;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceStateMetadataResponse)) {
            return false;
        }
        DeviceStateMetadataResponse deviceStateMetadataResponse = (DeviceStateMetadataResponse) obj;
        return Intrinsics.areEqual(this.perScreenMetadata, deviceStateMetadataResponse.perScreenMetadata) && Intrinsics.areEqual(this.deviceLocale, deviceStateMetadataResponse.deviceLocale) && Intrinsics.areEqual(this.globalHintText, deviceStateMetadataResponse.globalHintText) && Intrinsics.areEqual(this.itemizationTypes, deviceStateMetadataResponse.itemizationTypes);
    }

    public int hashCode() {
        return Objects.hash(this.perScreenMetadata, this.deviceLocale, this.globalHintText, this.itemizationTypes);
    }
}
