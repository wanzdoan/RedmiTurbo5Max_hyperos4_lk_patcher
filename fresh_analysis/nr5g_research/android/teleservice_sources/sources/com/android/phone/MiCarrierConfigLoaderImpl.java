package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.service.carrier.CarrierIdentifier;
import android.service.carrier.ICarrierService;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.MiuiOpconfigUtils;
import com.xiaomi.mirilhook.MiRilHook;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.Executor;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class MiCarrierConfigLoaderImpl implements MiCarrierConfigLoaderStub.IMiCarrierConfigLoader {
    private static boolean mWasLoadError = false;
    private static volatile MiCarrierConfigLoaderImpl sInstance;
    private PersistableBundle[] mConfigFromDefaultApp;
    private Context mContext;
    private Handler mHandler;
    private boolean[] mOemServiceBound;
    private ServiceConnection[] mOemServiceConnections;
    private OverlayManagerWrapper mOverlayManager;
    private CarrierConfigLoader mOwner;
    private final String VODAFOHE_COTA_RES_OVERLAY_PACKAGE = "com.miui.phone.carriers.overlay.vodafone";
    private final String H3G_COTA_RES_OVERLAY_PACKAGE = "com.miui.phone.carriers.overlay.h3g";
    private final BroadcastReceiver mCotaCarrierReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiCarrierConfigLoaderImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false) || "android.intent.action.PACKAGE_REPLACED".equals(action)) {
                action.hashCode();
                if (action.equals("com.android.updater.action.COTA_CARRIER")) {
                    boolean booleanExtra = intent.getBooleanExtra("carrierName", false);
                    Log.d("CarrierConfigLoader", "receive cota carrier prop update : " + booleanExtra);
                    if (booleanExtra) {
                        if (MiCarrierConfigLoaderImpl.this.clearCachedConfigForPackage(null)) {
                            int activeModemCount = TelephonyManager.from(MiCarrierConfigLoaderImpl.this.mContext).getActiveModemCount();
                            for (int i = 0; i < activeModemCount; i++) {
                                Log.d("CarrierConfigLoader", "cota carrier prop changed, phone=" + i);
                                MiCarrierConfigLoaderImpl.this.mHandler.sendMessage(MiCarrierConfigLoaderImpl.this.mHandler.obtainMessage(7, i, -1));
                            }
                        }
                        if (miui.telephony.TelephonyManager.isCustForEsVodafone() || miui.telephony.TelephonyManager.isCustForFrSfr()) {
                            Log.d("CarrierConfigLoader", "try enable vodafone res overlay...");
                            MiCarrierConfigLoaderImpl.this.enableResOverlayIfNeeded("com.miui.phone.carriers.overlay.vodafone", true);
                        } else if (miui.telephony.TelephonyManager.isCustForHkH3g()) {
                            Log.d("CarrierConfigLoader", "try enable h3g res overlay...");
                            MiCarrierConfigLoaderImpl.this.enableResOverlayIfNeeded("com.miui.phone.carriers.overlay.h3g", true);
                        }
                    }
                }
            }
        }
    };

    public static MiCarrierConfigLoaderImpl getInstance() {
        return sInstance;
    }

    public PersistableBundle[] getConfigFromDefaultApp() {
        return this.mConfigFromDefaultApp;
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public void init(CarrierConfigLoader carrierConfigLoader, Context context, PersistableBundle[] persistableBundleArr, Object... objArr) {
        this.mOwner = carrierConfigLoader;
        this.mContext = context;
        this.mConfigFromDefaultApp = persistableBundleArr;
        sInstance = this;
        this.mHandler = (Handler) objArr[0];
        this.mOverlayManager = new OverlayManagerWrapper();
        if (Build.IS_INTERNATIONAL_BUILD) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.updater.action.COTA_CARRIER");
            this.mContext.registerReceiver(this.mCotaCarrierReceiver, intentFilter, 2);
        }
        int activeModemCount = TelephonyManager.from(this.mContext).getActiveModemCount();
        this.mOemServiceConnections = new ServiceConnection[activeModemCount];
        this.mOemServiceBound = new boolean[activeModemCount];
        Log.d("CarrierConfigLoader", "Init done.");
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public boolean handleMessage(Message message, int i) {
        PersistableBundle persistableBundle;
        String str;
        int i2 = message.arg1;
        boolean z = false;
        if (!SubscriptionManager.isValidPhoneId(i2)) {
            return false;
        }
        switch (i) {
            case 100:
                String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i2);
                if (this.mConfigFromDefaultApp[i2] != null) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).tryRepair(this.mConfigFromDefaultApp[i2], simOperatorNumericForPhone);
                    ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).syncCloudCarrierConfig(simOperatorNumericForPhone, this.mConfigFromDefaultApp[i2]);
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).updateConfigForVolteGlobalization(simOperatorNumericForPhone, this.mConfigFromDefaultApp[i2]);
                    CarrierConfigLoaderInjector.updateVoLTECarrierConfig(this.mContext, this.mConfigFromDefaultApp[i2], simOperatorNumericForPhone, i2);
                }
                return true;
            case 101:
                if (!TextUtils.isEmpty(TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i2)) && (persistableBundle = this.mConfigFromDefaultApp[i2]) != null && PhoneProxy.getVolteCarrierConfigValue(persistableBundle)) {
                    CarrierConfigLoaderInjector.updateEnhanced4GLteDefaultValue(this.mContext, i2);
                }
                return true;
            case 102:
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                String string = defaultSharedPreferences.getString("build_fingerprint", null);
                String str2 = android.os.Build.FINGERPRINT;
                if (miui.telephony.TelephonyManager.isCarrierCotaSupported()) {
                    str = str2 + SystemProperties.get("ro.miui.customized.region", "");
                } else {
                    str = str2;
                }
                if (!str.equals(string)) {
                    Log.d("CarrierConfigLoader", "Build fingerprint changed. old: " + string + " new: " + str2);
                    if (!TextUtils.isEmpty(string)) {
                        broadcastOtaUpdate(string);
                    }
                    clearCachedConfigForPackage(null);
                    defaultSharedPreferences.edit().putString("build_fingerprint", str).apply();
                    z = true;
                }
                String opconfigVersion = MiuiOpconfigUtils.getOpconfigVersion();
                if (!TextUtils.isEmpty(opconfigVersion)) {
                    String string2 = defaultSharedPreferences.getString("opconfig_version", null);
                    Log.d("CarrierConfigLoader", "Build apex changed. old: " + string2 + " new: " + opconfigVersion);
                    if (!opconfigVersion.equals(string2)) {
                        if (!z) {
                            clearCachedConfigForPackage(null);
                        }
                        defaultSharedPreferences.edit().putString("opconfig_version", opconfigVersion).apply();
                        MiuiOpconfigUtils.sendOpconfigUpdateBroadcast(opconfigVersion);
                    }
                }
                return true;
            case 103:
                PersistableBundle persistableBundleRestoreConfigFromXml = this.mOwner.restoreConfigFromXml("com.xiaomi.carrierconfig", "", i2);
                if (persistableBundleRestoreConfigFromXml != null) {
                    Log.d("CarrierConfigLoader", "read cached config for oem directly. phoneId=" + i2);
                    PersistableBundle persistableBundle2 = this.mConfigFromDefaultApp[i2];
                    if (persistableBundle2 != null) {
                        persistableBundle2.putAll(persistableBundleRestoreConfigFromXml);
                    }
                    reportConfigErrorStatistics(i2, false);
                    this.mHandler.obtainMessage(107, i2, -1).sendToTarget();
                } else {
                    connectToOemCarrierConfig(i2);
                }
                return true;
            case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                handleOemServiceConnected(i2, (IBinder) message.obj);
                return true;
            case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
            case 106:
                handleOemFetchTimeout(i2);
                return true;
            case 107:
                continueAfterOemApplied(i2);
                return true;
            default:
                return false;
        }
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public void resetCarrierConfig() {
        if (clearCachedConfigForPackage(null)) {
            int activeModemCount = TelephonyManager.from(this.mContext).getActiveModemCount();
            for (int i = 0; i < activeModemCount; i++) {
                Log.d("CarrierConfigLoader", "reset carrier config, phone=" + i);
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(7, i, -1));
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).reset(i);
            }
        }
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public boolean isLoadedError(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length == 0 || subId[0] == -1 || mWasLoadError || !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isLoadedError(subId[0])) {
            return false;
        }
        mWasLoadError = true;
        return true;
    }

    public void reportConfigErrorStatistics(int i, boolean z) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).reportConfigErrorStatistics(i, z, this.mConfigFromDefaultApp[i]);
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public String getFilenameForConfig(String str, String str2, String str3, int i, String str4) {
        String simOperatorNumeric = getSimOperatorNumeric(str3);
        if (TextUtils.isEmpty(simOperatorNumeric) || i == -1) {
            return str4;
        }
        return "carrierconfig-" + str + str2 + "-" + str3 + "-" + simOperatorNumeric + ".xml";
    }

    private static String getSimOperatorNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null && str.equals(phone.getIccSerialNumber())) {
                return phone.getOperatorNumeric();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearCachedConfigForPackage(final String str) {
        File[] fileArrListFiles = this.mContext.getFilesDir().listFiles(new FilenameFilter(this) { // from class: com.android.phone.MiCarrierConfigLoaderImpl.2
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str2) {
                if (str != null) {
                    return str2.startsWith("carrierconfig-" + str + "-");
                }
                return str2.startsWith("carrierconfig-");
            }
        });
        if (fileArrListFiles == null || fileArrListFiles.length < 1) {
            return false;
        }
        for (File file : fileArrListFiles) {
            if (PhoneDebug.VDBG) {
                Log.d("CarrierConfigLoader", "deleting " + file.getName());
            }
            file.delete();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableResOverlayIfNeeded(String str, boolean z) {
        OverlayManagerWrapper overlayManagerWrapper = this.mOverlayManager;
        if (overlayManagerWrapper != null) {
            boolean zIsOverlayEnable = overlayManagerWrapper.isOverlayEnable(str, 0);
            Log.d("CarrierConfigLoader", "enableResOverlayIfNeeded curState : " + zIsOverlayEnable + " , targetState : " + z);
            if (zIsOverlayEnable != z) {
                this.mOverlayManager.setEnabled(str, z, 0);
            }
        }
    }

    @Override // com.android.phone.MiCarrierConfigLoaderStub.IMiCarrierConfigLoader
    public void updateConfigForPhoneId(int i, String str) {
        str.hashCode();
        switch (str) {
            case "LOADED":
                CarrierConfigLoaderInjector.onIccLoaded(i);
                break;
            case "ESSENTIAL_LOADED":
                notifyFeatureResolveSimReady(i);
                SubscriptionManagerService.getInstance().updateSimState(i, 12, (Executor) null, (Runnable) null);
                break;
            case "ABSENT":
                CarrierConfigLoaderInjector.onIccAbsent(i);
                break;
        }
    }

    private void notifyFeatureResolveSimReady(int i) {
        String strSubstring;
        String strSubstring2;
        String str;
        String groupIdLevel1;
        String groupIdLevel2;
        if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
            String simOperatorNameForPhone = TelephonyManager.from(this.mContext).getSimOperatorNameForPhone(i);
            String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i);
            String str2 = "";
            if (simOperatorNumericForPhone == null || simOperatorNumericForPhone.length() < 3) {
                strSubstring = "";
                strSubstring2 = strSubstring;
            } else {
                strSubstring2 = simOperatorNumericForPhone.substring(0, 3);
                strSubstring = simOperatorNumericForPhone.substring(3);
            }
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                str = "";
                groupIdLevel1 = str;
                groupIdLevel2 = groupIdLevel1;
            } else {
                String subscriberId = phone.getSubscriberId();
                groupIdLevel1 = phone.getGroupIdLevel1();
                groupIdLevel2 = phone.getGroupIdLevel2();
                String iccSerialNumber = phone.getIccSerialNumber();
                str = subscriberId;
                str2 = iccSerialNumber;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("phoneId", i);
            bundle.putString("mcc", strSubstring2);
            bundle.putString("mnc", strSubstring);
            bundle.putString("iccid", str2);
            bundle.putString("imsi", str);
            bundle.putString("gid1", groupIdLevel1);
            bundle.putString("gid2", groupIdLevel2);
            Log.d("CarrierConfigLoader", "notifyFeatureResolveSimReady phoneId " + i + " gid1= " + groupIdLevel1 + " gid2= " + groupIdLevel2 + " spn= " + simOperatorNameForPhone);
            HyperOSCustFeatureResolve.notifyCustFeatureResolve("simReady", bundle);
        }
    }

    private void broadcastOtaUpdate(String str) {
        Intent intent = new Intent("miui.intent.action.ACTION_OTA_UPDATE");
        intent.putExtra("lastFingerprint", str);
        this.mContext.sendBroadcast(intent);
    }

    private void connectToOemCarrierConfig(final int i) {
        Log.d("CarrierConfigLoader", "Binding to com.xiaomi.carrierconfig for phone " + i);
        this.mHandler.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, Integer.valueOf(i));
        this.mHandler.removeMessages(106, Integer.valueOf(i));
        Intent intent = new Intent("android.service.carrier.CarrierService");
        intent.setPackage("com.xiaomi.carrierconfig");
        ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.android.phone.MiCarrierConfigLoaderImpl.3
            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MiCarrierConfigLoaderImpl.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, i, -1, iBinder).sendToTarget();
            }
        };
        this.mOemServiceConnections[i] = serviceConnection;
        try {
            boolean zBindService = this.mContext.bindService(intent, serviceConnection, 1);
            this.mOemServiceBound[i] = zBindService;
            if (!zBindService) {
                Log.e("CarrierConfigLoader", "Bind to OEM config app fails: com.xiaomi.carrierconfig");
                this.mHandler.obtainMessage(107, i, -1).sendToTarget();
            } else {
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(106, i, -1, Integer.valueOf(i)), 30000L);
            }
        } catch (SecurityException e) {
            Log.e("CarrierConfigLoader", "Bind to OEM config app SecurityException: " + e);
            this.mHandler.obtainMessage(107, i, -1).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueAfterOemApplied(int i) {
        this.mHandler.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, Integer.valueOf(i));
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(100, i, -1));
        Handler handler2 = this.mHandler;
        handler2.sendMessage(handler2.obtainMessage(101, i, -1));
        String carrierServicePackageNameForLogicalSlot = TelephonyManager.from(this.mContext).getCarrierServicePackageNameForLogicalSlot(i);
        String defaultCarrierServicePackageName = this.mOwner.getDefaultCarrierServicePackageName();
        Log.d("CarrierConfigLoader", " platformPkg = " + defaultCarrierServicePackageName + " carrierPkg = " + carrierServicePackageNameForLogicalSlot);
        if (!TextUtils.isEmpty(carrierServicePackageNameForLogicalSlot) && !carrierServicePackageNameForLogicalSlot.equals(defaultCarrierServicePackageName)) {
            Log.d("CarrierConfigLoader", "continueAfterOemApplied, found carrier config app: " + carrierServicePackageNameForLogicalSlot + " for phone " + i);
            Handler handler3 = this.mHandler;
            handler3.sendMessage(handler3.obtainMessage(8, i, -1));
            return;
        }
        this.mOwner.updateSubscriptionDatabase(i);
    }

    private void handleOemServiceConnected(final int i, IBinder iBinder) {
        this.mHandler.removeMessages(106, Integer.valueOf(i));
        final CarrierIdentifier carrierIdentifierForPhoneId = this.mOwner.getCarrierIdentifierForPhoneId(i);
        try {
            ICarrierService.Stub.asInterface(iBinder).getCarrierConfig(i, carrierIdentifierForPhoneId, new ResultReceiver(this.mHandler) { // from class: com.android.phone.MiCarrierConfigLoaderImpl.4
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i2, Bundle bundle) {
                    MiCarrierConfigLoaderImpl.this.unbindOemIfBound(i);
                    if (i2 == 1 || bundle == null) {
                        Log.e("CarrierConfigLoader", "Failed to get carrier config from OEM app: com.xiaomi.carrierconfig");
                        MiCarrierConfigLoaderImpl.this.continueAfterOemApplied(i);
                        return;
                    }
                    PersistableBundle persistableBundle = (PersistableBundle) bundle.getParcelable("config_bundle");
                    if (persistableBundle != null && MiCarrierConfigLoaderImpl.this.mConfigFromDefaultApp[i] != null) {
                        MiCarrierConfigLoaderImpl.this.mConfigFromDefaultApp[i].putAll(persistableBundle);
                        MiCarrierConfigLoaderImpl.this.mOwner.saveConfigToXml("com.xiaomi.carrierconfig", "", i, carrierIdentifierForPhoneId, persistableBundle);
                    }
                    MiCarrierConfigLoaderImpl.this.reportConfigErrorStatistics(i, true);
                    MiCarrierConfigLoaderImpl.this.mHandler.obtainMessage(107, i, -1).sendToTarget();
                }
            });
        } catch (RemoteException e) {
            Log.e("CarrierConfigLoader", "Failed to get OEM carrier config: " + e);
            unbindOemIfBound(i);
            continueAfterOemApplied(i);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i, -1, Integer.valueOf(i)), 30000L);
    }

    private void handleOemFetchTimeout(int i) {
        Log.e("CarrierConfigLoader", "Bind/fetch timeout from OEM app: com.xiaomi.carrierconfig");
        unbindOemIfBound(i);
        continueAfterOemApplied(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindOemIfBound(int i) {
        ServiceConnection serviceConnection = this.mOemServiceConnections[i];
        if (serviceConnection != null) {
            boolean[] zArr = this.mOemServiceBound;
            if (zArr[i]) {
                zArr[i] = false;
                this.mContext.unbindService(serviceConnection);
            }
        }
        this.mOemServiceConnections[i] = null;
    }
}
