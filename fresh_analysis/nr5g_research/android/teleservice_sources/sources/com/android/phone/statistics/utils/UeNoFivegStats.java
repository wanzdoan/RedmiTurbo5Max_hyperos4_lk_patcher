package com.android.phone.statistics.utils;

import android.util.Log;
import com.android.phone.utils.ModemUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class UeNoFivegStats {
    protected static volatile UeNoFivegStats mInstance;
    public static final Map mNoSaFlagToScore = new HashMap() { // from class: com.android.phone.statistics.utils.UeNoFivegStats.1
        {
            put(0, 0);
            put(8, 1);
            put(4, 2);
            put(5, 2);
            put(6, 2);
            put(3, 3);
            put(2, 4);
            put(7, 5);
            put(1, 6);
            put(9, 6);
            put(10, 4);
            put(11, 4);
            put(12, 5);
        }
    };

    public static UeNoFivegStats getInstance() {
        if (mInstance == null) {
            synchronized (UeNoFivegStats.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new UeNoFivegStats();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    class UeNoFivegInfo {
        float arfcn;
        int band;
        String bpFaultTime;
        float cause;
        long cellId;
        float f_cell_quality_rsrp;
        int flag;
        int mcc;
        int mnc;
        float pci;
        float q_relevmin;
        float rat;
        float rejectCause;
        float rejectMsgType;
        float result;
        float rsrp;
        float s_qual;
        float s_rx_level;
        int tac;
        long time;
        float timerId;
        int version;

        UeNoFivegInfo(UeNoFivegStats ueNoFivegStats) {
        }

        public String toString() {
            return "[" + getCommonInfo() + ",cellId: " + this.cellId + ",tac: " + this.tac + ",]";
        }

        public String getCommonInfo() {
            StringBuilder sb = new StringBuilder("version: ");
            sb.append(this.version);
            sb.append(",time: ");
            sb.append(this.time);
            sb.append(",bpFaultTime: ");
            sb.append(this.bpFaultTime);
            sb.append(",mcc: ");
            sb.append(this.mcc);
            sb.append(",mnc: ");
            sb.append(this.mnc);
            sb.append(",band: ");
            sb.append(this.band);
            sb.append(",Flag: ");
            sb.append(this.flag);
            sb.append(",");
            switch (this.flag) {
                case 1:
                    sb.append("rejectMsgType: ");
                    sb.append(this.rejectMsgType);
                    sb.append(",rejectCause: ");
                    sb.append(this.rejectCause);
                    break;
                case 2:
                case 3:
                    sb.append("timerId: ");
                    sb.append(this.timerId);
                    break;
                case 4:
                case 5:
                    sb.append("cause: ");
                    sb.append(this.cause);
                    sb.append(",rsrp: ");
                    sb.append(this.rsrp);
                    break;
                case 6:
                    sb.append("rat: ");
                    sb.append(this.rat);
                    sb.append(",rsrp: ");
                    sb.append(this.rsrp);
                    break;
                case 7:
                    sb.append("rsrp: ");
                    sb.append(this.rsrp);
                    break;
                case 8:
                    sb.append("result: ");
                    sb.append(this.result);
                    sb.append(",q_relevmin: ");
                    sb.append(this.q_relevmin);
                    sb.append(",f_cell_quality_rsrp: ");
                    sb.append(this.f_cell_quality_rsrp);
                    sb.append(",s_rx_level: ");
                    sb.append(this.s_rx_level);
                    sb.append(",s_qual: ");
                    sb.append(this.s_qual);
                    sb.append(",arfcn: ");
                    sb.append(this.arfcn);
                    sb.append(",pci: ");
                    sb.append(this.pci);
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                    sb.append("rejectMsgType: ");
                    sb.append(this.rejectMsgType);
                    sb.append(",rejectCause: ");
                    sb.append(this.rejectCause);
                    break;
            }
            return sb.toString();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public List parseUeNoSaInfo(ModemUtils modemUtils, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        boolean z = true;
        while (z) {
            try {
                Object objCallQcrilHookGetObject = modemUtils.callQcrilHookGetObject("getUeNoSaInfo", i);
                if (objCallQcrilHookGetObject == null) {
                    Utils.logD("UeNoFivegStats", "getUeNoSaInfo is not support");
                    return arrayList;
                }
                ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                int i3 = byteBuffer.get() & TransitionInfo.INIT;
                int i4 = byteBuffer.getShort() & 65535;
                if (i4 <= 0) {
                    Utils.logD("UeNoFivegStats", "getUeNoSaInfo count = " + i4);
                    return arrayList;
                }
                if (i4 < (i3 == i2 ? 11 : 13)) {
                    z = false;
                }
                int i5 = 0;
                while (i5 < i4) {
                    UeNoFivegInfo ueNoFivegInfo = new UeNoFivegInfo(this);
                    ueNoFivegInfo.version = i3;
                    long j = byteBuffer.getLong();
                    ueNoFivegInfo.time = j;
                    ueNoFivegInfo.bpFaultTime = Utils.systemTimeToString(j);
                    ueNoFivegInfo.mcc = byteBuffer.getShort() & 65535;
                    ueNoFivegInfo.mnc = byteBuffer.getShort() & 65535;
                    ueNoFivegInfo.band = byteBuffer.getInt();
                    ueNoFivegInfo.cellId = byteBuffer.getLong();
                    ueNoFivegInfo.tac = byteBuffer.getInt();
                    int i6 = byteBuffer.get() & TransitionInfo.INIT;
                    ueNoFivegInfo.flag = i6;
                    if (i6 < 0 || i6 > 12) {
                        Utils.logD("UeNoFivegStats", "UeNoFivegInfo flag is invalid");
                    } else {
                        int i7 = 3;
                        switch (i6) {
                            case 0:
                                i7 = 5;
                                break;
                            case 1:
                                ueNoFivegInfo.rejectMsgType = byteBuffer.getFloat();
                                ueNoFivegInfo.rejectCause = byteBuffer.getFloat();
                                break;
                            case 2:
                            case 3:
                                ueNoFivegInfo.timerId = byteBuffer.getFloat();
                                i7 = 4;
                                break;
                            case 4:
                            case 5:
                                ueNoFivegInfo.cause = byteBuffer.getFloat();
                                ueNoFivegInfo.rsrp = byteBuffer.getFloat();
                                break;
                            case 6:
                                ueNoFivegInfo.rat = byteBuffer.getFloat();
                                ueNoFivegInfo.rsrp = byteBuffer.getFloat();
                                break;
                            case 7:
                                ueNoFivegInfo.rsrp = byteBuffer.getFloat();
                                i7 = 4;
                                break;
                            case 8:
                                ueNoFivegInfo.result = byteBuffer.getFloat();
                                ueNoFivegInfo.q_relevmin = byteBuffer.getFloat();
                                ueNoFivegInfo.f_cell_quality_rsrp = byteBuffer.getFloat();
                                ueNoFivegInfo.s_rx_level = byteBuffer.getFloat();
                                ueNoFivegInfo.s_qual = byteBuffer.getFloat();
                                if (i3 == i2) {
                                    ueNoFivegInfo.arfcn = byteBuffer.getFloat();
                                    ueNoFivegInfo.pci = byteBuffer.getFloat();
                                }
                                i7 = 0;
                                break;
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                                ueNoFivegInfo.rejectMsgType = byteBuffer.getFloat();
                                ueNoFivegInfo.rejectCause = byteBuffer.getFloat();
                                break;
                            default:
                                i7 = 0;
                                break;
                        }
                        if (i3 == i2 && ueNoFivegInfo.flag != 8) {
                            i7 += 2;
                        }
                        byteBuffer.position(byteBuffer.position() + (i7 * 4));
                        Log.d("UeNoFivegStats", "UeNoFivegInfo = " + ueNoFivegInfo.getCommonInfo());
                        addToSortedListWithDeduplication(arrayList, ueNoFivegInfo);
                    }
                    i5++;
                    i2 = 1;
                }
            } catch (Exception e) {
                Utils.logD("UeNoFivegStats", "exception:" + e);
            }
        }
        return arrayList;
    }

    private void addToSortedListWithDeduplication(List list, UeNoFivegInfo ueNoFivegInfo) {
        int i = ueNoFivegInfo.flag;
        long j = ueNoFivegInfo.time;
        for (int i2 = 0; i2 < list.size(); i2++) {
            UeNoFivegInfo ueNoFivegInfo2 = (UeNoFivegInfo) list.get(i2);
            if (ueNoFivegInfo2.flag == i) {
                long j2 = ueNoFivegInfo2.time;
                if (j > j2) {
                    list.remove(i2);
                    Log.d("UeNoFivegStats", "Removed old record for cause: " + i + ", time: " + j2);
                    break;
                }
                Log.d("UeNoFivegStats", "Kept existing record for cause: " + i + ", existing time: " + j2 + ", new time: " + j + " (discarded)");
                return;
            }
        }
        list.add(ueNoFivegInfo);
        Log.d("UeNoFivegStats", "Added record for cause: " + i + ", time: " + j);
    }
}
