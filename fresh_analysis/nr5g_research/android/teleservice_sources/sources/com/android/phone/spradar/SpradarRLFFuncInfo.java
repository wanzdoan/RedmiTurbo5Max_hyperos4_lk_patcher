package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarRLFFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarRLFFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarRLFFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarRLFFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarRLFFuncInfo[] newArray(int i) {
            return new SpradarRLFFuncInfo[i];
        }
    };
    private int cause;
    private int phoneId;
    private long timeStamp;
    private int ts;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 33033;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarRLFFuncInfo() {
        this.timeStamp = 0L;
        this.phoneId = -1;
    }

    public SpradarRLFFuncInfo(Parcel parcel) {
        this.timeStamp = 0L;
        this.phoneId = -1;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.ts = parcel.readInt();
        this.cause = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeStamp);
        parcel.writeInt(this.phoneId);
        parcel.writeInt(this.ts);
        parcel.writeInt(this.cause);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarRLFFuncInfo spradarRLFFuncInfo = new SpradarRLFFuncInfo();
        spradarRLFFuncInfo.timeStamp = System.currentTimeMillis();
        spradarRLFFuncInfo.phoneId = i;
        spradarRLFFuncInfo.ts = jSONObject.optInt("ts", -1);
        spradarRLFFuncInfo.cause = jSONObject.optInt("cause", -1);
        return spradarRLFFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " RLF_EXCEPTION,33033 ts:" + this.ts + " cause:" + this.cause;
    }
}
