package com.android.phone.network;

import android.content.Context;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import com.android.phone.FiveGManagerBase;
import com.android.phone.ModemProvider;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.PhoneGlobals;
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
public class MiuiNetworkRecoveryManagerEx extends MiuiNetworkRecoveryManager {
    private static Context mContext;
    private static final Map mActiveBand = new HashMap();
    private static Map mBandMappingScs = new HashMap();
    private static ArrayList mCacheNrDubiousCellInfoList = new ArrayList();
    private static ArrayList mCacheLteDubiousCellInfoList = new ArrayList();

    public static MiuiNetworkRecoveryManager make() {
        if (MiuiNetworkRecoveryManager.mInstance == null) {
            MiuiNetworkRecoveryManager.mInstance = new MiuiNetworkRecoveryManagerEx();
        }
        return MiuiNetworkRecoveryManager.mInstance;
    }

    private MiuiNetworkRecoveryManagerEx() {
        mContext = PhoneGlobals.getInstance();
        if (ModemProvider.IS_QCOM) {
            initBandData();
            initBandMappingScs();
        }
    }

    @Override // com.android.phone.network.MiuiNetworkRecoveryManager
    protected boolean doSnsDataStall(boolean z, int i) {
        if (!ModemProvider.IS_QCOM) {
            return super.doSnsDataStall(z, i);
        }
        if (!isSupportSetDubiousCell()) {
            return false;
        }
        if (NetworkRecoveryStrategyBase.getInstance() == null) {
            return true;
        }
        NetworkRecoveryStrategyBase.getInstance().alleviateDataStall(this.mPhone.getPhoneId(), i, 1, z, "congestion");
        return true;
    }

    @Override // com.android.phone.network.MiuiNetworkRecoveryManager
    protected boolean doBarDubiousCell(boolean z, CellIdentity cellIdentity) {
        if (ModemProvider.IS_MTK) {
            return doBarDubiousCellForM(z, cellIdentity);
        }
        if (ModemProvider.IS_QCOM) {
            return doBarDubiousCellForQ(z, cellIdentity);
        }
        return super.doBarDubiousCell(z, cellIdentity);
    }

    private boolean doBarDubiousCellForM(boolean z, CellIdentity cellIdentity) {
        int earfcn;
        int pci;
        int i;
        if (!z) {
            return true;
        }
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            earfcn = cellIdentityNr.getNrarfcn();
            pci = cellIdentityNr.getPci();
            i = 2;
        } else {
            if (!(cellIdentity instanceof CellIdentityLte)) {
                return false;
            }
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            earfcn = cellIdentityLte.getEarfcn();
            pci = cellIdentityLte.getPci();
            i = 1;
        }
        return barCell(!z ? 1 : 0, 1, i, earfcn, pci, 1200L);
    }

    private boolean doBarDubiousCellForQ(boolean z, CellIdentity cellIdentity) {
        if (!isSupportSetDubiousCell()) {
            return false;
        }
        if (!z) {
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mPhone.getPhoneId(), mCacheNrDubiousCellInfoList, 1, "congestion");
                NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mPhone.getPhoneId(), mCacheLteDubiousCellInfoList, 1, "congestion");
            }
            mCacheNrDubiousCellInfoList.clear();
            mCacheLteDubiousCellInfoList.clear();
            return true;
        }
        if (cellIdentity == null) {
            return false;
        }
        short s = Short.parseShort(cellIdentity.getMccString());
        short s2 = Short.parseShort(cellIdentity.getMncString());
        boolean z2 = cellIdentity.getMncString().length() == 3;
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            Map map = mActiveBand;
            if (map.get("NR5G_BAND_" + cellIdentityNr.getBands()[0]) == null) {
                log("nr band is null");
                return false;
            }
            int i = cellIdentityNr.getBands()[0];
            DubiousCellInfoS dubiousCellInfoS = getDubiousCellInfoS(s, s2, z2, ((Integer) map.get("NR5G_BAND_" + i)).intValue(), cellIdentityNr.getNrarfcn(), (short) cellIdentityNr.getPci());
            NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
            nrDubiousCellInfoS.cellInfo = dubiousCellInfoS;
            nrDubiousCellInfoS.cgi = cellIdentityNr.getNci();
            int i2 = (Integer) mBandMappingScs.get(Integer.valueOf(i));
            if (i2 == null) {
                i2 = 30;
            }
            nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(i2));
            mCacheNrDubiousCellInfoList.add(nrDubiousCellInfoS);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mPhone.getPhoneId(), mCacheNrDubiousCellInfoList, 0, "congestion");
            }
        } else if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            Map map2 = mActiveBand;
            if (map2.get("E_UTRA_OPERATING_BAND_" + cellIdentityLte.getBands()[0]) == null) {
                log("lte band is null");
                return false;
            }
            DubiousCellInfoS dubiousCellInfoS2 = getDubiousCellInfoS(s, s2, z2, ((Integer) map2.get("E_UTRA_OPERATING_BAND_" + cellIdentityLte.getBands()[0])).intValue(), cellIdentityLte.getEarfcn(), (short) cellIdentityLte.getPci());
            LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
            lteDubiousCellInfoS.cellInfo = dubiousCellInfoS2;
            lteDubiousCellInfoS.cgi = cellIdentityLte.getCi();
            mCacheLteDubiousCellInfoList.add(lteDubiousCellInfoS);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mPhone.getPhoneId(), mCacheLteDubiousCellInfoList, 0, "congestion");
            }
        }
        return true;
    }

    private boolean barCell(int i, int i2, int i3, int i4, int i5, long j) {
        String str = String.format("AT+EDMFAPP=20101,%d,%d,%d,%d,%d,%d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Long.valueOf(j));
        log("do barCell");
        return sendAtCommand(new String[]{str, ""});
    }

    private boolean sendAtCommand(String[] strArr) {
        FiveGManagerBase.getInstance().sendAtCommand(this.mPhone.getPhoneId(), strArr, (Message) null);
        return true;
    }

    private DubiousCellInfoS getDubiousCellInfoS(short s, short s2, boolean z, int i, int i2, short s3) {
        PlmnIdS plmnIdS = new PlmnIdS();
        plmnIdS.mcc = s;
        plmnIdS.mnc = s2;
        plmnIdS.mncIncludesPcsDigit = z;
        DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
        dubiousCellInfoS.plmnId = plmnIdS;
        dubiousCellInfoS.activeBand = i;
        dubiousCellInfoS.arfcn = i2;
        dubiousCellInfoS.pci = s3;
        dubiousCellInfoS.causeCodeBitMask = 8;
        return dubiousCellInfoS;
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

    @Override // com.android.phone.network.MiuiNetworkRecoveryManager
    protected boolean isSupportSetDubiousCell() {
        if (ModemProvider.IS_QCOM) {
            return TelephonyManagerEx.getDefault().isQmsSupported();
        }
        return super.isSupportSetDubiousCell();
    }
}
