package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarCallExceptionReportFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarCallExceptionReportFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarCallExceptionReportFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarCallExceptionReportFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarCallExceptionReportFuncInfo[] newArray(int i) {
            return new SpradarCallExceptionReportFuncInfo[i];
        }
    };
    private int arfcn;
    private int band;
    private int ccCause;
    private int cellId;
    private String direction;
    private int emmCause;
    private int isEmergency;
    private String mcc;
    private int mmCause;
    private String mnc;
    private int phoneId;
    private String rat;
    private int signalQuality;
    private int signalStrength;
    private int sipCause;
    private String subType;
    private int tac;
    private String time;
    private long timeStamp;
    private String type;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 10004;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarCallExceptionReportFuncInfo() {
        this.phoneId = -1;
        this.timeStamp = 0L;
    }

    public SpradarCallExceptionReportFuncInfo(Parcel parcel) {
        this.phoneId = -1;
        this.timeStamp = 0L;
        this.time = parcel.readString();
        this.mcc = parcel.readString();
        this.mnc = parcel.readString();
        this.tac = parcel.readInt();
        this.cellId = parcel.readInt();
        this.rat = parcel.readString();
        this.band = parcel.readInt();
        this.arfcn = parcel.readInt();
        this.direction = parcel.readString();
        this.type = parcel.readString();
        this.subType = parcel.readString();
        this.isEmergency = parcel.readInt();
        this.mmCause = parcel.readInt();
        this.emmCause = parcel.readInt();
        this.ccCause = parcel.readInt();
        this.sipCause = parcel.readInt();
        this.signalStrength = parcel.readInt();
        this.signalQuality = parcel.readInt();
        this.phoneId = parcel.readInt();
        this.timeStamp = parcel.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.time);
        parcel.writeString(this.mcc);
        parcel.writeString(this.mnc);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.cellId);
        parcel.writeString(this.rat);
        parcel.writeInt(this.band);
        parcel.writeInt(this.arfcn);
        parcel.writeString(this.direction);
        parcel.writeString(this.type);
        parcel.writeString(this.subType);
        parcel.writeInt(this.isEmergency);
        parcel.writeInt(this.mmCause);
        parcel.writeInt(this.emmCause);
        parcel.writeInt(this.ccCause);
        parcel.writeInt(this.sipCause);
        parcel.writeInt(this.signalStrength);
        parcel.writeInt(this.signalQuality);
        parcel.writeInt(this.phoneId);
        parcel.writeLong(this.timeStamp);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarCallExceptionReportFuncInfo spradarCallExceptionReportFuncInfo = new SpradarCallExceptionReportFuncInfo();
        spradarCallExceptionReportFuncInfo.timeStamp = System.currentTimeMillis();
        spradarCallExceptionReportFuncInfo.phoneId = i;
        spradarCallExceptionReportFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarCallExceptionReportFuncInfo.mcc = jSONObject.optString("mcc", "-1");
        spradarCallExceptionReportFuncInfo.mnc = jSONObject.optString("mnc", "-1");
        spradarCallExceptionReportFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarCallExceptionReportFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarCallExceptionReportFuncInfo.rat = jSONObject.optString("RAT", "-1");
        spradarCallExceptionReportFuncInfo.band = jSONObject.optInt("band", -1);
        spradarCallExceptionReportFuncInfo.arfcn = jSONObject.optInt("ARFCN", -1);
        spradarCallExceptionReportFuncInfo.direction = jSONObject.optString("direction", "-1");
        spradarCallExceptionReportFuncInfo.type = jSONObject.optString("type", "-1");
        spradarCallExceptionReportFuncInfo.subType = jSONObject.optString("subtype", "-1");
        spradarCallExceptionReportFuncInfo.isEmergency = jSONObject.optInt("is_emergency", -1);
        spradarCallExceptionReportFuncInfo.mmCause = jSONObject.optInt("mm_cause", -1);
        spradarCallExceptionReportFuncInfo.emmCause = jSONObject.optInt("emm_cause", -1);
        spradarCallExceptionReportFuncInfo.ccCause = jSONObject.optInt("cc_cause", -1);
        spradarCallExceptionReportFuncInfo.sipCause = jSONObject.optInt("sip_cause", -1);
        spradarCallExceptionReportFuncInfo.signalStrength = jSONObject.optInt("signalstrength", -1);
        spradarCallExceptionReportFuncInfo.signalQuality = jSONObject.optInt("signalquality", -1);
        return spradarCallExceptionReportFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.time) + " slot:" + this.phoneId + " CALL_EXCEPTION,10004 mcc:" + this.mcc + " mnc:" + this.mnc + " tac:" + this.tac + " cellId:" + this.cellId + " rat:" + this.rat + " band:" + this.band + " arfcn:" + this.arfcn + " direction:" + this.direction + " type:" + this.type + " subType:" + this.subType + " isEmergency:" + this.isEmergency + " mmCause:" + this.mmCause + " emmCause:" + this.emmCause + " ccCause:" + this.ccCause + " sipCause:" + this.sipCause + " signalStrength:" + this.signalStrength + " signalQuality:" + this.signalQuality;
    }
}
