package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccSmsStorageStatus implements Parcelable {
    public static final Parcelable.Creator<MtkIccSmsStorageStatus> CREATOR = new Parcelable.Creator<MtkIccSmsStorageStatus>() { // from class: com.mediatek.internal.telephony.MtkIccSmsStorageStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkIccSmsStorageStatus createFromParcel(Parcel source) {
            int used = source.readInt();
            int total = source.readInt();
            return new MtkIccSmsStorageStatus(used, total);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkIccSmsStorageStatus[] newArray(int size) {
            return new MtkIccSmsStorageStatus[size];
        }
    };
    public int mTotal;
    public int mUsed;

    public MtkIccSmsStorageStatus() {
        this.mUsed = 0;
        this.mTotal = 0;
    }

    public MtkIccSmsStorageStatus(int used, int total) {
        this.mUsed = used;
        this.mTotal = total;
    }

    public int getUsedCount() {
        return this.mUsed;
    }

    public int getTotalCount() {
        return this.mTotal;
    }

    public int getUnused() {
        return this.mTotal - this.mUsed;
    }

    public void reset() {
        this.mUsed = 0;
        this.mTotal = 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUsed);
        dest.writeInt(this.mTotal);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("[");
        sb.append(this.mUsed);
        sb.append(", ");
        sb.append(this.mTotal);
        sb.append("]");
        return sb.toString();
    }
}
