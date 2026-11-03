package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarLTERegisterRejectExceptionFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarLTERegisterRejectExceptionFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarLTERegisterRejectExceptionFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarLTERegisterRejectExceptionFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarLTERegisterRejectExceptionFuncInfo[] newArray(int i) {
            return new SpradarLTERegisterRejectExceptionFuncInfo[i];
        }
    };
    private int arfcn;
    private int band;
    private int cellId;
    private String domain;
    private int emmCause;
    private String homeMcc;
    private String homeMnc;
    private int isDataSim;
    private String mcc;
    private int mmCause;
    private String mnc;
    private int phoneId;
    private String rat;
    private int res1;
    private int res2;
    private int roaming;
    private int signalQuality;
    private int signalStrength;
    private int sipCause;
    private int subType;
    private int tac;
    private String time;
    private int type;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 10006;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarLTERegisterRejectExceptionFuncInfo() {
        this.phoneId = -1;
    }

    public SpradarLTERegisterRejectExceptionFuncInfo(Parcel parcel) {
        this.phoneId = -1;
        this.roaming = parcel.readInt();
        this.mcc = parcel.readString();
        this.mnc = parcel.readString();
        this.homeMcc = parcel.readString();
        this.homeMnc = parcel.readString();
        this.time = parcel.readString();
        this.tac = parcel.readInt();
        this.cellId = parcel.readInt();
        this.band = parcel.readInt();
        this.arfcn = parcel.readInt();
        this.type = parcel.readInt();
        this.subType = parcel.readInt();
        this.rat = parcel.readString();
        this.domain = parcel.readString();
        this.mmCause = parcel.readInt();
        this.emmCause = parcel.readInt();
        this.sipCause = parcel.readInt();
        this.isDataSim = parcel.readInt();
        this.signalStrength = parcel.readInt();
        this.signalQuality = parcel.readInt();
        this.res1 = parcel.readInt();
        this.res2 = parcel.readInt();
        this.phoneId = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.roaming);
        parcel.writeString(this.mcc);
        parcel.writeString(this.mnc);
        parcel.writeString(this.homeMcc);
        parcel.writeString(this.homeMnc);
        parcel.writeString(this.time);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.cellId);
        parcel.writeInt(this.band);
        parcel.writeInt(this.arfcn);
        parcel.writeInt(this.type);
        parcel.writeInt(this.subType);
        parcel.writeString(this.rat);
        parcel.writeString(this.domain);
        parcel.writeInt(this.mmCause);
        parcel.writeInt(this.emmCause);
        parcel.writeInt(this.sipCause);
        parcel.writeInt(this.isDataSim);
        parcel.writeInt(this.signalStrength);
        parcel.writeInt(this.signalQuality);
        parcel.writeInt(this.res1);
        parcel.writeInt(this.res2);
        parcel.writeInt(this.phoneId);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarLTERegisterRejectExceptionFuncInfo spradarLTERegisterRejectExceptionFuncInfo = new SpradarLTERegisterRejectExceptionFuncInfo();
        spradarLTERegisterRejectExceptionFuncInfo.phoneId = i;
        spradarLTERegisterRejectExceptionFuncInfo.roaming = jSONObject.optInt("roaming", -1);
        spradarLTERegisterRejectExceptionFuncInfo.mcc = jSONObject.optString("mcc", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.mnc = jSONObject.optString("mnc", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.homeMcc = jSONObject.optString("home_mcc", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.homeMnc = jSONObject.optString("home_mnc", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.time = jSONObject.optString("TIME", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarLTERegisterRejectExceptionFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarLTERegisterRejectExceptionFuncInfo.band = jSONObject.optInt("band", -1);
        spradarLTERegisterRejectExceptionFuncInfo.arfcn = jSONObject.optInt("ARFCN", -1);
        spradarLTERegisterRejectExceptionFuncInfo.type = jSONObject.optInt("type", -1);
        spradarLTERegisterRejectExceptionFuncInfo.subType = jSONObject.optInt("subtype", -1);
        spradarLTERegisterRejectExceptionFuncInfo.rat = jSONObject.optString("RAT", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.domain = jSONObject.optString("domain", "-1");
        spradarLTERegisterRejectExceptionFuncInfo.mmCause = jSONObject.optInt("mm_cause", -1);
        spradarLTERegisterRejectExceptionFuncInfo.emmCause = jSONObject.optInt("emm_cause", -1);
        spradarLTERegisterRejectExceptionFuncInfo.sipCause = jSONObject.optInt("sip_cause", -1);
        spradarLTERegisterRejectExceptionFuncInfo.isDataSim = jSONObject.optInt("is_data_sim", -1);
        spradarLTERegisterRejectExceptionFuncInfo.signalStrength = jSONObject.optInt("signalstrength", -1);
        spradarLTERegisterRejectExceptionFuncInfo.signalQuality = jSONObject.optInt("signalquality", -1);
        spradarLTERegisterRejectExceptionFuncInfo.res1 = jSONObject.optInt("res_1", -1);
        spradarLTERegisterRejectExceptionFuncInfo.res2 = jSONObject.optInt("res_2", -1);
        return spradarLTERegisterRejectExceptionFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.time) + " slot:" + this.phoneId + " REGISTER_EXCEPTION,10006 roaming:" + this.roaming + " mcc:" + this.mcc + " mnc:" + this.mnc + " homeMcc:" + this.homeMcc + " homeMnc:" + this.homeMnc + " tac:" + this.tac + " cellId:" + this.cellId + " band:" + this.band + " arfcn:" + this.arfcn + " type:" + this.type + " subType:" + this.subType + " rat:" + this.rat + " domain:" + this.domain + " mmCause:" + this.mmCause + " emmCause:" + this.emmCause + " sipCause:" + this.sipCause + " isDataSim:" + this.isDataSim + " signalStrength:" + this.signalStrength + " signalQuality:" + this.signalQuality + " res1:" + this.res1 + " res2:" + this.res2;
    }
}
