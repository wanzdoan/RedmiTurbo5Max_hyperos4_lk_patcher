package com.google.android.appfunctions.schema.common.v1.devicestate;

import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DeviceState.kt */
/* JADX INFO: loaded from: classes.dex */
public final class LocalizedString {
    private final String english;
    private final String id;
    private final String localized;
    private final String namespace;

    public LocalizedString(String str, String str2, String str3, String str4) {
        str.getClass();
        str2.getClass();
        str3.getClass();
        this.namespace = str;
        this.id = str2;
        this.english = str3;
        this.localized = str4;
    }

    public /* synthetic */ LocalizedString(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, str3, (i & 8) != 0 ? null : str4);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final String getEnglish() {
        return this.english;
    }

    public final String getLocalized() {
        return this.localized;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LocalizedString)) {
            return false;
        }
        LocalizedString localizedString = (LocalizedString) obj;
        return Intrinsics.areEqual(this.english, localizedString.english) && Intrinsics.areEqual(this.localized, localizedString.localized);
    }

    public int hashCode() {
        return Objects.hash(this.english, this.localized);
    }
}
