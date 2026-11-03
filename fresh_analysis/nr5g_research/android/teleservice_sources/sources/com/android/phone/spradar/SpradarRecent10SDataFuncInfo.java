package com.android.phone.spradar;

import android.os.Parcel;
import android.os.Parcelable;
import com.ot.pubsub.a.a;
import miuix.preference.flexible.PreferenceMarkLevel;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpradarRecent10SDataFuncInfo extends SpradarFuncInfo {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.spradar.SpradarRecent10SDataFuncInfo.1
        @Override // android.os.Parcelable.Creator
        public SpradarRecent10SDataFuncInfo createFromParcel(Parcel parcel) {
            return new SpradarRecent10SDataFuncInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SpradarRecent10SDataFuncInfo[] newArray(int i) {
            return new SpradarRecent10SDataFuncInfo[i];
        }
    };
    private int cc;
    private String cqi;
    private String dG;
    private String dRb;
    private String hspeed;
    private String l1DB;
    private String l1UB;
    private String mac;
    private String oFail;
    private String pMax;
    private String pR;
    private String pRrc;
    private String pS;
    private String pSnr;
    private String pdcpR;
    private String pdcpS;
    private int phoneId;
    private String psld;
    private String qci;
    private String rb;
    private String rbid;
    private String retr;
    private String rlfNum;
    private String rsrp4;
    private String rxNum;
    private String sA;
    private String sR;
    private String sRrc;
    private String sS;
    private String sSnr;
    private long timeStamp;
    private String txp;
    private String uG;
    private String uRb;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncId() {
        return PreferenceMarkLevel.LEVEL_LARGE_ONLY_SUMMARY;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getFuncType() {
        return 1;
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public int getResponseType() {
        return 0;
    }

    public SpradarRecent10SDataFuncInfo() {
        this.timeStamp = 0L;
        this.phoneId = -1;
    }

    protected SpradarRecent10SDataFuncInfo(Parcel parcel) {
        this.timeStamp = 0L;
        this.phoneId = -1;
        this.timeStamp = parcel.readLong();
        this.phoneId = parcel.readInt();
        this.cc = parcel.readInt();
        this.pR = parcel.readString();
        this.sR = parcel.readString();
        this.sA = parcel.readString();
        this.pRrc = parcel.readString();
        this.sRrc = parcel.readString();
        this.sS = parcel.readString();
        this.pS = parcel.readString();
        this.rxNum = parcel.readString();
        this.pSnr = parcel.readString();
        this.sSnr = parcel.readString();
        this.pMax = parcel.readString();
        this.txp = parcel.readString();
        this.rlfNum = parcel.readString();
        this.cqi = parcel.readString();
        this.uRb = parcel.readString();
        this.dRb = parcel.readString();
        this.uG = parcel.readString();
        this.dG = parcel.readString();
        this.retr = parcel.readString();
        this.pdcpS = parcel.readString();
        this.pdcpR = parcel.readString();
        this.mac = parcel.readString();
        this.l1UB = parcel.readString();
        this.l1DB = parcel.readString();
        this.rb = parcel.readString();
        this.psld = parcel.readString();
        this.hspeed = parcel.readString();
        this.qci = parcel.readString();
        this.rsrp4 = parcel.readString();
        this.oFail = parcel.readString();
        this.rbid = parcel.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timeStamp);
        parcel.writeInt(this.phoneId);
        parcel.writeInt(this.cc);
        parcel.writeString(this.pR);
        parcel.writeString(this.sR);
        parcel.writeString(this.sA);
        parcel.writeString(this.pRrc);
        parcel.writeString(this.sRrc);
        parcel.writeString(this.sS);
        parcel.writeString(this.pS);
        parcel.writeString(this.rxNum);
        parcel.writeString(this.pSnr);
        parcel.writeString(this.sSnr);
        parcel.writeString(this.pMax);
        parcel.writeString(this.txp);
        parcel.writeString(this.rlfNum);
        parcel.writeString(this.cqi);
        parcel.writeString(this.uRb);
        parcel.writeString(this.dRb);
        parcel.writeString(this.uG);
        parcel.writeString(this.dG);
        parcel.writeString(this.retr);
        parcel.writeString(this.pdcpS);
        parcel.writeString(this.pdcpR);
        parcel.writeString(this.mac);
        parcel.writeString(this.l1UB);
        parcel.writeString(this.l1DB);
        parcel.writeString(this.rb);
        parcel.writeString(this.psld);
        parcel.writeString(this.hspeed);
        parcel.writeString(this.qci);
        parcel.writeString(this.rsrp4);
        parcel.writeString(this.oFail);
        parcel.writeString(this.rbid);
    }

    @Override // com.android.phone.spradar.SpradarFuncInfo
    public SpradarFuncInfo fromJson(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        SpradarRecent10SDataFuncInfo spradarRecent10SDataFuncInfo = new SpradarRecent10SDataFuncInfo();
        spradarRecent10SDataFuncInfo.timeStamp = System.currentTimeMillis();
        spradarRecent10SDataFuncInfo.phoneId = i;
        spradarRecent10SDataFuncInfo.cc = jSONObject.optInt("cc", -1);
        spradarRecent10SDataFuncInfo.pR = jSONObject.optString("p_r", "-1");
        spradarRecent10SDataFuncInfo.sR = jSONObject.optString("s_r", "-1");
        spradarRecent10SDataFuncInfo.sA = jSONObject.optString("s_a", "-1");
        spradarRecent10SDataFuncInfo.pRrc = jSONObject.optString("p_rrc", "-1");
        spradarRecent10SDataFuncInfo.sRrc = jSONObject.optString("s_rrc", "-1");
        spradarRecent10SDataFuncInfo.sS = jSONObject.optString("s_s", "-1");
        spradarRecent10SDataFuncInfo.pS = jSONObject.optString("p_s", "-1");
        spradarRecent10SDataFuncInfo.rxNum = jSONObject.optString("rx_num", "-1");
        spradarRecent10SDataFuncInfo.rsrp4 = jSONObject.optString("rsrp_4", "-1");
        spradarRecent10SDataFuncInfo.pSnr = jSONObject.optString("p_snr", "-1");
        spradarRecent10SDataFuncInfo.sSnr = jSONObject.optString("s_snr", "-1");
        spradarRecent10SDataFuncInfo.pMax = jSONObject.optString("pmax", "-1");
        spradarRecent10SDataFuncInfo.txp = jSONObject.optString("txp", "-1");
        spradarRecent10SDataFuncInfo.rlfNum = jSONObject.optString("rlf_num", "-1");
        spradarRecent10SDataFuncInfo.oFail = jSONObject.optString("o_fail", "-1");
        spradarRecent10SDataFuncInfo.cqi = jSONObject.optString("cqi", "-1");
        spradarRecent10SDataFuncInfo.uRb = jSONObject.optString("u_rb", "-1");
        spradarRecent10SDataFuncInfo.dRb = jSONObject.optString("d_rb", "-1");
        spradarRecent10SDataFuncInfo.uG = jSONObject.optString("u_g", "-1");
        spradarRecent10SDataFuncInfo.dG = jSONObject.optString("d_g", "-1");
        spradarRecent10SDataFuncInfo.retr = jSONObject.optString("retr", "-1");
        spradarRecent10SDataFuncInfo.pdcpS = jSONObject.optString("pdcp_s", "-1");
        spradarRecent10SDataFuncInfo.pdcpR = jSONObject.optString("pdcp_r", "-1");
        spradarRecent10SDataFuncInfo.mac = jSONObject.optString(a.B, "-1");
        spradarRecent10SDataFuncInfo.l1UB = jSONObject.optString("l1_u_b", "-1");
        spradarRecent10SDataFuncInfo.l1DB = jSONObject.optString("l1_d_b", "-1");
        spradarRecent10SDataFuncInfo.rbid = jSONObject.optString("rbid", "-1");
        spradarRecent10SDataFuncInfo.rb = jSONObject.optString("rb", "-1");
        spradarRecent10SDataFuncInfo.psld = jSONObject.optString("psId", "-1");
        spradarRecent10SDataFuncInfo.hspeed = jSONObject.optString("hspeed", "-1");
        spradarRecent10SDataFuncInfo.qci = jSONObject.optString("QCI", "-1");
        return spradarRecent10SDataFuncInfo;
    }

    public String toString() {
        return SpradarFuncInfoFactory.timeLongToStringEx(this.timeStamp) + " slot:" + this.phoneId + " RECENT_10S_DATA,20002 cc:" + this.cc + " pR:" + this.pR + " sR:" + this.sR + " sA:" + this.sA + " pRrc:" + this.pRrc + " sRrc:" + this.sRrc + " sS:" + this.sS + " pS:" + this.pS + " rxNum:" + this.rxNum + " pSnr:" + this.pSnr + " sSnr:" + this.sSnr + " rsrp4:" + this.rsrp4 + " pMax:" + this.pMax + " txp:" + this.txp + " rlfNum:" + this.rlfNum + " oFail:" + this.oFail + " cqi:" + this.cqi + " uRb:" + this.uRb + " dRb:" + this.dRb + " uG:" + this.uG + " dG:" + this.dG + " retr:" + this.retr + " pdcpS:" + this.pdcpS + " pdcpR:" + this.pdcpR + " mac:" + this.mac + " l1UB:" + this.l1UB + " l1DB:" + this.l1DB + " rbid:" + this.rbid + " rb:" + this.rb + " psld:" + this.psld + " hspeed:" + this.hspeed + " qci:" + this.qci;
    }
}
