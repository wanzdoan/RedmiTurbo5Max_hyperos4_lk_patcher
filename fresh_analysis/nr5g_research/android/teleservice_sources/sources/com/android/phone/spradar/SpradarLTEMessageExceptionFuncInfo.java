package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarLTEMessageExceptionFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarLTEMessageExceptionFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarLTEMessageExceptionFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarLTEMessageExceptionFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarLTEMessageExceptionFuncInfo[] newArray(int i) {
            return new SpradarLTEMessageExceptionFuncInfo[i];
        }
    };
    private int band;
    private int cellId;
    private String direction;
    private String domain;
    private int earfcn;
    private int failCause;
    private int isRetrySms;
    private String mcc;
    private String mnc;
    private int pci;
    private int phoneId;
    private String rat;
    private int signalQuality;
    private int signalStrength;
    private int tac;
    private String time;
    private int unused1;
    private int unused2;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 10005;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarLTEMessageExceptionFuncInfo() {
        this.phoneId = -1;
    }

    public SpradarLTEMessageExceptionFuncInfo(Parcel parcel) {
        this.phoneId = -1;
        this.phoneId = parcel.readInt();
        this.time = parcel.readString();
        this.mcc = parcel.readString();
        this.mnc = parcel.readString();
        this.rat = parcel.readString();
        this.direction = parcel.readString();
        this.domain = parcel.readString();
        this.isRetrySms = parcel.readInt();
        this.failCause = parcel.readInt();
        this.tac = parcel.readInt();
        this.cellId = parcel.readInt();
        this.band = parcel.readInt();
        this.earfcn = parcel.readInt();
        this.pci = parcel.readInt();
        this.signalStrength = parcel.readInt();
        this.signalQuality = parcel.readInt();
        this.unused1 = parcel.readInt();
        this.unused2 = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.phoneId);
        parcel.writeString(this.time);
        parcel.writeString(this.mcc);
        parcel.writeString(this.mnc);
        parcel.writeString(this.rat);
        parcel.writeString(this.direction);
        parcel.writeString(this.domain);
        parcel.writeInt(this.isRetrySms);
        parcel.writeInt(this.failCause);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.cellId);
        parcel.writeInt(this.band);
        parcel.writeInt(this.earfcn);
        parcel.writeInt(this.pci);
        parcel.writeInt(this.signalStrength);
        parcel.writeInt(this.signalQuality);
        parcel.writeInt(this.unused1);
        parcel.writeInt(this.unused2);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarLTEMessageExceptionFuncInfo spradarLTEMessageExceptionFuncInfo = new SpradarLTEMessageExceptionFuncInfo();
        spradarLTEMessageExceptionFuncInfo.phoneId = i;
        spradarLTEMessageExceptionFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarLTEMessageExceptionFuncInfo.mcc = jSONObject.optString("mcc", "-1");
        spradarLTEMessageExceptionFuncInfo.mnc = jSONObject.optString("mnc", "-1");
        spradarLTEMessageExceptionFuncInfo.rat = jSONObject.optString("RAT", "-1");
        spradarLTEMessageExceptionFuncInfo.direction = jSONObject.optString("direction", "-1");
        spradarLTEMessageExceptionFuncInfo.domain = jSONObject.optString("domain", "-1");
        spradarLTEMessageExceptionFuncInfo.isRetrySms = jSONObject.optInt("is_retry_sms", -1);
        spradarLTEMessageExceptionFuncInfo.failCause = jSONObject.optInt("fail_cause", -1);
        spradarLTEMessageExceptionFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarLTEMessageExceptionFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarLTEMessageExceptionFuncInfo.band = jSONObject.optInt("band", -1);
        spradarLTEMessageExceptionFuncInfo.earfcn = jSONObject.optInt("EARFCN", -1);
        spradarLTEMessageExceptionFuncInfo.pci = jSONObject.optInt("PCI", -1);
        spradarLTEMessageExceptionFuncInfo.signalStrength = jSONObject.optInt("signalstrength", -1);
        spradarLTEMessageExceptionFuncInfo.signalQuality = jSONObject.optInt("signalquality", -1);
        spradarLTEMessageExceptionFuncInfo.unused1 = jSONObject.optInt("unused_1", -1);
        spradarLTEMessageExceptionFuncInfo.unused2 = jSONObject.optInt("unused_2", -1);
        return spradarLTEMessageExceptionFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.time) + " slot:" + this.phoneId + " MESSAGE_EXCEPTION,10005 mcc:" + this.mcc + " mnc:" + this.mnc + " rat:" + this.rat + " direction:" + this.direction + " domain:" + this.domain + " isRetrySms:" + this.isRetrySms + " failCause:" + this.failCause + " tac:" + this.tac + " cellId:" + this.cellId + " band:" + this.band + " earfcn:" + this.earfcn + " pci:" + this.pci + " signalStrength:" + this.signalStrength + " signalQuality:" + this.signalQuality + " unused1:" + this.unused1 + " unused2:" + this.unused2;
    }
}
