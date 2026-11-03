package com.android.phone;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyLocalConnection;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyServicesInitializer;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.widget.Toast;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.ims.ImsFeatureBinderRepository;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.os.BinderCallsStats;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.phone.vvm.CarrierVvmPackageInstalledReceiver;
import com.android.services.telephony.domainselection.DynamicRoutingController;
import com.android.services.telephony.rcs.TelephonyRcsService;
import com.google.android.collect.Sets;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.phone.ext.ExtensionManager;
import com.mediatek.settings.TelephonyUtils;
import com.mediatek.settings.network.CtVolteServiceChecker;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class PhoneGlobals extends ContextWrapper {
    private static final boolean DBG;
    static final int ROAMING_NOTIFICATION_CONNECTED = 1;
    static final int ROAMING_NOTIFICATION_DISCONNECTED = 2;
    static final int ROAMING_NOTIFICATION_NO_NOTIFICATION = 0;
    static final int ROAMING_NOTIFICATION_REASON_SERVICE_STATE_CHANGED = 3;
    private static PhoneGlobals sMe;
    static boolean sVoiceCapable;
    CarrierConfigLoader configLoader;
    public ImsRcsController imsRcsController;
    private BinderCallsStats.SettingsObserver mBinderCallsSettingsObserver;
    CallManager mCM;
    private final CarrierVvmPackageInstalledReceiver mCarrierVvmPackageInstalledReceiver;
    ArrayList mCurrentPhones;
    private int mCurrentRoamingNotification;
    private final LocalLog mDataRoamingNotifLog;
    int mDefaultDataSubId;
    private FeatureFlags mFeatureFlags;
    Handler mHandler;
    public ImsProvisioningController mImsProvisioningController;
    public ImsStateCallbackController mImsStateCallbackController;
    private KeyguardManager mKeyguardManager;
    private Activity mPUKEntryActivity;
    private ProgressDialog mPUKEntryProgressDialog;
    private final BroadcastReceiver mReceiver;
    private SettingsObserver mSettingsObserver;
    private ArraySet mShownNotificationReasons;
    private final Set mSubInfoUpdateListeners;
    private List mSubscriptionInfos;
    private PhoneAppCallback[] mTelephonyCallbacks;
    TelephonyRcsService mTelephonyRcsService;
    private AtomicBoolean mWaitForInternetDisconnection;
    NotificationMgr notificationMgr;
    CallNotifier notifier;
    private Phone phoneInEcm;
    public PhoneInterfaceManager phoneMgr;

    public interface SubInfoUpdateListener {
        void handleSubInfoUpdate();
    }

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
        sVoiceCapable = true;
    }

    class PhoneAppCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.DataConnectionStateListener {
        private final int mSubId;

        PhoneAppCallback(int i) {
            this.mSubId = i;
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            PhoneGlobals.this.handleServiceStateChanged(serviceState, this.mSubId);
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            int i3 = this.mSubId;
            PhoneGlobals phoneGlobals = PhoneGlobals.this;
            if (i3 == phoneGlobals.mDefaultDataSubId && i == 0) {
                phoneGlobals.mHandler.post(new Runnable() { // from class: com.android.phone.PhoneGlobals$PhoneAppCallback$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onDataConnectionStateChanged$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDataConnectionStateChanged$0() {
            if (PhoneGlobals.this.mWaitForInternetDisconnection.compareAndSet(true, false)) {
                Log.d("PhoneGlobals", "onDisconnectedInternetDataNetwork.");
                PhoneGlobals.this.updateDataRoamingStatus(5);
            }
        }

        public int getSubId() {
            return this.mSubId;
        }
    }

    class EventSimStateChangedBag {
        final String mIccStatus;
        final int mPhoneId;

        EventSimStateChangedBag(int i, String str) {
            this.mPhoneId = i;
            this.mIccStatus = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimLock(int i, Phone phone) {
        if ("za_mt".equals(SystemProperties.get("ro.miui.customized.region", ""))) {
            Log.i("PhoneGlobals", "za_mt Not showing 'SIM network unlock' screen");
            return;
        }
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(phone.getSubId());
        if (!CarrierConfigManager.isConfigForIdentifiedCarrier(carrierConfigForSubId)) {
            Log.i("PhoneGlobals", "Not showing 'SIM network unlock' screen. Carrier config not loaded");
            return;
        }
        if (carrierConfigForSubId.getBoolean("ignore_sim_network_locked_events_bool")) {
            Log.i("PhoneGlobals", "Not showing 'SIM network unlock' screen. Disabled by carrier config");
            return;
        }
        if (i == -1) {
            UiccPort uiccPort = phone.getUiccPort();
            if (uiccPort == null) {
                Log.e("PhoneGlobals", "handleSimLock: uiccPort for phone " + phone.getPhoneId() + " is null");
                return;
            }
            UiccProfile uiccProfile = uiccPort.getUiccProfile();
            if (uiccProfile == null) {
                Log.e("PhoneGlobals", "handleSimLock: uiccProfile for phone " + phone.getPhoneId() + " is null");
                return;
            }
            i = uiccProfile.getApplication(uiccProfile.mCurrentAppType).getPersoSubState().ordinal();
        }
        Log.i("PhoneGlobals", "show sim depersonal panel");
        IccNetworkDepersonalizationPanel.showDialog(phone, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSimLocked(Phone phone) {
        return ((TelephonyManager) getSystemService(TelephonyManager.class)).createForSubscriptionId(phone.getSubId()).getSimState() == 4;
    }

    public PhoneGlobals(Context context) {
        super(context);
        this.mCurrentRoamingNotification = 0;
        this.mWaitForInternetDisconnection = new AtomicBoolean(false);
        this.mShownNotificationReasons = new ArraySet();
        this.mDefaultDataSubId = -1;
        this.mDataRoamingNotifLog = new LocalLog(50);
        this.mReceiver = new PhoneAppBroadcastReceiver();
        this.mCarrierVvmPackageInstalledReceiver = new CarrierVvmPackageInstalledReceiver();
        this.mCurrentPhones = new ArrayList();
        this.mFeatureFlags = new FeatureFlagsImpl();
        this.mHandler = new Handler() { // from class: com.android.phone.PhoneGlobals.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 3) {
                    PhoneGlobals.this.handleSimLock(((Integer) ((AsyncResult) message.obj).result).intValue(), (Phone) ((AsyncResult) message.obj).userObj);
                    return;
                }
                if (i == 8) {
                    EventSimStateChangedBag eventSimStateChangedBag = (EventSimStateChangedBag) message.obj;
                    if ("ABSENT".equals(eventSimStateChangedBag.mIccStatus)) {
                        PhoneGlobals.this.notificationMgr.dismissNetworkSelectionNotificationForInactiveSubId();
                    }
                    if ("READY".equals(eventSimStateChangedBag.mIccStatus) || "LOADED".equals(eventSimStateChangedBag.mIccStatus) || "NOT_READY".equals(eventSimStateChangedBag.mIccStatus) || "ABSENT".equals(eventSimStateChangedBag.mIccStatus)) {
                        if (PhoneGlobals.this.mPUKEntryActivity != null) {
                            Log.i("PhoneGlobals", "Dismiss puk entry activity");
                            PhoneGlobals.this.mPUKEntryActivity.finish();
                            PhoneGlobals.this.mPUKEntryActivity = null;
                        }
                        if (TelephonyBaseUtilsStub.isMiuiRom()) {
                            MiPhoneAppStub.get().dismissUssdDialog();
                        }
                        if (PhoneGlobals.this.mPUKEntryProgressDialog != null) {
                            Log.i("PhoneGlobals", "Dismiss puk progress dialog");
                            PhoneGlobals.this.mPUKEntryProgressDialog.dismiss();
                            PhoneGlobals.this.mPUKEntryProgressDialog = null;
                        }
                        Log.i("PhoneGlobals", "Dismissing depersonal panel" + eventSimStateChangedBag.mIccStatus);
                        IccNetworkDepersonalizationPanel.dialogDismiss(eventSimStateChangedBag.mPhoneId);
                        return;
                    }
                    return;
                }
                if (i == 52) {
                    PhoneGlobals.this.onMMIComplete((AsyncResult) message.obj);
                    return;
                }
                if (i != 53) {
                    switch (i) {
                        case 10:
                            Log.d("PhoneGlobals", "EVENT_DATA_ROAMING_DISCONNECTED");
                            if (SubscriptionManagerService.getInstance().isEsimBootStrapProvisioningActiveForSubId(message.arg1)) {
                                Log.i("PhoneGlobals", "skip notification/warnings during esim bootstrap activation");
                            } else if (PhoneGlobals.this.skipDataRoamingDisconnectedNotificationInSatelliteMode(message.arg1)) {
                                Log.i("PhoneGlobals", "skip data roaming disconnected notification when device is connected to satellite network that does not support data.");
                            } else {
                                PhoneGlobals.this.notificationMgr.showDataRoamingNotification(message.arg1, false);
                            }
                            break;
                        case 11:
                            if (SubscriptionManagerService.getInstance().isEsimBootStrapProvisioningActiveForSubId(message.arg1)) {
                                Log.i("PhoneGlobals", "skip notification/warnings during esim bootstrap activation");
                            } else {
                                PhoneGlobals.this.notificationMgr.showDataRoamingNotification(message.arg1, true);
                            }
                            break;
                        case 12:
                            PhoneGlobals.this.notificationMgr.hideDataRoamingNotification();
                            break;
                        default:
                            switch (i) {
                                case 15:
                                    PhoneGlobals.this.updateDataRoamingStatus(1);
                                    break;
                                case 16:
                                    PhoneGlobals.this.updateDataRoamingStatus(0);
                                    break;
                                case 17:
                                    int iIntValue = ((Integer) message.obj).intValue();
                                    PhoneGlobals.this.refreshMwiIndicator(iIntValue);
                                    Phone phone = PhoneGlobals.getPhone(iIntValue);
                                    if (phone != null) {
                                        if (PhoneGlobals.this.isSimLocked(phone)) {
                                            PhoneGlobals.this.handleSimLock(-1, phone);
                                        }
                                        TelephonyManager telephonyManager = (TelephonyManager) PhoneGlobals.this.getSystemService(TelephonyManager.class);
                                        PhoneAppCallback phoneAppCallback = PhoneGlobals.this.mTelephonyCallbacks[phone.getPhoneId()];
                                        telephonyManager.createForSubscriptionId(phoneAppCallback.getSubId()).unregisterTelephonyCallback(phoneAppCallback);
                                        PhoneAppCallback phoneAppCallback2 = PhoneGlobals.this.new PhoneAppCallback(iIntValue);
                                        TelephonyManager telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(iIntValue);
                                        Handler handler = PhoneGlobals.this.mHandler;
                                        Objects.requireNonNull(handler);
                                        telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(1, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), phoneAppCallback2);
                                        PhoneGlobals.this.mTelephonyCallbacks[phone.getPhoneId()] = phoneAppCallback2;
                                    }
                                    break;
                                case 18:
                                    int iIntValue2 = ((Integer) ((AsyncResult) message.obj).result).intValue();
                                    TelephonyManager telephonyManager2 = (TelephonyManager) PhoneGlobals.this.getSystemService(TelephonyManager.class);
                                    for (int i2 = 0; i2 < PhoneGlobals.this.mTelephonyCallbacks.length; i2++) {
                                        PhoneAppCallback phoneAppCallback3 = PhoneGlobals.this.mTelephonyCallbacks[i2];
                                        if (phoneAppCallback3 != null) {
                                            telephonyManager2.createForSubscriptionId(phoneAppCallback3.getSubId()).unregisterTelephonyCallback(phoneAppCallback3);
                                            PhoneGlobals.this.mTelephonyCallbacks[i2] = null;
                                        }
                                    }
                                    for (int i3 = 0; i3 < iIntValue2; i3++) {
                                        int subId = PhoneFactory.getPhone(i3).getSubId();
                                        PhoneAppCallback phoneAppCallback4 = PhoneGlobals.this.new PhoneAppCallback(subId);
                                        TelephonyManager telephonyManagerCreateForSubscriptionId2 = telephonyManager2.createForSubscriptionId(subId);
                                        Handler handler2 = PhoneGlobals.this.mHandler;
                                        Objects.requireNonNull(handler2);
                                        telephonyManagerCreateForSubscriptionId2.registerTelephonyCallback(0, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler2), phoneAppCallback4);
                                        PhoneGlobals.this.mTelephonyCallbacks[i3] = phoneAppCallback4;
                                    }
                                    break;
                            }
                            break;
                    }
                    return;
                }
                PhoneUtils.cancelMmiCode(PhoneGlobals.this.mCM.getFgPhone());
            }
        };
        this.mSubInfoUpdateListeners = Sets.newArraySet();
        sMe = this;
        if (getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            this.mSettingsObserver = new SettingsObserver(context, this.mHandler);
        }
    }

    public void onCreate() {
        PhoneGlobals phoneGlobals;
        ExtensionManager.registerApplicationContext(getApplicationContext());
        ContentResolver contentResolver = getContentResolver();
        if (!getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled) && !getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            Log.v("PhoneGlobals", "onCreate()... but not defined FEATURE_TELEPHONY");
            return;
        }
        TelephonyLocalConnection.setInstance(new LocalConnectionImpl(this));
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TelephonyManager.class);
        sVoiceCapable = telephonyManager.isVoiceCapable();
        if (this.mCM == null) {
            AnomalyReporter.initialize(this);
            TelephonyComponentFactory.getInstance().injectTheComponentFactory(getResources().getXml(R.xml.telephony_injection));
            DomainSelectionResolver.make(this, getResources().getString(R.string.config_domain_selection_service_component_name));
            Log.v("PhoneGlobals", "PhoneGlobal enablePhoneNumberParsingApi");
            Log.v("PhoneGlobals", "PhoneGlobal TelephonyServicesInitializer.initialize(this)");
            TelephonyServicesInitializer.initialize(this);
            PhoneFactory.makeDefaultPhones(this, this.mFeatureFlags);
            if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                DomainSelectionResolver.getInstance().initialize();
                phoneGlobals = this;
                EmergencyStateTracker.make(phoneGlobals, getResources().getBoolean(R.bool.config_gnss_supl_requires_default_data_for_emergency), getResources().getInteger(R.integer.config_in_service_wait_timer_when_dialing_emergency_routing_ecc_in_apm), getResources().getBoolean(R.bool.config_turn_off_oem_enabled_satellite_during_emergency_call), getResources().getBoolean(R.bool.config_turn_off_non_emergency_nb_iot_ntn_satellite_for_emergency_call), this.mFeatureFlags);
                DynamicRoutingController.getInstance().initialize(phoneGlobals);
            } else {
                phoneGlobals = this;
            }
            if (phoneGlobals.getPackageManager().hasSystemFeature("android.hardware.telephony.ims")) {
                ImsResolver.make(phoneGlobals, phoneGlobals.getResources().getString(R.string.config_ims_mmtel_package), phoneGlobals.getResources().getString(R.string.config_ims_rcs_package), PhoneFactory.getPhones().length, new ImsFeatureBinderRepository());
                ImsResolver.getInstance().initialize();
                for (Phone phone : PhoneFactory.getPhones()) {
                    ImsPhone imsPhone = phone.getImsPhone();
                    if (imsPhone != null && (imsPhone instanceof ImsPhone)) {
                        ImsPhone imsPhone2 = imsPhone;
                        if (imsPhone2.getCallTracker() instanceof ImsPhoneCallTracker) {
                            ImsPhoneCallTracker callTracker = imsPhone2.getCallTracker();
                            ImsPhoneCallTracker.Config config = new ImsPhoneCallTracker.Config();
                            config.isD2DCommunicationSupported = phoneGlobals.getResources().getBoolean(R.bool.config_use_device_to_device_communication);
                            callTracker.setConfig(config);
                        }
                    }
                }
                RcsProvisioningMonitor.make(phoneGlobals, phoneGlobals.mFeatureFlags);
            }
            phoneGlobals.startService(new Intent(phoneGlobals, (Class<?>) TelephonyDebugService.class));
            phoneGlobals.mCM = CallManager.getInstance(phoneGlobals);
            for (Phone phone2 : PhoneFactory.getPhones()) {
                phoneGlobals.mCurrentPhones.add(phone2);
            }
            phoneGlobals.notificationMgr = NotificationMgr.init(phoneGlobals);
            SatelliteController.make(phoneGlobals, phoneGlobals.mFeatureFlags);
            phoneGlobals.mKeyguardManager = (KeyguardManager) phoneGlobals.getSystemService("keyguard");
            phoneGlobals.phoneMgr = PhoneInterfaceManager.init(phoneGlobals, phoneGlobals.mFeatureFlags);
            phoneGlobals.imsRcsController = ImsRcsController.init(phoneGlobals, phoneGlobals.mFeatureFlags);
            phoneGlobals.configLoader = CarrierConfigLoader.init(phoneGlobals, phoneGlobals.mFeatureFlags);
            XtsApp.make(phoneGlobals);
            if (phoneGlobals.getPackageManager().hasSystemFeature("android.hardware.telephony.ims")) {
                phoneGlobals.mImsStateCallbackController = ImsStateCallbackController.make(phoneGlobals, PhoneFactory.getPhones().length, phoneGlobals.mFeatureFlags);
                TelephonyRcsService telephonyRcsService = new TelephonyRcsService(phoneGlobals, PhoneFactory.getPhones().length, phoneGlobals.mFeatureFlags);
                phoneGlobals.mTelephonyRcsService = telephonyRcsService;
                telephonyRcsService.initialize();
                phoneGlobals.imsRcsController.setRcsService(phoneGlobals.mTelephonyRcsService);
                phoneGlobals.mImsProvisioningController = ImsProvisioningController.make(phoneGlobals, PhoneFactory.getPhones().length, phoneGlobals.mFeatureFlags);
            }
            phoneGlobals.notifier = CallNotifier.init(phoneGlobals);
            PhoneUtils.registerIccStatus(phoneGlobals.mHandler, 3);
            phoneGlobals.mCM.registerForMmiComplete(phoneGlobals.mHandler, 52, (Object) null);
            phoneGlobals.handleAirplaneModeChange(Settings.Global.getInt(phoneGlobals.getContentResolver(), "airplane_mode_on", 0) == 1);
            IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.RADIO_TECHNOLOGY");
            intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
            intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
            intentFilter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
            ExtensionManager.getMobileNetworkSettingsExt().customizeMobileNetworkIntentFilter(intentFilter);
            phoneGlobals.registerReceiver(phoneGlobals.mReceiver, intentFilter, 2);
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (SubscriptionManager.isValidSubscriptionId(defaultDataSubscriptionId)) {
                phoneGlobals.mDefaultDataSubId = defaultDataSubscriptionId;
                phoneGlobals.registerSettingsObserver();
                phoneGlobals.updateDataRoamingStatus(4);
            }
            phoneGlobals.mSubscriptionInfos = PhoneUtils.getActiveSubInfoList();
            boolean z = Settings.System.getInt(phoneGlobals.getContentResolver(), "airplane_mode_on", 0) != 0;
            if (DBG) {
                Log.d("PhoneGlobals", "Notify RadioManager with airplane mode:" + z);
            }
            phoneGlobals.handleAirplaneModeChange(z);
            PhoneConfigurationManager.registerForMultiSimConfigChange(phoneGlobals.mHandler, 18, (Object) null);
            phoneGlobals.mTelephonyCallbacks = new PhoneAppCallback[telephonyManager.getSupportedModemCount()];
            if (telephonyManager.getSupportedModemCount() > 0) {
                for (Phone phone3 : PhoneFactory.getPhones()) {
                    int subId = phone3.getSubId();
                    PhoneAppCallback phoneAppCallback = phoneGlobals.new PhoneAppCallback(subId);
                    TelephonyManager telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(subId);
                    Handler handler = phoneGlobals.mHandler;
                    Objects.requireNonNull(handler);
                    telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(0, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), phoneAppCallback);
                    phoneGlobals.mTelephonyCallbacks[phone3.getPhoneId()] = phoneAppCallback;
                }
            }
            phoneGlobals.mCarrierVvmPackageInstalledReceiver.register(phoneGlobals);
            PreferenceManager.setDefaultValues(phoneGlobals, R.xml.call_feature_setting, false);
            CtVolteServiceChecker.getInstance(phoneGlobals).init();
        } else {
            phoneGlobals = this;
        }
        contentResolver.getType(Uri.parse("content://icc/adn"));
        if (phoneGlobals.getResources().getBoolean(R.bool.hac_enabled)) {
            ((AudioManager) phoneGlobals.getSystemService("audio")).setParameters("HACSetting=".concat(Settings.System.getInt(phoneGlobals.getContentResolver(), "hearing_aid", 0) == 1 ? "ON" : "OFF"));
        }
        phoneGlobals.mBinderCallsSettingsObserver = new BinderCallsStats.SettingsObserver(phoneGlobals.getApplicationContext(), new BinderCallsStats(new BinderCallsStats.Injector(), 2));
    }

    public static PhoneGlobals getInstance() {
        PhoneGlobals phoneGlobals = sMe;
        if (phoneGlobals != null) {
            return phoneGlobals;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("No PhoneGlobals here!");
        return null;
    }

    public static Phone getPhone() {
        return PhoneFactory.getDefaultPhone();
    }

    public static Phone getPhone(int i) {
        return PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
    }

    CallManager getCallManager() {
        return this.mCM;
    }

    public PersistableBundle getCarrierConfig() {
        return getCarrierConfigForSubId(SubscriptionManager.getDefaultSubscriptionId());
    }

    public PersistableBundle getCarrierConfigForSubId(int i) {
        return this.configLoader.getConfigForSubIdWithFeature(i, getOpPackageName(), getAttributionTag());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSettingsObserver() {
        int i;
        SettingsObserver settingsObserver = this.mSettingsObserver;
        if (settingsObserver == null) {
            Log.w("PhoneGlobals", "Skipping settings observer: no telephony support");
            return;
        }
        try {
            settingsObserver.unobserve();
        } catch (Exception e) {
            Log.e("PhoneGlobals", "Error in unobserve: " + e.getMessage());
        }
        String str = "mobile_data";
        String str2 = "data_roaming";
        if (TelephonyManager.getDefault().getSimCount() > 1 && (i = this.mDefaultDataSubId) != -1) {
            str2 = "data_roaming" + i;
            str = "mobile_data" + i;
        }
        this.mSettingsObserver.observe(Settings.Global.getUriFor(str2), 15);
        this.mSettingsObserver.observe(Settings.Global.getUriFor(str), 16);
    }

    void setPukEntryActivity(Activity activity) {
        Log.i("PhoneGlobals", "setPukEntryActivity - set to ".concat(activity == null ? "null" : "activity"));
        this.mPUKEntryActivity = activity;
    }

    Activity getPUKEntryActivity() {
        return this.mPUKEntryActivity;
    }

    void setPukEntryProgressDialog(ProgressDialog progressDialog) {
        Log.i("PhoneGlobals", "setPukEntryProgressDialog - set to ".concat(progressDialog == null ? "null" : "activity"));
        this.mPUKEntryProgressDialog = progressDialog;
    }

    KeyguardManager getKeyguardManager() {
        return this.mKeyguardManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMMIComplete(AsyncResult asyncResult) {
        MmiCode mmiCode = (MmiCode) asyncResult.result;
        PhoneUtils.displayMMIComplete(mmiCode.getPhone(), getInstance(), mmiCode, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initForNewRadioTechnology() {
        if (DBG) {
            Log.d("PhoneGlobals", "initForNewRadioTechnology...");
        }
        this.notifier.updateCallNotifierRegistrationsAfterRadioTechnologyChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAirplaneModeChange(boolean z) {
        Log.i("PhoneGlobals", "handleAirplaneModeChange: isAirplaneNewlyOn=" + z);
        int i = Settings.Global.getInt(getContentResolver(), "cell_on", 1);
        if (i == 0) {
            Log.i("PhoneGlobals", "Ignore airplane mode change due to cell off.");
        } else if (i == 1) {
            maybeTurnCellOff(z);
        } else if (i == 2) {
            maybeTurnCellOn(z);
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.getServiceStateTracker().onAirplaneModeChanged(z);
        }
    }

    private boolean isCellOffInAirplaneMode() {
        String string = Settings.Global.getString(getContentResolver(), "airplane_mode_radios");
        return string == null || string.contains("cell");
    }

    private void setRadioPowerOff() {
        Log.i("PhoneGlobals", "Turning radio off - airplane");
        Settings.Global.putInt(getContentResolver(), "cell_on", 2);
        Settings.Global.putInt(getContentResolver(), "enable_cellular_on_boot", 0);
        TelephonyProperties.airplane_mode_on(Boolean.TRUE);
        RadioManager radioManager = RadioManager.getInstance();
        if (radioManager != null) {
            radioManager.notifyAirplaneModeChange(true);
        }
        if (radioManager != null && RadioManager.isPowerOnFeatureAllClosed()) {
            PhoneUtils.setRadioPower(false);
        }
        clearCacheOnRadioOff();
    }

    private void clearCacheOnRadioOff() {
        this.mShownNotificationReasons.clear();
    }

    private void setRadioPowerOn() {
        Log.i("PhoneGlobals", "Turning radio on - airplane");
        Settings.Global.putInt(getContentResolver(), "cell_on", 1);
        Settings.Global.putInt(getContentResolver(), "enable_cellular_on_boot", 1);
        TelephonyProperties.airplane_mode_on(Boolean.FALSE);
        RadioManager radioManager = RadioManager.getInstance();
        if (radioManager != null) {
            radioManager.notifyAirplaneModeChange(false);
        }
        if (radioManager == null || !RadioManager.isPowerOnFeatureAllClosed()) {
            return;
        }
        PhoneUtils.setRadioPower(true);
    }

    private void maybeTurnCellOff(boolean z) {
        if (!z) {
            Log.i("PhoneGlobals", "Ignoring airplane mode: not newly on");
            return;
        }
        TelecomManager telecomManager = (TelecomManager) getSystemService("telecom");
        if (telecomManager != null && telecomManager.isInEmergencyCall()) {
            ((ConnectivityManager) getSystemService("connectivity")).setAirplaneMode(false);
            Toast.makeText(this, R.string.radio_off_during_emergency_call, 1).show();
            Log.i("PhoneGlobals", "Ignoring airplane mode: emergency call. Turning airplane off");
        } else if (!isCellOffInAirplaneMode()) {
            Log.i("PhoneGlobals", "Ignoring airplane mode: settings prevent cell radio power off");
        } else {
            setRadioPowerOff();
        }
    }

    private void maybeTurnCellOn(boolean z) {
        if (!z) {
            setRadioPowerOn();
        } else {
            Log.i("PhoneGlobals", "Ignoring airplane mode off: radio is already on.");
        }
    }

    class PhoneAppBroadcastReceiver extends BroadcastReceiver {
        private PhoneAppBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                Log.d("PhoneGlobals", "Receive ACTION_AIRPLANE_MODE_CHANGED, state: " + booleanExtra);
                if (!((TelephonyManager) PhoneGlobals.this.getSystemService("phone")).isDataCapable()) {
                    Log.d("PhoneGlobals", "WifiOnly Project ,just return and not respond!");
                    return;
                }
                PhoneGlobals.this.handleAirplaneModeChange(booleanExtra);
            } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("phone", -1);
                if (SubscriptionManager.isValidPhoneId(intExtra)) {
                    PhoneUtils.unregisterIccStatus(PhoneGlobals.this.mHandler, intExtra);
                    PhoneUtils.registerIccStatus(PhoneGlobals.this.mHandler, 3, intExtra);
                }
                String stringExtra = intent.getStringExtra("ss");
                Handler handler = PhoneGlobals.this.mHandler;
                handler.sendMessage(handler.obtainMessage(8, new EventSimStateChangedBag(intExtra, stringExtra)));
            } else if (action.equals("android.intent.action.RADIO_TECHNOLOGY")) {
                Log.d("PhoneGlobals", "Radio technology switched. Now " + intent.getStringExtra("phoneName") + " is active.");
                PhoneGlobals.this.initForNewRadioTechnology();
            } else if (action.equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                int intExtra2 = intent.getIntExtra("phone", 0);
                PhoneGlobals.this.phoneInEcm = PhoneFactory.getPhone(intExtra2);
                Log.d("PhoneGlobals", "Emergency Callback Mode. phoneId:" + intExtra2);
                if (PhoneGlobals.this.phoneInEcm != null) {
                    if (com.android.internal.telephony.TelephonyCapabilities.supportsEcm(PhoneGlobals.this.phoneInEcm)) {
                        Log.d("PhoneGlobals", "Emergency Callback Mode arrived in PhoneApp.");
                        if (intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false)) {
                            context.startService(new Intent(context, (Class<?>) EmergencyCallbackModeService.class));
                        } else {
                            PhoneGlobals.this.phoneInEcm = null;
                            context.startService(new Intent(context, (Class<?>) EmergencyCallbackModeService.class));
                        }
                    } else {
                        Log.e("PhoneGlobals", "Got ACTION_EMERGENCY_CALLBACK_MODE_CHANGED, but ECM isn't supported for phone: " + PhoneGlobals.this.phoneInEcm.getPhoneName());
                        PhoneGlobals.this.phoneInEcm = null;
                    }
                } else {
                    Log.w("PhoneGlobals", "phoneInEcm is null.");
                }
            } else if (action.equals("com.mediatek.intent.action.MSIM_MODE")) {
                RadioManager.getInstance().notifyMSimModeChange(intent.getIntExtra("mode", -1));
            } else if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                PhoneGlobals.this.updateDataRoamingStatus(2);
                PhoneGlobals.this.updateLimitedSimFunctionForDualSim();
                int intExtra3 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                if (SubscriptionManager.isValidSubscriptionId(intExtra3)) {
                    Handler handler2 = PhoneGlobals.this.mHandler;
                    handler2.sendMessage(handler2.obtainMessage(17, new Integer(intExtra3)));
                }
            } else if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                PhoneGlobals.this.mDefaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
                PhoneGlobals.this.registerSettingsObserver();
                if (PhoneGlobals.getPhone(PhoneGlobals.this.mDefaultDataSubId) != null) {
                    PhoneGlobals.this.updateDataRoamingStatus(4);
                }
            } else if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
                Log.d("PhoneGlobals", "Receive ACTION_MULTI_SIM_CONFIG_CHANGED = " + intent.getIntExtra("android.telephony.extra.ACTIVE_SIM_SUPPORTED_COUNT", 0));
                PhoneGlobals phoneGlobals = PhoneGlobals.this;
                if (phoneGlobals.mCM != null) {
                    ArrayList arrayList = phoneGlobals.mCurrentPhones;
                    int size = arrayList.size();
                    int i = 0;
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        PhoneGlobals.this.mCM.unregisterPhone((Phone) obj);
                    }
                    PhoneGlobals.this.mCurrentPhones.clear();
                    for (Phone phone : PhoneFactory.getPhones()) {
                        PhoneGlobals.this.mCM.registerPhone(phone);
                        if (phone.getImsPhone() != null) {
                            PhoneGlobals.this.mCM.registerPhone(phone.getImsPhone());
                        }
                        PhoneGlobals.this.mCurrentPhones.add(phone);
                    }
                }
            }
            ExtensionManager.getMobileNetworkSettingsExt().handleMobileNetworkIntent(context, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleServiceStateChanged(ServiceState serviceState, int i) {
        this.notificationMgr.updateNetworkSelection(serviceState.getState(), i);
        if (i == this.mDefaultDataSubId) {
            updateDataRoamingStatus(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataRoamingStatus(int i) {
        Phone phone = getPhone(this.mDefaultDataSubId);
        if (phone == null) {
            Log.w("PhoneGlobals", "Can't get phone with sub id = " + this.mDefaultDataSubId);
            return;
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState == null) {
            Log.e("PhoneGlobals", "updateDataRoamingStatus: serviceState is null");
            return;
        }
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            MiPhoneAppStub.get().updateDataRoamingStatus(phone, null, this.mDefaultDataSubId);
            return;
        }
        List internetDataDisallowedReasons = phone.getDataNetworkController().getInternetDataDisallowedReasons();
        DataEvaluation.DataDisallowedReason dataDisallowedReason = DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK;
        if (internetDataDisallowedReasons.contains(dataDisallowedReason) && internetDataDisallowedReasons.contains(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED) && (i == 0 || i == 1)) {
            this.mWaitForInternetDisconnection.set(true);
            Log.d("PhoneGlobals", "updateDataRoamingStatus, wait for internet disconnection for single data network");
        } else if (!internetDataDisallowedReasons.contains(dataDisallowedReason) && this.mWaitForInternetDisconnection.compareAndSet(true, false)) {
            Log.d("PhoneGlobals", "updateDataRoamingStatus, cancel to wait for internet disconnection for single data network");
        }
        updateDataRoamingStatus(i, internetDataDisallowedReasons, serviceState);
    }

    void updateDataRoamingStatus(int i, List list, ServiceState serviceState) {
        String operatorNumeric = serviceState.getOperatorNumeric();
        String str = "RoamingNumeric=" + operatorNumeric;
        String str2 = "CallingReason=" + i;
        boolean dataRoaming = serviceState.getDataRoaming();
        boolean zIsEmpty = list.isEmpty();
        boolean z = list.size() == 1 && list.contains(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
        this.mDataRoamingNotifLog.log("updateDataRoamingStatus dataAllowed=" + zIsEmpty + ", disallowReasons=" + list + ", dataIsNowRoaming=" + dataRoaming + ", " + str + ", " + str2);
        boolean zContains = (i == 2 || i == 3) ? this.mShownNotificationReasons.contains(str) : false;
        boolean zContains2 = this.mShownNotificationReasons.contains(str2);
        if (!zIsEmpty && z) {
            if (!zContains && operatorNumeric != null) {
                this.mShownNotificationReasons.add(str);
            }
            if (!zContains2 && i == 2) {
                this.mShownNotificationReasons.add(str2);
            }
            if (getCurrentRoamingNotification() == 2) {
                return;
            }
            if (!zContains && !zContains2) {
                updateDataRoamingNotification(2);
                return;
            }
            Log.d("PhoneGlobals", "Skip roaming disconnected notification since already shownInThisNumeric=" + zContains + " shownForThisReason=" + zContains2);
            if (getCurrentRoamingNotification() != 0) {
                updateDataRoamingNotification(0);
                return;
            }
            return;
        }
        if (zIsEmpty && dataRoaming) {
            if (!zContains && operatorNumeric != null) {
                this.mShownNotificationReasons.add(str);
            }
            if (!zContains2 && i == 2) {
                this.mShownNotificationReasons.add(str2);
            }
            boolean zShouldShowRoamingNotification = shouldShowRoamingNotification(operatorNumeric);
            if (getCurrentRoamingNotification() == 1) {
                return;
            }
            if (!zContains && !zContains2 && zShouldShowRoamingNotification) {
                updateDataRoamingNotification(1);
                return;
            }
            Log.d("PhoneGlobals", "Skip roaming connected notification since already shownInThisNumeric:" + zContains + " shownForThisReason:" + zContains2 + " shouldShowRoamingNotification:" + zShouldShowRoamingNotification);
            if (getCurrentRoamingNotification() != 0) {
                updateDataRoamingNotification(0);
                return;
            }
            return;
        }
        if (getCurrentRoamingNotification() != 0) {
            updateDataRoamingNotification(0);
        }
    }

    private void updateDataRoamingNotification(int i) {
        int i2;
        if (i == 0) {
            Log.d("PhoneGlobals", "Dismiss roaming notification");
            this.mDataRoamingNotifLog.log("Hide roaming.");
            i2 = 12;
        } else if (i == 1) {
            Log.d("PhoneGlobals", "Show roaming connected notification");
            this.mDataRoamingNotifLog.log("Show roaming on.");
            i2 = 11;
        } else if (i != 2) {
            Log.d("PhoneGlobals", "Should never reach here.");
            this.mDataRoamingNotifLog.log("Should never reach here.");
            return;
        } else {
            Log.d("PhoneGlobals", "Show roaming disconnected notification");
            this.mDataRoamingNotifLog.log("Show roaming off.");
            i2 = 10;
        }
        this.mCurrentRoamingNotification = i;
        this.mHandler.obtainMessage(i2, this.mDefaultDataSubId, 0).sendToTarget();
    }

    int getCurrentRoamingNotification() {
        return this.mCurrentRoamingNotification;
    }

    private boolean shouldShowRoamingNotification(String str) {
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(this.mDefaultDataSubId);
        boolean z = carrierConfigForSubId.getBoolean("show_data_connected_roaming_notification");
        if (TextUtils.isEmpty(str)) {
            Log.d("PhoneGlobals", "shouldShowRoamingNotification: roamingNumeric=" + str);
            return z;
        }
        String[] stringArray = carrierConfigForSubId.getStringArray("data_connected_roaming_notification_included_mcc_mncs_string_array");
        if (stringArray != null) {
            for (String str2 : stringArray) {
                if (str.equals(str2)) {
                    Log.d("PhoneGlobals", "shouldShowRoamingNotification: show for MCC/MNC " + str2);
                    return z;
                }
            }
        }
        String[] stringArray2 = carrierConfigForSubId.getStringArray("data_connected_roaming_notification_excluded_mccs_string_array");
        String strSubstring = str.length() < 3 ? "" : str.substring(0, 3);
        if (stringArray2 != null && !TextUtils.isEmpty(strSubstring)) {
            for (String str3 : stringArray2) {
                if (strSubstring.equals(str3)) {
                    Log.d("PhoneGlobals", "shouldShowRoamingNotification: ignore for MCC " + str3);
                    return false;
                }
            }
        }
        if (z) {
            Log.d("PhoneGlobals", "shouldShowRoamingNotification: show for numeric ".concat(str));
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLimitedSimFunctionForDualSim() {
        if (DBG) {
            Log.d("PhoneGlobals", "updateLimitedSimFunctionForDualSim");
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) getSystemService("telephony_subscription_service")).getActiveSubscriptionInfoList(false);
        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 1) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getSystemService("carrier_config");
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(subscriptionInfo.getSubscriptionId());
                if (configForSubId != null) {
                    boolean z = configForSubId.getBoolean("limited_sim_function_notification_for_dsds_bool");
                    NotificationMgr notificationMgr = this.notificationMgr;
                    if (z) {
                        notificationMgr.showLimitedSimFunctionWarningNotification(subscriptionInfo.getSubscriptionId(), subscriptionInfo.getDisplayName().toString());
                    } else {
                        notificationMgr.dismissLimitedSimFunctionWarningNotification(subscriptionInfo.getSubscriptionId());
                    }
                }
            }
        } else {
            this.notificationMgr.dismissLimitedSimFunctionWarningNotification(-1);
        }
        this.notificationMgr.dismissLimitedSimFunctionWarningNotificationForInactiveSubs();
    }

    public Phone getPhoneInEcm() {
        return this.phoneInEcm;
    }

    public void refreshMwiIndicator(int i) {
        this.notificationMgr.refreshMwi(i);
    }

    public void onNetworkSelectionChanged(int i) {
        Phone phone = getPhone(i);
        if (phone != null) {
            this.notificationMgr.updateNetworkSelection(phone.getServiceState().getState(), i);
            return;
        }
        Log.w("PhoneGlobals", "onNetworkSelectionChanged on null phone, subId: " + i);
    }

    public boolean getDeviceUceEnabled() {
        TelephonyRcsService telephonyRcsService = this.mTelephonyRcsService;
        if (telephonyRcsService == null) {
            return false;
        }
        return telephonyRcsService.isDeviceUceEnabled();
    }

    public void setDeviceUceEnabled(boolean z) {
        TelephonyRcsService telephonyRcsService = this.mTelephonyRcsService;
        if (telephonyRcsService != null) {
            telephonyRcsService.setDeviceUceEnabled(z);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("------- PhoneGlobals -------");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("FeatureFlags:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("mCurrentRoamingNotification=" + this.mCurrentRoamingNotification);
        indentingPrintWriter.println("mDefaultDataSubId=" + this.mDefaultDataSubId);
        indentingPrintWriter.println("isSmsCapable=" + TelephonyManager.from(this).isSmsCapable());
        indentingPrintWriter.println("mDataRoamingNotifLog:");
        indentingPrintWriter.increaseIndent();
        this.mDataRoamingNotifLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("ImsResolver:");
        indentingPrintWriter.increaseIndent();
        try {
            if (ImsResolver.getInstance() != null) {
                ImsResolver.getInstance().dump(fileDescriptor, indentingPrintWriter, strArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("RcsService:");
        try {
            TelephonyRcsService telephonyRcsService = this.mTelephonyRcsService;
            if (telephonyRcsService != null) {
                telephonyRcsService.dump(fileDescriptor, indentingPrintWriter, strArr);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        indentingPrintWriter.println("ImsStateCallbackController:");
        try {
            ImsStateCallbackController imsStateCallbackController = this.mImsStateCallbackController;
            if (imsStateCallbackController != null) {
                imsStateCallbackController.dump(indentingPrintWriter);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        indentingPrintWriter.println("DomainSelectionResolver:");
        indentingPrintWriter.increaseIndent();
        try {
            if (DomainSelectionResolver.getInstance() != null) {
                DomainSelectionResolver.getInstance().dump(fileDescriptor, indentingPrintWriter, strArr);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("mShownNotificationReasons=" + this.mShownNotificationReasons);
        indentingPrintWriter.println("------- End PhoneGlobals -------");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean skipDataRoamingDisconnectedNotificationInSatelliteMode(int i) {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController.isSatelliteEnabledOrBeingEnabled()) {
            Log.d("PhoneGlobals", "skipDataRoamingDisconnected - skip notification as satellite is enabled or being enabled");
            return true;
        }
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        ServiceState serviceState = phone.getServiceState();
        if (serviceState != null && serviceState.isUsingNonTerrestrialNetwork()) {
            Log.d("PhoneGlobals", "skipDataRoamingDisconnected - isUsingNtn");
            if (!satelliteController.getCapabilitiesForCarrierRoamingSatelliteMode(phone).contains(2)) {
                Log.d("PhoneGlobals", "skipDataRoamingDisconnected - skip notification as NTN does not support data");
                return true;
            }
        }
        Log.d("PhoneGlobals", "skipDataRoamingDisconnected - do not skip notification.");
        return false;
    }

    public void addSubInfoUpdateListener(SubInfoUpdateListener subInfoUpdateListener) {
        subInfoUpdateListener.getClass();
        this.mSubInfoUpdateListeners.add(subInfoUpdateListener);
    }

    public void removeSubInfoUpdateListener(SubInfoUpdateListener subInfoUpdateListener) {
        subInfoUpdateListener.getClass();
        this.mSubInfoUpdateListeners.remove(subInfoUpdateListener);
    }

    public void onSubInfoUpdate() {
        List activeSubInfoList = PhoneUtils.getActiveSubInfoList();
        if (TelephonyUtils.isHotSwapHanppened(this.mSubscriptionInfos, activeSubInfoList)) {
            this.mSubscriptionInfos = activeSubInfoList;
            Iterator it = this.mSubInfoUpdateListeners.iterator();
            while (it.hasNext()) {
                ((SubInfoUpdateListener) it.next()).handleSubInfoUpdate();
            }
        }
    }
}
