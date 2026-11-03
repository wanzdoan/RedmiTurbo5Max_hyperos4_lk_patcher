package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarLTERACHFailFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarLTERACHFailFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarLTERACHFailFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarLTERACHFailFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarLTERACHFailFuncInfo[] newArray(int i) {
            return new SpradarLTERACHFailFuncInfo[i];
        }
    };
    private int band;
    private int cause;
    private int cellId;
    private int earfcn;
    private int failCount;
    private int pci;
    private int phoneId;
    private String plmn;
    private int rsrp;
    private int rsrq;
    private int sinr;
    private int successCount;
    private int tac;
    private String time;
    private long timeStamp;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 20004;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarLTERACHFailFuncInfo() {
        this.phoneId = -1;
        this.timeStamp = 0L;
    }

    public SpradarLTERACHFailFuncInfo(Parcel parcel) {
        this.phoneId = -1;
        this.timeStamp = 0L;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.time = parcel.readString();
        this.cause = parcel.readInt();
        this.plmn = parcel.readString();
        this.tac = parcel.readInt();
        this.earfcn = parcel.readInt();
        this.band = parcel.readInt();
        this.cellId = parcel.readInt();
        this.pci = parcel.readInt();
        this.rsrp = parcel.readInt();
        this.rsrq = parcel.readInt();
        this.sinr = parcel.readInt();
        this.successCount = parcel.readInt();
        this.failCount = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeStamp);
        parcel.writeInt(this.phoneId);
        parcel.writeString(this.time);
        parcel.writeInt(this.cause);
        parcel.writeString(this.plmn);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.earfcn);
        parcel.writeInt(this.band);
        parcel.writeInt(this.cellId);
        parcel.writeInt(this.pci);
        parcel.writeInt(this.rsrp);
        parcel.writeInt(this.rsrq);
        parcel.writeInt(this.sinr);
        parcel.writeInt(this.successCount);
        parcel.writeInt(this.failCount);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarLTERACHFailFuncInfo spradarLTERACHFailFuncInfo = new SpradarLTERACHFailFuncInfo();
        spradarLTERACHFailFuncInfo.timeStamp = System.currentTimeMillis();
        spradarLTERACHFailFuncInfo.phoneId = i;
        spradarLTERACHFailFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarLTERACHFailFuncInfo.cause = jSONObject.optInt("cause", -1);
        spradarLTERACHFailFuncInfo.plmn = jSONObject.optString("plmn", "-1");
        spradarLTERACHFailFuncInfo.tac = jSONObject.optInt("tac", -1);
        spradarLTERACHFailFuncInfo.earfcn = jSONObject.optInt("earfcn", -1);
        spradarLTERACHFailFuncInfo.band = jSONObject.optInt("band", -1);
        spradarLTERACHFailFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarLTERACHFailFuncInfo.pci = jSONObject.optInt("pci", -1);
        spradarLTERACHFailFuncInfo.rsrp = jSONObject.optInt("rsrp", -1);
        spradarLTERACHFailFuncInfo.rsrq = jSONObject.optInt("rsrq", -1);
        spradarLTERACHFailFuncInfo.sinr = jSONObject.optInt("SINR", -1);
        spradarLTERACHFailFuncInfo.successCount = jSONObject.optInt("success_count", -1);
        spradarLTERACHFailFuncInfo.failCount = jSONObject.optInt("fail_count", -1);
        return spradarLTERACHFailFuncInfo;
    }

    public String toString() {
        if (this.successCount != -1 && this.failCount != -1) {
            return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " RACH_FAILED,20004 successCount:" + this.successCount + " failCount:" + this.failCount;
        }
        return SpradarFuncInfoFactory.timeLongToStringEx(this.time) + " slot:" + this.phoneId + " RACH_FAILED,20004 plmn:" + this.plmn + " tac:" + this.tac + " earfcn:" + this.earfcn + " band:" + this.band + " cellId:" + this.cellId + " pci:" + this.pci + " rsrp:" + this.rsrp + " rsrq:" + this.rsrq + " sinr:" + this.sinr;
    }
}
