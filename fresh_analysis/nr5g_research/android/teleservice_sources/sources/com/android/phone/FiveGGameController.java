package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManagerEx;
import miui.util.FeatureParser;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FiveGGameController {
    private static final String KEY_NDDS_VOLTE_DATA_ENABLED;
    private static FiveGGameController sInstance;
    private boolean isInGame;
    private ContentResolver mContentResolver;
    private Context mContext;
    private ContentObserver mGameStatusObserver;
    private BroadcastReceiver mStateReceiver;
    private boolean mUserSettingOn;
    private final Handler mWorkHanlder = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.FiveGGameController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 100) {
                FiveGGameController.this.tryRestoreNdds();
            } else if (i == 101) {
                FiveGGameController.this.onConnectivityChanged((Intent) message.obj);
            } else {
                FiveGGameController.log("invalid message received");
            }
        }
    };

    static {
        KEY_NDDS_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
    }

    private FiveGGameController() {
        if (isFeatureSupported()) {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            this.mContext = phoneGlobals;
            this.mContentResolver = phoneGlobals.getContentResolver();
            registerObserver();
            boolean z = Settings.Secure.getInt(this.mContentResolver, "gb_disable_ndds", 0) == 1;
            this.mUserSettingOn = z;
            regUnregReceiver(z);
            observeGameStatus(this.mUserSettingOn);
        }
    }

    public static int getLteRsrp(int i) {
        Phone phone;
        if (!isValidatePhoneId(i) || (phone = PhoneFactory.getPhone(i)) == null) {
            return -140;
        }
        SignalStrength signalStrength = phone.getSignalStrength();
        int lteRsrp = signalStrength != null ? signalStrength.getLteRsrp() : -140;
        if (lteRsrp < -140 || lteRsrp > -44) {
            return -140;
        }
        return lteRsrp;
    }

    public static int getNRRsrp(int i) {
        if (!isValidatePhoneId(i)) {
            return -140;
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        int rsrp = fiveGServiceInfo != null ? fiveGServiceInfo.getRsrp() : -140;
        if (rsrp < -140 || rsrp > -44) {
            return -140;
        }
        return rsrp;
    }

    public static String getNetworkType(int i) {
        int nrConfigType;
        int allocated;
        if (!isValidatePhoneId(i)) {
            return "UNKNOW";
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        if (fiveGServiceInfo != null) {
            nrConfigType = fiveGServiceInfo.getNrConfigType();
            allocated = fiveGServiceInfo.getAllocated();
        } else {
            nrConfigType = -1;
            allocated = 0;
        }
        if (nrConfigType == 0 && allocated == 1) {
            return "NSA";
        }
        if (nrConfigType == 1) {
            return "SA";
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return "UNKNOW";
        }
        return TelephonyManagerEx.getDefault().getNetworkClassName(phone.getServiceState().getDataNetworkType());
    }

    public static void setMobileNetworkCapability(String str) {
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).setNetworkCapability(str);
    }

    public static String getMobileNetworkCapability(int i) {
        int nRRsrp;
        if (!isValidatePhoneId(i)) {
            i = DefaultSimManager.getDefaultDataSlotId();
        }
        String networkType = getNetworkType(i);
        if ("NSA".equals(networkType) || "SA".equals(networkType)) {
            nRRsrp = getNRRsrp(i);
        } else {
            nRRsrp = getLteRsrp(i);
        }
        log("networkType = " + networkType + ", signalStrength = " + nRRsrp);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("rat", networkType);
            jSONObject.put("signal_rsrp", nRRsrp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    private static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("FiveGGameController", str);
    }

    public static boolean isFeatureSupported() {
        return !Build.IS_INTERNATIONAL_BUILD && FeatureParser.getBoolean("support_disable_ndds_sim", false);
    }

    public static FiveGGameController init() {
        if (isFeatureSupported() && sInstance == null) {
            sInstance = new FiveGGameController();
        }
        return sInstance;
    }

    private void registerObserver() {
        Uri[] uriArr = {Settings.Secure.getUriFor("gb_disable_ndds"), Settings.Global.getUriFor("smart_dual_sim"), Settings.Global.getUriFor(KEY_NDDS_VOLTE_DATA_ENABLED)};
        ContentObserver contentObserver = new ContentObserver(this.mWorkHanlder) { // from class: com.android.phone.FiveGGameController.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                String lastPathSegment = uri.getLastPathSegment();
                FiveGGameController.log(lastPathSegment + " settings changed");
                if ("gb_disable_ndds".equals(lastPathSegment)) {
                    FiveGGameController fiveGGameController = FiveGGameController.this;
                    fiveGGameController.mUserSettingOn = Settings.Secure.getInt(fiveGGameController.mContentResolver, "gb_disable_ndds", 0) == 1;
                    FiveGGameController fiveGGameController2 = FiveGGameController.this;
                    fiveGGameController2.regUnregReceiver(fiveGGameController2.mUserSettingOn);
                    FiveGGameController fiveGGameController3 = FiveGGameController.this;
                    fiveGGameController3.observeGameStatus(fiveGGameController3.mUserSettingOn);
                }
                if (FiveGGameController.this.denyNddsDisable()) {
                    FiveGGameController.this.tryRestoreNdds();
                }
            }
        };
        for (int i = 0; i < 3; i++) {
            this.mContentResolver.registerContentObserver(uriArr[i], false, contentObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regUnregReceiver(boolean z) {
        BroadcastReceiver broadcastReceiver;
        if (z && this.mStateReceiver == null) {
            this.mStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.FiveGGameController.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    FiveGGameController.log(intent.getAction());
                    if ("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                        if (FiveGGameController.this.isInGame) {
                            return;
                        }
                        FiveGGameController.this.mWorkHanlder.sendEmptyMessage(100);
                    } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                        FiveGGameController.this.mWorkHanlder.obtainMessage(101, intent).sendToTarget();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.mContext.registerReceiver(this.mStateReceiver, intentFilter, 2);
            return;
        }
        if (z || (broadcastReceiver = this.mStateReceiver) == null) {
            return;
        }
        this.mContext.unregisterReceiver(broadcastReceiver);
        this.mStateReceiver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void observeGameStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mGameStatusObserver == null) {
            Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
            ContentObserver contentObserver2 = new ContentObserver(this.mWorkHanlder) { // from class: com.android.phone.FiveGGameController.4
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    FiveGGameController fiveGGameController = FiveGGameController.this;
                    fiveGGameController.isInGame = Settings.Secure.getInt(fiveGGameController.mContentResolver, "gb_boosting", 0) == 1;
                    FiveGGameController.log("enter game:" + FiveGGameController.this.isInGame);
                    boolean z3 = FiveGGameController.this.isInGame;
                    FiveGGameController fiveGGameController2 = FiveGGameController.this;
                    if (z3) {
                        fiveGGameController2.tryDisableNdds();
                    } else {
                        fiveGGameController2.tryRestoreNdds();
                    }
                }
            };
            this.mGameStatusObserver = contentObserver2;
            this.mContentResolver.registerContentObserver(uriFor, false, contentObserver2);
            return;
        }
        if (z || (contentObserver = this.mGameStatusObserver) == null) {
            return;
        }
        this.mContentResolver.unregisterContentObserver(contentObserver);
        this.mGameStatusObserver = null;
    }

    private int getNddsPhoneId() {
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        int phoneId = SubscriptionManager.getPhoneId(activeDataSubscriptionId);
        if (phoneId == -1) {
            log("getActiveDataPhoneId error activeSubId=" + activeDataSubscriptionId);
            phoneId = 0;
        }
        return phoneId == 0 ? 1 : 0;
    }

    private boolean isMobileDataConnected() {
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        Network activeNetwork = connectivityManager.getActiveNetwork();
        boolean z = false;
        if (activeNetwork != null && (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) != null && networkCapabilities.hasTransport(0)) {
            z = true;
        }
        log("isMobileDataConnected " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryRestoreNdds() {
        boolean zBooleanValue = PreferenceUtils.getInstance().getBooleanPreference("disable_ndds").booleanValue();
        log("tryRestoreNdds " + zBooleanValue);
        if (zBooleanValue && MiuiPhoneUtils.getInsertedSimCount() == 2) {
            int nddsPhoneId = getNddsPhoneId();
            if ((this.isInGame && isMobileDataConnected()) || MiuiPhoneUtils.isIccCardActivated(nddsPhoneId)) {
                return;
            }
            log("tryRestoreNdds slot:" + nddsPhoneId);
            if (setIccCardActivate(nddsPhoneId, true)) {
                PreferenceUtils.getInstance().setBooleanPreference("disable_ndds", false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDisableNdds() {
        if (denyNddsDisable()) {
            log("skip tryDisableNdds");
            return;
        }
        if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
            int nddsPhoneId = getNddsPhoneId();
            if (this.isInGame && MiuiPhoneUtils.isIccCardActivated(nddsPhoneId) && isMobileDataConnected()) {
                log("SIM disable on slot:" + nddsPhoneId);
                setIccCardActivate(nddsPhoneId, false);
                PreferenceUtils.getInstance().setBooleanPreference("disable_ndds", true);
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "disable_ndds");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean denyNddsDisable() {
        return !this.mUserSettingOn || (Settings.Global.getInt(this.mContentResolver, "smart_dual_sim", 0) == 1) || (Settings.Global.getInt(this.mContentResolver, KEY_NDDS_VOLTE_DATA_ENABLED, 0) == 1);
    }

    private boolean setIccCardActivate(int i, boolean z) {
        return ModemProvider.getInstance().setIccCardActivate(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectivityChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        int type = networkInfo.getType();
        NetworkInfo.State state = networkInfo.getState();
        if (type == 0) {
            if (state == NetworkInfo.State.DISCONNECTED && !isMobileDataConnected()) {
                tryRestoreNdds();
            } else if (state == NetworkInfo.State.CONNECTED && isMobileDataConnected()) {
                tryDisableNdds();
            }
        }
    }
}
