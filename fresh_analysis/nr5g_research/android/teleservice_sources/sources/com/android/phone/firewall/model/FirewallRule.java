package com.android.phone.firewall.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public enum FirewallRule implements Parcelable {
    Init(-1),
    Allow(0),
    Restrict(1),
    Alert(2);

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.firewall.model.FirewallRule.1
        @Override // android.os.Parcelable.Creator
        public FirewallRule createFromParcel(Parcel parcel) {
            return FirewallRule.parse(parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        public FirewallRule[] newArray(int i) {
            return new FirewallRule[i];
        }
    };
    private final int value;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    FirewallRule(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static FirewallRule parse(int i) {
        if (i == -1) {
            return Init;
        }
        if (i == 0) {
            return Allow;
        }
        if (i == 1) {
            return Restrict;
        }
        if (i == 2) {
            return Alert;
        }
        return Allow;
    }

    @Override // java.lang.Enum
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.value);
    }
}
