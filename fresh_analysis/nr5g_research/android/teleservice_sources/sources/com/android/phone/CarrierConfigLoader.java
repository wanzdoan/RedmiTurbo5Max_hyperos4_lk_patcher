package com.android.phone;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PermissionEnforcer;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.service.carrier.CarrierIdentifier;
import android.service.carrier.ICarrierService;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.ICarrierConfigLoader;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.mirilhook.MiRilHook;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.pickerwidget.date.Calendar$$ExternalSyntheticBUOutline1;

/* JADX INFO: loaded from: classes.dex */
public class CarrierConfigLoader extends ICarrierConfigLoader.Stub {
    private static CarrierConfigLoader sInstance;
    private final LocalLog mCarrierConfigLoadingLog;
    private CarrierServiceChangeCallback[] mCarrierServiceChangeCallbacks;
    private PersistableBundle[] mConfigFromCarrierApp;
    private PersistableBundle[] mConfigFromDefaultApp;
    private Context mContext;
    private final FeatureFlags mFeatureFlags;
    private boolean[] mFromSystemUnlocked;
    private final Handler mHandler;
    private boolean[] mHasSentConfigChange;
    private boolean[] mNeedNotifyCallback;
    private PersistableBundle mNoSimConfig;
    private int mNumPhones;
    private PersistableBundle[] mOverrideConfigs;
    private final PackageManager mPackageManager;
    private PersistableBundle[] mPersistentOverrideConfigs;
    private final String mPlatformCarrierConfigPackage;
    private boolean[] mServiceBound;
    private boolean[] mServiceBoundForNoSimConfig;
    private CarrierServiceConnection[] mServiceConnection;
    private CarrierServiceConnection[] mServiceConnectionForNoSimConfig;
    private final BroadcastReceiver mSystemBroadcastReceiver;
    private int mTimeoutId;
    private final int mVendorApiLevel;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final Set OVERRIDE_BLOCKLIST_ON_USER_BUILD = Set.of("satellite_entitlement_supported_bool", "satellite_data_support_mode_int");
    private static final String[] CONFIG_SUBSET_METADATA_KEYS = {"carrier_config_version_string", "carrier_config_applied_bool"};

