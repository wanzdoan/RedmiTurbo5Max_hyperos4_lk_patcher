package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;

/* JADX INFO: loaded from: classes.dex */
public class CellPingPongManager {
    private static boolean VDBG = false;
    private static final Map mActiveBand = new HashMap();
    private static Map mBandMappingScs = new HashMap();
    private static TelephonyCallBack mTelephonyCallBack;
    protected static volatile CellPingPongManager sInstance;
    private long mCellChgTime;
    private Context mContext;
    private CellPingPongHandler mHandler;
    private SettingsObserver mSettingsObserver;
    private HandlerThread mWorkThread;
    private boolean mIsWifiConnected = false;
    private boolean mIsMobileConnected = false;
    private int mDdsPhoneId = -1;
    private boolean mIsScreenOn = false;
    private boolean mIsSensorStaticState = false;
    private int mSensorState = 0;
    private boolean mIsLowLatencyMode = false;
    private CellPingPongDBHelper mCellPingPongDBHelper = null;
    private Map mGlobalCellIdCountMap = new HashMap();
    private long mLastSetGlobalCellId = Long.MAX_VALUE;
    private long mLastGlobalCellId = Long.MAX_VALUE;
    private String mDdsPlmn = null;
    private boolean mIsCleared = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CellPingPongManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            CellPingPongManager.this.log("receive action ".concat(action));
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                CellPingPongManager.this.mHandler.obtainMessage(2, intent).sendToTarget();
                return;
            }
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                CellPingPongManager.this.mHandler.obtainMessage(4).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                CellPingPongManager.this.mHandler.obtainMessage(5, intent).sendToTarget();
            } else if (action.equals("CellPingPongManager.intent.debugall")) {
                CellPingPongManager.this.mHandler.obtainMessage(100, intent).sendToTarget();
            }
        }
    };
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.phone.CellPingPongManager.2
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            boolean zIsScreenOn;
            if (i != 0 || (zIsScreenOn = CellPingPongManager.this.isScreenOn()) == CellPingPongManager.this.mIsScreenOn) {
                return;
            }
            CellPingPongManager.this.mIsScreenOn = zIsScreenOn;
        }
    };

    public static CellPingPongManager init() {
        if (sInstance == null) {
            synchronized (CellPingPongManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new CellPingPongManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private CellPingPongManager() {
        log("CellPingPongManager Init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("CellPingPongManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        CellPingPongHandler cellPingPongHandler = new CellPingPongHandler(this.mWorkThread.getLooper());
        this.mHandler = cellPingPongHandler;
        cellPingPongHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mCellPingPongDBHelper = CellPingPongDBHelper.getInstance(this.mContext);
        initBandData();
        initBandMappingScs();
        ((DisplayManager) this.mContext.getSystemService("display")).registerDisplayListener(this.mDisplayListener, null);
        registerReceiver();
        registerSettingsCallbacks();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("CellPingPongManager.intent.debugall");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void registerSettingsCallbacks() {
        SettingsObserver settingsObserver = new SettingsObserver(this.mContext, this.mHandler);
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe(Settings.Global.getUriFor("sensor_state_extent_move"), 6);
    }

    private void registerTelephonyCallBack() {
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
            if (CellPingPongManager.this.isDdsPhoneId(i)) {
                CellPingPongManager.this.mHandler.obtainMessage(3, i, 0, cellLocation).sendToTarget();
            }
        }
    }

    class CellPingPongHandler extends Handler {
        CellPingPongHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (CellPingPongManager.VDBG) {
                CellPingPongManager.this.log("handleMessage=" + message.what);
            }
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        CellPingPongManager.this.initAction();
                        break;
                    case 2:
                        CellPingPongManager.this.processNetworkStateChanged((Intent) message.obj);
                        break;
                    case 3:
                        CellPingPongManager.this.processCellLocationChange(message.arg1, (CellLocation) message.obj);
                        break;
                    case 4:
                        CellPingPongManager cellPingPongManager = CellPingPongManager.this;
                        cellPingPongManager.mDdsPhoneId = cellPingPongManager.getDefaultDataSlotId();
                        CellPingPongManager cellPingPongManager2 = CellPingPongManager.this;
                        cellPingPongManager2.mDdsPlmn = MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(cellPingPongManager2.mDdsPhoneId));
                        if (CellPingPongManager.VDBG) {
                            CellPingPongManager.this.log("EVENT_DATA_SLOT_READY_OR_CHANGED mDdsPlmn=" + CellPingPongManager.this.mDdsPlmn + ", mDdsPhoneId=" + CellPingPongManager.this.mDdsPhoneId);
                        }
                        break;
                    case 5:
                        CellPingPongManager.this.processLlmChangeLevel((Intent) message.obj);
                        break;
                    case 6:
                        CellPingPongManager.this.processSensorExtenMoveStateChanged();
                        break;
                }
                return;
            }
            CellPingPongManager.VDBG = true;
            if (CellPingPongManager.this.mCellPingPongDBHelper != null) {
                CellPingPongManager.this.mCellPingPongDBHelper.clearAllRecord();
            }
            CellPingPongManager.this.log("EVENT_DEBUG_ALL");
        }
    }

    private void initBandData() {
        for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
            String[] strArrSplit = str.split("=");
            mActiveBand.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
        }
    }

    private void initBandMappingScs() {
        for (String str : this.mContext.getResources().getStringArray(R.array.dubious_cell_band_mapping_scs)) {
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

    private boolean isNecessaryCase() {
        boolean z = this.mIsScreenOn && !this.mIsWifiConnected && this.mIsMobileConnected && this.mIsLowLatencyMode && this.mIsSensorStaticState && !isAirPlaneModeOn() && !MiuiPhoneUtils.isDuringCall();
        if (!z) {
            log("wifi=" + this.mIsWifiConnected + ", mobile=" + this.mIsMobileConnected + ", mode=" + this.mIsLowLatencyMode + ", sensorState=" + this.mIsSensorStaticState);
        }
        log("isNecessaryCase rst=" + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChange(int i, CellLocation cellLocation) {
        long ci;
        boolean z;
        if (VDBG) {
            log("processCellLocationChange, phoneId=" + i);
        }
        if (TextUtils.isEmpty(this.mDdsPlmn)) {
            log("invalid plmn");
            return;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || cellLocation == null || cellLocation.isEmpty() || currentCellIdentity == null) {
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
        if (ci == this.mLastGlobalCellId || ci == 2147483647L || ci == Long.MAX_VALUE) {
            if (VDBG) {
                log("skip globalCellId for invalid or no change, globalCellId=" + ci);
                return;
            }
            return;
        }
        this.mLastGlobalCellId = ci;
        if (VDBG) {
            log("processCellLocationChange: new globalCellId=" + ci);
        }
        if (isExistCellInDb(ci, z)) {
            if (VDBG) {
                log("lastSetCgi=" + this.mLastSetGlobalCellId + ", Cgi=" + ci);
            }
            if (this.mLastSetGlobalCellId != ci) {
                setLteNrDubiousCellIfNeed(ci, z);
            }
            this.mGlobalCellIdCountMap.clear();
            return;
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
                if (isExistCellInDb(ci, z)) {
                    return;
                }
                recordCellInfoToDb(currentCellIdentity);
                setLteNrDubiousCellIfNeed(ci, z);
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
            if (cellPingPongDBHelper.getNrDubiousCellCount("llm-cell-pingpong", this.mDdsPlmn) >= 16) {
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
            this.mCellPingPongDBHelper.insertRecord(new CellPingPongDBHelper.CellInfo("llm-cell-pingpong", this.mDdsPlmn, s, s2, z, nci, iIntValue, nrarfcn, pci, num.intValue()), true);
            return;
        }
        if (cellIdentity instanceof CellIdentityLte) {
            if (cellPingPongDBHelper.getLteDubiousCellCount("llm-cell-pingpong", this.mDdsPlmn) >= 16) {
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
            this.mCellPingPongDBHelper.insertRecord(new CellPingPongDBHelper.CellInfo("llm-cell-pingpong", this.mDdsPlmn, s3, s4, z2, ci, iIntValue2, earfcn, pci2), false);
        }
    }

    private void clearCellInDbIfNeed() {
        CellPingPongDBHelper cellPingPongDBHelper;
        Calendar calendar = Calendar.getInstance();
        int i = calendar != null ? calendar.get(5) : -1;
        if (i == 1 || i == 15) {
            if (this.mIsCleared || (cellPingPongDBHelper = this.mCellPingPongDBHelper) == null) {
                return;
            }
            cellPingPongDBHelper.deleteRecord("llm-cell-pingpong");
            this.mIsCleared = true;
            log("clear cell");
            return;
        }
        this.mIsCleared = false;
    }

    private boolean isExistCellInDb(long j, boolean z) {
        boolean zIsHasLteCellRecord;
        CellPingPongDBHelper cellPingPongDBHelper = this.mCellPingPongDBHelper;
        if (cellPingPongDBHelper == null) {
            return false;
        }
        if (z) {
            zIsHasLteCellRecord = cellPingPongDBHelper.isHasNrCellRecord(j);
        } else {
            zIsHasLteCellRecord = cellPingPongDBHelper.isHasLteCellRecord(j);
        }
        log("isExistCellInDb rst=" + zIsHasLteCellRecord);
        return zIsHasLteCellRecord;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDdsPhoneId(int i) {
        return i == this.mDdsPhoneId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
        if (state == state2 || state == NetworkInfo.State.DISCONNECTED) {
            int type = networkInfo.getType();
            boolean z = state == state2;
            log("networkType=" + type + ",connected=" + z);
            if (type == 0) {
                if (this.mIsMobileConnected == z) {
                    return;
                }
                this.mIsMobileConnected = z;
                log("mIsMobileConnected=" + this.mIsMobileConnected);
            } else if (type == 1) {
                if (this.mIsWifiConnected == z) {
                    return;
                }
                this.mIsWifiConnected = z;
                log("mIsWifiConnected=" + this.mIsMobileConnected);
            }
            startStopPingPongDetectionIfNeed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScreenOn() {
        Display[] displays = ((DisplayManager) this.mContext.getSystemService("display")).getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    return true;
                }
            }
            return false;
        }
        log("No displays found");
        return false;
    }

    private int getSensorExtenMoveState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_extent_move", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSensorExtenMoveStateChanged() {
        int sensorExtenMoveState = getSensorExtenMoveState();
        log("processSensorExtenMoveStateChanged: state=" + sensorExtenMoveState + ", lastState=" + this.mSensorState);
        if (this.mSensorState == sensorExtenMoveState) {
            return;
        }
        this.mSensorState = sensorExtenMoveState;
        if (sensorExtenMoveState == 2) {
            this.mIsSensorStaticState = true;
        } else {
            this.mIsSensorStaticState = false;
        }
        startStopPingPongDetectionIfNeed();
        clearCellInDbIfNeed();
    }

    private void startStopPingPongDetectionIfNeed() {
        if (isNecessaryCase()) {
            registerTelephonyCallBack();
        } else {
            unregisterTelephonyCallBack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLlmChangeLevel(Intent intent) {
        boolean z = (intent.getLongExtra("Level_UL", 1L) >= 2 && intent.getLongExtra("Level_DL", 1L) >= 2) || intent.getIntExtra("scene", -1) == 3;
        log("processLlmChangeLevel isLlm=" + z + ", lastIsLlm=" + this.mIsLowLatencyMode);
        if (z == this.mIsLowLatencyMode) {
            return;
        }
        this.mIsLowLatencyMode = z;
        startStopPingPongDetectionIfNeed();
    }

    private void setLteNrDubiousCellIfNeed(long j, boolean z) {
        log("setLteNrDubiousCellIfNeed: cgi=" + j + ", isNrCellType=" + z);
        if (this.mCellPingPongDBHelper == null || SystemClock.elapsedRealtime() <= RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            log("setLteNrDubiousCellIfNeed: exception");
            return;
        }
        this.mLastSetGlobalCellId = j;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        CellPingPongDBHelper cellPingPongDBHelper = this.mCellPingPongDBHelper;
        if (z) {
            NrDubiousCellInfoS nrDubiousCellInfoS = cellPingPongDBHelper.getNrDubiousCellInfoS(j);
            if (nrDubiousCellInfoS != null) {
                arrayList.add(nrDubiousCellInfoS);
                if (NetworkRecoveryStrategyBase.getInstance() != null) {
                    NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mDdsPhoneId, arrayList, 0, "llm");
                    return;
                }
                return;
            }
            return;
        }
        LteDubiousCellInfoS lteDubiousCellInfoS = cellPingPongDBHelper.getLteDubiousCellInfoS(j);
        if (lteDubiousCellInfoS != null) {
            arrayList2.add(lteDubiousCellInfoS);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mDdsPhoneId, arrayList2, 0, "llm");
            }
        }
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("CellPingPongManager", str);
    }
}
