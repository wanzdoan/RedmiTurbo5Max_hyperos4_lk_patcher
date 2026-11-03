package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarDataExceptionReportFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarDataExceptionReportFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarDataExceptionReportFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarDataExceptionReportFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarDataExceptionReportFuncInfo[] newArray(int i) {
            return new SpradarDataExceptionReportFuncInfo[i];
        }
    };
    private String apn;
    private int apnType;
    private int cellId;
    private int contextType;
    private int initiatedBy;
    private String mcc;
    private String mnc;
    private int nsapi;
    private int pdpType;
    private int phoneId;
    private String rat;
    private int sduSize;
    private int signalQuality;
    private int signalStrength;
    private int smCause;
    private int tac;
    private long timeStamp;
    private int type;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 10007;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarDataExceptionReportFuncInfo() {
        this.phoneId = -1;
        this.timeStamp = 0L;
    }

    public SpradarDataExceptionReportFuncInfo(Parcel parcel) {
        this.phoneId = -1;
        this.timeStamp = 0L;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.mcc = parcel.readString();
        this.mnc = parcel.readString();
        this.tac = parcel.readInt();
        this.cellId = parcel.readInt();
        this.type = parcel.readInt();
        this.rat = parcel.readString();
        this.smCause = parcel.readInt();
        this.apn = parcel.readString();
        this.pdpType = parcel.readInt();
        this.apnType = parcel.readInt();
        this.nsapi = parcel.readInt();
        this.contextType = parcel.readInt();
        this.initiatedBy = parcel.readInt();
        this.sduSize = parcel.readInt();
        this.signalStrength = parcel.readInt();
        this.signalQuality = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeStamp);
        parcel.writeInt(this.phoneId);
        parcel.writeString(this.mcc);
        parcel.writeString(this.mnc);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.cellId);
        parcel.writeInt(this.type);
        parcel.writeString(this.rat);
        parcel.writeInt(this.smCause);
        parcel.writeString(this.apn);
        parcel.writeInt(this.pdpType);
        parcel.writeInt(this.apnType);
        parcel.writeInt(this.nsapi);
        parcel.writeInt(this.contextType);
        parcel.writeInt(this.initiatedBy);
        parcel.writeInt(this.sduSize);
        parcel.writeInt(this.signalStrength);
        parcel.writeInt(this.signalQuality);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarDataExceptionReportFuncInfo spradarDataExceptionReportFuncInfo = new SpradarDataExceptionReportFuncInfo();
        spradarDataExceptionReportFuncInfo.timeStamp = System.currentTimeMillis();
        spradarDataExceptionReportFuncInfo.phoneId = i;
        spradarDataExceptionReportFuncInfo.mcc = jSONObject.optString("mcc", "-1");
        spradarDataExceptionReportFuncInfo.mnc = jSONObject.optString("mnc", "-1");
        spradarDataExceptionReportFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarDataExceptionReportFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarDataExceptionReportFuncInfo.type = jSONObject.optInt("type", -1);
        spradarDataExceptionReportFuncInfo.rat = jSONObject.optString("RAT", "-1");
        spradarDataExceptionReportFuncInfo.smCause = jSONObject.optInt("sm_cause", -1);
        spradarDataExceptionReportFuncInfo.apn = jSONObject.optString("apn", "-1");
        spradarDataExceptionReportFuncInfo.pdpType = jSONObject.optInt("pdp_type", -1);
        spradarDataExceptionReportFuncInfo.apnType = jSONObject.optInt("apn_type", -1);
        spradarDataExceptionReportFuncInfo.nsapi = jSONObject.optInt("nsapi", -1);
        spradarDataExceptionReportFuncInfo.contextType = jSONObject.optInt("context_type", -1);
        spradarDataExceptionReportFuncInfo.initiatedBy = jSONObject.optInt("initiated_by", -1);
        spradarDataExceptionReportFuncInfo.sduSize = jSONObject.optInt("sdu_size", -1);
        spradarDataExceptionReportFuncInfo.signalStrength = jSONObject.optInt("signalstrength", -1);
        spradarDataExceptionReportFuncInfo.signalQuality = jSONObject.optInt("signalquality", -1);
        return spradarDataExceptionReportFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " DATA_EXCEPTION,10007 mcc:" + this.mcc + " mnc:" + this.mnc + " tac:" + this.tac + " cellId:" + this.cellId + " type:" + this.type + " rat:" + this.rat + " smCause:" + this.smCause + " apn:" + this.apn + " pdpType:" + this.pdpType + " apnType:" + this.apnType + " nsapi:" + this.nsapi + " contextType:" + this.contextType + " initiatedBy:" + this.initiatedBy + " sduSize:" + this.sduSize + " signalStrength:" + this.signalStrength + " signalQuality:" + this.signalQuality;
    }
}
