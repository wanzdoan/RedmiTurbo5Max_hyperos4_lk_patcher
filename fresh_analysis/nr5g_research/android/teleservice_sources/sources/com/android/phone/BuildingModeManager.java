package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.statistics.utils.DigestUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class BuildingModeManager extends BuildingModeManagerBase {
    private static boolean VDBG = false;
    private static final Map mActiveBand = new HashMap();
    private static Map mBandMappingScs = new HashMap();
    private static Context mContext;
    private static TelephonyCallBack mTelephonyCallBack;
    private static WifiManager mWifiManager;
    private long mCellChgTime;
    private BuildingModeHandler mHandler;
    private HandlerThread mWorkThread;
    private boolean mIsWifiConnected = false;
    private boolean mLastWifiStatus = false;
    private int mDdsPhoneId = -1;
    private String mCurWifiSsid = null;
    private ArrayDeque mWifiSsidQueue = new ArrayDeque();
    private int mWifiSsidQueueMaxSize = 10;
    private boolean mIsBuildingMode = false;
    private CellPingPongDBHelper mCellPingPongDBHelper = null;
    private Map mGlobalCellIdCountMap = new HashMap();
    private Long mLastGlobalCellId = Long.MAX_VALUE;
    private String mDdsPlmn = null;
    private long mLastSetDubiousCellTime = -1;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.BuildingModeManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            BuildingModeManager.this.log("receive action ".concat(action));
            if (action.equals("android.net.wifi.STATE_CHANGE")) {
                BuildingModeManager.this.mHandler.obtainMessage(2, intent).sendToTarget();
                return;
            }
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                BuildingModeManager.this.mHandler.obtainMessage(4).sendToTarget();
            } else if (action.equals("BuildingModeManager.intent.debugall")) {
                BuildingModeManager.this.mHandler.obtainMessage(100, intent).sendToTarget();
            }
        }
    };

    public static BuildingModeManagerBase make() {
        if (!(BuildingModeManagerBase.mInstance instanceof BuildingModeManager)) {
            BuildingModeManagerBase.mInstance = new BuildingModeManager();
        }
        return BuildingModeManagerBase.mInstance;
    }

    private BuildingModeManager() {
        log("BuildingModeManager Init");
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("BuildingModeManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        BuildingModeHandler buildingModeHandler = new BuildingModeHandler(this.mWorkThread.getLooper());
        this.mHandler = buildingModeHandler;
        buildingModeHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        mWifiManager = (WifiManager) mContext.getSystemService("wifi");
        if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
            this.mCellPingPongDBHelper = CellPingPongDBHelper.getInstance(mContext);
        }
        initBandData();
        initBandMappingScs();
        fetchWifiSsidQueue();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        intentFilter.addAction("BuildingModeManager.intent.debugall");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTelephonyCallBack() {
        if (mTelephonyCallBack == null) {
            log("registerTelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        if (mTelephonyCallBack != null) {
            log("unregisterTelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(mTelephonyCallBack);
            mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            BuildingModeManager.this.mHandler.obtainMessage(3, i, 0, cellLocation).sendToTarget();
        }
    }

    class BuildingModeHandler extends Handler {
        BuildingModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (BuildingModeManager.VDBG) {
                BuildingModeManager.this.log("handleMessage=" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                BuildingModeManager.this.initAction();
                return;
            }
            if (i == 2) {
                BuildingModeManager.this.processWifiStateChanged((Intent) message.obj);
                return;
            }
            if (i == 3) {
                BuildingModeManager.this.processCellLocationChange(message.arg1, (CellLocation) message.obj);
                return;
            }
            if (i == 4) {
                BuildingModeManager buildingModeManager = BuildingModeManager.this;
                buildingModeManager.mDdsPhoneId = buildingModeManager.getDefaultDataSlotId();
                BuildingModeManager buildingModeManager2 = BuildingModeManager.this;
                buildingModeManager2.mDdsPlmn = MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(buildingModeManager2.mDdsPhoneId));
                if (BuildingModeManager.VDBG) {
                    BuildingModeManager.this.log("EVENT_DATA_SLOT_READY_OR_CHANGED mDdsPlmn=" + BuildingModeManager.this.mDdsPlmn + ", mDdsPhoneId=" + BuildingModeManager.this.mDdsPhoneId);
                    return;
                }
                return;
            }
            if (i == 5) {
                if (!BuildingModeManager.this.mIsWifiConnected || TextUtils.isEmpty(BuildingModeManager.this.mCurWifiSsid) || TextUtils.isEmpty(BuildingModeManager.this.mDdsPlmn)) {
                    return;
                }
                BuildingModeManager.this.setLteNrDubiousCellIfNeed(SystemClock.elapsedRealtime());
                BuildingModeManager buildingModeManager3 = BuildingModeManager.this;
                buildingModeManager3.updateWifiSsidQueue(buildingModeManager3.mCurWifiSsid);
                BuildingModeManager.this.registerTelephonyCallBack();
                return;
            }
            if (i != 100) {
                return;
            }
            BuildingModeManager.VDBG = true;
            BuildingModeManager.this.mWifiSsidQueueMaxSize = ((Intent) message.obj).getIntExtra("wifi_ssid_queue_max_size", 10);
            if (!BuildingModeManager.this.mWifiSsidQueue.isEmpty()) {
                BuildingModeManager.this.mWifiSsidQueue.clear();
            }
            if (BuildingModeManager.this.mCellPingPongDBHelper != null) {
                BuildingModeManager.this.mCellPingPongDBHelper.clearAllRecord();
            }
            BuildingModeManager.this.log("EVENT_DEBUG_ALL, mWifiSsidQueueMaxSize=" + BuildingModeManager.this.mWifiSsidQueueMaxSize);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChange(int i, CellLocation cellLocation) {
        long ci;
        boolean z;
        if (VDBG) {
            log("processCellLocationChange, phoneId=" + i);
        }
        if (TextUtils.isEmpty(this.mCurWifiSsid) || TextUtils.isEmpty(this.mDdsPlmn)) {
            log("invalid wifi ssid or plmn");
            return;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !isDdsPhoneId(i) || cellLocation == null || cellLocation.isEmpty() || currentCellIdentity == null) {
            log("invalid phone info");
            return;
        }
        if (currentCellIdentity instanceof CellIdentityNr) {
            ci = ((CellIdentityNr) currentCellIdentity).getNci();
            z = true;
        } else if (currentCellIdentity instanceof CellIdentityLte) {
            ci = ((CellIdentityLte) currentCellIdentity).getCi();
            z = false;
        } else {
            log("processCellLocationChange: ignore non 4/5G cell");
            return;
        }
        if (ci == this.mLastGlobalCellId.longValue() || ci == 2147483647L || ci == Long.MAX_VALUE) {
            if (VDBG) {
                log("skip globalCellId for invalid or no change, globalCellId=" + ci);
                return;
            }
            return;
        }
        this.mLastGlobalCellId = Long.valueOf(ci);
        if (VDBG) {
            log("processCellLocationChange: new globalCellId=" + ci);
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.mCellChgTime > 30000) {
            this.mGlobalCellIdCountMap.clear();
        }
        this.mCellChgTime = jElapsedRealtime;
        boolean zContainsKey = this.mGlobalCellIdCountMap.containsKey(Long.valueOf(ci));
        Map map = this.mGlobalCellIdCountMap;
        if (zContainsKey) {
            map.replace(Long.valueOf(ci), Integer.valueOf(((Integer) this.mGlobalCellIdCountMap.get(Long.valueOf(ci))).intValue() + 1));
            if (((Integer) this.mGlobalCellIdCountMap.get(Long.valueOf(ci))).intValue() > 2) {
                this.mGlobalCellIdCountMap.clear();
                long j = this.mLastSetDubiousCellTime;
                if (j != -1 && jElapsedRealtime - j <= 300000) {
                    recordDubiousCellPingPongFail();
                }
                if (isExistCellInDb(ci, z)) {
                    return;
                }
                recordCellInfoToDb(currentCellIdentity);
                setLteNrDubiousCellIfNeed(jElapsedRealtime);
                return;
            }
            return;
        }
        map.put(Long.valueOf(ci), 1);
        if (this.mGlobalCellIdCountMap.size() > 3) {
            log("more than 3 cells found, NO pingpong");
            this.mGlobalCellIdCountMap.clear();
        }
    }

    private void recordCellInfoToDb(CellIdentity cellIdentity) {
        CellPingPongDBHelper cellPingPongDBHelper;
        if (cellIdentity == null || (cellPingPongDBHelper = this.mCellPingPongDBHelper) == null) {
            return;
        }
        if (cellIdentity instanceof CellIdentityNr) {
            if (cellPingPongDBHelper.getNrDubiousCellCount(this.mCurWifiSsid, this.mDdsPlmn) >= 16) {
                log("recordCellInfoToDb: exceeds nr limit size");
                return;
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            short s = Short.parseShort(cellIdentityNr.getMccString());
            short s2 = Short.parseShort(cellIdentityNr.getMncString());
            boolean z = cellIdentityNr.getMncString().length() == 3;
            long nci = cellIdentityNr.getNci();
            int[] bands = cellIdentityNr.getBands();
            if (bands == null || bands.length == 0) {
                log("nr bands array is null or empty");
                return;
            }
            int i = bands[0];
            Map map = mActiveBand;
            if (map.get("NR5G_BAND_" + i) == null) {
                log("nr band is null");
                return;
            }
            int iIntValue = ((Integer) map.get("NR5G_BAND_" + i)).intValue();
            int nrarfcn = cellIdentityNr.getNrarfcn();
            short pci = (short) cellIdentityNr.getPci();
            Integer num = (Integer) mBandMappingScs.get(Integer.valueOf(i));
            if (num == null) {
                num = 30;
            }
            log("recordCellInfoToDb: nr mcc=" + ((int) s) + ", mnc=" + ((int) s2) + ", mncIncludesPcsDigit=" + z + ", activeBand=" + iIntValue + ", arfcn=" + nrarfcn + ", pci=" + ((int) pci) + ", scs=" + num);
            this.mCellPingPongDBHelper.insertRecord(new CellPingPongDBHelper.CellInfo(this.mCurWifiSsid, this.mDdsPlmn, s, s2, z, nci, iIntValue, nrarfcn, pci, num.intValue()), true);
            return;
        }
        if (cellIdentity instanceof CellIdentityLte) {
            if (cellPingPongDBHelper.getLteDubiousCellCount(this.mCurWifiSsid, this.mDdsPlmn) >= 16) {
                log("recordCellInfoToDb: exceeds lte limit size");
                return;
            }
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            short s3 = Short.parseShort(cellIdentityLte.getMccString());
            short s4 = Short.parseShort(cellIdentityLte.getMncString());
            boolean z2 = cellIdentityLte.getMncString().length() == 3;
            long ci = cellIdentityLte.getCi();
            int[] bands2 = cellIdentityLte.getBands();
            if (bands2 == null || bands2.length == 0) {
                log("lte bands array is null or empty");
                return;
            }
            int i2 = bands2[0];
            Map map2 = mActiveBand;
            if (map2.get("E_UTRA_OPERATING_BAND_" + i2) == null) {
                log("lte band is null");
                return;
            }
            int iIntValue2 = ((Integer) map2.get("E_UTRA_OPERATING_BAND_" + i2)).intValue();
            int earfcn = cellIdentityLte.getEarfcn();
            short pci2 = (short) cellIdentityLte.getPci();
            log("recordCellInfoToDb: lte mcc=" + ((int) s3) + ", mnc=" + ((int) s4) + ", mncIncludesPcsDigit=" + z2 + ", activeBand=" + iIntValue2 + ", arfcn=" + earfcn + ", pci=" + ((int) pci2));
            this.mCellPingPongDBHelper.insertRecord(new CellPingPongDBHelper.CellInfo(this.mCurWifiSsid, this.mDdsPlmn, s3, s4, z2, ci, iIntValue2, earfcn, pci2), false);
        }
    }

    private boolean isExistCellInDb(long j, boolean z) {
        CellPingPongDBHelper cellPingPongDBHelper = this.mCellPingPongDBHelper;
        if (cellPingPongDBHelper == null) {
            return false;
        }
        if (z) {
            return cellPingPongDBHelper.isHasNrCellRecord(j);
        }
        return cellPingPongDBHelper.isHasLteCellRecord(j);
    }

    private boolean isDdsPhoneId(int i) {
        return i == this.mDdsPhoneId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processWifiStateChanged(Intent intent) {
        WifiManager wifiManager;
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        this.mIsWifiConnected = networkInfo != null && networkInfo.isConnected();
        log("processWifiStateChanged: mIsWifiConnected=" + this.mIsWifiConnected + ", mLastWifiStatus=" + this.mLastWifiStatus);
        boolean z = this.mLastWifiStatus;
        boolean z2 = this.mIsWifiConnected;
        if (z != z2) {
            this.mLastWifiStatus = z2;
            if (!z2) {
                if (this.mIsBuildingMode) {
                    this.mIsBuildingMode = false;
                    sendBuildingModeStatus(false);
                    this.mCurWifiSsid = null;
                    unregisterTelephonyCallBack();
                    return;
                }
                return;
            }
            WifiInfo connectionInfo = (WifiInfo) intent.getParcelableExtra("wifiInfo");
            if (connectionInfo == null && (wifiManager = mWifiManager) != null) {
                connectionInfo = wifiManager.getConnectionInfo();
            } else if (mWifiManager == null) {
                return;
            }
            if (connectionInfo == null || isEnterpriseWifi(connectionInfo)) {
                return;
            }
            this.mIsBuildingMode = true;
            sendBuildingModeStatus(true);
            if (this.mCellPingPongDBHelper == null) {
                return;
            }
            String wifiSsid = getWifiSsid(connectionInfo);
            this.mDdsPlmn = MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(this.mDdsPhoneId));
            if (VDBG) {
                log("wifiSsid=" + wifiSsid + ", mDdsPlmn=" + this.mDdsPlmn);
            }
            if (TextUtils.isEmpty(wifiSsid) || TextUtils.isEmpty(this.mDdsPlmn)) {
                return;
            }
            String md5Digest = DigestUtils.getMd5Digest(wifiSsid);
            this.mCurWifiSsid = md5Digest;
            if (TextUtils.isEmpty(md5Digest) || this.mHandler.hasMessages(5)) {
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(5, 90000L);
        }
    }

    private String getWifiSsid(WifiInfo wifiInfo) {
        String ssid = wifiInfo.getSSID();
        if (ssid != null && !"<unknown ssid>".equals(ssid)) {
            return ssid;
        }
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks == null) {
            return null;
        }
        int size = configuredNetworks.size();
        for (int i = 0; i < size; i++) {
            if (configuredNetworks.get(i).networkId == wifiInfo.getNetworkId()) {
                return configuredNetworks.get(i).SSID;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLteNrDubiousCellIfNeed(long j) {
        log("setLteNrDubiousCellIfNeed: mCurWifiSsid = " + this.mCurWifiSsid + ", mDdsPlmn=" + this.mDdsPlmn);
        if (this.mCellPingPongDBHelper == null || TextUtils.isEmpty(this.mCurWifiSsid) || TextUtils.isEmpty(this.mDdsPlmn)) {
            return;
        }
        ArrayList lteDubiousCellInfoSList = this.mCellPingPongDBHelper.getLteDubiousCellInfoSList(this.mCurWifiSsid, this.mDdsPlmn);
        ArrayList nrDubiousCellInfoSList = this.mCellPingPongDBHelper.getNrDubiousCellInfoSList(this.mCurWifiSsid, this.mDdsPlmn);
        if (!lteDubiousCellInfoSList.isEmpty() && NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mDdsPhoneId, lteDubiousCellInfoSList, 0, "buildingMode");
        }
        if (nrDubiousCellInfoSList.isEmpty()) {
            return;
        }
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mDdsPhoneId, nrDubiousCellInfoSList, 0, "buildingMode");
        }
        this.mLastSetDubiousCellTime = j;
    }

    private void fetchWifiSsidQueue() {
        try {
            ArrayDeque arrayDeque = (ArrayDeque) ObjectSerializer.deserialize(mContext.getSharedPreferences("preference_residential_building", 0).getString("WIFI_SSID_QUEUE", ""));
            this.mWifiSsidQueue = arrayDeque;
            if (arrayDeque == null) {
                this.mWifiSsidQueue = new ArrayDeque();
            }
            log("fetchWifiSsidQueue size=" + this.mWifiSsidQueue.size());
        } catch (Exception e) {
            log("fetchWifiSsidQueue exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWifiSsidQueue(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        boolean zContains = this.mWifiSsidQueue.contains(str);
        ArrayDeque arrayDeque = this.mWifiSsidQueue;
        if (zContains) {
            arrayDeque.remove(str);
            this.mWifiSsidQueue.push(str);
        } else {
            arrayDeque.push(str);
        }
        if (this.mWifiSsidQueue.size() > this.mWifiSsidQueueMaxSize) {
            String str2 = (String) this.mWifiSsidQueue.removeLast();
            CellPingPongDBHelper cellPingPongDBHelper = this.mCellPingPongDBHelper;
            if (cellPingPongDBHelper != null) {
                cellPingPongDBHelper.deleteRecord(str2);
            }
        }
        log("updateWifiSsidQueue size=" + this.mWifiSsidQueue.size());
        SharedPreferences.Editor editorEdit = mContext.getSharedPreferences("preference_residential_building", 0).edit();
        try {
            editorEdit.putString("WIFI_SSID_QUEUE", ObjectSerializer.serialize(this.mWifiSsidQueue));
            editorEdit.commit();
        } catch (Exception e) {
            log("updateWifiSsidQueue exception: " + e);
        }
    }

    private synchronized void sendBuildingModeStatus(boolean z) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.BUILDING_MODE_STATUS_CHANGED");
        intent.putExtra("mIsBuildingModeStatus", z);
        mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        log("sendBuildingModeStatus: " + z);
    }

    private boolean isEnterpriseWifi(WifiInfo wifiInfo) {
        WifiManager wifiManager;
        List<WifiConfiguration> configuredNetworks;
        WifiEnterpriseConfig wifiEnterpriseConfig;
        boolean z = false;
        if (wifiInfo != null && (wifiManager = mWifiManager) != null && (configuredNetworks = wifiManager.getConfiguredNetworks()) != null) {
            if (VDBG) {
                log("configuredNetworks length=" + configuredNetworks.size());
            }
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                if (!isInvalidConfiguredNetwork(wifiConfiguration) && wifiConfiguration.networkId == wifiInfo.getNetworkId()) {
                    if (isEnterpriseSecurityType(wifiConfiguration) && (wifiEnterpriseConfig = wifiConfiguration.enterpriseConfig) != null && wifiEnterpriseConfig.getEapMethod() != -1) {
                        z = true;
                    }
                    log("isEnterpriseWifi=" + z);
                    break;
                }
            }
        }
        return z;
    }

    private boolean isEnterpriseSecurityType(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowedKeyManagement.get(2) || wifiConfiguration.allowedKeyManagement.get(3) || wifiConfiguration.allowedKeyManagement.get(10) || wifiConfiguration.allowedKeyManagement.get(14);
    }

    private boolean isInvalidConfiguredNetwork(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration == null || "<unknown ssid>".equals(wifiConfiguration.SSID) || wifiConfiguration.SSID == null;
    }

    private void recordDubiousCellPingPongFail() {
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_building_sns_de_pingpong_fail");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("BuildingModeManager", str);
    }
}
