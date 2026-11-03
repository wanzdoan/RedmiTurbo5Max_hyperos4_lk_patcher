package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
public final class PerScreenMetadata {
    private final String description;
    private final List deviceStateItemsMetadata;
    private final String id;
    private final String intentUri;
    private final List itemizationTypes;
    private final String namespace;

    public PerScreenMetadata(String str, String str2, String str3, String str4, List list, List list2) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        list2.getClass();
        this.namespace = str;
        this.id = str2;
        this.description = str3;
        this.intentUri = str4;
        this.itemizationTypes = list;
        this.deviceStateItemsMetadata = list2;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ PerScreenMetadata(String str, String str2, String str3, String str4, List list, List list2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i & 1) != 0 ? "" : str;
        str2 = (i & 2) != 0 ? "" : str2;
        str4 = (i & 8) != 0 ? null : str4;
        if ((i & 16) != 0) {
            list = Collections.EMPTY_LIST;
            list.getClass();
        }
        if ((i & 32) != 0) {
            list2 = Collections.EMPTY_LIST;
            list2.getClass();
        }
        this(str, str2, str3, str4, list, list2);
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

    public final List getItemizationTypes() {
        return this.itemizationTypes;
    }

    public final List getDeviceStateItemsMetadata() {
        return this.deviceStateItemsMetadata;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PerScreenMetadata)) {
            return false;
        }
        PerScreenMetadata perScreenMetadata = (PerScreenMetadata) obj;
        return Intrinsics.areEqual(this.description, perScreenMetadata.description) && Intrinsics.areEqual(this.intentUri, perScreenMetadata.intentUri) && Intrinsics.areEqual(this.itemizationTypes, perScreenMetadata.itemizationTypes) && Intrinsics.areEqual(this.deviceStateItemsMetadata, perScreenMetadata.deviceStateItemsMetadata);
    }

    public int hashCode() {
        return Objects.hash(this.description, this.intentUri, this.itemizationTypes, this.deviceStateItemsMetadata);
    }
}
