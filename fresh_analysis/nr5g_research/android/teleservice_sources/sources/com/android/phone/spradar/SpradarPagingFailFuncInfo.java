package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarPagingFailFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarPagingFailFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarPagingFailFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarPagingFailFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarPagingFailFuncInfo[] newArray(int i) {
            return new SpradarPagingFailFuncInfo[i];
        }
    };
    private int band;
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
        return 20005;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarPagingFailFuncInfo() {
        this.timeStamp = 0L;
        this.phoneId = -1;
    }

    public SpradarPagingFailFuncInfo(Parcel parcel) {
        this.timeStamp = 0L;
        this.phoneId = -1;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.time = parcel.readString();
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
        SpradarPagingFailFuncInfo spradarPagingFailFuncInfo = new SpradarPagingFailFuncInfo();
        spradarPagingFailFuncInfo.timeStamp = System.currentTimeMillis();
        spradarPagingFailFuncInfo.phoneId = i;
        spradarPagingFailFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarPagingFailFuncInfo.plmn = jSONObject.optString("PLMN", "-1");
        spradarPagingFailFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarPagingFailFuncInfo.earfcn = jSONObject.optInt("EARFCN", -1);
        spradarPagingFailFuncInfo.band = jSONObject.optInt("band", -1);
        spradarPagingFailFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarPagingFailFuncInfo.pci = jSONObject.optInt("PCI", -1);
        spradarPagingFailFuncInfo.rsrp = jSONObject.optInt("RSRP", -1);
        spradarPagingFailFuncInfo.rsrq = jSONObject.optInt("RSRQ", -1);
        spradarPagingFailFuncInfo.sinr = jSONObject.optInt("SINR", -1);
        spradarPagingFailFuncInfo.successCount = jSONObject.optInt("success_count", -1);
        spradarPagingFailFuncInfo.failCount = jSONObject.optInt("fail_count", -1);
        return spradarPagingFailFuncInfo;
    }

    public String toString() {
        if (this.successCount != -1 && this.failCount != -1) {
            return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " PAGING_FAILED,20005 successCount:" + this.successCount + " failCount:" + this.failCount;
        }
        return SpradarFuncInfoFactory.timeLongToStringEx(this.time) + " slot:" + this.phoneId + " PAGING_FAILED,20005 plmn:" + this.plmn + " tac:" + this.tac + " earfcn:" + this.earfcn + " band:" + this.band + " cellId:" + this.cellId + " pci:" + this.pci + " rsrp:" + this.rsrp + " rsrq:" + this.rsrq + " sinr:" + this.sinr;
    }
}
