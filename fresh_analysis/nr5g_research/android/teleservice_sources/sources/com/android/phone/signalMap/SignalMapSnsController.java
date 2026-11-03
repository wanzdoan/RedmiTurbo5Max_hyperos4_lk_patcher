package com.android.phone.signalMap;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapSnsController extends SignalMapSnsControllerBase {
    public static boolean DEBUG = false;
    private static final Map mActiveBand = new HashMap();
    private static Map mBandMappingScs = new HashMap();
    private static Context mContext;
    private String[] mBlackCells;
    private CloudCellDbHelper mDbHelper;
    private int mDdsPhoneId;
    private SignalMapHandler mHandler;
    private SignalMapCloudController mSignalMapCloudController;
    private HandlerThread mWorkThread;
    private ArrayList mNrDubiousCellInfoSList = new ArrayList();
    private ArrayList mLteDubiousCellInfoSList = new ArrayList();

    public static synchronized SignalMapSnsControllerBase make() {
        if (ModemProvider.IS_QCOM) {
            if (SignalMapSnsControllerBase.mInstance instanceof SignalMapSnsController) {
                return SignalMapSnsControllerBase.mInstance;
            }
            SignalMapSnsControllerBase.mInstance = new SignalMapSnsController();
        }
        return SignalMapSnsControllerBase.mInstance;
    }

    private SignalMapSnsController() {
        this.mHandler = null;
        this.mWorkThread = null;
        this.mDdsPhoneId = -1;
        log("SignalMapSnsController init");
        mContext = PhoneGlobals.getInstance();
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        HandlerThread handlerThread = new HandlerThread("SignalMapSnsController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapHandler signalMapHandler = new SignalMapHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapHandler;
        signalMapHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        initBandData();
        initBandMappingScs();
    }

    class SignalMapHandler extends Handler {
        SignalMapHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SignalMapSnsController.this.logD("msg.what = " + message.what);
            if (message.what != 1) {
                return;
            }
            SignalMapSnsController.this.initAction();
            SignalMapSnsController.this.mDbHelper = CloudCellDbHelper.getInstance(SignalMapSnsController.mContext);
        }
    }

    @Override // com.android.phone.signalMap.SignalMapSnsControllerBase
    public void updateSignalMapCellCloud() {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            SignalMapCloudController signalMapCloudController = SignalMapCloudController.getInstance();
            this.mSignalMapCloudController = signalMapCloudController;
            String city = signalMapCloudController.getCity();
            if (TextUtils.isEmpty(city)) {
                log("city is empty!");
                return;
            }
            String signalMapSnsCellBlackListByCloud = this.mSignalMapCloudController.getSignalMapSnsCellBlackListByCloud(city);
            logD("SignalMapCellBlackList :" + signalMapSnsCellBlackListByCloud);
            if (TextUtils.isEmpty(signalMapSnsCellBlackListByCloud)) {
                log("SignalMapCellBlackList is empty");
                return;
            }
            try {
                this.mBlackCells = signalMapSnsCellBlackListByCloud.split(",");
            } catch (Exception e) {
                log("updateSignalMapCellCloud exception:" + e);
            }
        }
    }

    @Override // com.android.phone.signalMap.SignalMapSnsControllerBase
    public void setLteNrDubiousCellIfNeed(List list, String str) {
        if (updateLteNrDubiousData(list)) {
            if (!this.mLteDubiousCellInfoSList.isEmpty() && NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mDdsPhoneId, this.mLteDubiousCellInfoSList, 0, str);
            }
            if (this.mNrDubiousCellInfoSList.isEmpty() || NetworkRecoveryStrategyBase.getInstance() == null) {
                return;
            }
            NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mDdsPhoneId, this.mNrDubiousCellInfoSList, 0, str);
        }
    }

    private boolean updateLteNrDubiousData(List list) {
        if (list == null || list.isEmpty() || mActiveBand.isEmpty()) {
            log("updateLteNrDubiousData: dubiousCellData empty");
            return false;
        }
        this.mLteDubiousCellInfoSList.clear();
        this.mNrDubiousCellInfoSList.clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split("\\|");
            DubiousCellInfoS commonCellInfo = parseCommonCellInfo(strArrSplit);
            if (commonCellInfo == null) {
                return false;
            }
            try {
                long j = Long.parseLong(strArrSplit[3]);
                String str = strArrSplit[0];
                if ("lte".equals(str)) {
                    LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
                    lteDubiousCellInfoS.cellInfo = commonCellInfo;
                    lteDubiousCellInfoS.cgi = (int) j;
                    this.mLteDubiousCellInfoSList.add(lteDubiousCellInfoS);
                } else if ("nr".equals(str)) {
                    String[] strArrSplit2 = strArrSplit[4].split("_");
                    if (strArrSplit2.length != 3) {
                        return false;
                    }
                    int i = Integer.parseInt(strArrSplit2[2]);
                    NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
                    nrDubiousCellInfoS.cellInfo = commonCellInfo;
                    nrDubiousCellInfoS.cgi = j;
                    Integer num = (Integer) mBandMappingScs.get(Integer.valueOf(i));
                    if (num == null) {
                        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(strArrSplit[8]);
                    } else {
                        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(num));
                    }
                    this.mNrDubiousCellInfoSList.add(nrDubiousCellInfoS);
                } else {
                    log("updateLteNrDubiousData: network_type exception");
                    return false;
                }
            } catch (Exception e) {
                log("updateLteNrDubiousData exception:" + e);
                return false;
            }
        }
        logD("updateLteNrDubiousData mLteDubiousCellInfoSList=" + this.mLteDubiousCellInfoSList);
        logD("updateLteNrDubiousData mNrDubiousCellInfoSList=" + this.mNrDubiousCellInfoSList);
        return true;
    }

    public void setOrResetLteDubiousCellIfNeed(ArrayList arrayList, boolean z, String str) {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mDdsPhoneId, arrayList, !z ? 1 : 0, str);
        }
    }

    public void setOrReserNrDubiousCellIfNeed(ArrayList arrayList, boolean z, String str) {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mDdsPhoneId, arrayList, !z ? 1 : 0, str);
        }
    }

    public LteDubiousCellInfoS constructLteDubiousCellInfo(int i, short s, short s2, int i2, short s3, int i3, String str) {
        Map map = mActiveBand;
        if (map.get("E_UTRA_OPERATING_BAND_" + i3) == null) {
            log("lte band is null");
            return null;
        }
        LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
        DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
        int dubiousCellCause = getDubiousCellCause(str.split(":"));
        PlmnIdS plmnIdS = new PlmnIdS();
        plmnIdS.mcc = s;
        plmnIdS.mnc = s2;
        dubiousCellInfoS.plmnId = plmnIdS;
        dubiousCellInfoS.activeBand = ((Integer) map.get("E_UTRA_OPERATING_BAND_" + i3)).intValue();
        dubiousCellInfoS.arfcn = i2;
        dubiousCellInfoS.pci = s3;
        dubiousCellInfoS.causeCodeBitMask = dubiousCellCause;
        lteDubiousCellInfoS.cellInfo = dubiousCellInfoS;
        lteDubiousCellInfoS.cgi = i;
        return lteDubiousCellInfoS;
    }

    public NrDubiousCellInfoS constructNrDubiousCellInfo(long j, short s, short s2, int i, short s3, int i2, String str) {
        Map map = mActiveBand;
        if (map.get("NR5G_BAND_" + i2) == null) {
            log("nr band is null");
            return null;
        }
        NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
        DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
        int dubiousCellCause = getDubiousCellCause(str.split(":"));
        PlmnIdS plmnIdS = new PlmnIdS();
        plmnIdS.mcc = s;
        plmnIdS.mnc = s2;
        dubiousCellInfoS.plmnId = plmnIdS;
        dubiousCellInfoS.activeBand = ((Integer) map.get("NR5G_BAND_" + i2)).intValue();
        dubiousCellInfoS.arfcn = i;
        dubiousCellInfoS.pci = s3;
        dubiousCellInfoS.causeCodeBitMask = dubiousCellCause;
        nrDubiousCellInfoS.cellInfo = dubiousCellInfoS;
        nrDubiousCellInfoS.cgi = j;
        Integer num = (Integer) mBandMappingScs.get(Integer.valueOf(i2));
        if (num == null) {
            nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs("30");
            return nrDubiousCellInfoS;
        }
        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(num));
        return nrDubiousCellInfoS;
    }

    private boolean checkCellInfo(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            log("parseCommonCellInfo: cellInfo exception");
            return false;
        }
        String str = strArr[0];
        if (!TextUtils.isEmpty(str) && ((!"lte".equals(str) || strArr.length == 8) && ((!"nr".equals(str) || strArr.length == 9) && ("lte".equals(str) || "nr".equals(str))))) {
            return true;
        }
        log("parseCommonCellInfo: cellInfo exception");
        return false;
    }

    private DubiousCellInfoS parseCommonCellInfo(String[] strArr) {
        if (!checkCellInfo(strArr)) {
            return null;
        }
        try {
            short s = Short.parseShort(strArr[1]);
            short s2 = Short.parseShort(strArr[2]);
            int i = Integer.parseInt(strArr[5]);
            short s3 = Short.parseShort(strArr[6]);
            int dubiousCellCause = getDubiousCellCause(strArr[7].split(":"));
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = s;
            plmnIdS.mnc = s2;
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.plmnId = plmnIdS;
            Map map = mActiveBand;
            if (map.get(strArr[4]) == null) {
                log("parseCommonCellInfo: a non-existent band");
                return null;
            }
            dubiousCellInfoS.activeBand = ((Integer) map.get(strArr[4])).intValue();
            dubiousCellInfoS.arfcn = i;
            dubiousCellInfoS.pci = s3;
            dubiousCellInfoS.causeCodeBitMask = dubiousCellCause;
            return dubiousCellInfoS;
        } catch (Exception e) {
            log("parseCommonCellInfo exception:" + e);
            return null;
        }
    }

    private int getNrDubiousCellScs(String str) {
        if ("15".equals(str)) {
            return 0;
        }
        if ("30".equals(str)) {
            return 1;
        }
        if ("60".equals(str)) {
            return 2;
        }
        if ("120".equals(str)) {
            return 3;
        }
        return "240".equals(str) ? 4 : 0;
    }

    private int getDubiousCellCause(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return 0;
        }
        int i = 0;
        for (String str : strArr) {
            if ("CEF".equals(str)) {
                i |= 1;
            } else if ("RLF".equals(str)) {
                i |= 2;
            } else if ("PINGPONG".equals(str)) {
                i |= 4;
            } else if ("LOW_DATA_RATE_PS".equals(str)) {
                i |= 8;
            } else if ("LOW_DATA_RATE_IMS".equals(str)) {
                i |= 16;
            } else if ("NAS_REJ_CELLBARRING".equals(str)) {
                i |= 32;
            }
        }
        return i;
    }

    private void initBandData() {
        for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
            String[] strArrSplit = str.split("=");
            mActiveBand.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
        }
    }

    private void initBandMappingScs() {
        for (String str : mContext.getResources().getStringArray(R.array.dubious_cell_band_mapping_scs)) {
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length != 2) {
                log("bandMappingScs config exception");
                return;
            }
            String str2 = strArrSplit[0];
            if (str2 == null || strArrSplit[1] == null) {
                log("initBandMappingScs exception");
                return;
            }
            mBandMappingScs.put(Integer.valueOf(Integer.parseInt(str2)), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
        }
    }

    private void log(String str) {
        Log.d("SignalMapSnsController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapSnsController", str);
        }
    }
}
