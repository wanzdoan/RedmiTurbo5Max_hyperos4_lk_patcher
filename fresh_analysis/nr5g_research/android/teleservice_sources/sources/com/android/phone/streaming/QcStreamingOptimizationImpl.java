package com.android.phone.streaming;

import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.CellPreferManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;

/* JADX INFO: loaded from: classes.dex */
public class QcStreamingOptimizationImpl extends StreamingOptimizationController {
    private final Map mActiveBand = new HashMap();
    private final Map mBandMappingScs = new HashMap();
    private final ArrayList mCacheNrDubiousCellInfoList = new ArrayList();
    private final ArrayList mCacheLteDubiousCellInfoList = new ArrayList();
    private final Object mDubiousCellLock = new Object();
    private volatile boolean mBandDataInitialized = false;
    private volatile boolean mBandMappingScsInitialized = false;
    private final NetworkRecoveryStrategyBase mRecovery = NetworkRecoveryStrategyBase.getInstance();

    public static void make() {
        if (StreamingOptimizationController.mInstance instanceof QcStreamingOptimizationImpl) {
            return;
        }
        StreamingOptimizationController.mInstance = new QcStreamingOptimizationImpl();
        ((QcStreamingOptimizationImpl) StreamingOptimizationController.mInstance).initBandDataIfNeeded();
        ((QcStreamingOptimizationImpl) StreamingOptimizationController.mInstance).initBandMappingScsIfNeeded();
    }

