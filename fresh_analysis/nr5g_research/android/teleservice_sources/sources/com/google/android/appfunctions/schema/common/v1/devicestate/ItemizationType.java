package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceStateMetadata.kt */
/* JADX INFO: loaded from: classes.dex */
public final class ItemizationType {
    private final String hintText;
    private final String id;
    private final String key;
    private final String namespace;
    private final List values;

    public ItemizationType(String str, String str2, String str3, List list, String str4) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        list.getClass();
        this.namespace = str;
        this.id = str2;
        this.key = str3;
        this.values = list;
        this.hintText = str4;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ ItemizationType(String str, String str2, String str3, List list, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i & 1) != 0 ? "" : str;
        str2 = (i & 2) != 0 ? "" : str2;
        if ((i & 8) != 0) {
            list = Collections.EMPTY_LIST;
            list.getClass();
        }
        this(str, str2, str3, list, (i & 16) != 0 ? null : str4);
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

    public final List getValues() {
        return this.values;
    }

    public final String getHintText() {
        return this.hintText;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ItemizationType)) {
            return false;
        }
        ItemizationType itemizationType = (ItemizationType) obj;
        return Intrinsics.areEqual(this.key, itemizationType.key) && Intrinsics.areEqual(this.values, itemizationType.values) && Intrinsics.areEqual(this.hintText, itemizationType.hintText);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.values, this.hintText);
    }
}