    class ConfigHandler extends Handler {
        ConfigHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            final int i = message.arg1;
            CarrierConfigLoader.this.logd(CarrierConfigLoader.eventToString(message.what) + " phoneId: " + i);
            if (SubscriptionManager.isValidPhoneId(i) || message.what == 17) {
                int i2 = message.what;
                if (i2 == 0) {
                    CarrierConfigLoader.this.mNeedNotifyCallback[i] = true;
                    CarrierConfigLoader.this.clearConfigForPhone(i, true);
                    return;
                }
                switch (i2) {
                    case 3:
                        removeMessages(10, CarrierConfigLoader.this.getMessageToken(i));
                        final CarrierServiceConnection carrierServiceConnection = (CarrierServiceConnection) message.obj;
                        if (CarrierConfigLoader.this.mServiceConnection[i] != carrierServiceConnection || carrierServiceConnection.service == null) {
                            CarrierConfigLoader carrierConfigLoader = CarrierConfigLoader.this;
                            carrierConfigLoader.unbindIfBound(carrierConfigLoader.mContext, carrierServiceConnection, i);
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        }
                        final CarrierIdentifier carrierIdentifierForPhoneId = CarrierConfigLoader.this.getCarrierIdentifierForPhoneId(i);
                        try {
                            ICarrierService.Stub.asInterface(carrierServiceConnection.service).getCarrierConfig(i, carrierIdentifierForPhoneId, new ResultReceiver(this) { // from class: com.android.phone.CarrierConfigLoader.ConfigHandler.1
                                @Override // android.os.ResultReceiver
                                public void onReceiveResult(int i3, Bundle bundle) {
                                    CarrierConfigLoader carrierConfigLoader2 = CarrierConfigLoader.this;
                                    carrierConfigLoader2.unbindIfBound(carrierConfigLoader2.mContext, carrierServiceConnection, i);
                                    ConfigHandler configHandler = ConfigHandler.this;
                                    configHandler.removeMessages(14, CarrierConfigLoader.this.getMessageToken(i));
                                    if (CarrierConfigLoader.this.mServiceConnection[i] != carrierServiceConnection) {
                                        CarrierConfigLoader.this.loge("Received response for stale request to default app.");
                                        if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                            CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                            return;
                                        }
                                        return;
                                    }
                                    if (i3 == 1 || bundle == null) {
                                        CarrierConfigLoader.this.loge("Failed to get carrier config");
                                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                                        return;
                                    }
                                    PersistableBundle persistableBundle = (PersistableBundle) bundle.getParcelable("config_bundle");
                                    CarrierConfigLoader carrierConfigLoader3 = CarrierConfigLoader.this;
                                    carrierConfigLoader3.saveConfigToXml(carrierConfigLoader3.mPlatformCarrierConfigPackage, "", i, carrierIdentifierForPhoneId, persistableBundle);
                                    PersistableBundle[] persistableBundleArr = CarrierConfigLoader.this.mConfigFromDefaultApp;
                                    int i4 = i;
                                    persistableBundleArr[i4] = persistableBundle;
                                    ConfigHandler configHandler2 = ConfigHandler.this;
                                    configHandler2.sendMessage(configHandler2.obtainMessage(5, i4, -1));
                                }
                            });
                            CarrierConfigLoader.this.logl("Fetch config for default app: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage + ", carrierId=" + carrierIdentifierForPhoneId.getSpecificCarrierId());
                            sendMessageDelayed(obtainMessage(14, i, -1, CarrierConfigLoader.this.getMessageToken(i)), 30000L);
                            return;
                        } catch (RemoteException e) {
                            CarrierConfigLoader.this.loge("Failed to get carrier config from default app: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage + " err: " + e);
                            CarrierConfigLoader carrierConfigLoader2 = CarrierConfigLoader.this;
                            carrierConfigLoader2.unbindIfBound(carrierConfigLoader2.mContext, carrierServiceConnection, i);
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        }
                    case 4:
                        removeMessages(11, CarrierConfigLoader.this.getMessageToken(i));
                        final CarrierServiceConnection carrierServiceConnection2 = (CarrierServiceConnection) message.obj;
                        if (CarrierConfigLoader.this.mServiceConnection[i] != carrierServiceConnection2 || carrierServiceConnection2.service == null) {
                            CarrierConfigLoader carrierConfigLoader3 = CarrierConfigLoader.this;
                            carrierConfigLoader3.unbindIfBound(carrierConfigLoader3.mContext, carrierServiceConnection2, i);
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        }
                        final CarrierIdentifier carrierIdentifierForPhoneId2 = CarrierConfigLoader.this.getCarrierIdentifierForPhoneId(i);
                        try {
                            ICarrierService.Stub.asInterface(carrierServiceConnection2.service).getCarrierConfig(i, carrierIdentifierForPhoneId2, new ResultReceiver(this) { // from class: com.android.phone.CarrierConfigLoader.ConfigHandler.2
                                @Override // android.os.ResultReceiver
                                public void onReceiveResult(int i3, Bundle bundle) {
                                    CarrierConfigLoader carrierConfigLoader4 = CarrierConfigLoader.this;
                                    carrierConfigLoader4.unbindIfBound(carrierConfigLoader4.mContext, carrierServiceConnection2, i);
                                    ConfigHandler configHandler = ConfigHandler.this;
                                    configHandler.removeMessages(15, CarrierConfigLoader.this.getMessageToken(i));
                                    if (CarrierConfigLoader.this.mServiceConnection[i] != carrierServiceConnection2) {
                                        CarrierConfigLoader.this.loge("Received response for stale request to carrier app.");
                                        if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                            CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                            return;
                                        }
                                        return;
                                    }
                                    if (i3 == 1 || bundle == null) {
                                        CarrierConfigLoader.this.loge("Failed to get carrier config from carrier app: " + CarrierConfigLoader.this.getCarrierPackageForPhoneId(i));
                                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i);
                                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                                        return;
                                    }
                                    PersistableBundle persistableBundle = (PersistableBundle) bundle.getParcelable("config_bundle");
                                    CarrierConfigLoader carrierConfigLoader5 = CarrierConfigLoader.this;
                                    carrierConfigLoader5.saveConfigToXml(carrierConfigLoader5.getCarrierPackageForPhoneId(i), "", i, carrierIdentifierForPhoneId2, persistableBundle);
                                    ConfigHandler configHandler2 = ConfigHandler.this;
                                    if (persistableBundle != null) {
                                        CarrierConfigLoader.this.logCarrierServiceCarrierConfigOverrides(i, persistableBundle);
                                        CarrierConfigLoader.this.mConfigFromCarrierApp[i] = persistableBundle;
                                    } else {
                                        CarrierConfigLoader.this.logl("Config from carrier app is null for phoneId " + i);
                                        CarrierConfigLoader.this.mConfigFromCarrierApp[i] = new PersistableBundle();
                                    }
                                    ConfigHandler configHandler3 = ConfigHandler.this;
                                    configHandler3.sendMessage(configHandler3.obtainMessage(6, i, -1));
                                }
                            });
                            CarrierConfigLoader.this.logl("Fetch config for carrier app: " + CarrierConfigLoader.this.getCarrierPackageForPhoneId(i) + ", carrierId=" + carrierIdentifierForPhoneId2.getSpecificCarrierId());
                            sendMessageDelayed(obtainMessage(15, i, -1, CarrierConfigLoader.this.getMessageToken(i)), 30000L);
                            return;
                        } catch (RemoteException e2) {
                            CarrierConfigLoader.this.loge("Failed to get carrier config: " + e2);
                            CarrierConfigLoader carrierConfigLoader4 = CarrierConfigLoader.this;
                            carrierConfigLoader4.unbindIfBound(carrierConfigLoader4.mContext, carrierServiceConnection2, i);
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        }
                    case 5:
                        if (!message.getData().getBoolean("loaded_from_xml", false) && CarrierConfigLoader.this.mServiceConnection[i] == null) {
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        }
                        boolean zIsMiuiRom = TelephonyBaseUtilsStub.isMiuiRom();
                        CarrierConfigLoader carrierConfigLoader5 = CarrierConfigLoader.this;
                        if (zIsMiuiRom) {
                            carrierConfigLoader5.mServiceConnection[i] = null;
                            MiCarrierConfigLoaderStub.get().handleMessage(message, 103);
                            return;
                        }
                        String carrierPackageForPhoneId = carrierConfigLoader5.getCarrierPackageForPhoneId(i);
                        CarrierConfigLoader carrierConfigLoader6 = CarrierConfigLoader.this;
                        if (carrierPackageForPhoneId != null) {
                            carrierConfigLoader6.logd("Found carrier config app: ".concat(carrierPackageForPhoneId));
                            sendMessage(obtainMessage(8, i, -1));
                            return;
                        } else {
                            carrierConfigLoader6.updateSubscriptionDatabase(i);
                            CarrierConfigLoader.this.mServiceConnection[i] = null;
                            return;
                        }
                    case 6:
                        if (!message.getData().getBoolean("loaded_from_xml", false) && CarrierConfigLoader.this.mServiceConnection[i] == null) {
                            if (CarrierConfigLoader.this.mFromSystemUnlocked[i]) {
                                CarrierConfigLoader.this.mFromSystemUnlocked[i] = false;
                                return;
                            }
                            return;
                        } else {
                            CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                            CarrierConfigLoader.this.mServiceConnection[i] = null;
                            return;
                        }
                    case 7:
                        if (CarrierConfigLoader.this.mConfigFromCarrierApp[i] != null && CarrierConfigLoader.this.getCarrierPackageForPhoneId(i) == null) {
                            CarrierConfigLoader.this.mConfigFromCarrierApp[i] = null;
                        }
                        CarrierConfigLoader carrierConfigLoader7 = CarrierConfigLoader.this;
                        PersistableBundle persistableBundleRestoreConfigFromXml = carrierConfigLoader7.restoreConfigFromXml(carrierConfigLoader7.mPlatformCarrierConfigPackage, "-override", i);
                        if (persistableBundleRestoreConfigFromXml != null) {
                            CarrierConfigLoader.this.mPersistentOverrideConfigs[i] = persistableBundleRestoreConfigFromXml;
                        }
                        CarrierConfigLoader carrierConfigLoader8 = CarrierConfigLoader.this;
                        PersistableBundle persistableBundleRestoreConfigFromXml2 = carrierConfigLoader8.restoreConfigFromXml(carrierConfigLoader8.mPlatformCarrierConfigPackage, "", i);
                        CarrierConfigLoader carrierConfigLoader9 = CarrierConfigLoader.this;
                        if (persistableBundleRestoreConfigFromXml2 != null) {
                            carrierConfigLoader9.mConfigFromDefaultApp[i] = persistableBundleRestoreConfigFromXml2;
                            Message messageObtainMessage = obtainMessage(5, i, -1);
                            messageObtainMessage.getData().putBoolean("loaded_from_xml", true);
                            CarrierConfigLoader.this.mHandler.sendMessage(messageObtainMessage);
                            return;
                        }
                        boolean zBindToConfigPackage = carrierConfigLoader9.bindToConfigPackage(carrierConfigLoader9.mPlatformCarrierConfigPackage, i, 3);
                        CarrierConfigLoader carrierConfigLoader10 = CarrierConfigLoader.this;
                        if (zBindToConfigPackage) {
                            sendMessageDelayed(obtainMessage(10, i, -1, carrierConfigLoader10.getMessageToken(i)), 30000L);
                            CarrierConfigLoader.this.mTimeoutId = 10;
                            return;
                        }
                        carrierConfigLoader10.mConfigFromDefaultApp[i] = new PersistableBundle();
                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                        CarrierConfigLoader.this.loge("binding to default app: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage + " fails");
                        return;
                    case 8:
                        String carrierPackageForPhoneId2 = CarrierConfigLoader.this.getCarrierPackageForPhoneId(i);
                        PersistableBundle persistableBundleRestoreConfigFromXml3 = CarrierConfigLoader.this.restoreConfigFromXml(carrierPackageForPhoneId2, "", i);
                        if (persistableBundleRestoreConfigFromXml3 != null) {
                            CarrierConfigLoader.this.mConfigFromCarrierApp[i] = persistableBundleRestoreConfigFromXml3;
                            Message messageObtainMessage2 = obtainMessage(6, i, -1);
                            messageObtainMessage2.getData().putBoolean("loaded_from_xml", true);
                            sendMessage(messageObtainMessage2);
                            return;
                        }
                        if (carrierPackageForPhoneId2 != null && CarrierConfigLoader.this.bindToConfigPackage(carrierPackageForPhoneId2, i, 4)) {
                            sendMessageDelayed(obtainMessage(11, i, -1, CarrierConfigLoader.this.getMessageToken(i)), 30000L);
                            CarrierConfigLoader.this.mTimeoutId = 11;
                            return;
                        }
                        CarrierConfigLoader.this.mConfigFromCarrierApp[i] = new PersistableBundle();
                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i);
                        CarrierConfigLoader.this.loge("Bind to carrier app: " + carrierPackageForPhoneId2 + " fails");
                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                        return;
                    case 9:
                        String str = (String) message.obj;
                        CarrierConfigLoader.this.clearCachedConfigForPackage(str);
                        CarrierConfigLoader.this.logl("Package changed: " + str + ", phone=" + i);
                        CarrierConfigLoader.this.mNeedNotifyCallback[i] = true;
                        CarrierConfigLoader.this.updateConfigForPhoneId(i);
                        return;
                    case 10:
                    case 14:
                        CarrierConfigLoader.this.loge("Bind/fetch time out from " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage);
                        removeMessages(14, CarrierConfigLoader.this.getMessageToken(i));
                        if (CarrierConfigLoader.this.mServiceConnection[i] != null) {
                            CarrierConfigLoader carrierConfigLoader11 = CarrierConfigLoader.this;
                            carrierConfigLoader11.unbindIfBound(carrierConfigLoader11.mContext, CarrierConfigLoader.this.mServiceConnection[i], i);
                            CarrierConfigLoader.this.broadcastConfigChangedIntent(i);
                            CarrierConfigLoader.this.mServiceConnection[i] = null;
                        }
                        CarrierConfigLoader.this.mConfigFromDefaultApp[i] = new PersistableBundle();
                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                        return;
                    case 11:
                    case 15:
                        CarrierConfigLoader.this.loge("Bind/fetch from carrier app timeout, package=" + CarrierConfigLoader.this.getCarrierPackageForPhoneId(i));
                        removeMessages(15, CarrierConfigLoader.this.getMessageToken(i));
                        if (CarrierConfigLoader.this.mServiceConnection[i] != null) {
                            CarrierConfigLoader carrierConfigLoader12 = CarrierConfigLoader.this;
                            carrierConfigLoader12.unbindIfBound(carrierConfigLoader12.mContext, CarrierConfigLoader.this.mServiceConnection[i], i);
                            CarrierConfigLoader.this.broadcastConfigChangedIntent(i);
                            CarrierConfigLoader.this.mServiceConnection[i] = null;
                        }
                        CarrierConfigLoader.this.mConfigFromCarrierApp[i] = new PersistableBundle();
                        CarrierConfigLoader.this.updateSubscriptionDatabase(i);
                        return;
                    case 12:
                        if (MiCarrierConfigLoaderStub.get().handleMessage(message, 102)) {
                            return;
                        }
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CarrierConfigLoader.this.mContext);
                        String string = defaultSharedPreferences.getString("build_fingerprint", null);
                        String str2 = Build.FINGERPRINT;
                        if (str2.equals(string)) {
                            return;
                        }
                        CarrierConfigLoader.this.logd("Build fingerprint changed. old: " + string + " new: " + str2);
                        CarrierConfigLoader.this.clearCachedConfigForPackage(null);
                        defaultSharedPreferences.edit().putString("build_fingerprint", str2).apply();
                        return;
                    case 13:
                        break;
                    case 16:
                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i);
                        return;
                    case 17:
                        CarrierConfigLoader.this.onMultiSimConfigChanged();
                        return;
                    case 18:
                        CarrierConfigLoader carrierConfigLoader13 = CarrierConfigLoader.this;
                        PersistableBundle persistableBundleRestoreNoSimConfigFromXml = carrierConfigLoader13.restoreNoSimConfigFromXml(carrierConfigLoader13.mPlatformCarrierConfigPackage);
                        CarrierConfigLoader carrierConfigLoader14 = CarrierConfigLoader.this;
                        if (persistableBundleRestoreNoSimConfigFromXml != null) {
                            carrierConfigLoader14.mNoSimConfig = persistableBundleRestoreNoSimConfigFromXml;
                            sendMessage(obtainMessage(19, i, -1));
                            return;
                        }
                        if (carrierConfigLoader14.mServiceBoundForNoSimConfig[i]) {
                            CarrierConfigLoader.this.loge("Unbind stale connection before bind to default no sim config.");
                            removeMessages(22);
                            CarrierConfigLoader carrierConfigLoader15 = CarrierConfigLoader.this;
                            carrierConfigLoader15.unbindIfBoundForNoSimConfig(carrierConfigLoader15.mContext, CarrierConfigLoader.this.mServiceConnectionForNoSimConfig[i], i);
                        }
                        CarrierConfigLoader carrierConfigLoader16 = CarrierConfigLoader.this;
                        if (carrierConfigLoader16.bindToConfigPackage(carrierConfigLoader16.mPlatformCarrierConfigPackage, i, 20)) {
                            sendMessageDelayed(obtainMessage(21, i, -1), 30000L);
                            return;
                        }
                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i, false);
                        CarrierConfigLoader.this.loge("binding to default app to fetch no SIM config: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage + " fails");
                        return;
                    case 19:
                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i, false);
                        return;
                    case 20:
                        removeMessages(21);
                        final CarrierServiceConnection carrierServiceConnection3 = (CarrierServiceConnection) message.obj;
                        if (CarrierConfigLoader.this.mServiceConnectionForNoSimConfig[i] != carrierServiceConnection3) {
                            CarrierConfigLoader.this.loge("Received stale EVENT_CONNECTED_TO_DEFAULT_FOR_NO_SIM_CONFIG" + CarrierConfigLoader.this.mPlatformCarrierConfigPackage);
                            return;
                        }
                        if (carrierServiceConnection3.service == null) {
                            CarrierConfigLoader carrierConfigLoader17 = CarrierConfigLoader.this;
                            carrierConfigLoader17.unbindIfBoundForNoSimConfig(carrierConfigLoader17.mContext, carrierServiceConnection3, i);
                            return;
                        }
                        try {
                            ICarrierService.Stub.asInterface(carrierServiceConnection3.service).getCarrierConfig(i, (CarrierIdentifier) null, new ResultReceiver(this) { // from class: com.android.phone.CarrierConfigLoader.ConfigHandler.3
                                @Override // android.os.ResultReceiver
                                public void onReceiveResult(int i3, Bundle bundle) {
                                    CarrierServiceConnection carrierServiceConnection4 = CarrierConfigLoader.this.mServiceConnectionForNoSimConfig[i];
                                    CarrierServiceConnection carrierServiceConnection5 = carrierServiceConnection3;
                                    ConfigHandler configHandler = ConfigHandler.this;
                                    if (carrierServiceConnection4 != carrierServiceConnection5) {
                                        CarrierConfigLoader.this.loge("Received response for stale request.");
                                        return;
                                    }
                                    CarrierConfigLoader carrierConfigLoader18 = CarrierConfigLoader.this;
                                    carrierConfigLoader18.unbindIfBoundForNoSimConfig(carrierConfigLoader18.mContext, carrierServiceConnection3, i);
                                    ConfigHandler.this.removeMessages(22);
                                    if (i3 == 1 || bundle == null) {
                                        CarrierConfigLoader.this.loge("Failed to get no SIM carrier config");
                                        return;
                                    }
                                    PersistableBundle persistableBundle = (PersistableBundle) bundle.getParcelable("config_bundle");
                                    CarrierConfigLoader carrierConfigLoader19 = CarrierConfigLoader.this;
                                    carrierConfigLoader19.saveNoSimConfigToXml(carrierConfigLoader19.mPlatformCarrierConfigPackage, persistableBundle);
                                    CarrierConfigLoader.this.mNoSimConfig = persistableBundle;
                                    ConfigHandler configHandler2 = ConfigHandler.this;
                                    configHandler2.sendMessage(configHandler2.obtainMessage(19, i, -1));
                                }
                            });
                            CarrierConfigLoader.this.logl("Fetch no sim config from default app: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage);
                            sendMessageDelayed(obtainMessage(22, i, -1), 30000L);
                            return;
                        } catch (RemoteException e3) {
                            CarrierConfigLoader.this.loge("Failed to get no sim carrier config from default app: " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage + " err: " + e3);
                            CarrierConfigLoader carrierConfigLoader18 = CarrierConfigLoader.this;
                            carrierConfigLoader18.unbindIfBoundForNoSimConfig(carrierConfigLoader18.mContext, carrierServiceConnection3, i);
                            return;
                        }
                    case 21:
                    case 22:
                        CarrierConfigLoader.this.loge("Bind/fetch time out for no SIM config from " + CarrierConfigLoader.this.mPlatformCarrierConfigPackage);
                        removeMessages(22);
                        if (CarrierConfigLoader.this.mServiceConnectionForNoSimConfig[i] != null) {
                            CarrierConfigLoader carrierConfigLoader19 = CarrierConfigLoader.this;
                            carrierConfigLoader19.unbindIfBoundForNoSimConfig(carrierConfigLoader19.mContext, CarrierConfigLoader.this.mServiceConnectionForNoSimConfig[i], i);
                        }
                        CarrierConfigLoader.this.broadcastConfigChangedIntent(i, false);
                        return;
                    default:
                        if (MiCarrierConfigLoaderStub.get().handleMessage(message, message.what)) {
                            return;
                        }
                        CarrierConfigLoader.this.loge("unknown message:" + message.what);
                        return;
                }
                for (int i3 = 0; i3 < CarrierConfigLoader.this.mNumPhones; i3++) {
                    if (CarrierConfigLoader.this.mHasSentConfigChange[i3]) {
                        CarrierConfigLoader.this.logl("System unlocked");
                        CarrierConfigLoader.this.mFromSystemUnlocked[i3] = true;
                        CarrierConfigLoader.this.updateConfigForPhoneId(i3);
                    }
                }
            }
        }
    }

    CarrierConfigLoader(Context context, Looper looper, FeatureFlags featureFlags) {
        super(PermissionEnforcer.fromContext(context));
        ConfigLoaderBroadcastReceiver configLoaderBroadcastReceiver = new ConfigLoaderBroadcastReceiver();
        this.mSystemBroadcastReceiver = configLoaderBroadcastReceiver;
        this.mCarrierConfigLoadingLog = new LocalLog(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        this.mContext = context;
        this.mPlatformCarrierConfigPackage = context.getString(R.string.platform_carrier_config_package);
        this.mHandler = new ConfigHandler(looper);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        context.registerReceiver(configLoaderBroadcastReceiver, intentFilter);
        int activeModemCount = TelephonyManager.from(context).getActiveModemCount();
        this.mNumPhones = activeModemCount;
        this.mConfigFromDefaultApp = new PersistableBundle[activeModemCount];
        this.mConfigFromCarrierApp = new PersistableBundle[activeModemCount];
        this.mPersistentOverrideConfigs = new PersistableBundle[activeModemCount];
        this.mOverrideConfigs = new PersistableBundle[activeModemCount];
        this.mNoSimConfig = new PersistableBundle();
        int i = this.mNumPhones;
        this.mServiceConnection = new CarrierServiceConnection[i];
        this.mServiceBound = new boolean[i];
        this.mHasSentConfigChange = new boolean[i];
        this.mFromSystemUnlocked = new boolean[i];
        this.mNeedNotifyCallback = new boolean[i];
        this.mServiceConnectionForNoSimConfig = new CarrierServiceConnection[i];
        this.mServiceBoundForNoSimConfig = new boolean[i];
        this.mCarrierServiceChangeCallbacks = new CarrierServiceChangeCallback[i];
        for (int i2 = 0; i2 < this.mNumPhones; i2++) {
            this.mCarrierServiceChangeCallbacks[i2] = new CarrierServiceChangeCallback(i2);
            TelephonyManager.from(context).registerCarrierPrivilegesCallback(i2, new HandlerExecutor(this.mHandler), this.mCarrierServiceChangeCallbacks[i2]);
        }
        this.mFeatureFlags = featureFlags;
        this.mPackageManager = context.getPackageManager();
        this.mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
        logd("CarrierConfigLoader has started");
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mHandler, 17, (Object) null);
        this.mHandler.sendEmptyMessage(12);
        MiCarrierConfigLoaderStub.get().init(this, this.mContext, this.mConfigFromDefaultApp, this.mHandler);
    }

    static CarrierConfigLoader init(Context context, FeatureFlags featureFlags) {
        CarrierConfigLoader carrierConfigLoader;
        synchronized (CarrierConfigLoader.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("CarrierConfigLoader", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new CarrierConfigLoader(context, Looper.myLooper(), featureFlags);
                    TelephonyFrameworkInitializer.getTelephonyServiceManager().getCarrierConfigServiceRegisterer().register(sInstance);
                }
                carrierConfigLoader = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return carrierConfigLoader;
    }

    void clearConfigForPhone(int i, boolean z) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || !phone.isShuttingDown()) {
            if (TelephonyManager.from(this.mContext).getActiveModemCount() == 0) {
                logd("clearConfigForPhone return directly for numPhones == 0.");
                return;
            }
            PersistableBundle[] persistableBundleArr = this.mConfigFromDefaultApp;
            if (persistableBundleArr.length <= i) {
                Log.wtf("CarrierConfigLoader", "Invalid phone id " + i);
                return;
            }
            persistableBundleArr[i] = null;
            this.mConfigFromCarrierApp[i] = null;
            this.mServiceConnection[i] = null;
            this.mHasSentConfigChange[i] = false;
            if (z) {
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(18, i, -1));
            }
        }
    }

    protected void updateSubscriptionDatabase(final int i) {
        String carrierPackageForPhoneId;
        PersistableBundle persistableBundle;
        logd("updateSubscriptionDatabase: phoneId=" + i);
        getSpecificCarrierIdForPhoneId(i);
        if (this.mConfigFromCarrierApp[i] != null) {
            carrierPackageForPhoneId = getCarrierPackageForPhoneId(i);
            persistableBundle = this.mConfigFromCarrierApp[i];
        } else {
            carrierPackageForPhoneId = this.mPlatformCarrierConfigPackage;
            persistableBundle = this.mConfigFromDefaultApp[i];
        }
        if (persistableBundle == null) {
            persistableBundle = new PersistableBundle();
        }
        PersistableBundle persistableBundle2 = this.mOverrideConfigs[i];
        if (persistableBundle2 != null) {
            PersistableBundle persistableBundle3 = new PersistableBundle(persistableBundle);
            persistableBundle3.putAll(persistableBundle2);
            persistableBundle = persistableBundle3;
        }
        SubscriptionManagerService subscriptionManagerService = SubscriptionManagerService.getInstance();
        if (subscriptionManagerService == null) {
            loge("SubscriptionManagerService missing");
        } else {
            subscriptionManagerService.updateSubscriptionByCarrierConfig(i, carrierPackageForPhoneId, persistableBundle, new Runnable() { // from class: com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSubscriptionDatabase$0(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSubscriptionDatabase$0(int i) {
        this.mHandler.obtainMessage(16, i, -1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastConfigChangedIntent(int i) {
        broadcastConfigChangedIntent(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastConfigChangedIntent(int i, boolean z) {
        int subscriptionId;
        int carrierIdForPhoneId;
        int specificCarrierIdForPhoneId;
        int simApplicationStateForPhone;
        if (MiCarrierConfigLoaderStub.get().isLoadedError(i)) {
            MiCarrierConfigLoaderStub.get().resetCarrierConfig();
            return;
        }
        Intent intent = new Intent("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intent.addFlags(335544320);
        if (!z || (simApplicationStateForPhone = getSimApplicationStateForPhone(i)) == 0 || simApplicationStateForPhone == 6) {
            subscriptionId = -1;
            carrierIdForPhoneId = -1;
            specificCarrierIdForPhoneId = -1;
        } else {
            subscriptionId = SubscriptionManager.getSubscriptionId(i);
            carrierIdForPhoneId = getCarrierIdForPhoneId(i);
            specificCarrierIdForPhoneId = getSpecificCarrierIdForPhoneId(i);
            intent.putExtra("android.telephony.extra.SPECIFIC_CARRIER_ID", specificCarrierIdForPhoneId);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, i);
            intent.putExtra("android.telephony.extra.CARRIER_ID", carrierIdForPhoneId);
        }
        intent.putExtra("android.telephony.extra.SLOT_INDEX", i);
        intent.putExtra("android.telephony.extra.REBROADCAST_ON_UNLOCK", this.mFromSystemUnlocked[i]);
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) this.mContext.getSystemService(TelephonyRegistryManager.class);
        if (telephonyRegistryManager != null && this.mNeedNotifyCallback[i]) {
            logl("Notify carrier config changed callback for phone " + i);
            telephonyRegistryManager.notifyCarrierConfigChanged(i, subscriptionId, carrierIdForPhoneId, specificCarrierIdForPhoneId);
            this.mNeedNotifyCallback[i] = false;
        } else {
            logl("Skipped notifying carrier config changed callback for phone " + i);
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            logl("Broadcast CARRIER_CONFIG_CHANGED for phone " + i + ", subId=" + subscriptionId);
        } else {
            logl("Broadcast CARRIER_CONFIG_CHANGED for phone " + i);
        }
        this.mHasSentConfigChange[i] = true;
        this.mFromSystemUnlocked[i] = false;
    }

    private int getSimApplicationStateForPhone(int i) {
        TelephonyManager telephonyManagerCreateForSubscriptionId;
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionId) && (telephonyManagerCreateForSubscriptionId = TelephonyManager.from(this.mContext).createForSubscriptionId(subscriptionId)) != null) {
            return telephonyManagerCreateForSubscriptionId.getSimApplicationState();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindToConfigPackage(String str, int i, int i2) {
        logl("Binding to " + str + " for phone " + i);
        Intent intent = new Intent("android.service.carrier.CarrierService");
        intent.setPackage(str);
        CarrierServiceConnection carrierServiceConnection = new CarrierServiceConnection(i, str, i2);
        if (i2 == 20) {
            this.mServiceConnectionForNoSimConfig[i] = carrierServiceConnection;
        } else {
            this.mServiceConnection[i] = carrierServiceConnection;
        }
        try {
            if (this.mContext.bindServiceAsUser(intent, carrierServiceConnection, 1, UserHandle.of(ActivityManager.getCurrentUser()))) {
                if (i2 == 20) {
                    this.mServiceBoundForNoSimConfig[i] = true;
                } else {
                    this.mServiceBound[i] = true;
                }
                return true;
            }
        } catch (SecurityException unused) {
        }
        return false;
    }

    CarrierIdentifier getCarrierIdentifierForPhoneId(int i) {
        String str;
        String strSubstring;
        String str2;
        String str3;
        int i2;
        int specificCarrierId;
        String str4;
        String simOperatorNameForPhone = TelephonyManager.from(this.mContext).getSimOperatorNameForPhone(i);
        String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i);
        if (simOperatorNumericForPhone == null || simOperatorNumericForPhone.length() < 3) {
            str = "";
            strSubstring = str;
        } else {
            String strSubstring2 = simOperatorNumericForPhone.substring(0, 3);
            strSubstring = simOperatorNumericForPhone.substring(3);
            str = strSubstring2;
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            str2 = "";
            str3 = str2;
            i2 = -1;
            specificCarrierId = -1;
            str4 = str3;
        } else {
            String subscriberId = phone.getSubscriberId();
            String groupIdLevel1 = phone.getGroupIdLevel1();
            String groupIdLevel2 = phone.getGroupIdLevel2();
            int carrierId = phone.getCarrierId();
            specificCarrierId = phone.getSpecificCarrierId();
            str2 = groupIdLevel1;
            str3 = groupIdLevel2;
            i2 = carrierId;
            str4 = subscriberId;
        }
        return new CarrierIdentifier(str, strSubstring, simOperatorNameForPhone, str4, str2, str3, i2, specificCarrierId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCarrierPackageForPhoneId(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return TelephonyManager.from(this.mContext).getCarrierServicePackageNameForLogicalSlot(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private String getIccIdForPhoneId(int i) {
        Phone phone;
        if (SubscriptionManager.isValidPhoneId(i) && (phone = PhoneFactory.getPhone(i)) != null) {
            return phone.getIccSerialNumber();
        }
        return null;
    }

    private int getSpecificCarrierIdForPhoneId(int i) {
        Phone phone;
        if (SubscriptionManager.isValidPhoneId(i) && (phone = PhoneFactory.getPhone(i)) != null) {
            return phone.getSpecificCarrierId();
        }
        return -1;
    }

    private int getCarrierIdForPhoneId(int i) {
        Phone phone;
        if (SubscriptionManager.isValidPhoneId(i) && (phone = PhoneFactory.getPhone(i)) != null) {
            return phone.getCarrierId();
        }
        return -1;
    }

    protected void saveConfigToXml(String str, String str2, int i, CarrierIdentifier carrierIdentifier, PersistableBundle persistableBundle, boolean z) {
        String filenameForConfig;
        if (str == null) {
            loge("Cannot save config with null packageName. phoneId=" + i);
            return;
        }
        if (z) {
            filenameForConfig = getFilenameForNoSimConfig(str);
        } else {
            if (TelephonyManager.getSimStateForSlotIndex(i) != 10) {
                loge("Skip saving config because SIM records are not loaded. phoneId=" + i);
                return;
            }
            String iccIdForPhoneId = getIccIdForPhoneId(i);
            int specificCarrierId = carrierIdentifier != null ? carrierIdentifier.getSpecificCarrierId() : -1;
            if (iccIdForPhoneId == null) {
                loge("Cannot save config with null iccid. phoneId=" + i);
                return;
            }
            filenameForConfig = getFilenameForConfig(str, str2, iccIdForPhoneId, specificCarrierId);
        }
        if (persistableBundle == null || persistableBundle.isEmpty()) {
            return;
        }
        String packageVersion = getPackageVersion(str);
        if (packageVersion == null) {
            loge("Failed to get package version for: " + str + ", phoneId=" + i);
            return;
        }
        logl("Save carrier config to cache. phoneId=" + i + ", xml=" + getFilePathForLogging(filenameForConfig) + ", version=" + packageVersion);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getFilesDir(), filenameForConfig));
            persistableBundle.putString("__carrier_config_package_version__", packageVersion);
            persistableBundle.writeToStream(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            loge(e.toString());
        }
    }

    void saveConfigToXml(String str, String str2, int i, CarrierIdentifier carrierIdentifier, PersistableBundle persistableBundle) {
        saveConfigToXml(str, str2, i, carrierIdentifier, persistableBundle, false);
    }

    void saveNoSimConfigToXml(String str, PersistableBundle persistableBundle) {
        saveConfigToXml(str, "", -1, null, persistableBundle, true);
    }

    /* JADX WARN: Code duplicated, block: B:49:0x00d2  */
    /* JADX WARN: Code duplicated, block: B:50:0x00ee  */
    /* JADX WARN: Instruction removed from duplicated block: B:49:0x00d2, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:50:0x00ee, please report this as an issue */
    private PersistableBundle restoreConfigFromXml(String str, String str2, int i, boolean z) throws Throwable {
        String iccIdForPhoneId;
        String filenameForConfig;
        String string;
        if (str == null) {
            loge("Cannot restore config with null packageName");
        }
        String packageVersion = getPackageVersion(str);
        PersistableBundle persistableBundle = null;
        if (packageVersion == null) {
            loge("Failed to get package version for: " + str);
            return null;
        }
        if (z) {
            filenameForConfig = getFilenameForNoSimConfig(str);
            iccIdForPhoneId = null;
        } else {
            if (TelephonyManager.getSimStateForSlotIndex(i) != 10) {
                loge("Skip restore config because SIM records are not loaded. phoneId=" + i);
                return null;
            }
            iccIdForPhoneId = getIccIdForPhoneId(i);
            int specificCarrierIdForPhoneId = getSpecificCarrierIdForPhoneId(i);
            if (iccIdForPhoneId == null) {
                loge("Cannot restore config with null iccid. phoneId=" + i);
                return null;
            }
            filenameForConfig = getFilenameForConfig(str, str2, iccIdForPhoneId, specificCarrierIdForPhoneId);
        }
        File file = new File(this.mContext.getFilesDir(), filenameForConfig);
        String path = file.getPath();
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    PersistableBundle fromStream = PersistableBundle.readFromStream(fileInputStream);
                    try {
                        string = fromStream.getString("__carrier_config_package_version__");
                        try {
                            fromStream.remove("__carrier_config_package_version__");
                            if (packageVersion.equals(string)) {
                                persistableBundle = fromStream;
                            } else {
                                loge("Saved version mismatch: " + packageVersion + " vs " + string + ", phoneId=" + i);
                            }
                            fileInputStream.close();
                        } catch (Throwable th) {
                            th = th;
                            try {
                                fileInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            } catch (FileNotFoundException unused) {
                if (z) {
                    logd("File not found: " + file.getPath() + ", phoneId=" + i);
                } else {
                    logd("File not found : " + getFilePathForLogging(path, iccIdForPhoneId) + ", phoneId=" + i);
                }
            } catch (IOException e) {
                e = e;
                loge(e.toString());
            }
        } catch (FileNotFoundException unused2) {
            string = null;
            if (z) {
                logd("File not found: " + file.getPath() + ", phoneId=" + i);
            } else {
                logd("File not found : " + getFilePathForLogging(path, iccIdForPhoneId) + ", phoneId=" + i);
            }
        } catch (IOException e2) {
            e = e2;
            string = null;
            loge(e.toString());
        }
        if (persistableBundle != null) {
            logl("Restored carrier config from cache. phoneId=" + i + ", xml=" + getFilePathForLogging(filenameForConfig) + ", version=" + string + ", modified time=" + getFileTime(path));
        }
        return persistableBundle;
    }

    private String getFilePathForLogging(String str, String str2) {
        if (!TelephonyUtils.IS_DEBUGGABLE) {
            if ((str2 != null ? str2.length() : 0) > 5 && str != null) {
                return str.replace(str2.substring(5), "***************");
            }
        }
        return str;
    }

    protected PersistableBundle restoreConfigFromXml(String str, String str2, int i) {
        return restoreConfigFromXml(str, str2, i, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PersistableBundle restoreNoSimConfigFromXml(String str) {
        return restoreConfigFromXml(str, "", -1, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearCachedConfigForPackage(final String str) {
        File[] fileArrListFiles = this.mContext.getFilesDir().listFiles(new FilenameFilter(this) { // from class: com.android.phone.CarrierConfigLoader.1
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
            logl("Deleting " + getFilePathForLogging(file.getName()));
            file.delete();
        }
        return true;
    }

    private String getFilePathForLogging(String str) {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str) || (strArrSplit = str.split("-")) == null || strArrSplit.length <= 2) {
            return str;
        }
        String str2 = strArrSplit[strArrSplit.length - 2];
        if (TextUtils.isEmpty(str2) && strArrSplit.length > 3) {
            str2 = strArrSplit[strArrSplit.length - 3];
        }
        return getFilePathForLogging(str, str2);
    }

    private static String getFilenameForConfig(String str, String str2, String str3, int i) {
        return MiCarrierConfigLoaderStub.get().getFilenameForConfig(str, str2, str3, i, "carrierconfig-" + str + str2 + "-" + str3 + "-" + i + ".xml");
    }

    private String getFilenameForNoSimConfig(String str) {
        return "carrierconfig-" + str + "-nosim.xml";
    }

    private String getPackageVersion(String str) {
        try {
            return Long.toString(this.mContext.getPackageManager().getPackageInfoAsUser(str, 0, ActivityManager.getCurrentUser()).getLongVersionCode());
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigForPhoneId(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(7, i, -1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged() {
        int i = this.mNumPhones;
        int activeModemCount = TelephonyManager.from(this.mContext).getActiveModemCount();
        this.mNumPhones = activeModemCount;
        if (activeModemCount == i) {
            return;
        }
        logl("mNumPhones change from " + i + " to " + this.mNumPhones);
        for (int i2 = this.mNumPhones; i2 < i; i2++) {
            CarrierServiceConnection carrierServiceConnection = this.mServiceConnection[i2];
            if (carrierServiceConnection != null) {
                unbindIfBound(this.mContext, carrierServiceConnection, i2);
            }
            CarrierServiceConnection carrierServiceConnection2 = this.mServiceConnectionForNoSimConfig[i2];
            if (carrierServiceConnection2 != null) {
                unbindIfBoundForNoSimConfig(this.mContext, carrierServiceConnection2, i2);
            }
        }
        for (int i3 = 0; i3 < i; i3++) {
            if (this.mCarrierServiceChangeCallbacks[i3] != null) {
                TelephonyManager.from(this.mContext).unregisterCarrierPrivilegesCallback(this.mCarrierServiceChangeCallbacks[i3]);
            }
        }
        this.mConfigFromDefaultApp = (PersistableBundle[]) Arrays.copyOf(this.mConfigFromDefaultApp, this.mNumPhones);
        this.mConfigFromCarrierApp = (PersistableBundle[]) Arrays.copyOf(this.mConfigFromCarrierApp, this.mNumPhones);
        this.mPersistentOverrideConfigs = (PersistableBundle[]) Arrays.copyOf(this.mPersistentOverrideConfigs, this.mNumPhones);
        this.mOverrideConfigs = (PersistableBundle[]) Arrays.copyOf(this.mOverrideConfigs, this.mNumPhones);
        this.mServiceConnection = (CarrierServiceConnection[]) Arrays.copyOf(this.mServiceConnection, this.mNumPhones);
        this.mServiceConnectionForNoSimConfig = (CarrierServiceConnection[]) Arrays.copyOf(this.mServiceConnectionForNoSimConfig, this.mNumPhones);
        this.mServiceBound = Arrays.copyOf(this.mServiceBound, this.mNumPhones);
        this.mServiceBoundForNoSimConfig = Arrays.copyOf(this.mServiceBoundForNoSimConfig, this.mNumPhones);
        this.mHasSentConfigChange = Arrays.copyOf(this.mHasSentConfigChange, this.mNumPhones);
        this.mFromSystemUnlocked = Arrays.copyOf(this.mFromSystemUnlocked, this.mNumPhones);
        this.mNeedNotifyCallback = Arrays.copyOf(this.mNeedNotifyCallback, this.mNumPhones);
        this.mCarrierServiceChangeCallbacks = (CarrierServiceChangeCallback[]) Arrays.copyOf(this.mCarrierServiceChangeCallbacks, this.mNumPhones);
        for (int i4 = 0; i4 < this.mNumPhones; i4++) {
            this.mNeedNotifyCallback[i4] = true;
            updateConfigForPhoneId(i4);
            this.mCarrierServiceChangeCallbacks[i4] = new CarrierServiceChangeCallback(i4);
            TelephonyManager.from(this.mContext).registerCarrierPrivilegesCallback(i4, new HandlerExecutor(this.mHandler), this.mCarrierServiceChangeCallbacks[i4]);
        }
    }

    public PersistableBundle getConfigForSubId(int i, String str) {
        return getConfigForSubIdWithFeature(i, str, null);
    }

    public PersistableBundle getConfigForSubIdWithFeature(int i, String str, String str2) {
        if (this.mContext.checkCallingOrSelfPermission("com.android.telephonycore.permission.TELECOM_UI_ACCESS") != 0 && !TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str, str2, "getCarrierConfig")) {
            return new PersistableBundle();
        }
        if (!this.mContext.getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getConfigForSubIdWithFeature");
        }
        int phoneId = SubscriptionManager.getPhoneId(i);
        PersistableBundle defaultConfig = CarrierConfigManager.getDefaultConfig();
        if (TelephonyManager.from(this.mContext).getActiveModemCount() > 0 && SubscriptionManager.isValidPhoneId(phoneId)) {
            PersistableBundle persistableBundle = this.mConfigFromDefaultApp[phoneId];
            if (persistableBundle != null) {
                defaultConfig.putAll(persistableBundle);
            }
            PersistableBundle persistableBundle2 = this.mConfigFromCarrierApp[phoneId];
            if (persistableBundle2 != null) {
                defaultConfig.putAll(persistableBundle2);
            }
            PersistableBundle persistableBundle3 = this.mPersistentOverrideConfigs[phoneId];
            if (persistableBundle3 != null) {
                defaultConfig.putAll(persistableBundle3);
            }
            PersistableBundle persistableBundle4 = this.mOverrideConfigs[phoneId];
            if (persistableBundle4 != null) {
                defaultConfig.putAll(persistableBundle4);
            }
            defaultConfig.putBoolean("carrier_config_applied_bool", (this.mConfigFromCarrierApp[phoneId] != null || getCarrierPackageForPhoneId(phoneId) == null) && this.mConfigFromDefaultApp[phoneId] != null);
            return defaultConfig;
        }
        PersistableBundle persistableBundle5 = this.mNoSimConfig;
        if (persistableBundle5 != null) {
            defaultConfig.putAll(persistableBundle5);
        }
        return defaultConfig;
    }

    public PersistableBundle getConfigSubsetForSubIdWithFeature(int i, String str, String str2, String[] strArr) {
        Objects.requireNonNull(str, "Calling package must be non-null");
        Objects.requireNonNull(strArr, "Config keys must be non-null");
        enforceCallerIsSystemOrRequestingPackage(str);
        enforceTelephonyFeatureWithException(str, "getConfigSubsetForSubIdWithFeature");
        PersistableBundle configForSubIdWithFeature = getConfigForSubIdWithFeature(i, str, str2);
        if (configForSubIdWithFeature.isEmpty()) {
            return configForSubIdWithFeature;
        }
        for (String str3 : strArr) {
            Objects.requireNonNull(str3, "Config key must be non-null");
        }
        PersistableBundle persistableBundle = new PersistableBundle(strArr.length + CONFIG_SUBSET_METADATA_KEYS.length);
        for (String str4 : strArr) {
            Object obj = configForSubIdWithFeature.get(str4);
            if (obj != null) {
                if (obj instanceof PersistableBundle) {
                    persistableBundle.putPersistableBundle(str4, (PersistableBundle) obj);
                } else {
                    persistableBundle.putObject(str4, obj);
                }
            }
        }
        for (String str5 : CONFIG_SUBSET_METADATA_KEYS) {
            persistableBundle.putObject(str5, configForSubIdWithFeature.get(str5));
        }
        return persistableBundle;
    }

    private void secureOverrideConfig(PersistableBundle persistableBundle, boolean z) {
        if (TelephonyPermissions.isShell(getBinderCallingUid())) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("overrideConfig cannot be invoked by shell");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String modemService = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getModemService();
            logd("modemService=" + modemService);
            boolean zEquals = "android.telephony.mockmodem.MockModemService".equals(modemService);
            if (isUserBuild() && persistableBundle != null && !zEquals) {
                for (String str : OVERRIDE_BLOCKLIST_ON_USER_BUILD) {
                    if (persistableBundle.containsKey(str)) {
                        throw new SecurityException("Overriding " + str + " is not allowed on user builds.");
                    }
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            if (z && isUserBuild() && !isSystemApp()) {
                ImsRcsController$$ExternalSyntheticBUOutline0.m("overrideConfig with persistent=true only can be invoked by system app");
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void overrideConfig(final int i, final PersistableBundle persistableBundle, final boolean z) {
        overrideConfig_enforcePermission();
        secureOverrideConfig(persistableBundle, z);
        final int phoneId = SubscriptionManager.getPhoneId(i);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            logd("Ignore invalid phoneId: " + phoneId + " for subId: " + i);
            Calendar$$ExternalSyntheticBUOutline1.m("Invalid phoneId ", phoneId, " for subId ", i);
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "overrideConfig");
        this.mHandler.post(new Runnable() { // from class: com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$overrideConfig$1(phoneId, persistableBundle, z, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$overrideConfig$1(int i, PersistableBundle persistableBundle, boolean z, int i2) {
        CarrierConfigLoader carrierConfigLoader;
        int i3;
        this.mNeedNotifyCallback[i] = true;
        overrideConfig(this.mOverrideConfigs, i, persistableBundle);
        if (z) {
            overrideConfig(this.mPersistentOverrideConfigs, i, persistableBundle);
            if (persistableBundle != null) {
                carrierConfigLoader = this;
                i3 = i;
                carrierConfigLoader.saveConfigToXml(this.mPlatformCarrierConfigPackage, "-override", i3, getCarrierIdentifierForPhoneId(i), this.mPersistentOverrideConfigs[i]);
            } else {
                carrierConfigLoader = this;
                i3 = i;
                new File(carrierConfigLoader.mContext.getFilesDir(), getFilenameForConfig(carrierConfigLoader.mPlatformCarrierConfigPackage, "-override", carrierConfigLoader.getIccIdForPhoneId(i3), carrierConfigLoader.getSpecificCarrierIdForPhoneId(i3))).delete();
            }
        } else {
            carrierConfigLoader = this;
            i3 = i;
        }
        carrierConfigLoader.logl("overrideConfig: subId=" + i2 + ", persistent=" + z + ", overrides=" + persistableBundle);
        carrierConfigLoader.updateSubscriptionDatabase(i3);
    }

    private boolean isSystemApp() {
        int binderCallingUid = getBinderCallingUid();
        if (isSdkSandboxUidInternal(binderCallingUid)) {
            loge("isSystemApp: rejected call from SDK sandbox with uid=" + binderCallingUid);
            return false;
        }
        String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(binderCallingUid);
        if (packagesForUid != null) {
            for (String str : packagesForUid) {
                try {
                    ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(str, 0);
                    if (applicationInfo != null) {
                        int i = applicationInfo.flags;
                        if ((i & 1) != 0 || (i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) != 0) {
                            return true;
                        }
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    loge("isSystemApp: failed to get application info for " + str + ": " + e);
                }
            }
        }
        return false;
    }

    public boolean isUserBuild() {
        return "user".equals(Build.TYPE);
    }

    private void overrideConfig(PersistableBundle[] persistableBundleArr, int i, PersistableBundle persistableBundle) {
        if (persistableBundle == null) {
            persistableBundleArr[i] = new PersistableBundle();
            return;
        }
        PersistableBundle persistableBundle2 = persistableBundleArr[i];
        if (persistableBundle2 == null) {
            persistableBundleArr[i] = persistableBundle;
        } else {
            persistableBundle2.putAll(persistableBundle);
        }
    }

    public void notifyConfigChangedForSubId(int i) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mContext, i, "Require carrier privileges or MODIFY_PHONE_STATE permission.");
        int phoneId = SubscriptionManager.getPhoneId(i);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            String str = "Ignore invalid phoneId: " + phoneId + " for subId: " + i;
            AnomalyReporter.reportAnomaly(UUID.fromString("d81cef11-c2f1-4d76-955d-7f50e8590c48"), str);
            logd(str);
            Folme$$ExternalSyntheticBUOutline0.m(str);
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "notifyConfigChangedForSubId");
        logl("Notified carrier config changed. phoneId=" + phoneId + ", subId=" + i);
        clearCachedConfigForPackage(this.mContext.getPackageManager().getNameForUid(getBinderCallingUid()));
        this.mNeedNotifyCallback[phoneId] = true;
        updateConfigForPhoneId(phoneId);
    }

    public void updateConfigForPhoneId(int i, String str) {
        updateConfigForPhoneId_enforcePermission();
        logl("Update config for phoneId=" + i + " simState=" + str);
        if (!SubscriptionManager.isValidPhoneId(i)) {
            loge("updateConfigForPhoneId() Ignore invalid phoneId: " + i);
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "updateConfigForPhoneId");
        str.hashCode();
        switch (str) {
            case "LOADED":
            case "LOCKED":
                this.mNeedNotifyCallback[i] = true;
                updateConfigForPhoneId(i);
                break;
            case "CARD_IO_ERROR":
            case "UNKNOWN":
            case "NOT_READY":
            case "CARD_RESTRICTED":
            case "ABSENT":
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(0, i, -1));
                break;
        }
        MiCarrierConfigLoaderStub.get().updateConfigForPhoneId(i, str);
    }

    public String getDefaultCarrierServicePackageName() {
        getDefaultCarrierServicePackageName_enforcePermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "getDefaultCarrierServicePackageName");
        return this.mPlatformCarrierConfigPackage;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    PersistableBundle getConfigFromDefaultApp(int i) {
        return this.mConfigFromDefaultApp[i];
    }

    PersistableBundle getConfigFromCarrierApp(int i) {
        return this.mConfigFromCarrierApp[i];
    }

    PersistableBundle getNoSimConfig() {
        return this.mNoSimConfig;
    }

    PersistableBundle getOverrideConfig(int i) {
        return this.mOverrideConfigs[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindIfBound(Context context, CarrierServiceConnection carrierServiceConnection, int i) {
        try {
            boolean[] zArr = this.mServiceBound;
            if (zArr[i]) {
                zArr[i] = false;
                context.unbindService(carrierServiceConnection);
            }
        } catch (IllegalArgumentException unused) {
            loge("unbindIfBound : CarrierServiceConnection not registered when trying to unbind for phoneId: " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindIfBoundForNoSimConfig(Context context, CarrierServiceConnection carrierServiceConnection, int i) {
        try {
            boolean[] zArr = this.mServiceBoundForNoSimConfig;
            if (zArr[i]) {
                zArr[i] = false;
                context.unbindService(carrierServiceConnection);
            }
        } catch (IllegalArgumentException unused) {
            loge("unbindIfBoundForNoSimConfig : CarrierServiceConnection not registered when trying to unbind for phoneId: " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Integer getMessageToken(int i) {
        if (i < -128 || i > 127) {
            Folme$$ExternalSyntheticBUOutline0.m("phoneId should be in range [-128, 127], inclusive");
            return null;
        }
        return Integer.valueOf(i);
    }

    private String getFileTime(String str) {
        try {
            return TIME_FORMAT.format(Long.valueOf(Files.readAttributes(Paths.get(str, new String[0]), BasicFileAttributes.class, new LinkOption[0]).lastModifiedTime().toMillis()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0050  */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        CarrierConfigLoader carrierConfigLoader;
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "    ");
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            indentingPrintWriter.println("Permission Denial: can't dump carrierconfig from pid=" + Binder.getCallingPid() + ", uid=" + getBinderCallingUid());
            return;
        }
        int iIndexOf = ArrayUtils.indexOf(strArr, "--requesting-package");
        if (iIndexOf < 0 || iIndexOf >= strArr.length - 1) {
            str = null;
        } else {
            int i = iIndexOf + 1;
            if (TextUtils.isEmpty(strArr[i])) {
                str = null;
            } else {
                str = strArr[i];
                enforceCallerIsSystemOrRequestingPackage(str);
            }
        }
        indentingPrintWriter.println("CarrierConfigLoader: " + this);
        printConfig(CarrierConfigManager.getDefaultConfig(), indentingPrintWriter, "Default Values from CarrierConfigManager");
        for (int i2 = 0; i2 < this.mNumPhones; i2++) {
            indentingPrintWriter.println("Phone Id = " + i2);
            printConfig(this.mConfigFromDefaultApp[i2], indentingPrintWriter, "mConfigFromDefaultApp");
            printConfig(this.mConfigFromCarrierApp[i2], indentingPrintWriter, "mConfigFromCarrierApp");
            printConfig(this.mPersistentOverrideConfigs[i2], indentingPrintWriter, "mPersistentOverrideConfigs");
            printConfig(this.mOverrideConfigs[i2], indentingPrintWriter, "mOverrideConfigs");
        }
        printConfig(this.mNoSimConfig, indentingPrintWriter, "mNoSimConfig");
        indentingPrintWriter.println("mNumPhones=" + this.mNumPhones);
        indentingPrintWriter.println("mPlatformCarrierConfigPackage=" + this.mPlatformCarrierConfigPackage);
        indentingPrintWriter.println("mServiceConnection=[" + ((String) Stream.of((Object[]) this.mServiceConnection).map(new Function() { // from class: com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CarrierConfigLoader.$r8$lambda$YPqmXjplOg2ucJK27a6p7I3EXZQ((CarrierConfigLoader.CarrierServiceConnection) obj);
            }
        }).collect(Collectors.joining(", "))) + "]");
        StringBuilder sb = new StringBuilder("mServiceBoundForNoSimConfig=");
        sb.append(Arrays.toString(this.mServiceBoundForNoSimConfig));
        indentingPrintWriter.println(sb.toString());
        indentingPrintWriter.println("mHasSentConfigChange=" + Arrays.toString(this.mHasSentConfigChange));
        indentingPrintWriter.println("mFromSystemUnlocked=" + Arrays.toString(this.mFromSystemUnlocked));
        indentingPrintWriter.println("mNeedNotifyCallback=" + Arrays.toString(this.mNeedNotifyCallback));
        indentingPrintWriter.println();
        indentingPrintWriter.println("CarrierConfigLoader local log=");
        indentingPrintWriter.increaseIndent();
        this.mCarrierConfigLoadingLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        if (str != null) {
            logd("Including default and requesting package " + str + " carrier services in dump");
            indentingPrintWriter.println("");
            indentingPrintWriter.println("Connected services");
            carrierConfigLoader = this;
            carrierConfigLoader.dumpCarrierServiceIfBound(fileDescriptor, indentingPrintWriter, "Default config package", this.mPlatformCarrierConfigPackage, false);
            carrierConfigLoader.dumpCarrierServiceIfBound(fileDescriptor, indentingPrintWriter, "Requesting package", str, true);
        } else {
            carrierConfigLoader = this;
        }
        indentingPrintWriter.println();
        indentingPrintWriter.println("Cached config files:");
        indentingPrintWriter.increaseIndent();
        for (File file : carrierConfigLoader.mContext.getFilesDir().listFiles(new FilenameFilter() { // from class: com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda3
            @Override // java.io.FilenameFilter
            public final boolean accept(File file2, String str2) {
                return str2.startsWith("carrierconfig-");
            }
        })) {
            indentingPrintWriter.println(carrierConfigLoader.getFilePathForLogging(file.getName()) + ", modified time=" + carrierConfigLoader.getFileTime(file.getAbsolutePath()));
        }
        indentingPrintWriter.decreaseIndent();
    }

    public static /* synthetic */ String $r8$lambda$YPqmXjplOg2ucJK27a6p7I3EXZQ(CarrierServiceConnection carrierServiceConnection) {
        if (carrierServiceConnection != null) {
            return carrierServiceConnection.pkgName;
        }
        return null;
    }

    private void printConfig(PersistableBundle persistableBundle, IndentingPrintWriter indentingPrintWriter, String str) {
        indentingPrintWriter.increaseIndent();
        if (persistableBundle == null) {
            indentingPrintWriter.println(str + " : null ");
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("");
            return;
        }
        indentingPrintWriter.println(str + " : ");
        ArrayList arrayList = new ArrayList(persistableBundle.keySet());
        Collections.sort(arrayList);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.increaseIndent();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            String str2 = (String) obj;
            if (persistableBundle.get(str2) != null && (persistableBundle.get(str2) instanceof Object[])) {
                indentingPrintWriter.println(str2 + " = " + Arrays.toString((Object[]) persistableBundle.get(str2)));
            } else if (persistableBundle.get(str2) != null && (persistableBundle.get(str2) instanceof int[])) {
                indentingPrintWriter.println(str2 + " = " + Arrays.toString((int[]) persistableBundle.get(str2)));
            } else {
                indentingPrintWriter.println(str2 + " = " + persistableBundle.get(str2));
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("");
    }

    private void enforceCallerIsSystemOrRequestingPackage(String str) {
        int binderCallingUid = getBinderCallingUid();
        if (TelephonyPermissions.isRootOrShell(binderCallingUid) || TelephonyPermissions.isSystemOrPhone(binderCallingUid)) {
            return;
        }
        AppOpsManager appOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        if (appOpsManager == null) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("No AppOps");
        } else {
            appOpsManager.checkPackage(binderCallingUid, str);
        }
    }

    private void dumpCarrierServiceIfBound(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String str, String str2, boolean z) {
        IBinder iBinder;
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(str + " : " + str2);
        ArraySet arraySet = new ArraySet(this.mServiceConnection.length);
        CarrierServiceConnection[] carrierServiceConnectionArr = this.mServiceConnection;
        int length = carrierServiceConnectionArr.length;
        for (int i = 0; i < length; i++) {
            CarrierServiceConnection carrierServiceConnection = carrierServiceConnectionArr[i];
            if (carrierServiceConnection != null && SubscriptionManager.isValidPhoneId(carrierServiceConnection.phoneId) && !TextUtils.isEmpty(carrierServiceConnection.pkgName)) {
                String str3 = carrierServiceConnection.pkgName;
                boolean zEquals = TextUtils.equals(str2, str3);
                boolean z2 = z && hasCarrierPrivileges(str2, carrierServiceConnection.phoneId);
                if ((zEquals || z2) && (iBinder = carrierServiceConnection.service) != null && iBinder.isBinderAlive() && iBinder.pingBinder() && arraySet.add(str3)) {
                    if (!zEquals) {
                        logd(str2 + " has carrier privileges on phoneId " + carrierServiceConnection.phoneId + ", service provided by " + str3);
                        indentingPrintWriter.increaseIndent();
                        StringBuilder sb = new StringBuilder("Proxy : ");
                        sb.append(str3);
                        indentingPrintWriter.println(sb.toString());
                        indentingPrintWriter.decreaseIndent();
                    }
                    indentingPrintWriter.flush();
                    try {
                        logd("Dumping " + str3);
                        carrierServiceConnection.service.dump(fileDescriptor, null);
                        logd("Done with " + str3);
                    } catch (RemoteException e) {
                        logd("RemoteException from " + str3, e);
                        indentingPrintWriter.increaseIndent();
                        indentingPrintWriter.println("RemoteException");
                        indentingPrintWriter.increaseIndent();
                        e.printStackTrace(indentingPrintWriter);
                        indentingPrintWriter.decreaseIndent();
                        indentingPrintWriter.decreaseIndent();
                    }
                    indentingPrintWriter.println("");
                }
            }
        }
        if (arraySet.isEmpty()) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("Not bound");
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("");
        }
        indentingPrintWriter.decreaseIndent();
    }

    private int getPackageUidForAnyUser(String str) {
        try {
            return this.mPackageManager.getPackageUid(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            if (userManager == null) {
                return -1;
            }
            Iterator it = userManager.getUsers().iterator();
            while (it.hasNext()) {
                try {
                    return this.mPackageManager.getPackageUidAsUser(str, ((UserInfo) it.next()).getUserHandle().getIdentifier());
                } catch (PackageManager.NameNotFoundException unused2) {
                }
            }
            return -1;
        }
    }

    void logCarrierServiceCarrierConfigOverrides(int i, PersistableBundle persistableBundle) {
        int packageUidForAnyUser;
        String carrierPackageForPhoneId = getCarrierPackageForPhoneId(i);
        if (TextUtils.isEmpty(carrierPackageForPhoneId)) {
            Log.wtf("CarrierConfigLoader", "logCarrierServiceCarrierConfigOverrides called with null or empty carrierServicePackageName for phoneId=" + i);
            return;
        }
        int packageUidForAnyUser2 = getPackageUidForAnyUser(carrierPackageForPhoneId);
        if (packageUidForAnyUser2 == -1) {
            Log.wtf("CarrierConfigLoader", "Unable to find carrier service package: " + carrierPackageForPhoneId);
            return;
        }
        String[] stringArray = persistableBundle.getStringArray("carrier_certificate_string_array");
        if (ArrayUtils.isEmpty(stringArray)) {
            return;
        }
        UiccAccessRule[] uiccAccessRuleArrDecodeRulesFromCarrierConfig = UiccAccessRule.decodeRulesFromCarrierConfig(stringArray);
        if (ArrayUtils.isEmpty(uiccAccessRuleArrDecodeRulesFromCarrierConfig)) {
            return;
        }
        int specificCarrierIdForPhoneId = getSpecificCarrierIdForPhoneId(i);
        ArraySet arraySet = new ArraySet();
        for (UiccAccessRule uiccAccessRule : uiccAccessRuleArrDecodeRulesFromCarrierConfig) {
            String packageName = uiccAccessRule.getPackageName();
            if (packageName != null && (packageUidForAnyUser = getPackageUidForAnyUser(packageName)) != -1) {
                arraySet.add(Integer.valueOf(packageUidForAnyUser));
            }
        }
        writeCarrierServiceConfigOverridesReported(specificCarrierIdForPhoneId, packageUidForAnyUser2, arraySet.stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray());
    }

    protected void writeCarrierServiceConfigOverridesReported(int i, int i2, int[] iArr) {
        TelephonyStatsLog.write(1270, i, i2, iArr);
    }

    protected int getBinderCallingUid() {
        return Binder.getCallingUid();
    }

    protected boolean isSdkSandboxUidInternal(int i) {
        return Process.isSdkSandboxUid(i);
    }

    private boolean hasCarrierPrivileges(String str, int i) {
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        return SubscriptionManager.isValidSubscriptionId(subscriptionId) && TelephonyManager.from(this.mContext).createForSubscriptionId(subscriptionId).checkCarrierPrivilegesForPackage(str) == 1;
    }

    private String getCurrentPackageName() {
        String[] packagesForUid;
        PackageManager packageManager = this.mContext.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(getBinderCallingUid())) == null) {
            return null;
        }
        return packagesForUid[0];
    }

    private void enforceTelephonyFeatureWithException(String str, String str2) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return;
        }
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, "android.hardware.telephony.subscription", str2);
    }

    class CarrierServiceConnection implements ServiceConnection {
        final int eventId;
        final int phoneId;
        final String pkgName;
        IBinder service;

        CarrierServiceConnection(int i, String str, int i2) {
            this.phoneId = i;
            this.pkgName = str;
            this.eventId = i2;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CarrierConfigLoader.this.logd("Connected to config app: " + componentName.flattenToShortString());
            this.service = iBinder;
            CarrierConfigLoader.this.mHandler.sendMessage(CarrierConfigLoader.this.mHandler.obtainMessage(this.eventId, this.phoneId, -1, this));
            CarrierConfigLoader.this.logd("Remove timeout message since service is connected. id = " + CarrierConfigLoader.this.mTimeoutId);
            CarrierConfigLoader.this.mHandler.removeMessages(CarrierConfigLoader.this.mTimeoutId);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            CarrierConfigLoader.this.logd("Disconnected from config app: " + componentName.flattenToShortString());
            this.service = null;
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            CarrierConfigLoader.this.logd("Binding died from config app: " + componentName.flattenToShortString());
            this.service = null;
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            CarrierConfigLoader.this.logd("Null binding from config app: " + componentName.flattenToShortString());
            this.service = null;
        }
    }

    class ConfigLoaderBroadcastReceiver extends BroadcastReceiver {
        private ConfigLoaderBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                CarrierConfigLoader.this.mHandler.sendMessage(CarrierConfigLoader.this.mHandler.obtainMessage(13, null));
            }
        }
    }

    class CarrierServiceChangeCallback implements TelephonyManager.CarrierPrivilegesCallback {
        private boolean mHasSentServiceChangeCallback = false;
        final int mPhoneId;

        public void onCarrierPrivilegesChanged(Set set, Set set2) {
        }

        CarrierServiceChangeCallback(int i) {
            this.mPhoneId = i;
        }

        public void onCarrierServiceChanged(String str, int i) {
            if (!this.mHasSentServiceChangeCallback) {
                this.mHasSentServiceChangeCallback = true;
            } else {
                CarrierConfigLoader.this.mHandler.sendMessage(CarrierConfigLoader.this.mHandler.obtainMessage(9, this.mPhoneId, -1, str));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String eventToString(int i) {
        if (i == 0) {
            return "EVENT_CLEAR_CONFIG";
        }
        switch (i) {
            case 3:
                return "EVENT_CONNECTED_TO_DEFAULT";
            case 4:
                return "EVENT_CONNECTED_TO_CARRIER";
            case 5:
                return "EVENT_FETCH_DEFAULT_DONE";
            case 6:
                return "EVENT_FETCH_CARRIER_DONE";
            case 7:
                return "EVENT_DO_FETCH_DEFAULT";
            case 8:
                return "EVENT_DO_FETCH_CARRIER";
            case 9:
                return "EVENT_PACKAGE_CHANGED";
            case 10:
                return "EVENT_BIND_DEFAULT_TIMEOUT";
            case 11:
                return "EVENT_BIND_CARRIER_TIMEOUT";
            case 12:
                return "EVENT_CHECK_SYSTEM_UPDATE";
            case 13:
                return "EVENT_SYSTEM_UNLOCKED";
            case 14:
                return "EVENT_FETCH_DEFAULT_TIMEOUT";
            case 15:
                return "EVENT_FETCH_CARRIER_TIMEOUT";
            case 16:
                return "EVENT_SUBSCRIPTION_INFO_UPDATED";
            case 17:
                return "EVENT_MULTI_SIM_CONFIG_CHANGED";
            case 18:
                return "EVENT_DO_FETCH_DEFAULT_FOR_NO_SIM_CONFIG";
            case 19:
                return "EVENT_FETCH_DEFAULT_FOR_NO_SIM_CONFIG_DONE";
            case 20:
                return "EVENT_CONNECTED_TO_DEFAULT_FOR_NO_SIM_CONFIG";
            case 21:
                return "EVENT_BIND_DEFAULT_FOR_NO_SIM_CONFIG_TIMEOUT";
            case 22:
                return "EVENT_FETCH_DEFAULT_FOR_NO_SIM_CONFIG_TIMEOUT";
            default:
                switch (i) {
                    case 103:
                        return "EVENT_DO_FETCH_OEM";
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        return "EVENT_OEM_SERVICE_CONNECTED";
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        return "EVENT_FETCH_OEM_TIMEOUT";
                    case 106:
                        return "EVENT_BIND_OEM_TIMEOUT";
                    case 107:
                        return "EVENT_FETCH_OEM_DONE";
                    default:
                        return "UNKNOWN(" + i + ")";
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("CarrierConfigLoader", str);
    }

    private void logd(String str, Throwable th) {
        Log.d("CarrierConfigLoader", str, th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logl(String str) {
        Log.d("CarrierConfigLoader", str);
        this.mCarrierConfigLoadingLog.log(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Log.e("CarrierConfigLoader", str);
        this.mCarrierConfigLoadingLog.log(str);
    }
}
