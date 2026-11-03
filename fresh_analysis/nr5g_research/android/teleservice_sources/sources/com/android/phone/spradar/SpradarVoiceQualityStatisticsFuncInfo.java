package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarVoiceQualityStatisticsFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarVoiceQualityStatisticsFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarVoiceQualityStatisticsFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarVoiceQualityStatisticsFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarVoiceQualityStatisticsFuncInfo[] newArray(int i) {
            return new SpradarVoiceQualityStatisticsFuncInfo[i];
        }
    };
    private int band;
    private int callId;
    private String callType;
    private int cellId;
    private String csSpeechCodec;
    private int dlRtpavNum;
    private int dlRtpbaNum;
    private int dlRtpbeNum;
    private int dlRtpgoNum;
    private int dlRtpwoNum;
    private int earfcn;
    private int pci;
    private int phoneId;
    private String plmn;
    private String rat;
    private int rsrp;
    private int rsrq;
    private int sinr;
    private String speechCodec;
    private int tac;
    private long timeStamp;
    private int ulPdcpavNum;
    private int ulPdcpbaNum;
    private int ulPdcpbeNum;
    private int ulPdcpgoNum;
    private int ulPdcpwoNum;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return 20007;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarVoiceQualityStatisticsFuncInfo() {
        this.timeStamp = 0L;
        this.phoneId = -1;
    }

    public SpradarVoiceQualityStatisticsFuncInfo(Parcel parcel) {
        this.timeStamp = 0L;
        this.phoneId = -1;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.callId = parcel.readInt();
        this.callType = parcel.readString();
        this.plmn = parcel.readString();
        this.rat = parcel.readString();
        this.tac = parcel.readInt();
        this.earfcn = parcel.readInt();
        this.band = parcel.readInt();
        this.cellId = parcel.readInt();
        this.pci = parcel.readInt();
        this.rsrp = parcel.readInt();
        this.rsrq = parcel.readInt();
        this.sinr = parcel.readInt();
        this.speechCodec = parcel.readString();
        this.dlRtpwoNum = parcel.readInt();
        this.dlRtpbaNum = parcel.readInt();
        this.dlRtpavNum = parcel.readInt();
        this.dlRtpgoNum = parcel.readInt();
        this.dlRtpbeNum = parcel.readInt();
        this.ulPdcpwoNum = parcel.readInt();
        this.ulPdcpbaNum = parcel.readInt();
        this.ulPdcpavNum = parcel.readInt();
        this.ulPdcpgoNum = parcel.readInt();
        this.ulPdcpbeNum = parcel.readInt();
        this.csSpeechCodec = parcel.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeStamp);
        parcel.writeInt(this.phoneId);
        parcel.writeInt(this.callId);
        parcel.writeString(this.callType);
        parcel.writeString(this.plmn);
        parcel.writeString(this.rat);
        parcel.writeInt(this.tac);
        parcel.writeInt(this.earfcn);
        parcel.writeInt(this.band);
        parcel.writeInt(this.cellId);
        parcel.writeInt(this.pci);
        parcel.writeInt(this.rsrp);
        parcel.writeInt(this.rsrq);
        parcel.writeInt(this.sinr);
        parcel.writeString(this.speechCodec);
        parcel.writeInt(this.dlRtpwoNum);
        parcel.writeInt(this.dlRtpbaNum);
        parcel.writeInt(this.dlRtpavNum);
        parcel.writeInt(this.dlRtpgoNum);
        parcel.writeInt(this.dlRtpbeNum);
        parcel.writeInt(this.ulPdcpwoNum);
        parcel.writeInt(this.ulPdcpbaNum);
        parcel.writeInt(this.ulPdcpavNum);
        parcel.writeInt(this.ulPdcpgoNum);
        parcel.writeInt(this.ulPdcpbeNum);
        parcel.writeString(this.csSpeechCodec);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarVoiceQualityStatisticsFuncInfo spradarVoiceQualityStatisticsFuncInfo = new SpradarVoiceQualityStatisticsFuncInfo();
        spradarVoiceQualityStatisticsFuncInfo.timeStamp = System.currentTimeMillis();
        spradarVoiceQualityStatisticsFuncInfo.phoneId = i;
        spradarVoiceQualityStatisticsFuncInfo.callId = jSONObject.optInt("callId", -1);
        spradarVoiceQualityStatisticsFuncInfo.callType = jSONObject.optString("callType", "-1");
        spradarVoiceQualityStatisticsFuncInfo.plmn = jSONObject.optString("PLMN", "-1");
        spradarVoiceQualityStatisticsFuncInfo.rat = jSONObject.optString("RAT", "-1");
        spradarVoiceQualityStatisticsFuncInfo.tac = jSONObject.optInt("TAC", -1);
        spradarVoiceQualityStatisticsFuncInfo.earfcn = jSONObject.optInt("EARFCN", -1);
        spradarVoiceQualityStatisticsFuncInfo.band = jSONObject.optInt("band", -1);
        spradarVoiceQualityStatisticsFuncInfo.cellId = jSONObject.optInt("cellId", -1);
        spradarVoiceQualityStatisticsFuncInfo.pci = jSONObject.optInt("PCI", -1);
        spradarVoiceQualityStatisticsFuncInfo.rsrp = jSONObject.optInt("RSRP", -1);
        spradarVoiceQualityStatisticsFuncInfo.rsrq = jSONObject.optInt("RSRQ", -1);
        spradarVoiceQualityStatisticsFuncInfo.sinr = jSONObject.optInt("SINR", -1);
        spradarVoiceQualityStatisticsFuncInfo.speechCodec = jSONObject.optString("speechCodec", "-1");
        spradarVoiceQualityStatisticsFuncInfo.dlRtpwoNum = jSONObject.optInt("dl_rtpwo_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.dlRtpbaNum = jSONObject.optInt("dl_rtpba_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.dlRtpavNum = jSONObject.optInt("dl_rtpav_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.dlRtpgoNum = jSONObject.optInt("dl_rtpgo_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.dlRtpbeNum = jSONObject.optInt("dl_rtpbe_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.ulPdcpwoNum = jSONObject.optInt("ul_pdcpwo_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.ulPdcpbaNum = jSONObject.optInt("ul_pdcpba_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.ulPdcpavNum = jSONObject.optInt("ul_pdcpav_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.ulPdcpgoNum = jSONObject.optInt("ul_pdcpgo_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.ulPdcpbeNum = jSONObject.optInt("ul_pdcpbe_num", -1);
        spradarVoiceQualityStatisticsFuncInfo.csSpeechCodec = jSONObject.optString("CSspeechCodec", "-1");
        return spradarVoiceQualityStatisticsFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " CALL_QUALITY,20007 callId:" + this.callId + " callType:" + this.callType + " plmn:" + this.plmn + " rat:" + this.rat + " tac:" + this.tac + " earfcn:" + this.earfcn + " band:" + this.band + " cellId:" + this.cellId + " pci:" + this.pci + " rsrp:" + this.rsrp + " rsrq:" + this.rsrq + " sinr:" + this.sinr + " speechCodec:" + this.speechCodec + " dlRtpwoNum:" + this.dlRtpwoNum + " dlRtpbaNum:" + this.dlRtpbaNum + " dlRtpavNum:" + this.dlRtpavNum + " dlRtpgoNum:" + this.dlRtpgoNum + " dlRtpbeNum:" + this.dlRtpbeNum + " ulPdcpwoNum:" + this.ulPdcpwoNum + " ulPdcpbaNum:" + this.ulPdcpbaNum + " ulPdcpavNum:" + this.ulPdcpavNum + " ulPdcpgoNum:" + this.ulPdcpgoNum + " ulPdcpbeNum:" + this.ulPdcpbeNum + " csSpeechCodec:" + this.csSpeechCodec;
    }
}
