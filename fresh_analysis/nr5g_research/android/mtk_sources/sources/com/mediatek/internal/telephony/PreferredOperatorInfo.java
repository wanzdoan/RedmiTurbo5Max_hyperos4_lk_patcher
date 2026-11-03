package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class PreferredOperatorInfo implements Parcelable {
    public static final Parcelable.Creator<PreferredOperatorInfo> CREATOR = new Parcelable.Creator<PreferredOperatorInfo>() { // from class: com.mediatek.internal.telephony.PreferredOperatorInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PreferredOperatorInfo createFromParcel(Parcel in) {
            return new PreferredOperatorInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PreferredOperatorInfo[] newArray(int size) {
            return new PreferredOperatorInfo[size];
        }
    };
    private int mAcessTech;
    private String mName;
    private String mNumeric;
    private int mPriority;

    public String getOperatorAlphaName() {
        return this.mName;
    }

    public String getOperatorNumeric() {
        return this.mNumeric;
    }

    public int getAccessTechnology() {
        return this.mAcessTech;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setOperatorAlphaName(String mName) {
        this.mName = mName;
    }

    public void setOperatorNumeric(String mNumeric) {
        this.mNumeric = mNumeric;
    }

    public void setAccessTechnology(int mAcessTech) {
        this.mAcessTech = mAcessTech;
    }

    public void setPriority(int nIndex) {
        this.mPriority = nIndex;
    }

    public PreferredOperatorInfo(String operatorAlphaLong, String mNumeric, int mAcessTech, int mPriority) {
        this.mName = operatorAlphaLong;
        this.mNumeric = mNumeric;
        this.mAcessTech = mAcessTech;
        this.mPriority = mPriority;
    }

    private PreferredOperatorInfo(Parcel in) {
        this.mName = in.readString();
        this.mNumeric = in.readString();
        this.mAcessTech = in.readInt();
        this.mPriority = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mNumeric);
        dest.writeInt(this.mAcessTech);
        dest.writeInt(this.mPriority);
    }

    public String toString() {
        return "PreferredOperatorInfo: mName: " + this.mName + ", mNumeric: " + this.mNumeric + ", mAcessTech: " + this.mAcessTech + ", mPriority: " + this.mPriority + ")";
    }
}
