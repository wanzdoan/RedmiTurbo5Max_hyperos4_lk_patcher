package com.android.phone.network;

import android.os.Bundle;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.phone.utils.NetworkSpeedTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ModemQoeMetrics {
    private static ModemQoeMetrics mInstance;
    private static final HashMap sLocalLogs = new HashMap();
    private long band;
    private long cellId;
    private byte cqi;
    private int defaultCallEndReason;
    private int defaultCallEndReasonType;
    private int dlBler;
    private int dlBw;
    private long dlEarfcn;
    private int dlGrantRatio;
    private int dlMcs;
    private long dlTbBytes;
    private int fullSchNum;
    private int lteMaxPower;
    private long ltePdcpUlDataBytesFromWm;
    private long ltePdcpUlDiscardSduBytes;
    private long ltePdcpUlNumPdcpUiBufBytes;
    private short lteTxPusch;
    private byte nasRejectCause;
    private byte nasRejectMsgType;
    private long nr5gMtplTx;
    private long nr5gPdcpUlCurNumWmBytes;
    private long nr5gPdcpUlCurWmDropBytes;
    private long nr5gPdcpUlTotalNumDiscardBytes;
    private long nr5gPdcpUlTotalNumRxBytes;
    private short nr5gTargetTxPower;
    private byte numLayers;
    private byte numRbs;
    private int pci;
    private long pdcpDlNumDataPduRcv;
    private long pdcpDlNumMissPdu;
    private long pdcpDlNumOutOfWinPdu;
    private byte pwrErrCode;
    private int rachNumAttempts;
    private byte rachResult;
    private float rat;
    private byte ri;
    private int rlcDlNumDataPdu;
    private int rlcDlNumDroppedPdu;
    private int rlcDlNumRetxPdu;
    private int rlcUlTotalRetxPdus;
    private long rlcUlTotalTxPdus;
    private float rsrp;
    private float rsrq;
    private float snr;
    private int ulBler;
    private int ulBw;
    private long ulEarfcn;
    private int ulGrantRatio;
    private int ulMcs;

    public static synchronized ModemQoeMetrics getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new ModemQoeMetrics();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private ModemQoeMetrics() {
    }

    public float[] getAllData() {
        return new float[]{this.cellId, this.dlEarfcn, this.ulEarfcn, this.band, this.dlBw, this.ulBw, this.pci, this.rat, this.rsrp, this.rsrq, this.snr, this.dlBler, this.ulBler, this.dlMcs, this.ulMcs, this.lteTxPusch, this.lteMaxPower, this.nr5gTargetTxPower, this.nr5gMtplTx, this.nasRejectMsgType, this.nasRejectCause, this.pwrErrCode, this.cqi, this.ri, this.numLayers, this.dlTbBytes, this.dlGrantRatio, this.ulGrantRatio, this.pdcpDlNumMissPdu, this.pdcpDlNumDataPduRcv, this.pdcpDlNumOutOfWinPdu, this.numRbs, this.nr5gPdcpUlTotalNumDiscardBytes, this.nr5gPdcpUlCurNumWmBytes, this.nr5gPdcpUlCurWmDropBytes, this.nr5gPdcpUlTotalNumRxBytes, this.ltePdcpUlNumPdcpUiBufBytes, this.ltePdcpUlDataBytesFromWm, this.ltePdcpUlDiscardSduBytes, this.rachNumAttempts, this.rachResult, this.rlcUlTotalTxPdus, this.rlcUlTotalRetxPdus, this.rlcDlNumDataPdu, this.rlcDlNumRetxPdu, this.rlcDlNumDroppedPdu, this.fullSchNum};
    }

    public long getCellId() {
        return this.cellId;
    }

    public void setCellId(long j) {
        this.cellId = j;
    }

    public long getDlEarfcn() {
        return this.dlEarfcn;
    }

    public void setDlEarfcn(long j) {
        this.dlEarfcn = j;
    }

    public long getUlEarfcn() {
        return this.ulEarfcn;
    }

    public void setUlEarfcn(long j) {
        this.ulEarfcn = j;
    }

    public long getBand() {
        return this.band;
    }

    public void setBand(long j) {
        this.band = j;
    }

    public int getDlBw() {
        return this.dlBw;
    }

    public void setDlBw(int i) {
        this.dlBw = i;
    }

    public int getUlBw() {
        return this.ulBw;
    }

    public void setUlBw(int i) {
        this.ulBw = i;
    }

    public int getPci() {
        return this.pci;
    }

    public void setPci(int i) {
        this.pci = i;
    }

    public float getRat() {
        return this.rat;
    }

    public void setRat(float f) {
        this.rat = f;
    }

    public float getRsrp() {
        return this.rsrp;
    }

    public void setRsrp(float f) {
        this.rsrp = f;
    }

    public float getRsrq() {
        return this.rsrq;
    }

    public void setRsrq(float f) {
        this.rsrq = f;
    }

    public float getSnr() {
        return this.snr;
    }

    public void setSnr(float f) {
        this.snr = f;
    }

    public int getDlBler() {
        return this.dlBler;
    }

    public void setDlBler(int i) {
        this.dlBler = i;
    }

    public int getUlBler() {
        return this.ulBler;
    }

    public void setUlBler(int i) {
        this.ulBler = i;
    }

    public int getDlMcs() {
        return this.dlMcs;
    }

    public void setDlMcs(int i) {
        this.dlMcs = i;
    }

    public int getUlMcs() {
        return this.ulMcs;
    }

    public void setUlMcs(int i) {
        this.ulMcs = i;
    }

    public short getLteTxPusch() {
        return this.lteTxPusch;
    }

    public void setLteTxPusch(short s) {
        this.lteTxPusch = s;
    }

    public int getLteMaxPower() {
        return this.lteMaxPower;
    }

    public void setLteMaxPower(int i) {
        this.lteMaxPower = i;
    }

    public short getNr5gTargetTxPower() {
        return this.nr5gTargetTxPower;
    }

    public void setNr5gTargetTxPower(short s) {
        this.nr5gTargetTxPower = s;
    }

    public long getNr5gMtplTx() {
        return this.nr5gMtplTx;
    }

    public void setNr5gMtplTx(long j) {
        this.nr5gMtplTx = j;
    }

    public byte getNasRejectMsgType() {
        return this.nasRejectMsgType;
    }

    public void setNasRejectMsgType(byte b) {
        this.nasRejectMsgType = b;
    }

    public byte getNasRejectCause() {
        return this.nasRejectCause;
    }

    public void setNasRejectCause(byte b) {
        this.nasRejectCause = b;
    }

    public byte getPwrErrCode() {
        return this.pwrErrCode;
    }

    public void setPwrErrCode(byte b) {
        this.pwrErrCode = b;
    }

    public byte getCqi() {
        return this.cqi;
    }

    public void setCqi(byte b) {
        this.cqi = b;
    }

    public byte getRi() {
        return this.ri;
    }

    public void setRi(byte b) {
        this.ri = b;
    }

    public byte getNumLayers() {
        return this.numLayers;
    }

    public void setNumLayers(byte b) {
        this.numLayers = b;
    }

    public long getDlTbBytes() {
        return this.dlTbBytes;
    }

    public void setDlTbBytes(long j) {
        this.dlTbBytes = j;
    }

    public int getDlGrantRatio() {
        return this.dlGrantRatio;
    }

    public void setDlGrantRatio(int i) {
        this.dlGrantRatio = i;
    }

    public int getUlGrantRatio() {
        return this.ulGrantRatio;
    }

    public void setUlGrantRatio(int i) {
        this.ulGrantRatio = i;
    }

    public long getPdcpDlNumMissPdu() {
        return this.pdcpDlNumMissPdu;
    }

    public void setPdcpDlNumMissPdu(long j) {
        this.pdcpDlNumMissPdu = j;
    }

    public long getPdcpDlNumDataPduRcv() {
        return this.pdcpDlNumDataPduRcv;
    }

    public void setPdcpDlNumDataPduRcv(long j) {
        this.pdcpDlNumDataPduRcv = j;
    }

    public long getPdcpDlNumOutOfWinPdu() {
        return this.pdcpDlNumOutOfWinPdu;
    }

    public void setPdcpDlNumOutOfWinPdu(long j) {
        this.pdcpDlNumOutOfWinPdu = j;
    }

    public byte getNumRbs() {
        return this.numRbs;
    }

    public void setNumRbs(byte b) {
        this.numRbs = b;
    }

    public long getNr5gPdcpUlTotalNumDiscardBytes() {
        return this.nr5gPdcpUlTotalNumDiscardBytes;
    }

    public void setNr5gPdcpUlTotalNumDiscardBytes(long j) {
        this.nr5gPdcpUlTotalNumDiscardBytes = j;
    }

    public long getNr5gPdcpUlCurNumWmBytes() {
        return this.nr5gPdcpUlCurNumWmBytes;
    }

    public void setNr5gPdcpUlCurNumWmBytes(long j) {
        this.nr5gPdcpUlCurNumWmBytes = j;
    }

    public long getNr5gPdcpUlCurWmDropBytes() {
        return this.nr5gPdcpUlCurWmDropBytes;
    }

    public void setNr5gPdcpUlCurWmDropBytes(long j) {
        this.nr5gPdcpUlCurWmDropBytes = j;
    }

    public long getNr5gPdcpUlTotalNumRxBytes() {
        return this.nr5gPdcpUlTotalNumRxBytes;
    }

    public void setNr5gPdcpUlTotalNumRxBytes(long j) {
        this.nr5gPdcpUlTotalNumRxBytes = j;
    }

    public long getLtePdcpUlNumPdcpUiBufBytes() {
        return this.ltePdcpUlNumPdcpUiBufBytes;
    }

    public void setLtePdcpUlNumPdcpUiBufBytes(long j) {
        this.ltePdcpUlNumPdcpUiBufBytes = j;
    }

    public long getLtePdcpUlDataBytesFromWm() {
        return this.ltePdcpUlDataBytesFromWm;
    }

    public void setLtePdcpUlDataBytesFromWm(long j) {
        this.ltePdcpUlDataBytesFromWm = j;
    }

    public long getLtePdcpUlDiscardSduBytes() {
        return this.ltePdcpUlDiscardSduBytes;
    }

    public void setLtePdcpUlDiscardSduBytes(long j) {
        this.ltePdcpUlDiscardSduBytes = j;
    }

    public int getRachNumAttempts() {
        return this.rachNumAttempts;
    }

    public void setRachNumAttempts(int i) {
        this.rachNumAttempts = i;
    }

    public byte getRachResult() {
        return this.rachResult;
    }

    public void setRachResult(byte b) {
        this.rachResult = b;
    }

    public long getRlcUlTotalTxPdus() {
        return this.rlcUlTotalTxPdus;
    }

    public void setRlcUlTotalTxPdus(long j) {
        this.rlcUlTotalTxPdus = j;
    }

    public int getRlcUlTotalRetxPdus() {
        return this.rlcUlTotalRetxPdus;
    }

    public void setRlcUlTotalRetxPdus(int i) {
        this.rlcUlTotalRetxPdus = i;
    }

    public int getRlcDlNumDataPdu() {
        return this.rlcDlNumDataPdu;
    }

    public void setRlcDlNumDataPdu(int i) {
        this.rlcDlNumDataPdu = i;
    }

    public int getRlcDlNumRetxPdu() {
        return this.rlcDlNumRetxPdu;
    }

    public void setRlcDlNumRetxPdu(int i) {
        this.rlcDlNumRetxPdu = i;
    }

    public int getRlcDlNumDroppedPdu() {
        return this.rlcDlNumDroppedPdu;
    }

    public void setRlcDlNumDroppedPdu(int i) {
        this.rlcDlNumDroppedPdu = i;
    }

    public int getFullSchNum() {
        return this.fullSchNum;
    }

    public void setFullSchNum(int i) {
        this.fullSchNum = i;
    }

    public int getDefaultCallEndReasonType() {
        return this.defaultCallEndReasonType;
    }

    public void setDefaultCallEndReasonType(int i) {
        this.defaultCallEndReasonType = i;
    }

    public int getDefaultCallEndReason() {
        return this.defaultCallEndReason;
    }

    public void setDefaultCallEndReason(int i) {
        this.defaultCallEndReason = i;
    }

    public String toString() {
        return "ModemQoeMetrics{\n  cellId=" + this.cellId + ",\n  dlEarfcn=" + this.dlEarfcn + ",\n  ulEarfcn=" + this.ulEarfcn + ",\n  band=" + this.band + ",\n  dlBw=" + this.dlBw + ",\n  ulBw=" + this.ulBw + ",\n  pci=" + this.pci + ",\n  rat=" + this.rat + ",\n  rsrp=" + this.rsrp + ",\n  rsrq=" + this.rsrq + ",\n  snr=" + this.snr + ",\n  dlBler=" + this.dlBler + ",\n  ulBler=" + this.ulBler + ",\n  dlMcs=" + this.dlMcs + ",\n  ulMcs=" + this.ulMcs + ",\n  lteTxPusch=" + ((int) this.lteTxPusch) + ",\n  lteMaxPower=" + this.lteMaxPower + ",\n  nr5gTargetTxPower=" + ((int) this.nr5gTargetTxPower) + ",\n  nr5gMtplTx=" + this.nr5gMtplTx + ",\n  nasRejectMsgType=" + ((int) this.nasRejectMsgType) + ",\n  nasRejectCause=" + ((int) this.nasRejectCause) + ",\n  pwrErrCode=" + ((int) this.pwrErrCode) + ",\n  cqi=" + ((int) this.cqi) + ",\n  ri=" + ((int) this.ri) + ",\n  numLayers=" + ((int) this.numLayers) + ",\n  dlTbBytes=" + this.dlTbBytes + ",\n  dlGrantRatio=" + this.dlGrantRatio + ",\n  ulGrantRatio=" + this.ulGrantRatio + ",\n  pdcpDlNumMissPdu=" + this.pdcpDlNumMissPdu + ",\n  pdcpDlNumDataPduRcv=" + this.pdcpDlNumDataPduRcv + ",\n  pdcpDlNumOutOfWinPdu=" + this.pdcpDlNumOutOfWinPdu + ",\n  numRbs=" + ((int) this.numRbs) + ",\n  nr5gPdcpUlTotalNumDiscardBytes=" + this.nr5gPdcpUlTotalNumDiscardBytes + ",\n  nr5gPdcpUlCurNumWmBytes=" + this.nr5gPdcpUlCurNumWmBytes + ",\n  nr5gPdcpUlCurWmDropBytes=" + this.nr5gPdcpUlCurWmDropBytes + ",\n  nr5gPdcpUlTotalNumRxBytes=" + this.nr5gPdcpUlTotalNumRxBytes + ",\n  ltePdcpUlNumPdcpUiBufBytes=" + this.ltePdcpUlNumPdcpUiBufBytes + ",\n  ltePdcpUlDataBytesFromWm=" + this.ltePdcpUlDataBytesFromWm + ",\n  ltePdcpUlDiscardSduBytes=" + this.ltePdcpUlDiscardSduBytes + ",\n  rachNumAttempts=" + this.rachNumAttempts + ",\n  rachResult=" + ((int) this.rachResult) + ",\n  rlcUlTotalTxPdus=" + this.rlcUlTotalTxPdus + ",\n  rlcUlTotalRetxPdus=" + this.rlcUlTotalRetxPdus + ",\n  rlcDlNumDataPdu=" + this.rlcDlNumDataPdu + ",\n  rlcDlNumRetxPdu=" + this.rlcDlNumRetxPdu + ",\n  rlcDlNumDroppedPdu=" + this.rlcDlNumDroppedPdu + ",\n  fullSchNum=" + this.fullSchNum + "\n}";
    }

    public Bundle getModemQosParams(int i) {
        Bundle bundle = new Bundle();
        try {
            bundle.putInt("dl_bler", getDlBler());
            bundle.putInt("ul_bler", getUlBler());
            long pdcpDlNumMissPdu = getPdcpDlNumMissPdu();
            long pdcpDlNumDataPduRcv = getPdcpDlNumDataPduRcv();
            bundle.putFloat("dl_loss_rate", pdcpDlNumDataPduRcv > 0 ? pdcpDlNumMissPdu / pdcpDlNumDataPduRcv : 0.0f);
            NetworkSpeedTracker networkSpeedTracker = NetworkSpeedTracker.getInstance();
            if (networkSpeedTracker != null) {
                bundle.putInt("dl_bw_kbps", (int) (networkSpeedTracker.getCurrentDlRateBps() / 1000));
                bundle.putInt("ul_bw_kbps", (int) (networkSpeedTracker.getCurrentUlRateBps() / 1000));
                return bundle;
            }
            bundle.putInt("dl_bw_kbps", 0);
            bundle.putInt("ul_bw_kbps", 0);
            return bundle;
        } catch (Exception e) {
            Log.w("ModemQoeMetrics", "getModemQosParams error: " + e);
            return bundle;
        }
    }

    public static void localLog(String str, String str2, int i) {
        localLog("ModemQoeMetrics", str, str2, i);
    }

    private static void localLog(String str, String str2, String str3, int i) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(i));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ModemQoeMetrics dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("ModemQoeMetrics dump end------");
        printWriter.flush();
    }
}
