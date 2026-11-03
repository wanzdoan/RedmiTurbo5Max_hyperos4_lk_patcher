package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class FiveGModemExpress implements MiuiAppStateMonitor.TopAppObserver {
    private static FiveGModemExpress sInstance;
    private MiuiAppStateMonitor appStateMonitor;
    private boolean lastMobileDataConnected;
    private boolean mAllAppEnable;
    private Context mContext;
    private boolean mEnabled;
    private ExtTelephonyManager mExtTelephonyManager;
    private boolean mFeatureEnable;
    private int mHomeUid;
    private boolean mIsHyperEngineSupported;
    private PackageManager mPackageManager;
    private final CmNetworkCallback mNetworkCallback = new CmNetworkCallback();
    private HashSet mWhitelistAppPkgs = new HashSet();
    private HashSet mMediumQApps = new HashSet();
    private HashSet mCloudHighQAppSet = new HashSet();
    private HashSet mCloudMediumQAppSet = new HashSet();
    private volatile int mLastPrioUid = -1;
    private int mNetdagentRetryCount = 0;
    private final Handler mWorkHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.FiveGModemExpress.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 101) {
                if (i != 103) {
                    if (i != 105) {
                        return;
                    }
                    FiveGModemExpress.this.updateCloudFeatureStatus();
                    return;
                }
                boolean zIsMobileDataConnected = MiuiPhoneUtils.isMobileDataConnected();
                if (FiveGModemExpress.this.lastMobileDataConnected && !zIsMobileDataConnected) {
                    FiveGModemExpress.this.lastMobileDataConnected = false;
                    FiveGModemExpress fiveGModemExpress = FiveGModemExpress.this;
                    fiveGModemExpress.dispatchPriority(2, fiveGModemExpress.mLastPrioUid);
                    return;
                } else {
                    if (FiveGModemExpress.this.lastMobileDataConnected || !zIsMobileDataConnected) {
                        return;
                    }
                    FiveGModemExpress.this.lastMobileDataConnected = true;
                    if (FiveGModemExpress.this.appStateMonitor != null) {
                        FiveGModemExpress.this.mWorkHandler.obtainMessage(101, FiveGModemExpress.this.appStateMonitor.getCurrentWindowingMode(), 0, FiveGModemExpress.this.appStateMonitor.getCurrentTopActivities()).sendToTarget();
                        return;
                    }
                    return;
                }
            }
            if (message.arg1 == 1) {
                FiveGModemExpress.this.log("Not supported under Split Screen");
                FiveGModemExpress fiveGModemExpress2 = FiveGModemExpress.this;
                fiveGModemExpress2.dispatchPriority(2, fiveGModemExpress2.mLastPrioUid);
                return;
            }
            ArrayList arrayList = (ArrayList) message.obj;
            if (arrayList.isEmpty()) {
                return;
            }
            ComponentName componentName = (ComponentName) arrayList.get(0);
            try {
                int packageUid = FiveGModemExpress.this.mPackageManager.getPackageUid(componentName.getPackageName(), 0);
                if (FiveGModemExpress.this.lastMobileDataConnected) {
                    String packageName = componentName.getPackageName();
                    if (FiveGModemExpress.this.mWhitelistAppPkgs.contains(packageName) || FiveGModemExpress.this.mCloudHighQAppSet.contains(packageName)) {
                        FiveGModemExpress.this.dispatchPriority(1, packageUid);
                    } else if (FiveGModemExpress.this.mAllAppEnable || FiveGModemExpress.this.mCloudMediumQAppSet.contains(packageName)) {
                        FiveGModemExpress.this.dispatchPriority(3, packageUid);
                    } else {
                        FiveGModemExpress fiveGModemExpress3 = FiveGModemExpress.this;
                        fiveGModemExpress3.dispatchPriority(2, fiveGModemExpress3.mLastPrioUid);
                    }
                }
            } catch (Exception unused) {
                FiveGModemExpress.this.log(componentName + " NOT FOUND!");
            }
        }
    };
    BroadcastReceiver mReceiver = new AnonymousClass3();
    private final BaseEvent.IEventListener mCloudChangeListener = new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGModemExpress.4
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            FiveGModemExpress.this.mWorkHandler.sendEmptyMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
        }
    };

    private static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("FiveGModemExpress", str);
        }
    }

    private FiveGModemExpress() {
        this.mHomeUid = -1;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mPackageManager = phoneGlobals.getPackageManager();
        ComponentName homeActivities = this.mPackageManager.getHomeActivities(new ArrayList());
        if (homeActivities != null) {
            try {
                this.mHomeUid = this.mPackageManager.getPackageUid(homeActivities.getPackageName(), 0);
                log("mHomeUid=" + this.mHomeUid);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.mContext.registerReceiver(new AnonymousClass2(), new IntentFilter("android.intent.action.LOCALE_CHANGED"), 2);
        this.mFeatureEnable = readCloudEnabled(this.mContext);
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(this.mContext);
        initCloudAppList();
        log("FiveGModemExpress enable:" + this.mFeatureEnable);
        enableFeature(this.mFeatureEnable);
        registerCloudChangeListener();
        this.mIsHyperEngineSupported = isHyperEngineSupported();
    }

    /* JADX INFO: renamed from: com.android.phone.FiveGModemExpress$2, reason: invalid class name */
    class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            FiveGModemExpress.this.log("onReceive:" + intent.getAction());
            FiveGModemExpress.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.FiveGModemExpress$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            FiveGModemExpress fiveGModemExpress = FiveGModemExpress.this;
            fiveGModemExpress.enableFeature(fiveGModemExpress.mFeatureEnable);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.FiveGModemExpress$3, reason: invalid class name */
    class AnonymousClass3 extends BroadcastReceiver {
        AnonymousClass3() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            FiveGModemExpress.this.log("onReceive " + intent.getAction());
            if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(intent.getAction())) {
                FiveGModemExpress.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.FiveGModemExpress$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            int intExtra = intent.getIntExtra("scene", -1);
            boolean z = true;
            if (intExtra == 1 || intExtra == 3 || intExtra == 5) {
                return;
            }
            long longExtra = intent.getLongExtra("Level_UL", 1L);
            if (intent.getLongExtra("Level_DL", 1L) == 1 && longExtra == 1) {
                z = false;
            }
            FiveGModemExpress.this.enableGameModeExpress(z);
        }
    }

    public static void make() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (sInstance == null && !Build.IS_INTERNATIONAL_BUILD && HyperOSCustFeatureResolve.getBoolean("support_modem_express_bool", false)) {
            if (!readCloudEnabled(phoneGlobals)) {
                Rlog.d("FiveGModemExpress", "FiveGModemExpress disabled by cloud");
                return;
            } else {
                sInstance = new FiveGModemExpress();
                return;
            }
        }
        if (ModemProvider.IS_MTK && sInstance == null) {
            if (!readCloudEnabled(phoneGlobals)) {
                Rlog.d("FiveGModemExpress", "FiveGModemExpress disabled by cloud");
            } else {
                sInstance = new FiveGModemExpress();
            }
        }
    }

    private static boolean isFeatureSupportedByRegion(Context context) {
        return ArrayUtils.contains(context.getResources().getStringArray(R.array.config_mdexpress_support_country), SystemProperties.get("ro.miui.region", ""));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableGameModeExpress(boolean z) {
        logV("enableGameModeExpress isGame=" + z);
        if (this.lastMobileDataConnected && z && this.appStateMonitor.getCurrentWindowingMode() == 0) {
            try {
                String packageName = ((ComponentName) this.appStateMonitor.getCurrentTopActivities().get(0)).getPackageName();
                int packageUid = this.mPackageManager.getPackageUid(packageName, 0);
                if (this.mIsHyperEngineSupported) {
                    List stringArray = HyperOSCustFeatureResolve.getStringArray("hyper_engine_game_white_list_default");
                    if (stringArray != null && stringArray.contains(packageName)) {
                        dispatchPriority(1, packageUid);
                        return;
                    }
                    log("HyperEngine game whiteList not matched pkgName=" + packageName);
                    return;
                }
                dispatchPriority(1, packageUid);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        dispatchPriority(2, this.mLastPrioUid);
    }

    private void registerDeviceState(boolean z) {
        log("registerDeviceState" + z);
        this.appStateMonitor = MiuiAppStateMonitor.init();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        MiuiAppStateMonitor miuiAppStateMonitor = this.appStateMonitor;
        if (z) {
            miuiAppStateMonitor.registerAppObserver(this);
            if (connectivityManager != null) {
                connectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback, this.mWorkHandler);
            }
            this.mContext.registerReceiver(this.mReceiver, new IntentFilter("com.android.phone.intent.action.CHANGE_LEVEL"), 2);
            return;
        }
        miuiAppStateMonitor.unregisterAppObserver(this);
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableFeature(boolean z) {
        boolean z2 = z && isFeatureSupportedByRegion(this.mContext);
        logV("enable feature:" + z2);
        if (z2 && !this.mEnabled) {
            initPresetAppPkgList();
            registerDeviceState(true);
            this.mEnabled = true;
        } else {
            if (z2 || !this.mEnabled) {
                return;
            }
            registerDeviceState(false);
            dispatchPriority(2, this.mLastPrioUid);
            this.mEnabled = false;
        }
    }

    private boolean isHyperEngineSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("is_hyper_engine_optimization_supported", false);
    }

    private void initPresetAppPkgList() {
        HashSet hashSet;
        HashSet hashSet2;
        this.mWhitelistAppPkgs.clear();
        this.mMediumQApps.clear();
        this.mAllAppEnable = this.mCloudMediumQAppSet.contains("*");
        List stringArray = HyperOSCustFeatureResolve.getStringArray("config_modem_express_mediumq_applist");
        this.mMediumQApps = stringArray != null ? new HashSet(stringArray) : new HashSet();
        List stringArray2 = HyperOSCustFeatureResolve.getStringArray("profession_mode_whitelist_app_packages");
        if (stringArray2 != null) {
            this.mWhitelistAppPkgs.addAll(new HashSet(stringArray2));
            return;
        }
        if (MiuiPhoneUtils.isSupportMtkGlobalDataOptimization()) {
            if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isCloudGlobalStreamMediaOn()) {
                List stringArray3 = HyperOSCustFeatureResolve.getStringArray("config_global_data_opt_game_whitelist_pkgs");
                if (stringArray3 != null) {
                    hashSet2 = new HashSet(stringArray3);
                } else {
                    hashSet2 = new HashSet();
                }
                this.mWhitelistAppPkgs = hashSet2;
                return;
            }
            return;
        }
        List stringArray4 = HyperOSCustFeatureResolve.getStringArray("config_modem_express_whitelist_pkgs");
        if (stringArray4 != null) {
            hashSet = new HashSet(stringArray4);
        } else {
            hashSet = new HashSet();
        }
        this.mWhitelistAppPkgs = hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPriority(int i, int i2) {
        if (this.mIsHyperEngineSupported) {
            doModemExpress(i, i2);
        } else {
            doPowerHal(i, i2);
        }
    }

    private void doPowerHal(int i, int i2) {
        if (i != 1) {
            if (i == 2) {
                if (isValidApp(i2)) {
                    setPriorityByUid(2, i2);
                    logV("unprio " + i2);
                }
                this.mLastPrioUid = -1;
                return;
            }
            if (i != 3) {
                log("invalid call");
                return;
            }
        }
        if (this.mLastPrioUid == i2 || !isValidApp(i2)) {
            return;
        }
        logV("flush Priority Rules");
        flushPriorityRules(1);
        setPriorityByUid(1, i2);
        logV("unprio " + this.mLastPrioUid + " prio " + i2);
        this.mLastPrioUid = i2;
    }

    private void doModemExpress(int i, int i2) {
        if (i == 1) {
            if (this.mLastPrioUid == i2 || !isValidApp(i2)) {
                return;
            }
            if (this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_clear_uid_all")) {
                this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_set_uid " + i2);
                log("unprio " + this.mLastPrioUid + " prio " + i2);
                this.mLastPrioUid = i2;
                return;
            }
            log("flush Priority Rules failed");
            return;
        }
        if (i == 2) {
            this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_clear_uid_all");
            this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_clear_uid_all.2");
            log("clear Priority Rules");
            this.mLastPrioUid = -1;
            return;
        }
        if (i == 3) {
            if (this.mLastPrioUid == i2 || !isValidApp(i2)) {
                return;
            }
            if (this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_clear_uid_all.2")) {
                this.mExtTelephonyManager.dispatchNetdagentCmd("netdagent firewall priority_set_uid.2 " + i2);
                log("unprio " + this.mLastPrioUid + " prio_medium " + i2);
                this.mLastPrioUid = i2;
                return;
            }
            log("flush Priority Rules failed");
            return;
        }
        log("invalid call");
    }

    @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
    public void onTopAppChanged(List list, int i) {
        this.mWorkHandler.obtainMessage(101, i, 0, list).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("FiveGModemExpress", str);
    }

    private boolean isValidApp(int i) {
        return UserHandle.isApp(i) && UserHandle.getAppId(i) != this.mHomeUid;
    }

    private static boolean readCloudEnabled(Context context) {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(context.getContentResolver(), "TelephonyMtkFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        return cloudData.getBoolean("cloud_mdexpress_enable", true);
                    }
                }
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCloudAppList() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyMtkFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2) && cloudData.json() != null) {
                        if (cloudData.json().has("cloud_highq_app_list")) {
                            JSONArray jSONArray = cloudData.json().getJSONArray("cloud_highq_app_list");
                            this.mCloudHighQAppSet.clear();
                            for (int i = 0; i < jSONArray.length(); i++) {
                                this.mCloudHighQAppSet.add(jSONArray.getString(i));
                            }
                        }
                        if (cloudData.json().has("cloud_mediumq_app_list")) {
                            JSONArray jSONArray2 = cloudData.json().getJSONArray("cloud_mediumq_app_list");
                            this.mCloudMediumQAppSet.clear();
                            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                                this.mCloudMediumQAppSet.add(jSONArray2.getString(i2));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudFeatureStatus() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyMtkFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("updateCloudFeatureStatus data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        boolean z = cloudData.getBoolean("cloud_mdexpress_enable", true);
                        if (z != this.mFeatureEnable) {
                            this.mFeatureEnable = z;
                            enableFeature(z);
                        }
                        if (cloudData.json() != null) {
                            if (cloudData.json().has("cloud_highq_app_list")) {
                                JSONArray jSONArray = cloudData.json().getJSONArray("cloud_highq_app_list");
                                this.mCloudHighQAppSet.clear();
                                for (int i = 0; i < jSONArray.length(); i++) {
                                    this.mCloudHighQAppSet.add(jSONArray.getString(i));
                                }
                            }
                            if (cloudData.json().has("cloud_mediumq_app_list")) {
                                JSONArray jSONArray2 = cloudData.json().getJSONArray("cloud_mediumq_app_list");
                                this.mCloudMediumQAppSet.clear();
                                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                                    this.mCloudMediumQAppSet.add(jSONArray2.getString(i2));
                                }
                            }
                            initPresetAppPkgList();
                        }
                    }
                }
                return;
            }
            log("updateCloudFeatureStatus cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CmNetworkCallback extends ConnectivityManager.NetworkCallback {
        private CmNetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            FiveGModemExpress.this.log(network + " onLost");
            FiveGModemExpress.this.mWorkHandler.removeMessages(103);
            FiveGModemExpress.this.mWorkHandler.sendEmptyMessageDelayed(103, 1000L);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            FiveGModemExpress.this.log(network + " onAvailable");
            FiveGModemExpress.this.mWorkHandler.removeMessages(103);
            FiveGModemExpress.this.mWorkHandler.sendEmptyMessageDelayed(103, 1000L);
        }
    }

    private void registerCloudChangeListener() {
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", this.mCloudChangeListener);
    }

    private void setPriorityByUid(int i, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt("action", i);
        bundle.putInt("uid", i2);
        RadioConfig.getInstance().configureParameters(1000, bundle);
    }

    private void flushPriorityRules(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", i);
        RadioConfig.getInstance().configureParameters(1001, bundle);
    }
}