    private void initBandDataIfNeeded() {
        if (this.mBandDataInitialized) {
            return;
        }
        synchronized (this) {
            if (this.mBandDataInitialized) {
                return;
            }
            try {
                for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
                    String[] strArrSplit = str.split("=");
                    if (strArrSplit.length == 2) {
                        try {
                            this.mActiveBand.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
                        } catch (NumberFormatException unused) {
                            Log.d("QcStreamingOptimizationImpl", "Invalid band value: " + strArrSplit[1]);
                        }
                    }
                }
                this.mBandDataInitialized = true;
            } catch (Exception e) {
                this.mBandDataInitialized = true;
                Log.e("QcStreamingOptimizationImpl", "Failed to init band data", e);
            }
        }
    }

    private void initBandMappingScsIfNeeded() {
        if (this.mBandMappingScsInitialized || this.mContext == null) {
            return;
        }
        synchronized (this) {
            try {
                try {
                    for (String str : this.mContext.getResources().getStringArray(R.array.dubious_cell_band_mapping_scs)) {
                        String[] strArrSplit = str.split(",");
                        if (strArrSplit.length != 2) {
                            Log.d("QcStreamingOptimizationImpl", "bandMappingScs config exception");
                        } else {
                            String str2 = strArrSplit[0];
                            if (str2 == null || strArrSplit[1] == null) {
                                Log.d("QcStreamingOptimizationImpl", "initBandMappingScs exception: null value");
                            } else {
                                try {
                                    this.mBandMappingScs.put(Integer.valueOf(Integer.parseInt(str2)), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
                                } catch (NumberFormatException unused) {
                                    Log.d("QcStreamingOptimizationImpl", "Invalid band mapping scs value");
                                }
                            }
                        }
                    }
                    this.mBandMappingScsInitialized = true;
                } catch (Exception e) {
                    this.mBandMappingScsInitialized = true;
                    Log.e("QcStreamingOptimizationImpl", "Failed to init band mapping scs", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void processPlatformMessage(Message message) {
        if (message == null) {
            Log.e("QcStreamingOptimizationImpl", "processPlatformMessage: message is null");
            return;
        }
        int i = message.what;
        if (i == 5) {
            setCoverageState(message.arg1, 1, "low_bandwidth_congestion_timeout");
            return;
        }
        if (i == 6) {
            handleCoverageStateTimeout(message.arg1);
            return;
        }
        if (i == 7) {
            handleStableCellTimeout();
        } else {
            if (i == 8) {
                disableStableCellStrategy();
                return;
            }
            Log.e("QcStreamingOptimizationImpl", "Unknown message: " + message.what);
        }
    }

    private void handleStableCellTimeout() {
        if (this.mOptStateMgr.getOptState() != 0) {
            Log.d("QcStreamingOptimizationImpl", "In optimization state, skip stable cell strategy");
        } else if (this.mOptStateMgr.getSceneContext().isDynamicScene() || !this.mOptStateMgr.getSceneContext().isResidentCell()) {
            Log.d("QcStreamingOptimizationImpl", "In dynamic scene, skip stable cell strategy");
        } else {
            enableStableCellStrategy();
        }
    }

    private void handleCoverageStateTimeout(int i) {
        Log.i("QcStreamingOptimizationImpl", "handleCoverageStateTimeout: slotId=" + i);
        setCoverageState(i, 2, "low_bandwidth_congestion_timeout");
    }

    private void setCoverageState(int i, int i2, String str) {
        Log.i("QcStreamingOptimizationImpl", "setCoverageState: slotId=" + i + ", coverageState=" + i2 + ", triggerReason=" + str);
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = NetworkRecoveryStrategyBase.getInstance();
        if (networkRecoveryStrategyBase != null) {
            networkRecoveryStrategyBase.setCoverageState(i, i2, str);
        }
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void executeDataStallEscape(int i) {
        Log.d("QcStreamingOptimizationImpl", "executeDataStallEscape slotId: " + i);
        if (TelephonyManagerEx.getDefault().isQmsSupported()) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone == null) {
                Log.d("QcStreamingOptimizationImpl", "executeDataStallEscape phone is null");
                return;
            }
            CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
            if (currentCellIdentity == null) {
                Log.e("QcStreamingOptimizationImpl", "executeDataStallEscape Current cell is null");
                return;
            }
            try {
                if (currentCellIdentity instanceof CellIdentityNr) {
                    addNrDubiousCell(phone, (CellIdentityNr) currentCellIdentity);
                } else if (currentCellIdentity instanceof CellIdentityLte) {
                    addLteDubiousCell(phone, (CellIdentityLte) currentCellIdentity);
                }
            } catch (Exception e) {
                Log.e("QcStreamingOptimizationImpl", "Error executing data stall escape", e);
            }
        }
    }

    private void addNrDubiousCell(Phone phone, CellIdentityNr cellIdentityNr) {
        int[] bands = cellIdentityNr.getBands();
        if (bands == null || bands.length == 0) {
            Log.d("QcStreamingOptimizationImpl", "NR bands is empty or null");
            return;
        }
        int i = bands[0];
        Integer num = (Integer) this.mActiveBand.get("NR5G_BAND_" + i);
        if (num == null) {
            Log.d("QcStreamingOptimizationImpl", "NR band not active: " + i);
            return;
        }
        DubiousCellInfoS dubiousCellInfoSCreateDubiousCellInfo = createDubiousCellInfo(cellIdentityNr, num.intValue());
        if (dubiousCellInfoSCreateDubiousCellInfo == null) {
            Log.d("QcStreamingOptimizationImpl", "Failed to create dubious cell info");
            return;
        }
        NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
        nrDubiousCellInfoS.cellInfo = dubiousCellInfoSCreateDubiousCellInfo;
        nrDubiousCellInfoS.cgi = cellIdentityNr.getNci();
        int i2 = (Integer) this.mBandMappingScs.get(Integer.valueOf(i));
        if (i2 == null) {
            i2 = 30;
        }
        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(i2));
        synchronized (this.mDubiousCellLock) {
            try {
                if (this.mCacheNrDubiousCellInfoList.size() >= 100) {
                    this.mCacheNrDubiousCellInfoList.remove(0);
                }
                this.mCacheNrDubiousCellInfoList.add(nrDubiousCellInfoS);
                this.mRecovery.setNrDubiousCell(phone.getPhoneId(), this.mCacheNrDubiousCellInfoList, 0, "congestion");
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void addLteDubiousCell(Phone phone, CellIdentityLte cellIdentityLte) {
        int[] bands = cellIdentityLte.getBands();
        if (bands == null || bands.length == 0) {
            Log.d("QcStreamingOptimizationImpl", "LTE bands is empty or null");
            return;
        }
        int i = bands[0];
        Integer num = (Integer) this.mActiveBand.get("E_UTRA_OPERATING_BAND_" + i);
        if (num == null) {
            Log.d("QcStreamingOptimizationImpl", "LTE band not active: " + i);
            return;
        }
        DubiousCellInfoS dubiousCellInfoSCreateDubiousCellInfo = createDubiousCellInfo(cellIdentityLte, num.intValue());
        if (dubiousCellInfoSCreateDubiousCellInfo == null) {
            Log.d("QcStreamingOptimizationImpl", "Failed to create dubious cell info");
            return;
        }
        LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
        lteDubiousCellInfoS.cellInfo = dubiousCellInfoSCreateDubiousCellInfo;
        lteDubiousCellInfoS.cgi = cellIdentityLte.getCi();
        synchronized (this.mDubiousCellLock) {
            try {
                if (this.mCacheLteDubiousCellInfoList.size() >= 100) {
                    this.mCacheLteDubiousCellInfoList.remove(0);
                }
                this.mCacheLteDubiousCellInfoList.add(lteDubiousCellInfoS);
                this.mRecovery.setLteDubiousCell(phone.getPhoneId(), this.mCacheLteDubiousCellInfoList, 0, "congestion");
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private DubiousCellInfoS createDubiousCellInfo(CellIdentity cellIdentity, int i) {
        int pci;
        String mccString = cellIdentity.getMccString();
        String mncString = cellIdentity.getMncString();
        if (mccString == null || mncString == null) {
            Log.d("QcStreamingOptimizationImpl", "MCC/MNC is null, skip creating dubious cell info");
            return null;
        }
        try {
            short s = Short.parseShort(mccString);
            short s2 = Short.parseShort(mncString);
            boolean z = mncString.length() == 3;
            boolean z2 = cellIdentity instanceof CellIdentityNr;
            int nrarfcn = z2 ? ((CellIdentityNr) cellIdentity).getNrarfcn() : ((CellIdentityLte) cellIdentity).getEarfcn();
            if (z2) {
                pci = ((CellIdentityNr) cellIdentity).getPci();
            } else {
                pci = ((CellIdentityLte) cellIdentity).getPci();
            }
            short s3 = (short) pci;
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = s;
            plmnIdS.mnc = s2;
            plmnIdS.mncIncludesPcsDigit = z;
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.plmnId = plmnIdS;
            dubiousCellInfoS.activeBand = i;
            dubiousCellInfoS.arfcn = nrarfcn;
            dubiousCellInfoS.pci = s3;
            dubiousCellInfoS.causeCodeBitMask = 8;
            return dubiousCellInfoS;
        } catch (NumberFormatException unused) {
            Log.d("QcStreamingOptimizationImpl", "Invalid MCC/MNC value: mcc=" + mccString + ", mnc=" + mncString);
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

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void resetDataStallEscape(int i) {
        Log.d("QcStreamingOptimizationImpl", "resetDataStallEscapeImpl slotId=" + i);
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            synchronized (this.mDubiousCellLock) {
                this.mRecovery.setNrDubiousCell(phone.getPhoneId(), this.mCacheNrDubiousCellInfoList, 1, "congestion");
                this.mRecovery.setLteDubiousCell(phone.getPhoneId(), this.mCacheLteDubiousCellInfoList, 1, "congestion");
                this.mCacheNrDubiousCellInfoList.clear();
                this.mCacheLteDubiousCellInfoList.clear();
            }
        }
    }

    private void enableStableCellStrategy() {
        Log.i("QcStreamingOptimizationImpl", "enableStableCellStrategy: QC platform");
        this.mOptStateMgr.getAuxState().setStableCellEnabled(true);
        enableHdr(true);
        Log.i("QcStreamingOptimizationImpl", "QC stable cell strategy enabled (HDR)");
    }

    private void disableStableCellStrategy() {
        Log.i("QcStreamingOptimizationImpl", "disableStableCellStrategy: QC platform");
        this.mOptStateMgr.getAuxState().setStableCellEnabled(false);
        enableHdr(false);
        Log.i("QcStreamingOptimizationImpl", "QC stable cell strategy disabled");
    }

    private void enableHdr(boolean z) {
        CellIdentity currentCellIdentity;
        CellInfoData cellInfoDataProcessLteCell;
        Phone phone = MiuiPhoneUtils.getPhone(this.mOptStateMgr.getSceneContext().getDdsSlot());
        if (phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
            return;
        }
        try {
            String mccString = currentCellIdentity.getMccString();
            String mncString = currentCellIdentity.getMncString();
            if (currentCellIdentity instanceof CellIdentityNr) {
                cellInfoDataProcessLteCell = processNrCell((CellIdentityNr) currentCellIdentity, z);
            } else {
                if (!(currentCellIdentity instanceof CellIdentityLte)) {
                    Log.d("QcStreamingOptimizationImpl", "Unsupported cell identity type");
                    return;
                }
                cellInfoDataProcessLteCell = processLteCell((CellIdentityLte) currentCellIdentity, z);
            }
            if (cellInfoDataProcessLteCell == null) {
                return;
            }
            String strJoin = String.join(",", cellInfoDataProcessLteCell.networkType, mccString, mncString, String.valueOf(cellInfoDataProcessLteCell.cgi), cellInfoDataProcessLteCell.activeBandKey, String.valueOf(cellInfoDataProcessLteCell.arfcn), String.valueOf(cellInfoDataProcessLteCell.pci));
            if (MiuiPhoneUtils.isCellPreferOptSupported() && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
                CellPreferManager.getInstance().processSetHdrCellInfo(strJoin);
            }
        } catch (Exception e) {
            Log.e("QcStreamingOptimizationImpl", "Error setting HDR cell info", e);
        }
    }

    private CellInfoData processNrCell(CellIdentityNr cellIdentityNr, boolean z) {
        int[] bands = cellIdentityNr.getBands();
        if (!validateBands(bands)) {
            return null;
        }
        String str = "NR5G_BAND_" + bands[0];
        if (this.mActiveBand.get(str) == null) {
            Log.d("QcStreamingOptimizationImpl", "nr band is null");
            return null;
        }
        CellInfoData cellInfoData = new CellInfoData();
        cellInfoData.networkType = "nr";
        cellInfoData.activeBandKey = str;
        cellInfoData.cgi = z ? cellIdentityNr.getNci() : 0L;
        cellInfoData.arfcn = z ? cellIdentityNr.getNrarfcn() : 0;
        cellInfoData.pci = z ? cellIdentityNr.getPci() : 2000;
        return cellInfoData;
    }

    private CellInfoData processLteCell(CellIdentityLte cellIdentityLte, boolean z) {
        int[] bands = cellIdentityLte.getBands();
        if (!validateBands(bands)) {
            return null;
        }
        String str = "E_UTRA_OPERATING_BAND_" + bands[0];
        if (this.mActiveBand.get(str) == null) {
            Log.d("QcStreamingOptimizationImpl", "lte band is null");
            return null;
        }
        CellInfoData cellInfoData = new CellInfoData();
        cellInfoData.networkType = "lte";
        cellInfoData.activeBandKey = str;
        cellInfoData.cgi = z ? cellIdentityLte.getCi() : 0L;
        cellInfoData.arfcn = z ? cellIdentityLte.getEarfcn() : 0;
        cellInfoData.pci = z ? cellIdentityLte.getPci() : 2000;
        return cellInfoData;
    }

    private boolean validateBands(int[] iArr) {
        if (iArr != null && iArr.length != 0) {
            return true;
        }
        Log.d("QcStreamingOptimizationImpl", "bands is null or empty");
        return false;
    }

    class CellInfoData {
        String activeBandKey;
        int arfcn;
        long cgi;
        String networkType;
        int pci;

        private CellInfoData() {
        }
    }
}
