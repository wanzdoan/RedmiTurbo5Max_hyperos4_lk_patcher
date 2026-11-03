package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarLTERRCRecreateFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarLTERRCRecreateFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarLTERRCRecreateFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarLTERRCRecreateFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarLTERRCRecreateFuncInfo[] newArray(int i) {
            return new SpradarLTERRCRecreateFuncInfo[i];
        }
    };
    private int band;
    private int cause;
    private int cellId;
    private int earfcn;
    private int pci;
    private int phoneId;
    private String plmn;
    private int rsrp;
    private int rsrq;
    private int sinr;
    private int tac;
    private String time;
    private long timeStamp;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 20003;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarLTERRCRecreateFuncInfo() {
        this.timeStamp = 0L;
        this.phoneId = -1;
    }

    public SpradarLTERRCRecreateFuncInfo(Parcel parcel) {
        this.timeStamp = 0L;
        this.phoneId = -1;
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
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarLTERRCRecreateFuncInfo spradarLTERRCRecreateFuncInfo = new SpradarLTERRCRecreateFuncInfo();
        spradarLTERRCRecreateFuncInfo.timeStamp = System.currentTimeMillis();
        spradarLTERRCRecreateFuncInfo.phoneId = i;
        spradarLTERRCRecreateFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarLTERRCRecreateFuncInfo.cause = jSONObject.optInt("CAUSE", -1);
        spradarLTERRCRecreateFuncInfo.plmn = jSONObject.optString("PLMN", "-1");
        spradarLTERRCRecreateFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarLTERRCRecreateFuncInfo.earfcn = jSONObject.optInt("EARFCN", -1);
        spradarLTERRCRecreateFuncInfo.band = jSONObject.optInt("band", -1);
        spradarLTERRCRecreateFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarLTERRCRecreateFuncInfo.pci = jSONObject.optInt("PCI", -1);
        spradarLTERRCRecreateFuncInfo.rsrp = jSONObject.optInt("RSRP", -1);
        spradarLTERRCRecreateFuncInfo.rsrq = jSONObject.optInt("RSRQ", -1);
        spradarLTERRCRecreateFuncInfo.sinr = jSONObject.optInt("SINR", -1);
        return spradarLTERRCRecreateFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " RRC_REESTABLISH,20003 cause:" + this.cause + " plmn:" + this.plmn + " tac:" + this.tac + " earfcn:" + this.earfcn + " band:" + this.band + " cellId:" + this.cellId + " pci:" + this.pci + " rsrp:" + this.rsrp + " rsrq:" + this.rsrq + " sinr:" + this.sinr;
    }
}
