package com.android.phone.freqopt.util;

import android.os.Handler;
import android.os.Looper;
import com.android.phone.PhoneGlobals;
import com.android.phone.QmsClientBase;
import com.android.phone.utils.ModemUtils;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public abstract class FreqOptUtils {
    private static volatile Map sActiveBandMap;
    private static final Handler sNoOpHandler = new Handler(Looper.getMainLooper());

    public static int convertToLteBandwidthE(int i) {
        if (i == 1400) {
            return 0;
        }
        if (i == 3000) {
            return 1;
        }
        if (i == 5000) {
            return 2;
        }
        if (i == 10000) {
            return 3;
        }
        if (i != 15000) {
            return i != 20000 ? -1 : 5;
        }
        return 4;
    }

    public static long parseLong(Map map, String str, long j) {
        String str2 = (String) map.get(str);
        if (str2 == null) {
            return j;
        }
        try {
            return Long.parseLong(str2);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    public static int parseInt(Map map, String str, int i) {
        String str2 = (String) map.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return Integer.parseInt(str2);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static void setCoverageState(int i, int i2, String str, Handler handler) {
        LogUtils.d("Freq_Opt_Utils", "setCoverageState slotId=" + i + ", state=" + i2 + ", reason=" + str);
        QmsClientBase qmsClientBase = QmsClientBase.getInstance();
        if (qmsClientBase == null) {
            LogUtils.w("Freq_Opt_Utils", "setCoverageState: QmsClientBase not initialized");
            return;
        }
        if (handler == null) {
            handler = sNoOpHandler;
        }
        qmsClientBase.setCoverageState(i, i2, handler.obtainMessage(0, str));
    }

    public static void setCellListInfo(List list, String str, Handler handler) {
        StringBuilder sb = new StringBuilder("setCellListInfo size=");
        sb.append(list != null ? list.size() : 0);
        sb.append(", reason=");
        sb.append(str);
        LogUtils.d("Freq_Opt_Utils", sb.toString());
        QmsClientBase qmsClientBase = QmsClientBase.getInstance();
        if (qmsClientBase == null) {
            LogUtils.w("Freq_Opt_Utils", "setCellListInfo: QmsClientBase not initialized");
            return;
        }
        if (handler == null) {
            handler = sNoOpHandler;
        }
        qmsClientBase.setCellListInfo(list, handler.obtainMessage(0, str));
    }

    public static void setPreferredFrequencyList(int i, int i2, int i3, List list, List list2, String str, Handler handler) {
        StringBuilder sb = new StringBuilder("setPreferredFrequencyList slotId=");
        sb.append(i);
        sb.append(", usageType=");
        sb.append(i2);
        sb.append(", action=");
        sb.append(i3);
        sb.append(", lteSize=");
        sb.append(list != null ? list.size() : 0);
        sb.append(", nrSize=");
        sb.append(list2 != null ? list2.size() : 0);
        sb.append(", reason=");
        sb.append(str);
        LogUtils.d("Freq_Opt_Utils", sb.toString());
        QmsClientBase qmsClientBase = QmsClientBase.getInstance();
        if (qmsClientBase == null) {
            LogUtils.w("Freq_Opt_Utils", "setPreferredFrequencyList: QmsClientBase not initialized");
            return;
        }
        if (handler == null) {
            handler = sNoOpHandler;
        }
        qmsClientBase.setPreferredFrequencyList(i, i2, i3, list, list2, handler.obtainMessage(0, str));
    }

    public static int getNr5gRrcState(int i) {
        ModemUtils modemUtils = getModemUtils();
        if (modemUtils == null) {
            return 6;
        }
        int nr5gRrcState = modemUtils.getNr5gRrcState(i);
        LogUtils.d("Freq_Opt_Utils", "getNr5gRrcState phoneId=" + i + ", state=" + nr5gRrcState);
        if (nr5gRrcState < 0 || nr5gRrcState > 5) {
            return 6;
        }
        return nr5gRrcState;
    }

    public static boolean setRapidFadingEnabled(boolean z, int i) {
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            byteBufferAllocate.order(ByteOrder.nativeOrder());
            byteBufferAllocate.putInt(z ? 1 : 0);
            if (XmTelephonyManagerInner.getDefault().sendCommMsgSync(i, 14, byteBufferAllocate.array()) == null) {
                LogUtils.w("Freq_Opt_Utils", "setRapidFadingEnabled: response is null");
                return false;
            }
            LogUtils.d("Freq_Opt_Utils", "setRapidFadingEnabled enable=" + z + ", phoneId=" + i + ", success");
            return true;
        } catch (Exception e) {
            LogUtils.w("Freq_Opt_Utils", "setRapidFadingEnabled failed", e);
            return false;
        }
    }

    private static ModemUtils getModemUtils() {
        ModemUtils modemUtils = ModemUtils.getInstance();
        if (modemUtils == null) {
            ModemUtils.init(PhoneGlobals.getInstance());
            modemUtils = ModemUtils.getInstance();
        }
        if (modemUtils == null) {
            LogUtils.e("Freq_Opt_Utils", "ModemUtils init failed");
        }
        return modemUtils;
    }

    public static boolean isFreqCtlEnabled() {
        return isN79HostOptEnabled() || isFddTddOptEnabled() || isNrLteOptEnabled();
    }

    public static boolean isN79HostOptEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("freq_n79_host_opt_enabled", false);
    }

    public static boolean isFddTddOptEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("freq_fdd_tdd_opt_enabled", false);
    }

    public static boolean isNrLteOptEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("freq_nr_lte_opt_enabled", false);
    }

    private static Map getActiveBandMap() {
        if (sActiveBandMap == null) {
            synchronized (FreqOptUtils.class) {
                try {
                    if (sActiveBandMap == null) {
                        HashMap map = new HashMap();
                        for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
                            String[] strArrSplit = str.split("=");
                            if (strArrSplit.length == 2) {
                                map.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
                            }
                        }
                        sActiveBandMap = map;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sActiveBandMap;
    }

    public static int convertToActiveBand(int i) {
        if (i <= 0) {
            return 0;
        }
        Integer num = (Integer) getActiveBandMap().get("E_UTRA_OPERATING_BAND_" + i);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static int convertNrToActiveBand(int i) {
        if (i <= 0) {
            return 0;
        }
        Integer num = (Integer) getActiveBandMap().get("NR5G_BAND_" + i);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }
}
