package com.android.internal.telephony.subscription;

import android.R;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.TelephonyServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.provider.Telephony;
import android.service.carrier.CarrierIdentifier;
import android.service.euicc.EuiccProfileInfo;
import android.service.euicc.EuiccService;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.RadioAccessFamily;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionPlan;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Base64;
import android.util.EventLog;
import android.util.Log;
import android.util.RecurrenceRule;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.BinaryXmlPullParser;
import com.android.internal.telephony.BinaryXmlSerializer;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CallWaitingController;
import com.android.internal.telephony.CarrierResolver;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperFrameworkTelephonyManager;
import com.android.internal.telephony.IHyperUiccManager;
import com.android.internal.telephony.ISetOpportunisticDataCallback;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccProvider;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.MiuiPhoneStub;
import com.android.internal.telephony.MiuiSubscriptionControllerStub;
import com.android.internal.telephony.MultiSimSettingController;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline3;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.SmsController$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.TypedXmlPullParser;
import com.android.internal.telephony.TypedXmlSerializer;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.euicc.EuiccController;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.Inet4AddressUtils$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.telephony.util.WorkerThread;
import com.android.internal.util.FunctionalUtils;
import com.android.telephony.Rlog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class SubscriptionManagerService extends ISub.Stub {
    public static final long FILTER_ACCESSIBLE_SUBS_BY_USER = 296076674;
    public static final long REQUIRE_DEVICE_IDENTIFIERS_FOR_GROUP_UUID = 213902861;
    public static final int SIM_STATE_ESSENTIAL_RECORDS_LOADED = 12;
    private static CountDownTimer bootstrapProvisioningTimer;
    private static SubscriptionManagerService sInstance;
    private final AppOpsManager mAppOpsManager;
    private final Handler mBackgroundHandler;
    private final Context mContext;
    private final WatchedInt mDefaultDataSubId;
    private final WatchedInt mDefaultSmsSubId;
    private final WatchedInt mDefaultSubId;
    private final WatchedInt mDefaultVoiceSubId;
    private final Map mEnrollablePlanExpirationRunnables;
    private final Map mEnrollablePlanExpirationTime;
    private final AtomicFile mEnrollablePlansFile;
    private final Map mEnrollableSubscriptionPlans;
    private final Map mEnrollableSubscriptionPlansOwner;
    private EuiccController mEuiccController;
    private final EuiccManager mEuiccManager;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private final Map mImsNumberUpdateStatus;
    private volatile boolean mIsDeviceShuttingDown;
    private final PackageManager mPackageManager;
    protected final int[] mSimState;
    protected final SubscriptionDatabaseManager mSubscriptionDatabaseManager;
    private final SubscriptionManager mSubscriptionManager;
    private final Set mSubscriptionManagerServiceCallbacks;
    private final TelephonyManager mTelephonyManager;
    private final UiccController mUiccController;
    private Map mUserIdToAvailableSubs;
    private final int mVendorApiLevel;
    private static final Set DIRECT_ACCESS_SUBSCRIPTION_COLUMNS = Set.of((Object[]) new String[]{"enable_cmas_extreme_threat_alerts", "enable_cmas_severe_threat_alerts", "enable_cmas_amber_alerts", "enable_emergency_alerts", "alert_sound_duration", "alert_reminder_interval", "enable_alert_vibrate", "enable_alert_speech", "enable_etws_test_alerts", "enable_channel_50_alerts", "enable_cmas_test_alerts", "show_cmas_opt_out_dialog", "volte_vt_enabled", "vt_ims_enabled", "wfc_ims_enabled", "wfc_ims_mode", "wfc_ims_roaming_mode", "wfc_ims_roaming_enabled", "enabled_mobile_data_policies", "ims_rcs_uce_enabled", "cross_sim_calling_enabled", "rcs_config", "allowed_network_types_for_reasons", "d2d_sharing_status", "voims_opt_in_status", "d2d_sharing_contacts", "nr_advanced_calling_enabled", "satellite_enabled", "satellite_attach_enabled_for_carrier", "is_only_ntn", "satellite_entitlement_status", "satellite_entitlement_plmns", "satellite_esos_supported", "is_satellite_provisioned_for_non_ip_datagram", "satellite_entitlement_barred_plmns", "satellite_entitlement_data_plan_plmns", "satellite_entitlement_service_type_map", "satellite_entitlement_data_service_policy", "satellite_entitlement_voice_service_policy", "streaming_app_max_downlink_kbps", "streaming_app_max_uplink_kbps"});
    private static BinderWrapper BINDER_WRAPPER = new BinderWrapper();
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    private final LocalLog mLocalLog = new LocalLog(CallFailCause.RADIO_UPLINK_FAILURE);
    private final SubscriptionMap mSlotIndexToSubId = new SubscriptionMap();
    private final SubscriptionSet mRemoteSubIds = new SubscriptionSet();

    private void enforcePermissionAccessAllUserProfiles() {
    }

    private int getDefaultAsUser(int i, int i2) {
        return i2;
    }

    private boolean isSubscriptionAssociatedWithUserInternal(SubscriptionInfoInternal subscriptionInfoInternal, int i) {
        return true;
    }

    public boolean isFlexMapSupportNeeded() {
        return false;
    }

    protected boolean needSkipUpdateSubscription(int i) {
        return i == 6;
    }

    protected boolean onCheckIfSubscriptionLoaded(int i) {
        return false;
    }

    public void onHandleSimLoaded(int i) {
    }

    protected void onSubscriptionDatabaseManagerInitialized() {
    }

    public static class SubscriptionMap<K, V> extends ConcurrentHashMap<K, V> {
        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            super.clear();
            SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public V put(K k, V v) {
            V v2 = (V) super.put(k, v);
            if (!Objects.equals(v2, v)) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return v2;
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            V v = (V) super.remove(obj);
            if (v != null) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return v;
        }
    }

    public static class SubscriptionSet<T extends Comparable<? super T>> extends AbstractSet<T> {
        private final Set mBackingSet = ConcurrentHashMap.newKeySet();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.mBackingSet.clear();
            SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(T t) {
            boolean zAdd = this.mBackingSet.add(t);
            if (zAdd) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return zAdd;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            boolean zRemove = this.mBackingSet.remove(obj);
            if (zRemove) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return zRemove;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends T> collection) {
            boolean zAddAll = this.mBackingSet.addAll(collection);
            if (zAddAll) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return zAddAll;
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            boolean zRemoveAll = this.mBackingSet.removeAll(collection);
            if (zRemoveAll) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return zRemoveAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.mBackingSet.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.mBackingSet.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.mBackingSet.contains(obj);
        }

        private Stream sortedStream() {
            return this.mBackingSet.stream().sorted();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<T> iterator() {
            return sortedStream().iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return sortedStream().toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <t> t[] toArray(t[] tArr) {
            return (t[]) sortedStream().toList().toArray(tArr);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return this.mBackingSet.containsAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            boolean zRetainAll = this.mBackingSet.retainAll(collection);
            if (zRetainAll) {
                SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            }
            return zRetainAll;
        }

        public T getLargest() {
            return (T) this.mBackingSet.stream().max(Comparator.naturalOrder()).orElse(null);
        }
    }

    public static class WatchedInt {
        protected int mValue;

        public WatchedInt(int i) {
            this.mValue = i;
        }

        public int get() {
            return this.mValue;
        }

        public boolean set(int i) {
            if (this.mValue == i) {
                return false;
            }
            this.mValue = i;
            SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
            return true;
        }
    }

    public static class BinderWrapper {
        public UserHandle getCallingUserHandle() {
            return Binder.getCallingUserHandle();
        }

        public int getCallingUid() {
            return Binder.getCallingUid();
        }
    }

    public static class SubscriptionManagerServiceCallback {
        private final Executor mExecutor;

        public void onDefaultDataSubscriptionChanged(int i) {
        }

        public void onSubscriptionChanged(int i) {
        }

        public void onUiccApplicationsEnabledChanged(int i) {
        }

        public SubscriptionManagerServiceCallback(Executor executor) {
            this.mExecutor = executor;
        }

        public Executor getExecutor() {
            return this.mExecutor;
        }

        public void invokeFromExecutor(Runnable runnable) {
            this.mExecutor.execute(runnable);
        }
    }

    public static SubscriptionManagerService init(Context context, Looper looper, FeatureFlags featureFlags) {
        synchronized (SubscriptionManagerService.class) {
            try {
                if (sInstance == null) {
                    sInstance = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeSubscriptionManagerService(context, looper, featureFlags);
                    TelephonyServiceManager.ServiceRegisterer subscriptionServiceRegisterer = TelephonyFrameworkInitializer.getTelephonyServiceManager().getSubscriptionServiceRegisterer();
                    if (subscriptionServiceRegisterer.get() == null) {
                        subscriptionServiceRegisterer.register(sInstance);
                    }
                } else {
                    Log.wtf("SMSVC", "SubscriptionManagerService is already initialized.");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public SubscriptionManagerService(Context context, Looper looper, FeatureFlags featureFlags) {
        ArraySet arraySet = new ArraySet();
        this.mSubscriptionManagerServiceCallbacks = arraySet;
        this.mIsDeviceShuttingDown = false;
        this.mUserIdToAvailableSubs = new ConcurrentHashMap();
        this.mImsNumberUpdateStatus = new ConcurrentHashMap();
        this.mEnrollableSubscriptionPlans = new ConcurrentHashMap();
        this.mEnrollableSubscriptionPlansOwner = new ConcurrentHashMap();
        this.mEnrollablePlanExpirationRunnables = new ConcurrentHashMap();
        this.mEnrollablePlanExpirationTime = new ConcurrentHashMap();
        logl("Created SubscriptionManagerService");
        sInstance = this;
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mEuiccManager = (EuiccManager) context.getSystemService(EuiccManager.class);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mPackageManager = context.getPackageManager();
        this.mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
        this.mUiccController = UiccController.getInstance();
        Handler handler = new Handler(looper);
        this.mHandler = handler;
        this.mBackgroundHandler = new Handler(WorkerThread.get().getLooper());
        this.mDefaultVoiceSubId = new WatchedInt(Settings.Global.getInt(context.getContentResolver(), "multi_sim_voice_call", -1)) { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.1
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.WatchedInt
            public boolean set(int i) {
                int i2 = this.mValue;
                if (!super.set(i)) {
                    return false;
                }
                ((IHyperFrameworkTelephonyManager) HyperTelephonyFactory.getInstance().initModule(IHyperFrameworkTelephonyManager.DEFAULT, new Object[0])).popupDataEnabledForSubsidy(i2, i);
                SubscriptionManagerService.this.logl("Default voice subId changed from " + i2 + " to " + i);
                Settings.Global.putInt(SubscriptionManagerService.this.mContext.getContentResolver(), "multi_sim_voice_call", i);
                return true;
            }
        };
        this.mDefaultDataSubId = new WatchedInt(Settings.Global.getInt(context.getContentResolver(), "multi_sim_data_call", -1)) { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.2
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.WatchedInt
            public boolean set(int i) {
                int i2 = this.mValue;
                if (!super.set(i)) {
                    return false;
                }
                SubscriptionManagerService.this.logl("Default data subId changed from " + i2 + " to " + i);
                Settings.Global.putInt(SubscriptionManagerService.this.mContext.getContentResolver(), "multi_sim_data_call", i);
                return true;
            }
        };
        this.mDefaultSmsSubId = new WatchedInt(Settings.Global.getInt(context.getContentResolver(), "multi_sim_sms", -1)) { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.3
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.WatchedInt
            public boolean set(int i) {
                int i2 = this.mValue;
                if (!super.set(i)) {
                    return false;
                }
                SubscriptionManagerService.this.logl("Default SMS subId changed from " + i2 + " to " + i);
                Settings.Global.putInt(SubscriptionManagerService.this.mContext.getContentResolver(), "multi_sim_sms", i);
                return true;
            }
        };
        this.mDefaultSubId = new WatchedInt(-1);
        int[] iArr = new int[telephonyManager.getSupportedModemCount()];
        this.mSimState = iArr;
        Arrays.fill(iArr, 0);
        HandlerThread handlerThread = new HandlerThread("SMSVC");
        handlerThread.start();
        Looper looper2 = handlerThread.getLooper();
        Objects.requireNonNull(handler);
        this.mSubscriptionDatabaseManager = onCreateSubscriptionDatabaseManager(context, looper2, featureFlags, new SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.4
            @Override // com.android.internal.telephony.subscription.SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback
            public void onInitialized() {
                SubscriptionManagerService.this.log("Subscription database has been initialized.");
                int i = 0;
                while (true) {
                    int supportedModemCount = SubscriptionManagerService.this.mTelephonyManager.getSupportedModemCount();
                    SubscriptionManagerService subscriptionManagerService = SubscriptionManagerService.this;
                    if (i < supportedModemCount) {
                        subscriptionManagerService.markSubscriptionsInactive(i);
                        i++;
                    } else {
                        subscriptionManagerService.onSubscriptionDatabaseManagerInitialized();
                        return;
                    }
                }
            }

            @Override // com.android.internal.telephony.subscription.SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback
            public void onSubscriptionChanged(int i) {
                MiuiSubscriptionControllerStub.get().notifyFeatureResolveSimReady(SubscriptionManagerService.this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i));
                SubscriptionManagerService.this.notifySubscriptionChanged(i);
            }
        });
        broadcastSubId("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED", getDefaultDataSubId());
        arraySet.forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$new$1((SubscriptionManagerService.SubscriptionManagerServiceCallback) obj);
            }
        });
        broadcastSubId("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED", getDefaultVoiceSubId());
        broadcastSubId("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED", getDefaultSmsSubId());
        updateDefaultSubId();
        handler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$2();
            }
        });
        File protectedStorageDir = getProtectedStorageDir();
        if (protectedStorageDir != null) {
            this.mEnrollablePlansFile = new AtomicFile(new File(protectedStorageDir, "enrollable-plans.xml"));
        } else {
            this.mEnrollablePlansFile = null;
        }
        handler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.readEnrollableSubscriptionPlans();
            }
        });
        context.registerReceiver(new AnonymousClass5(), new IntentFilter("android.intent.action.TIME_SET"));
        SubscriptionManager.invalidateSubscriptionManagerServiceCaches();
        context.registerReceiver(new BroadcastReceiver() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                SubscriptionManagerService.this.updateEmbeddedSubscriptions();
            }
        }, new IntentFilter("android.intent.action.USER_UNLOCKED"));
        logl("Registered iSub service");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final SubscriptionManagerServiceCallback subscriptionManagerServiceCallback) {
        subscriptionManagerServiceCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(subscriptionManagerServiceCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(SubscriptionManagerServiceCallback subscriptionManagerServiceCallback) {
        subscriptionManagerServiceCallback.onDefaultDataSubscriptionChanged(getDefaultDataSubId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.euicc")) {
            this.mEuiccController = EuiccController.get();
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.subscription.SubscriptionManagerService$5, reason: invalid class name */
    class AnonymousClass5 extends BroadcastReceiver {
        AnonymousClass5() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            SubscriptionManagerService.this.rescheduleAllPlanExpirations();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SubscriptionManagerService.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            });
        }
    }

    public static SubscriptionManagerService getInstance() {
        return sInstance;
    }

    private boolean canPackageManageGroup(final ParcelUuid parcelUuid, String str) {
        if (parcelUuid == null) {
            Phone$$ExternalSyntheticBUOutline0.m("Invalid groupUuid");
            return false;
        }
        if (TextUtils.isEmpty(str)) {
            Phone$$ExternalSyntheticBUOutline0.m("Empty callingPackage");
            return false;
        }
        List list = (List) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SubscriptionInfoInternal) obj).getGroupUuid().equals(parcelUuid.toString());
            }
        }).map(new SubscriptionManagerService$$ExternalSyntheticLambda3()).collect(Collectors.toList());
        if (ArrayUtils.isEmpty(list) || str.equals(((SubscriptionInfo) list.get(0)).getGroupOwner())) {
            return true;
        }
        return checkCarrierPrivilegeOnSubList(list.stream().mapToInt(new SatelliteController$$ExternalSyntheticLambda1()).toArray(), str);
    }

    private boolean checkCarrierPrivilegeOnSubList(int[] iArr, String str) {
        for (int i : iArr) {
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal == null) {
                loge("checkCarrierPrivilegeOnSubList: subId " + i + " does not exist.");
                return false;
            }
            if (subscriptionInfoInternal.isActive()) {
                if (!this.mTelephonyManager.hasCarrierPrivileges(i)) {
                    loge("checkCarrierPrivilegeOnSubList: Does not have carrier privilege on sub " + i);
                    return false;
                }
            } else if (!canManageSubscription(subscriptionInfoInternal.toSubscriptionInfo(), str)) {
                loge("checkCarrierPrivilegeOnSubList: cannot manage sub " + i);
                return false;
            }
        }
        return true;
    }

    public void syncGroupedSetting(final int i) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$syncGroupedSetting$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$syncGroupedSetting$4(int i) {
        if (this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i) == null) {
            loge("syncSettings: Can't find subscription info for sub " + i);
            return;
        }
        this.mSubscriptionDatabaseManager.syncToGroup(i);
    }

    private boolean hasPhoneNumberAccess(int i, String str, String str2, String str3) {
        try {
            return TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(this.mContext, i, str, str2, str3);
        } catch (SecurityException unused) {
            return false;
        }
    }

    private boolean hasSubscriberIdentifierAccess(int i, String str, String str2, String str3, boolean z) {
        try {
            return TelephonyPermissions.checkCallingOrSelfReadSubscriberIdentifiers(this.mContext, i, str, str2, str3, z);
        } catch (SecurityException unused) {
            return false;
        }
    }

    private SubscriptionInfo conditionallyRemoveIdentifiers(SubscriptionInfo subscriptionInfo, String str, String str2, String str3) {
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        boolean zHasSubscriberIdentifierAccess = hasSubscriberIdentifierAccess(subscriptionId, str, str2, str3, true);
        boolean zHasPhoneNumberAccess = hasPhoneNumberAccess(subscriptionId, str, str2, str3);
        if (zHasSubscriberIdentifierAccess && zHasPhoneNumberAccess) {
            return subscriptionInfo;
        }
        SubscriptionInfo.Builder builder = new SubscriptionInfo.Builder(subscriptionInfo);
        if (!zHasSubscriberIdentifierAccess) {
            builder.setIccId((String) null);
            builder.setCardString((String) null);
            builder.setGroupUuid((String) null);
        }
        if (!zHasPhoneNumberAccess) {
            builder.setNumber((String) null);
        }
        return builder.build();
    }

    public static String getStrippedIccid(String str) {
        return MAC_ADDRESS_PATTERN.matcher(str).matches() ? str : TextUtils.emptyIfNull(IccUtils.stripTrailingFs(str));
    }

    private List getIccIdsOfInsertedPhysicalSims() {
        ArrayList arrayList = new ArrayList();
        UiccSlot[] uiccSlots = this.mUiccController.getUiccSlots();
        if (uiccSlots != null) {
            for (UiccSlot uiccSlot : uiccSlots) {
                if (uiccSlot != null && uiccSlot.getCardState() != null && uiccSlot.getCardState().isCardPresent() && !uiccSlot.isEuicc()) {
                    String iccId = uiccSlot.getIccId(0);
                    if (!TextUtils.isEmpty(iccId)) {
                        arrayList.add(getStrippedIccid(iccId));
                    }
                }
            }
        }
        return arrayList;
    }

    public void setCarrierId(int i, int i2) {
        try {
            this.mSubscriptionDatabaseManager.setCarrierId(i, i2);
        } catch (IllegalArgumentException unused) {
            loge("setCarrierId: invalid subId=" + i);
        }
    }

    public void setMccMnc(int i, String str) {
        try {
            this.mSubscriptionDatabaseManager.setMcc(i, str.substring(0, 3));
            this.mSubscriptionDatabaseManager.setMnc(i, str.substring(3));
        } catch (IllegalArgumentException unused) {
            loge("setMccMnc: invalid subId=" + i);
        }
    }

    public void setNtn(int i, boolean z) {
        try {
            this.mSubscriptionDatabaseManager.setNtn(i, z ? 1 : 0);
        } catch (IllegalArgumentException unused) {
            loge("setOnlyNonTerrestrialNetwork: invalid subId=" + i);
        }
    }

    public void setCountryIso(int i, String str) {
        logl("setCountryIso: subId=" + i + ", iso=" + str);
        try {
            this.mSubscriptionDatabaseManager.setCountryIso(i, str);
        } catch (IllegalArgumentException unused) {
            loge("setCountryIso: invalid subId=" + i);
        }
    }

    public void setCarrierName(int i, String str) {
        logl("setCarrierName: subId=" + i + ", carrierName=" + str);
        try {
            this.mSubscriptionDatabaseManager.setCarrierName(i, str);
        } catch (IllegalArgumentException unused) {
            loge("setCarrierName: invalid subId=" + i);
        }
    }

    public void setGroupOwner(int i, String str) {
        enforcePermissions("setGroupOwner", "android.permission.MODIFY_PHONE_STATE");
        try {
            this.mSubscriptionDatabaseManager.setGroupOwner(i, str);
        } catch (IllegalArgumentException unused) {
            loge("setManaged: invalid subId=" + i);
        }
    }

    public void setLastUsedTPMessageReference(int i, int i2) {
        try {
            this.mSubscriptionDatabaseManager.setLastUsedTPMessageReference(i, i2);
        } catch (IllegalArgumentException unused) {
            loge("setLastUsedTPMessageReference: invalid subId=" + i);
        }
    }

    public void setEnabledMobileDataPolicies(int i, String str) {
        try {
            this.mSubscriptionDatabaseManager.setEnabledMobileDataPolicies(i, str);
        } catch (IllegalArgumentException unused) {
            loge("setEnabledMobileDataPolicies: invalid subId=" + i);
        }
    }

    public void setNumberFromIms(int i, String str) {
        logl("setNumberFromIms: subId=" + i + ", number=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str));
        try {
            this.mSubscriptionDatabaseManager.setNumberFromIms(i, str);
        } catch (IllegalArgumentException unused) {
            loge("setNumberFromIms: invalid subId=" + i);
        }
    }

    public void markSubscriptionsInactive(final int i) {
        logl("markSubscriptionsInactive: slot " + i);
        this.mSlotIndexToSubId.remove(Integer.valueOf(i));
        this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda41
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SubscriptionManagerService.$r8$lambda$IbbtxufaQUpvKtXXBShxG6JX_iA(i, (SubscriptionInfoInternal) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda42
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$markSubscriptionsInactive$6((SubscriptionInfoInternal) obj);
            }
        });
        updateGroupDisabled();
        logl("markSubscriptionsInactive: current mapping " + slotMappingToString());
    }

    public static /* synthetic */ boolean $r8$lambda$IbbtxufaQUpvKtXXBShxG6JX_iA(int i, SubscriptionInfoInternal subscriptionInfoInternal) {
        return subscriptionInfoInternal.getSimSlotIndex() == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$markSubscriptionsInactive$6(SubscriptionInfoInternal subscriptionInfoInternal) {
        this.mSubscriptionDatabaseManager.setSimSlotIndex(subscriptionInfoInternal.getSubscriptionId(), -1);
        this.mSubscriptionDatabaseManager.setPortIndex(subscriptionInfoInternal.getSubscriptionId(), getPortIndex(subscriptionInfoInternal.getIccId()));
    }

    private static int getNameSourcePriority(int i) {
        return Math.max(0, Arrays.asList(-1, 0, 4, 1, 3, 2).indexOf(Integer.valueOf(i)));
    }

    private int getColor() {
        int[] intArray = this.mContext.getResources().getIntArray(17236304);
        if (intArray.length == 0) {
            return -1;
        }
        return intArray[new Random().nextInt(intArray.length)];
    }

    private int getPortIndex(String str) {
        int portIndexFromIccId;
        for (UiccSlot uiccSlot : this.mUiccController.getUiccSlots()) {
            if (uiccSlot != null && (portIndexFromIccId = uiccSlot.getPortIndexFromIccId(str)) != -1) {
                return portIndexFromIccId;
            }
        }
        return -1;
    }

    private int insertSubscriptionInfo(String str, int i, String str2, int i2) {
        SubscriptionInfoInternal.Builder allowedNetworkTypesForReasons = new SubscriptionInfoInternal.Builder().setIccId(str).setCardString(str).setSimSlotIndex(i).setType(i2).setIconTint(getColor()).setAllowedNetworkTypesForReasons(Phone.convertAllowedNetworkTypeMapIndexToDbName(0) + "=" + RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE));
        if (str2 != null) {
            allowedNetworkTypesForReasons.setDisplayName(str2);
        }
        int iInsertSubscriptionInfo = this.mSubscriptionDatabaseManager.insertSubscriptionInfo(allowedNetworkTypesForReasons.build());
        logl("insertSubscriptionInfo: Inserted a new subscription. subId=" + iInsertSubscriptionInfo + ", slotIndex=" + i + ", iccId=" + SubscriptionInfo.getPrintableId(str) + ", displayName=" + str2 + ", type=" + SubscriptionManager.subscriptionTypeToString(i2));
        return iInsertSubscriptionInfo;
    }

    public void updateEmbeddedSubscriptions(final List<Integer> list, final Runnable runnable) {
        this.mBackgroundHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateEmbeddedSubscriptions$11(runnable, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$updateEmbeddedSubscriptions$11(final Runnable runnable, List list) {
        boolean z;
        EuiccManager euiccManager = this.mEuiccManager;
        if (euiccManager == null || !euiccManager.isEnabled() || this.mEuiccController == null) {
            loge("updateEmbeddedSubscriptions: eUICC not enabled");
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        final ArraySet arraySet = new ArraySet();
        log("updateEmbeddedSubscriptions: start to get euicc profiles.");
        int i = 0;
        for (UiccSlot uiccSlot : this.mUiccController.getUiccSlots()) {
            if (uiccSlot != null) {
                log("  " + uiccSlot);
            }
        }
        Iterator it = list.iterator();
        Object[] objArr = false;
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            GetEuiccProfileInfoListResult getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList = this.mEuiccController.blockingGetEuiccProfileInfoList(iIntValue);
            logl("updateEmbeddedSubscriptions: cardId=" + iIntValue + ", result=" + getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList);
            if (getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList == null) {
                loge("Failed to get euicc profiles.");
            } else if (getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getResult() != 0) {
                loge("Failed to get euicc profile info. result=" + EuiccService.resultToString(getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getResult()));
            } else {
                if (getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getProfiles() == null || getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getProfiles().isEmpty()) {
                    loge("No profiles returned.");
                } else {
                    boolean isRemovable = getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getIsRemovable();
                    for (EuiccProfileInfo euiccProfileInfo : getEuiccProfileInfoListResultBlockingGetEuiccProfileInfoList.getProfiles()) {
                        SubscriptionInfoInternal subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(euiccProfileInfo.getIccid());
                        if (subscriptionInfoInternalByIccId == null) {
                            int iInsertSubscriptionInfo = insertSubscriptionInfo(euiccProfileInfo.getIccid(), -1, null, i);
                            this.mSubscriptionDatabaseManager.setDisplayName(iInsertSubscriptionInfo, this.mContext.getResources().getString(R.string.faceunlock_multiple_failures, Integer.valueOf(getCardNumber(iInsertSubscriptionInfo))));
                            subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(iInsertSubscriptionInfo);
                        }
                        int displayNameSource = subscriptionInfoInternalByIccId.getDisplayNameSource();
                        int carrierId = subscriptionInfoInternalByIccId.getCarrierId();
                        SubscriptionInfoInternal.Builder builderOnCreateSubscriptionInfoInternalBuilder = this.mSubscriptionDatabaseManager.onCreateSubscriptionInfoInternalBuilder(subscriptionInfoInternalByIccId);
                        builderOnCreateSubscriptionInfoInternalBuilder.setEmbedded(1);
                        List uiccAccessRules = euiccProfileInfo.getUiccAccessRules();
                        if (uiccAccessRules != null && !uiccAccessRules.isEmpty()) {
                            builderOnCreateSubscriptionInfoInternalBuilder.setNativeAccessRules(euiccProfileInfo.getUiccAccessRules());
                        }
                        builderOnCreateSubscriptionInfoInternalBuilder.setRemovableEmbedded(isRemovable);
                        String nickname = euiccProfileInfo.getNickname();
                        if (nickname != null && getNameSourcePriority(displayNameSource) <= getNameSourcePriority(3)) {
                            builderOnCreateSubscriptionInfoInternalBuilder.setDisplayName(nickname);
                            builderOnCreateSubscriptionInfoInternalBuilder.setDisplayNameSource(3);
                        }
                        if (isSatelliteSpn(euiccProfileInfo.getServiceProviderName())) {
                            builderOnCreateSubscriptionInfoInternalBuilder.setOnlyNonTerrestrialNetwork(1);
                            z = true;
                        } else {
                            z = false;
                        }
                        builderOnCreateSubscriptionInfoInternalBuilder.setProfileClass(euiccProfileInfo.getProfileClass());
                        builderOnCreateSubscriptionInfoInternalBuilder.setPortIndex(getPortIndex(euiccProfileInfo.getIccid()));
                        CarrierIdentifier carrierIdentifier = euiccProfileInfo.getCarrierIdentifier();
                        if (carrierIdentifier != null) {
                            if (carrierId == -1) {
                                builderOnCreateSubscriptionInfoInternalBuilder.setCarrierId(CarrierResolver.getCarrierIdFromIdentifier(this.mContext, carrierIdentifier));
                            }
                            String mcc = carrierIdentifier.getMcc();
                            String mnc = carrierIdentifier.getMnc();
                            builderOnCreateSubscriptionInfoInternalBuilder.setMcc(mcc);
                            builderOnCreateSubscriptionInfoInternalBuilder.setMnc(mnc);
                            if (!z) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(mcc);
                                sb.append(mnc);
                                builderOnCreateSubscriptionInfoInternalBuilder.setOnlyNonTerrestrialNetwork(isSatellitePlmn(sb.toString()) ? 1 : 0);
                            }
                        }
                        if (iIntValue >= 0 && this.mUiccController.getCardIdForDefaultEuicc() != -1) {
                            builderOnCreateSubscriptionInfoInternalBuilder.setCardId(iIntValue);
                            builderOnCreateSubscriptionInfoInternalBuilder.setCardString(this.mUiccController.convertToCardString(iIntValue));
                        }
                        builderOnCreateSubscriptionInfoInternalBuilder.setTransferStatus(subscriptionInfoInternalByIccId.getTransferStatus());
                        arraySet.add(Integer.valueOf(subscriptionInfoInternalByIccId.getSubscriptionId()));
                        SubscriptionInfoInternal subscriptionInfoInternalBuild = builderOnCreateSubscriptionInfoInternalBuilder.build();
                        log("updateEmbeddedSubscriptions: update subscription " + subscriptionInfoInternalBuild);
                        this.mSubscriptionDatabaseManager.updateSubscription(subscriptionInfoInternalBuild);
                        i = 0;
                    }
                }
                objArr = true;
                i = 0;
            }
        }
        if (objArr != false) {
            this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new SubscriptionManagerService$$ExternalSyntheticLambda11()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda54
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return SubscriptionManagerService.m1909$r8$lambda$HQ6sDYpo_ZQxJMbV4JvI1m1c(arraySet, (SubscriptionInfoInternal) obj);
                }
            }).forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda55
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$updateEmbeddedSubscriptions$8((SubscriptionInfoInternal) obj);
                }
            });
            if (this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda56
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$updateEmbeddedSubscriptions$9((SubscriptionInfoInternal) obj);
                }
            })) {
                AnomalyReporter.reportAnomaly(UUID.fromString("38fdf63c-3bd9-4fc2-ad33-a20246a32fa7"), "SubscriptionManagerService: Found Invalid portIndex in active subscriptions");
            }
        } else {
            loge("The eSIM profiles update was not successful.");
        }
        log("updateEmbeddedSubscriptions: Finished embedded subscription update.");
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                SubscriptionManagerService.m1926$r8$lambda$zh5QeNF7NgxQ27iGsPl2hgvyto(runnable);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$HQ6sD-Ypo_ZQxJMbV4JvI1--m1c, reason: not valid java name */
    public static /* synthetic */ boolean m1909$r8$lambda$HQ6sDYpo_ZQxJMbV4JvI1m1c(Set set, SubscriptionInfoInternal subscriptionInfoInternal) {
        return !set.contains(Integer.valueOf(subscriptionInfoInternal.getSubscriptionId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEmbeddedSubscriptions$8(SubscriptionInfoInternal subscriptionInfoInternal) {
        logl("updateEmbeddedSubscriptions: Mark the deleted sub " + subscriptionInfoInternal.getSubscriptionId() + " as non-embedded.");
        this.mSubscriptionDatabaseManager.setEmbedded(subscriptionInfoInternal.getSubscriptionId(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateEmbeddedSubscriptions$9(SubscriptionInfoInternal subscriptionInfoInternal) {
        return subscriptionInfoInternal.isEmbedded() && subscriptionInfoInternal.isActive() && subscriptionInfoInternal.getPortIndex() == -1 && this.mSimState[subscriptionInfoInternal.getSimSlotIndex()] == 10;
    }

    /* JADX INFO: renamed from: $r8$lambda$zh5QeNF7Ngx-Q27iGsPl2hgvyto, reason: not valid java name */
    public static /* synthetic */ void m1926$r8$lambda$zh5QeNF7NgxQ27iGsPl2hgvyto(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmbeddedSubscriptions() {
        UiccSlot[] uiccSlots = this.mUiccController.getUiccSlots();
        if (uiccSlots != null) {
            ArrayList arrayList = new ArrayList();
            for (UiccSlot uiccSlot : uiccSlots) {
                if (uiccSlot != null && uiccSlot.isEuicc() && uiccSlot.getUiccCard() != null) {
                    arrayList.add(Integer.valueOf(this.mUiccController.convertToPublicCardId(uiccSlot.getUiccCard().getCardId())));
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            updateEmbeddedSubscriptions(arrayList, null);
        }
    }

    public boolean areUiccAppsEnabledOnCard(int i) {
        SubscriptionInfoInternal subscriptionInfoInternalByIccId;
        String strOnGetIccId = onGetIccId(i);
        return (strOnGetIccId == null || (subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(getStrippedIccid(strOnGetIccId))) == null || !subscriptionInfoInternalByIccId.areUiccApplicationsEnabled()) ? false : true;
    }

    protected String getIccId(int i) {
        UiccPort uiccPort = this.mUiccController.getUiccPort(i);
        return uiccPort == null ? "" : getStrippedIccid(uiccPort.getIccId());
    }

    public boolean areAllSubscriptionsLoaded() {
        for (int i = 0; i < this.mTelephonyManager.getActiveModemCount(); i++) {
            UiccSlot uiccSlotForPhone = this.mUiccController.getUiccSlotForPhone(i);
            if (uiccSlotForPhone == null) {
                if (!onCheckIfSubscriptionLoaded(i)) {
                    log("areAllSubscriptionsLoaded: slot is null. phoneId=" + i);
                    return false;
                }
            } else {
                if (!uiccSlotForPhone.isActive()) {
                    log("areAllSubscriptionsLoaded: slot is inactive. phoneId=" + i);
                    return false;
                }
                if (uiccSlotForPhone.isEuicc() && this.mUiccController.getUiccPort(i) == null) {
                    log("Wait for port corresponding to phone " + i + " to be active, portIndex is " + uiccSlotForPhone.getPortIndexFromPhoneId(i));
                    return false;
                }
                if (this.mSimState[i] == 6 && !PhoneFactory.getPhone(i).getIccCard().isEmptyProfile() && areUiccAppsEnabledOnCard(i)) {
                    log("areAllSubscriptionsLoaded: NOT_READY is not a final state.");
                    return false;
                }
                if (this.mSimState[i] == 0) {
                    log("areAllSubscriptionsLoaded: SIM " + i + " state is still unknown.");
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateSubscription(final int i) {
        int subscriptionId;
        String cardId;
        int i2 = this.mSimState[i];
        log("updateSubscription: phoneId=" + i + ", simState=" + TelephonyManager.simStateToString(i2));
        UiccSlot[] uiccSlots = this.mUiccController.getUiccSlots();
        int length = uiccSlots.length;
        for (int i3 = 0; i3 < length; i3++) {
            UiccSlot uiccSlot = uiccSlots[i3];
            if (uiccSlot != null) {
                log("  " + uiccSlot);
            }
        }
        if (i2 == 1) {
            SatelliteController satelliteController = SatelliteController.getInstance();
            if (!(satelliteController != null ? satelliteController.isSatelliteEnabledOrBeingEnabled() : false)) {
                if (this.mIsDeviceShuttingDown) {
                    log("updateSubscription: Skip re-enabling Uicc applications during device shutdown on phone " + i);
                } else {
                    final List iccIdsOfInsertedPhysicalSims = getIccIdsOfInsertedPhysicalSims();
                    this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda24
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return SubscriptionManagerService.m1911$r8$lambda$MdVJ7g7TzWsca3APy990nxl4Yg(iccIdsOfInsertedPhysicalSims, (SubscriptionInfoInternal) obj);
                        }
                    }).forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda25
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            this.f$0.lambda$updateSubscription$13((SubscriptionInfoInternal) obj);
                        }
                    });
                }
            }
            if (this.mSlotIndexToSubId.containsKey(Integer.valueOf(i))) {
                markSubscriptionsInactive(i);
            }
            ((IHyperUiccManager) HyperTelephonyFactory.getInstance().initModule(IHyperUiccManager.DEFAULT, new Object[0])).notifySimOperatorChanged(this.mContext, null, i);
        } else if (needSkipUpdateSubscription(i2)) {
            IccCard iccCard = PhoneFactory.getPhone(i).getIccCard();
            if (iccCard.isEmptyProfile()) {
                log("updateSubscription: iccCard has empty profile.");
            }
            if (!iccCard.isEmptyProfile() && areUiccAppsEnabledOnCard(i)) {
                log("updateSubscription: SIM_STATE_NOT_READY is not a final state. Will update subscription later.");
                return;
            }
            logl("updateSubscription: UICC app disabled on slot " + i);
            markSubscriptionsInactive(i);
        } else {
            final String iccId = getIccId(i);
            log("updateSubscription: Found iccId=" + SubscriptionInfo.getPrintableId(iccId) + " on phone " + i);
            SubscriptionInfoInternal subscriptionInfoInternalOrElse = this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda26
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return SubscriptionManagerService.m1910$r8$lambda$HfmFG67cFifheiL2Hbxhd5KTX0(i, iccId, (SubscriptionInfoInternal) obj);
                }
            }).findFirst().orElse(null);
            if (subscriptionInfoInternalOrElse != null) {
                log("updateSubscription: Found previous active sub " + subscriptionInfoInternalOrElse.getSubscriptionId() + " that doesn't match current iccid on slot " + i + ".");
                markSubscriptionsInactive(i);
            }
            if (isValidIccId(iccId)) {
                SubscriptionInfoInternal subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(iccId);
                if (subscriptionInfoInternalByIccId == null) {
                    subscriptionId = insertSubscriptionInfo(iccId, i, null, 0);
                    this.mSubscriptionDatabaseManager.setDisplayName(subscriptionId, this.mContext.getResources().getString(R.string.faceunlock_multiple_failures, Integer.valueOf(getCardNumber(subscriptionId))));
                } else {
                    subscriptionId = subscriptionInfoInternalByIccId.getSubscriptionId();
                    log("updateSubscription: Found existing subscription. subId= " + subscriptionId + ", phoneId=" + i);
                }
                SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(subscriptionId);
                if (subscriptionInfoInternal != null && subscriptionInfoInternal.areUiccApplicationsEnabled()) {
                    this.mSlotIndexToSubId.put(Integer.valueOf(i), Integer.valueOf(subscriptionId));
                    this.mSubscriptionDatabaseManager.setSimSlotIndex(subscriptionId, i);
                    logl("updateSubscription: current mapping " + slotMappingToString());
                    if (i2 == 5) {
                        MiuiPhoneStub.get(i).purgeGuti(this.mContext, "addSubInfo", i);
                    }
                }
                UiccCard uiccCardForPhone = this.mUiccController.getUiccCardForPhone(i);
                if (uiccCardForPhone != null && (cardId = uiccCardForPhone.getCardId()) != null) {
                    this.mSubscriptionDatabaseManager.setCardString(subscriptionId, cardId);
                }
                this.mSubscriptionDatabaseManager.setPortIndex(subscriptionId, getPortIndex(iccId));
                ((IHyperUiccManager) HyperTelephonyFactory.getInstance().initModule(IHyperUiccManager.DEFAULT, new Object[0])).notifySimOperatorChanged(this.mContext, subscriptionInfoInternal, i);
                if (i2 == 10 || i2 == 12) {
                    String simOperatorNumeric = this.mTelephonyManager.getSimOperatorNumeric(subscriptionId);
                    if (!TextUtils.isEmpty(simOperatorNumeric)) {
                        if (subscriptionId == getDefaultSubId()) {
                            MccTable.updateMccMncConfiguration(this.mContext, simOperatorNumeric);
                        }
                        setMccMnc(subscriptionId, simOperatorNumeric);
                        if (isSatelliteSpn(subscriptionInfoInternal.getDisplayName()) || isSatellitePlmn(simOperatorNumeric)) {
                            setNtn(subscriptionId, true);
                        }
                    } else {
                        loge("updateSubscription: mcc/mnc is empty");
                    }
                    String simCountryIsoForPhone = TelephonyManager.getSimCountryIsoForPhone(i);
                    if (!TextUtils.isEmpty(simCountryIsoForPhone)) {
                        setCountryIso(subscriptionId, simCountryIsoForPhone);
                    } else {
                        loge("updateSubscription: sim country iso is null");
                    }
                    String number = subscriptionInfoInternal != null ? subscriptionInfoInternal.getNumber() : "";
                    if (!MiuiSubscriptionControllerStub.get().isUpdatedPhoneNumberByUser(this.mContext, subscriptionId) || TextUtils.isEmpty(number)) {
                        String line1Number = this.mTelephonyManager.getLine1Number(subscriptionId);
                        if (!TextUtils.isEmpty(line1Number)) {
                            setDisplayNumber(line1Number, subscriptionId);
                        }
                    }
                    String subscriberId = this.mTelephonyManager.createForSubscriptionId(subscriptionId).getSubscriberId();
                    if (subscriberId != null) {
                        this.mSubscriptionDatabaseManager.setImsi(subscriptionId, subscriberId);
                    }
                    IccCard iccCard2 = PhoneFactory.getPhone(i).getIccCard();
                    if (iccCard2 != null) {
                        IccRecords iccRecords = iccCard2.getIccRecords();
                        if (iccRecords != null) {
                            String[] ehplmns = iccRecords.getEhplmns();
                            if (ehplmns != null) {
                                this.mSubscriptionDatabaseManager.setEhplmns(subscriptionId, ehplmns);
                            }
                            String[] plmnsFromHplmnActRecord = iccRecords.getPlmnsFromHplmnActRecord();
                            if (plmnsFromHplmnActRecord != null) {
                                this.mSubscriptionDatabaseManager.setHplmns(subscriptionId, plmnsFromHplmnActRecord);
                            }
                        } else {
                            loge("updateSubscription: ICC records are not available.");
                        }
                    } else {
                        loge("updateSubscription: ICC card is not available.");
                    }
                    onHandleSimLoaded(i);
                    Bundle bundleCall = this.mContext.getContentResolver().call(SubscriptionManager.SIM_INFO_BACKUP_AND_RESTORE_CONTENT_URI, "restoreSimSpecificSettings", iccId, (Bundle) null);
                    if (bundleCall != null && bundleCall.getBoolean("restoreSimSpecificSettingsDatabaseUpdated")) {
                        logl("Sim specific settings changed the database.");
                        this.mSubscriptionDatabaseManager.reloadDatabaseSync();
                        PhoneFactory.getPhone(i).loadAllowedNetworksFromSubscriptionDatabase();
                    }
                }
                log("updateSubscription: " + this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(subscriptionId));
                MiuiSubscriptionControllerStub.get().notifyFeatureResolveSimReady(this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(subscriptionId));
            } else {
                log("updateSubscription: No ICCID available for phone " + i);
                this.mSlotIndexToSubId.remove(Integer.valueOf(i));
                logl("updateSubscription: current mapping " + slotMappingToString());
            }
        }
        if (areAllSubscriptionsLoaded()) {
            log("Notify all subscriptions loaded.");
            MultiSimSettingController.getInstance().notifyAllSubscriptionLoaded();
        }
        updateGroupDisabled();
        updateDefaultSubId();
        if (this.mSlotIndexToSubId.containsKey(Integer.valueOf(i)) && isEsimBootStrapProvisioningActiveForSubId(((Integer) this.mSlotIndexToSubId.get(Integer.valueOf(i))).intValue())) {
            startEsimBootstrapTimer();
        } else {
            cancelEsimBootstrapTimer();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$MdVJ7g7TzWs-ca3APy990nxl4Yg, reason: not valid java name */
    public static /* synthetic */ boolean m1911$r8$lambda$MdVJ7g7TzWsca3APy990nxl4Yg(List list, SubscriptionInfoInternal subscriptionInfoInternal) {
        return (list.contains(subscriptionInfoInternal.getIccId()) || subscriptionInfoInternal.isEmbedded()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSubscription$13(SubscriptionInfoInternal subscriptionInfoInternal) {
        this.mSubscriptionDatabaseManager.setPortIndex(subscriptionInfoInternal.getSubscriptionId(), -1);
    }

    /* JADX INFO: renamed from: $r8$lambda$HfmFG67cF-ifheiL2Hbxhd5KTX0, reason: not valid java name */
    public static /* synthetic */ boolean m1910$r8$lambda$HfmFG67cFifheiL2Hbxhd5KTX0(int i, String str, SubscriptionInfoInternal subscriptionInfoInternal) {
        return subscriptionInfoInternal.getSimSlotIndex() == i && !str.equals(subscriptionInfoInternal.getIccId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelEsimBootstrapTimer() {
        CountDownTimer countDownTimer = bootstrapProvisioningTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            bootstrapProvisioningTimer = null;
            log("bootstrapProvisioningTimer timer cancelled.");
        }
    }

    private void startEsimBootstrapTimer() {
        if (bootstrapProvisioningTimer == null) {
            bootstrapProvisioningTimer = new CountDownTimer(1200000L, 1200000L) { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService.7
                @Override // android.os.CountDownTimer
                public void onTick(long j) {
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    AnomalyReporter.reportAnomaly(UUID.fromString("40587b0f-27c9-4b39-b94d-71fc9771f354"), "eSim bootstrap has been active for too long.");
                    SubscriptionManagerService.this.log("bootstrapProvisioningTimer: timer finished esim was not disabled.");
                    SubscriptionManagerService.this.cancelEsimBootstrapTimer();
                }
            }.start();
            log("bootstrapProvisioningTimer timer started.");
        }
    }

    public void setDeviceShuttingDown(boolean z) {
        logl("setDeviceShuttingDown: " + z);
        this.mIsDeviceShuttingDown = z;
    }

    public int calculateUsageSetting(int i, int i2) {
        try {
            int[] intArray = this.mContext.getResources().getIntArray(R.array.non_removable_euicc_slots);
            if (intArray != null && intArray.length >= 1) {
                if (i < 0 || i > 2) {
                    log("calculateUsageSetting: Updating usage setting for current subscription");
                    i = 0;
                }
                if (i2 >= 0 && i2 <= 2) {
                    if (i2 != 0) {
                        for (int i3 : intArray) {
                            if (i2 != i3) {
                            }
                        }
                        return i;
                    }
                    return i2;
                }
                loge("calculateUsageSetting: Invalid usage setting!" + i2);
            }
            return i;
        } catch (Resources.NotFoundException unused) {
            loge("calculateUsageSetting: Failed to load usage setting resources!");
            return i;
        }
    }

    public void updateSubscriptionByCarrierConfig(final int i, final String str, final PersistableBundle persistableBundle, final Runnable runnable) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSubscriptionByCarrierConfig$15(i, str, persistableBundle, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSubscriptionByCarrierConfig$15(int i, String str, PersistableBundle persistableBundle, Runnable runnable) {
        updateSubscriptionByCarrierConfigInternal(i, str, persistableBundle);
        runnable.run();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateSubscriptionByCarrierConfigInternal(int i, String str, PersistableBundle persistableBundle) {
        int iServiceCapabilityToBitmask;
        boolean z;
        log("updateSubscriptionByCarrierConfig: phoneId=" + i + ", configPackageName=" + str);
        if (!SubscriptionManager.isValidPhoneId(i) || TextUtils.isEmpty(str) || persistableBundle == null) {
            StringBuilder sb = new StringBuilder("updateSubscriptionByCarrierConfig: Failed to update the subscription. phoneId=");
            sb.append(i);
            sb.append(" configPackageName=");
            sb.append(str);
            sb.append(" config=");
            sb.append(persistableBundle == null ? "null" : Integer.valueOf(persistableBundle.hashCode()));
            loge(sb.toString());
            return;
        }
        if (!this.mSlotIndexToSubId.containsKey(Integer.valueOf(i))) {
            log("updateSubscriptionByCarrierConfig: No subscription is active for phone being updated.");
            return;
        }
        int iIntValue = ((Integer) this.mSlotIndexToSubId.get(Integer.valueOf(i))).intValue();
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(iIntValue);
        if (subscriptionInfoInternal == null) {
            loge("updateSubscriptionByCarrierConfig: Couldn't retrieve subscription info for current subscription. subId=" + iIntValue);
            return;
        }
        UiccAccessRule[] uiccAccessRuleArrDecodeRulesFromCarrierConfig = UiccAccessRule.decodeRulesFromCarrierConfig(persistableBundle.getStringArray("carrier_certificate_string_array"));
        if (uiccAccessRuleArrDecodeRulesFromCarrierConfig != null) {
            this.mSubscriptionDatabaseManager.setCarrierConfigAccessRules(iIntValue, uiccAccessRuleArrDecodeRulesFromCarrierConfig);
        }
        this.mSubscriptionDatabaseManager.setOpportunistic(iIntValue, persistableBundle.getBoolean("is_opportunistic_subscription_bool", subscriptionInfoInternal.isOpportunistic()));
        String string = persistableBundle.getString("subscription_group_uuid_string", "");
        String groupUuid = subscriptionInfoInternal.getGroupUuid();
        if (!TextUtils.isEmpty(string)) {
            try {
                ParcelUuid parcelUuidFromString = ParcelUuid.fromString(string);
                if (string.equals("00000000-0000-0000-0000-000000000000")) {
                    this.mSubscriptionDatabaseManager.setGroupUuid(iIntValue, "");
                } else if (canPackageManageGroup(parcelUuidFromString, str)) {
                    this.mSubscriptionDatabaseManager.setGroupUuid(iIntValue, string);
                    this.mSubscriptionDatabaseManager.setGroupOwner(iIntValue, str);
                    log("updateSubscriptionByCarrierConfig: Group added for sub " + iIntValue);
                } else {
                    loge("updateSubscriptionByCarrierConfig: configPackageName " + str + " doesn't own groupUuid " + parcelUuidFromString);
                }
                if (!string.equals(groupUuid)) {
                    MultiSimSettingController.getInstance().notifySubscriptionGroupChanged(parcelUuidFromString);
                }
            } catch (IllegalArgumentException unused) {
                loge("updateSubscriptionByCarrierConfig: Invalid Group UUID=" + string);
            }
        }
        updateGroupDisabled();
        int i2 = persistableBundle.getInt("cellular_usage_setting_int", -1);
        int iCalculateUsageSetting = calculateUsageSetting(subscriptionInfoInternal.getUsageSetting(), i2);
        if (iCalculateUsageSetting != subscriptionInfoInternal.getUsageSetting()) {
            this.mSubscriptionDatabaseManager.setUsageSetting(iIntValue, iCalculateUsageSetting);
            log("updateSubscriptionByCarrierConfig: UsageSetting changed, oldSetting=" + SubscriptionManager.usageSettingToString(subscriptionInfoInternal.getUsageSetting()) + " preferredSetting=" + SubscriptionManager.usageSettingToString(i2) + " newSetting=" + SubscriptionManager.usageSettingToString(iCalculateUsageSetting));
        }
        int[] intArray = persistableBundle.getIntArray("cellular_service_capabilities_int_array");
        int i3 = 1;
        if (intArray == null) {
            z = false;
            iServiceCapabilityToBitmask = 0;
        } else {
            int length = intArray.length;
            int i4 = 0;
            iServiceCapabilityToBitmask = 0;
            while (true) {
                if (i4 >= length) {
                    z = true;
                    break;
                }
                int i5 = intArray[i4];
                if (i5 < 1 || i5 > 3) {
                    z = false;
                    break;
                } else {
                    iServiceCapabilityToBitmask |= SubscriptionManager.serviceCapabilityToBitmask(i5);
                    i4++;
                }
            }
        }
        if (!z) {
            iServiceCapabilityToBitmask = SubscriptionManager.getAllServiceCapabilityBitmasks();
        }
        if (iServiceCapabilityToBitmask != subscriptionInfoInternal.getServiceCapabilities()) {
            log("updateSubscriptionByCarrierConfig: serviceCapabilities updated from " + subscriptionInfoInternal.getServiceCapabilities() + " to " + iServiceCapabilityToBitmask);
            this.mSubscriptionDatabaseManager.setServiceCapabilities(iIntValue, iServiceCapabilityToBitmask);
        }
        if (!"999".equals(subscriptionInfoInternal.getMcc()) && !persistableBundle.getBoolean("is_private_network_bool", false)) {
            i3 = 0;
        }
        this.mSubscriptionDatabaseManager.setIsPrivateNetwork(iIntValue, i3);
    }

    public List<SubscriptionInfo> getAllSubInfoList(final String str, final String str2) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Binder.getCallingPid(), Binder.getCallingUid(), str, str2, "getAllSubInfoList")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return null;
        }
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getAllSubInfoList");
        }
        return (List) getSubscriptionInfoStreamAsUser(BINDER_WRAPPER.getCallingUserHandle()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getAllSubInfoList$16(str, str2, (SubscriptionInfoInternal) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda37
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getAllSubInfoList$17(str, str2, (SubscriptionInfoInternal) obj);
            }
        }).sorted(Comparator.comparing(new SubscriptionManagerService$$ExternalSyntheticLambda32()).thenComparing(new SubscriptionManagerService$$ExternalSyntheticLambda33())).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAllSubInfoList$16(String str, String str2, SubscriptionInfoInternal subscriptionInfoInternal) {
        return TelephonyPermissions.checkCallingOrSelfReadPhoneStateNoThrow(this.mContext, subscriptionInfoInternal.getSubscriptionId(), str, str2, "getAllSubInfoList");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SubscriptionInfo lambda$getAllSubInfoList$17(String str, String str2, SubscriptionInfoInternal subscriptionInfoInternal) {
        return conditionallyRemoveIdentifiers(subscriptionInfoInternal.toSubscriptionInfo(), str, str2, "getAllSubInfoList");
    }

    public SubscriptionInfo getActiveSubscriptionInfo(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str, str2, "getActiveSubscriptionInfo")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return null;
        }
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getActiveSubscriptionInfo");
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null || !subscriptionInfoInternal.isActive()) {
            return null;
        }
        return conditionallyRemoveIdentifiers(subscriptionInfoInternal.toSubscriptionInfo(), str, str2, "getActiveSubscriptionInfo");
    }

    public SubscriptionInfo getActiveSubscriptionInfoForIccId(String str, String str2, String str3) {
        enforcePermissions("getActiveSubscriptionInfoForIccId", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str2, "getActiveSubscriptionInfoForIccId");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(getStrippedIccid(str));
            return (subscriptionInfoInternalByIccId == null || !subscriptionInfoInternalByIccId.isActive()) ? null : subscriptionInfoInternalByIccId.toSubscriptionInfo();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int i, String str, String str2) {
        int iIntValue = ((Integer) this.mSlotIndexToSubId.getOrDefault(Integer.valueOf(i), -1)).intValue();
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, iIntValue, str, str2, "getActiveSubscriptionInfoForSimSlotIndex")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return null;
        }
        enforceTelephonyFeatureWithException(str, "getActiveSubscriptionInfoForSimSlotIndex");
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid slot index ", i);
            return null;
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(iIntValue);
        if (subscriptionInfoInternal == null || !subscriptionInfoInternal.isActive()) {
            return null;
        }
        return conditionallyRemoveIdentifiers(subscriptionInfoInternal.toSubscriptionInfo(), str, str2, "getActiveSubscriptionInfoForSimSlotIndex");
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList(final String str, final String str2, boolean z) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Binder.getCallingPid(), Binder.getCallingUid(), str, str2, "getActiveSubscriptionInfoList")) {
            loge("getActiveSubscriptionInfoList: " + str + " does not have enough permission. Returning empty list here.");
            return Collections.EMPTY_LIST;
        }
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getActiveSubscriptionInfoList");
        }
        if (z) {
            enforcePermissionAccessAllUserProfiles();
        }
        return (List) getSubscriptionInfoStreamAsUser(z ? UserHandle.ALL : BINDER_WRAPPER.getCallingUserHandle()).filter(new SubscriptionManagerService$$ExternalSyntheticLambda2()).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda47
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getActiveSubscriptionInfoList$18(str, str2, (SubscriptionInfoInternal) obj);
            }
        }).sorted(Comparator.comparing(new SubscriptionManagerService$$ExternalSyntheticLambda32()).thenComparing(new SubscriptionManagerService$$ExternalSyntheticLambda33())).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SubscriptionInfo lambda$getActiveSubscriptionInfoList$18(String str, String str2, SubscriptionInfoInternal subscriptionInfoInternal) {
        return conditionallyRemoveIdentifiers(subscriptionInfoInternal.toSubscriptionInfo(), str, str2, "getActiveSubscriptionInfoList");
    }

    public int getActiveSubInfoCount(String str, String str2, boolean z) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Binder.getCallingPid(), Binder.getCallingUid(), str, str2, "getAllSubInfoList")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return 0;
        }
        if (z) {
            enforcePermissionAccessAllUserProfiles();
        }
        enforceTelephonyFeatureWithException(str, "getActiveSubInfoCount");
        return getActiveSubIdListAsUser(false, z ? UserHandle.ALL : BINDER_WRAPPER.getCallingUserHandle()).length;
    }

    public int getActiveSubInfoCountMax() {
        return this.mTelephonyManager.getActiveModemCount();
    }

    public List<SubscriptionInfo> getAvailableSubscriptionInfoList(String str, String str2) {
        enforcePermissions("getAvailableSubscriptionInfoList", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getAvailableSubscriptionInfoList");
        }
        return (List) getAvailableSubscriptionsInternalStream().sorted(Comparator.comparing(new SubscriptionDatabaseManager$$ExternalSyntheticLambda141()).thenComparing(new SubscriptionDatabaseManager$$ExternalSyntheticLambda64())).map(new SubscriptionManagerService$$ExternalSyntheticLambda3()).collect(Collectors.toList());
    }

    private Stream getAvailableSubscriptionsInternalStream() {
        final List iccIdsOfInsertedPhysicalSims = getIccIdsOfInsertedPhysicalSims();
        return this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda61
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getAvailableSubscriptionsInternalStream$19(iccIdsOfInsertedPhysicalSims, (SubscriptionInfoInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAvailableSubscriptionsInternalStream$19(List list, SubscriptionInfoInternal subscriptionInfoInternal) {
        if (subscriptionInfoInternal.isActive() || list.contains(subscriptionInfoInternal.getIccId())) {
            return true;
        }
        EuiccManager euiccManager = this.mEuiccManager;
        return euiccManager != null && euiccManager.isEnabled() && subscriptionInfoInternal.isEmbedded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySubscriptionChanged(final int i) {
        updateUserIdToAvailableSubs();
        this.mSubscriptionManagerServiceCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda40
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SubscriptionManagerService.SubscriptionManagerServiceCallback subscriptionManagerServiceCallback = (SubscriptionManagerService.SubscriptionManagerServiceCallback) obj;
                subscriptionManagerServiceCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda52
                    @Override // java.lang.Runnable
                    public final void run() {
                        subscriptionManagerServiceCallback.onSubscriptionChanged(i);
                    }
                });
            }
        });
        MultiSimSettingController.getInstance().notifySubscriptionInfoChanged();
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) this.mContext.getSystemService(TelephonyRegistryManager.class);
        if (telephonyRegistryManager != null) {
            telephonyRegistryManager.notifySubscriptionInfoChanged();
        }
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null || !subscriptionInfoInternal.isOpportunistic() || telephonyRegistryManager == null) {
            return;
        }
        telephonyRegistryManager.notifyOpportunisticSubscriptionInfoChanged();
    }

    private void updateUserIdToAvailableSubs() {
        this.mUserIdToAvailableSubs = (Map) getAvailableSubscriptionsInternalStream().collect(Collectors.groupingBy(new SubscriptionDatabaseManager$$ExternalSyntheticLambda175(), Collectors.mapping(new SubscriptionDatabaseManager$$ExternalSyntheticLambda64(), Collectors.toList())));
        log("updateUserIdToAvailableSubs: " + this.mUserIdToAvailableSubs);
    }

    public List<SubscriptionInfo> getAccessibleSubscriptionInfoList(final String str) {
        EuiccManager euiccManager = this.mEuiccManager;
        if (euiccManager == null || !euiccManager.isEnabled()) {
            return null;
        }
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        return (List) getSubscriptionInfoStreamAsUser(BINDER_WRAPPER.getCallingUserHandle()).map(new SubscriptionManagerService$$ExternalSyntheticLambda3()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda51
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getAccessibleSubscriptionInfoList$22(str, (SubscriptionInfo) obj);
            }
        }).sorted(Comparator.comparing(new SubscriptionManagerService$$ExternalSyntheticLambda32()).thenComparing(new SubscriptionManagerService$$ExternalSyntheticLambda33())).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAccessibleSubscriptionInfoList$22(String str, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.isEmbedded() && canManageSubscription(subscriptionInfo, str);
    }

    public void requestEmbeddedSubscriptionInfoListRefresh(int i) {
        enforcePermissions("requestEmbeddedSubscriptionInfoListRefresh", "android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS");
        updateEmbeddedSubscriptions(List.of(Integer.valueOf(i)), null);
    }

    public int addSubInfo(String str, String str2, int i, int i2) {
        enforcePermissions("addSubInfo", "android.permission.MODIFY_PHONE_STATE");
        logl("addSubInfo: iccId=" + SubscriptionInfo.getPrintableId(str) + ", slotIndex=" + i + ", displayName=" + str2 + ", type=" + SubscriptionManager.subscriptionTypeToString(i2) + ", " + getCallingPackage());
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "addSubInfo");
        }
        if (i2 == 0) {
            if (!SubscriptionManager.isValidSlotIndex(i)) {
                Phone$$ExternalSyntheticBUOutline3.m("Invalid slot index ", i, " for local SIM");
                return 0;
            }
        } else {
            if (i2 != 1) {
                RILUtils$$ExternalSyntheticBUOutline0.m("Invalid subscription type ", i2);
                return 0;
            }
            i = -2;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (TextUtils.isEmpty(str)) {
                loge("addSubInfo: null or empty iccId");
                return -1;
            }
            String strippedIccid = getStrippedIccid(str);
            if (this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(strippedIccid) != null) {
                loge("Subscription record already existed.");
                return -1;
            }
            if (i2 == 0 && this.mSlotIndexToSubId.containsKey(Integer.valueOf(i))) {
                loge("Already a subscription on slot " + i);
                return -1;
            }
            int iInsertSubscriptionInfo = insertSubscriptionInfo(strippedIccid, i, str2, i2);
            updateGroupDisabled();
            if (i2 == 1) {
                this.mRemoteSubIds.add(Integer.valueOf(iInsertSubscriptionInfo));
            } else {
                this.mSlotIndexToSubId.put(Integer.valueOf(i), Integer.valueOf(iInsertSubscriptionInfo));
            }
            logl("addSubInfo: current mapping " + slotMappingToString());
            return 0;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean removeSubInfo(String str, int i) {
        enforcePermissions("removeSubInfo", "android.permission.MODIFY_PHONE_STATE");
        logl("removeSubInfo: uniqueId=" + SubscriptionInfo.getPrintableId(str) + ", " + SubscriptionManager.subscriptionTypeToString(i) + ", " + getCallingPackage());
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "removeSubInfo");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(str);
            if (subscriptionInfoInternalByIccId == null) {
                loge("Cannot find subscription with uniqueId " + str);
                return false;
            }
            if (subscriptionInfoInternalByIccId.getSubscriptionType() != i) {
                loge("The subscription type does not match.");
                return false;
            }
            if (i == 1) {
                this.mRemoteSubIds.remove(Integer.valueOf(subscriptionInfoInternalByIccId.getSubscriptionId()));
            } else {
                this.mSlotIndexToSubId.remove(Integer.valueOf(subscriptionInfoInternalByIccId.getSimSlotIndex()));
            }
            this.mSubscriptionDatabaseManager.removeSubscriptionInfo(subscriptionInfoInternalByIccId.getSubscriptionId());
            return true;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setIconTint(int i, int i2) {
        enforcePermissions("setIconTint", "android.permission.MODIFY_PHONE_STATE");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!SubscriptionManager.isValidSubscriptionId(i)) {
                throw new IllegalArgumentException("Invalid sub id passed as parameter");
            }
            this.mSubscriptionDatabaseManager.setIconTint(i, i2);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return 1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setDisplayNameUsingSrc(String str, int i, int i2) {
        enforcePermissions("setDisplayNameUsingSrc", "android.permission.MODIFY_PHONE_STATE");
        String callingPackage = getCallingPackage();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Objects.requireNonNull(str, "setDisplayNameUsingSrc");
            if (i2 < 0 || i2 > 4) {
                throw new IllegalArgumentException("illegal name source " + i2);
            }
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal == null) {
                throw new IllegalArgumentException("Cannot find subscription info with sub id " + i);
            }
            if (getNameSourcePriority(subscriptionInfoInternal.getDisplayNameSource()) <= getNameSourcePriority(i2) && (getNameSourcePriority(subscriptionInfoInternal.getDisplayNameSource()) != getNameSourcePriority(i2) || !TextUtils.equals(str, subscriptionInfoInternal.getDisplayName()))) {
                if (TextUtils.isEmpty(str) || str.trim().length() == 0) {
                    str = this.mTelephonyManager.getSimOperatorName(i);
                    if (TextUtils.isEmpty(str)) {
                        str = (i2 == 2 && SubscriptionManager.isValidSlotIndex(getSlotIndex(i))) ? Resources.getSystem().getString(R.string.faceunlock_multiple_failures, Integer.valueOf(getSlotIndex(i) + 1)) : this.mContext.getString(R.string.unknownName);
                    }
                }
                logl("setDisplayNameUsingSrc: subId=" + i + ", name=" + str + ", nameSource=" + SubscriptionManager.displayNameSourceToString(i2) + ", calling package=" + callingPackage);
                this.mSubscriptionDatabaseManager.setDisplayName(i, str);
                this.mSubscriptionDatabaseManager.setDisplayNameSource(i, i2);
                SubscriptionInfo subscriptionInfo = getSubscriptionInfo(i);
                if (subscriptionInfo != null && subscriptionInfo.isEmbedded()) {
                    if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.euicc.mep") && i2 != 3 && i2 != 2) {
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return 1;
                    }
                    log("Updating embedded sub nickname on cardId: " + subscriptionInfo.getCardId());
                    this.mEuiccManager.updateSubscriptionNickname(i, str, PendingIntent.getService(this.mContext, 0, new Intent(), 67108864));
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return 1;
            }
            log("No need to update the display name. nameSource=" + SubscriptionManager.displayNameSourceToString(i2) + ", existing name=" + subscriptionInfoInternal.getDisplayName() + ", source=" + SubscriptionManager.displayNameSourceToString(subscriptionInfoInternal.getDisplayNameSource()));
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return 0;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setDisplayNumber(String str, int i) {
        enforcePermissions("setDisplayNumber", "android.permission.MODIFY_PHONE_STATE");
        logl("setDisplayNumber: subId=" + i + ", number=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + ", calling package=" + getCallingPackage());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSubscriptionDatabaseManager.setNumber(i, str);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setDataRoaming(int i, int i2) {
        enforcePermissions("setDataRoaming", "android.permission.MODIFY_PHONE_STATE");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i < 0) {
                throw new IllegalArgumentException("Invalid roaming value " + i);
            }
            this.mSubscriptionDatabaseManager.setDataRoaming(i2, i);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return 1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setOpportunistic(boolean z, int i, String str) {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mContext, i, Binder.getCallingUid(), true, "setOpportunistic", new String[]{"android.permission.MODIFY_PHONE_STATE"});
        enforceTelephonyFeatureWithException(str, "setOpportunistic");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSubscriptionDatabaseManager.setOpportunistic(i, z);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public ParcelUuid createSubscriptionGroup(int[] iArr, String str) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        Objects.requireNonNull(iArr, "createSubscriptionGroup");
        if (iArr.length == 0) {
            Inet4AddressUtils$$ExternalSyntheticBUOutline0.m("Invalid subIdList ", Arrays.toString(iArr));
            return null;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0 && !checkCarrierPrivilegeOnSubList(iArr, str)) {
            SmsController$$ExternalSyntheticBUOutline0.m("CreateSubscriptionGroup needs MODIFY_PHONE_STATE or carrier privilege permission on all specified subscriptions");
            return null;
        }
        enforceTelephonyFeatureWithException(str, "createSubscriptionGroup");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ParcelUuid parcelUuid = new ParcelUuid(UUID.randomUUID());
            String string = parcelUuid.toString();
            for (int i : iArr) {
                this.mSubscriptionDatabaseManager.setGroupUuid(i, string);
                this.mSubscriptionDatabaseManager.setGroupOwner(i, str);
            }
            updateGroupDisabled();
            MultiSimSettingController.getInstance().notifySubscriptionGroupChanged(parcelUuid);
            return parcelUuid;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setPreferredDataSubscriptionId(int i, boolean z, ISetOpportunisticDataCallback iSetOpportunisticDataCallback) {
        enforcePermissions("setPreferredDataSubscriptionId", "android.permission.MODIFY_PHONE_STATE");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setPreferredDataSubscriptionId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
            if (phoneSwitcher == null) {
                loge("Set preferred data sub: phoneSwitcher is null.");
                if (iSetOpportunisticDataCallback != null) {
                    try {
                        iSetOpportunisticDataCallback.onComplete(4);
                    } catch (RemoteException e) {
                        loge("RemoteException " + e);
                    }
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return;
            }
            phoneSwitcher.trySetOpportunisticDataSubscription(i, z, iSetOpportunisticDataCallback);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getPreferredDataSubscriptionId() {
        enforcePermissions("getPreferredDataSubscriptionId", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
            if (phoneSwitcher == null) {
                loge("getPreferredDataSubscriptionId: PhoneSwitcher not available. Return the default data sub " + getDefaultDataSubId());
                return getDefaultDataSubId();
            }
            return phoneSwitcher.getAutoSelectedDataSubId();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List<SubscriptionInfo> getOpportunisticSubscriptions(final String str, final String str2) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Binder.getCallingPid(), Binder.getCallingUid(), str, str2, "getOpportunisticSubscriptions")) {
            loge("getOpportunisticSubscriptions: " + str + " does not have enough permission. Returning empty list here.");
            return Collections.EMPTY_LIST;
        }
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "getOpportunisticSubscriptions");
        }
        return (List) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda30
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getOpportunisticSubscriptions$23(str, str2, (SubscriptionInfoInternal) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda31
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getOpportunisticSubscriptions$24(str, str2, (SubscriptionInfoInternal) obj);
            }
        }).sorted(Comparator.comparing(new SubscriptionManagerService$$ExternalSyntheticLambda32()).thenComparing(new SubscriptionManagerService$$ExternalSyntheticLambda33())).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getOpportunisticSubscriptions$23(String str, String str2, SubscriptionInfoInternal subscriptionInfoInternal) {
        return subscriptionInfoInternal.isOpportunistic() && TelephonyPermissions.checkCallingOrSelfReadPhoneStateNoThrow(this.mContext, subscriptionInfoInternal.getSubscriptionId(), str, str2, "getOpportunisticSubscriptions");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SubscriptionInfo lambda$getOpportunisticSubscriptions$24(String str, String str2, SubscriptionInfoInternal subscriptionInfoInternal) {
        return conditionallyRemoveIdentifiers(subscriptionInfoInternal.toSubscriptionInfo(), str, str2, "getOpportunisticSubscriptions");
    }

    public void removeSubscriptionsFromGroup(int[] iArr, ParcelUuid parcelUuid, String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0 && (!checkCarrierPrivilegeOnSubList(iArr, str) || !canPackageManageGroup(parcelUuid, str))) {
            SmsController$$ExternalSyntheticBUOutline0.m("removeSubscriptionsFromGroup needs MODIFY_PHONE_STATE or carrier privilege permission on all specified subscriptions.");
            return;
        }
        Objects.requireNonNull(iArr);
        Objects.requireNonNull(parcelUuid);
        if (iArr.length == 0) {
            Phone$$ExternalSyntheticBUOutline0.m("subIdList is empty.");
            return;
        }
        enforceTelephonyFeatureWithException(str, "removeSubscriptionsFromGroup");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (int i : iArr) {
                SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
                if (subscriptionInfoInternal == null) {
                    throw new IllegalArgumentException("The provided sub id " + i + " is not valid.");
                }
                if (!parcelUuid.toString().equals(subscriptionInfoInternal.getGroupUuid())) {
                    throw new IllegalArgumentException("Subscription " + subscriptionInfoInternal.getSubscriptionId() + " doesn't belong to group " + parcelUuid);
                }
            }
            for (final SubscriptionInfoInternal subscriptionInfoInternal2 : this.mSubscriptionDatabaseManager.getAllSubscriptions()) {
                if (IntStream.of(iArr).anyMatch(new IntPredicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda48
                    @Override // java.util.function.IntPredicate
                    public final boolean test(int i2) {
                        return SubscriptionManagerService.m1905$r8$lambda$4VeHrGHHmvsZ6BoANuqfjzO84(subscriptionInfoInternal2, i2);
                    }
                })) {
                    this.mSubscriptionDatabaseManager.setGroupUuid(subscriptionInfoInternal2.getSubscriptionId(), "");
                    this.mSubscriptionDatabaseManager.setGroupOwner(subscriptionInfoInternal2.getSubscriptionId(), "");
                } else if (subscriptionInfoInternal2.getGroupUuid().equals(parcelUuid.toString())) {
                    this.mSubscriptionDatabaseManager.setGroupOwner(subscriptionInfoInternal2.getSubscriptionId(), str);
                }
            }
            updateGroupDisabled();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$4VeHrGHHmvsZ6B-oANuqf-jzO84, reason: not valid java name */
    public static /* synthetic */ boolean m1905$r8$lambda$4VeHrGHHmvsZ6BoANuqfjzO84(SubscriptionInfoInternal subscriptionInfoInternal, int i) {
        return i == subscriptionInfoInternal.getSubscriptionId();
    }

    public void addSubscriptionsIntoGroup(int[] iArr, ParcelUuid parcelUuid, String str) {
        Objects.requireNonNull(iArr, "subIdList");
        if (iArr.length == 0) {
            Phone$$ExternalSyntheticBUOutline0.m("Invalid subId list");
            return;
        }
        Objects.requireNonNull(parcelUuid, "groupUuid");
        String string = parcelUuid.toString();
        if (string.equals("00000000-0000-0000-0000-000000000000")) {
            Phone$$ExternalSyntheticBUOutline0.m("Invalid groupUuid");
            return;
        }
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0 && (!checkCarrierPrivilegeOnSubList(iArr, str) || !canPackageManageGroup(parcelUuid, str))) {
            SmsController$$ExternalSyntheticBUOutline0.m("Requires MODIFY_PHONE_STATE or carrier privilege permissions on subscriptions and the group.");
            return;
        }
        enforceTelephonyFeatureWithException(str, "addSubscriptionsIntoGroup");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (int i : iArr) {
                this.mSubscriptionDatabaseManager.setGroupUuid(i, string);
                this.mSubscriptionDatabaseManager.setGroupOwner(i, str);
            }
            updateGroupDisabled();
            MultiSimSettingController.getInstance().notifySubscriptionGroupChanged(parcelUuid);
            logl("addSubscriptionsIntoGroup: add subs " + Arrays.toString(iArr) + " to the group.");
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List<SubscriptionInfo> getSubscriptionsInGroup(final ParcelUuid parcelUuid, final String str, final String str2) {
        if (CompatChanges.isChangeEnabled(REQUIRE_DEVICE_IDENTIFIERS_FOR_GROUP_UUID, Binder.getCallingUid())) {
            try {
                if (!TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mContext, str, str2, "getSubscriptionsInGroup")) {
                    EventLog.writeEvent(1397638484, "213902861", Integer.valueOf(Binder.getCallingUid()));
                    throw new SecurityException("Need to have carrier privileges or access to device identifiers to call getSubscriptionsInGroup");
                }
            } catch (SecurityException e) {
                EventLog.writeEvent(1397638484, "213902861", Integer.valueOf(Binder.getCallingUid()));
                throw e;
            }
        }
        enforceTelephonyFeatureWithException(str, "getSubscriptionsInGroup");
        return (List) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().map(new SubscriptionManagerService$$ExternalSyntheticLambda3()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda49
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getSubscriptionsInGroup$26(parcelUuid, str, str2, (SubscriptionInfo) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda50
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getSubscriptionsInGroup$27(str, str2, (SubscriptionInfo) obj);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSubscriptionsInGroup$26(ParcelUuid parcelUuid, String str, String str2, SubscriptionInfo subscriptionInfo) {
        if (parcelUuid.equals(subscriptionInfo.getGroupUuid())) {
            return canManageSubscription(subscriptionInfo, str) || TelephonyPermissions.checkCallingOrSelfReadPhoneStateNoThrow(this.mContext, subscriptionInfo.getSubscriptionId(), str, str2, "getSubscriptionsInGroup");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SubscriptionInfo lambda$getSubscriptionsInGroup$27(String str, String str2, SubscriptionInfo subscriptionInfo) {
        return conditionallyRemoveIdentifiers(subscriptionInfo, str, str2, "getSubscriptionsInGroup");
    }

    public int getSlotIndex(int i) {
        if (i == Integer.MAX_VALUE) {
            i = getDefaultSubId();
        }
        if (this.mRemoteSubIds.contains(Integer.valueOf(i))) {
            return -2;
        }
        Iterator it = this.mSlotIndexToSubId.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (((Integer) entry.getValue()).intValue() == i) {
                return ((Integer) entry.getKey()).intValue();
            }
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getSubId(int i) {
        if (i == Integer.MAX_VALUE) {
            i = getSlotIndex(getDefaultSubId());
        }
        if (i == -2) {
            if (this.mRemoteSubIds.isEmpty()) {
                return -1;
            }
            return ((Integer) this.mRemoteSubIds.getLargest()).intValue();
        }
        if (SubscriptionManager.isValidSlotIndex(i)) {
            return ((Integer) this.mSlotIndexToSubId.getOrDefault(Integer.valueOf(i), -1)).intValue();
        }
        return -1;
    }

    public void updateDefaultSubId() {
        int defaultDataSubId;
        if (this.mTelephonyManager.isVoiceCapable()) {
            defaultDataSubId = getDefaultVoiceSubId();
        } else {
            defaultDataSubId = getDefaultDataSubId();
        }
        if (!this.mSlotIndexToSubId.containsValue(Integer.valueOf(defaultDataSubId)) && !this.mRemoteSubIds.contains(Integer.valueOf(defaultDataSubId))) {
            int[] activeLocalSubIdList = getActiveLocalSubIdList();
            int[] activeRemoteSubIdList = getActiveRemoteSubIdList();
            if (activeLocalSubIdList.length > 0) {
                defaultDataSubId = activeLocalSubIdList[0];
                log("updateDefaultSubId: First available active sub = " + defaultDataSubId + ", type = local");
            } else if (activeRemoteSubIdList.length > 0) {
                defaultDataSubId = activeRemoteSubIdList[0];
                log("updateDefaultSubId: First available active sub = " + defaultDataSubId + ", type = remote");
            } else {
                defaultDataSubId = -1;
            }
        }
        if (this.mDefaultSubId.get() != defaultDataSubId) {
            int phoneId = getPhoneId(defaultDataSubId);
            logl("updateDefaultSubId: Default sub id updated from " + this.mDefaultSubId.get() + " to " + defaultDataSubId + ", phoneId=" + phoneId);
            this.mDefaultSubId.set(defaultDataSubId);
            MccTable.updateMccMncConfiguration(this.mContext, this.mTelephonyManager.getSimOperatorNumeric(defaultDataSubId));
            Intent intent = new Intent("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
            intent.addFlags(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phoneId, defaultDataSubId);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    public int getDefaultSubId() {
        return getDefaultSubIdAsUser(BINDER_WRAPPER.getCallingUserHandle().getIdentifier());
    }

    public int getDefaultSubIdAsUser(int i) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "getDefaultSubIdAsUser");
        return getDefaultAsUser(i, this.mDefaultSubId.get());
    }

    public int getPhoneId(int i) {
        if (i == Integer.MAX_VALUE) {
            i = getDefaultSubId();
        }
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return -1;
        }
        int slotIndex = getSlotIndex(i);
        if (SubscriptionManager.isValidSlotIndex(slotIndex)) {
            return slotIndex;
        }
        return Integer.MAX_VALUE;
    }

    public int getDefaultDataSubId() {
        return this.mDefaultDataSubId.get();
    }

    public void setDefaultDataSubId(final int i) {
        enforcePermissions("setDefaultDataSubId", "android.permission.MODIFY_PHONE_STATE");
        if (i == Integer.MAX_VALUE) {
            Phone$$ExternalSyntheticBUOutline1.m("setDefaultDataSubId called with DEFAULT_SUBSCRIPTION_ID");
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setDefaultDataSubId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mDefaultDataSubId.set(i)) {
                if (isFlexMapSupportNeeded()) {
                    remapRafIfApplicable();
                }
                MultiSimSettingController.getInstance().notifyDefaultDataSubChanged();
                broadcastSubId("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED", i);
                this.mSubscriptionManagerServiceCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda38
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        SubscriptionManagerService.SubscriptionManagerServiceCallback subscriptionManagerServiceCallback = (SubscriptionManagerService.SubscriptionManagerServiceCallback) obj;
                        subscriptionManagerServiceCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda53
                            @Override // java.lang.Runnable
                            public final void run() {
                                subscriptionManagerServiceCallback.onDefaultDataSubscriptionChanged(i);
                            }
                        });
                    }
                });
                updateDefaultSubId();
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void remapRafIfApplicable() {
        if (this.mSlotIndexToSubId.containsValue(Integer.valueOf(getDefaultDataSubId()))) {
            ProxyController proxyController = ProxyController.getInstance();
            int activeModemCount = this.mTelephonyManager.getActiveModemCount();
            RadioAccessFamily[] radioAccessFamilyArr = new RadioAccessFamily[activeModemCount];
            for (int i = 0; i < activeModemCount; i++) {
                radioAccessFamilyArr[i] = new RadioAccessFamily(i, ((Integer) this.mSlotIndexToSubId.getOrDefault(Integer.valueOf(i), -1)).intValue() == getDefaultDataSubId() ? proxyController.getMaxRafSupported() : proxyController.getMinRafSupported());
            }
            proxyController.setRadioCapability(radioAccessFamilyArr);
        }
    }

    public int getDefaultVoiceSubId() {
        return getDefaultVoiceSubIdAsUser(BINDER_WRAPPER.getCallingUserHandle().getIdentifier());
    }

    public int getDefaultVoiceSubIdAsUser(int i) {
        return getDefaultAsUser(i, this.mDefaultVoiceSubId.get());
    }

    public void setDefaultVoiceSubId(int i) {
        enforcePermissions("setDefaultVoiceSubId", "android.permission.MODIFY_PHONE_STATE");
        if (i == Integer.MAX_VALUE) {
            Phone$$ExternalSyntheticBUOutline1.m("setDefaultVoiceSubId called with DEFAULT_SUB_ID");
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setDefaultVoiceSubId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mDefaultVoiceSubId.set(i)) {
                broadcastSubId("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED", i);
                PhoneAccountHandle phoneAccountHandleForSubscriptionId = i == -1 ? null : this.mTelephonyManager.getPhoneAccountHandleForSubscriptionId(i);
                TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
                if (telecomManager != null) {
                    telecomManager.setUserSelectedOutgoingPhoneAccount(phoneAccountHandleForSubscriptionId);
                }
                updateDefaultSubId();
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getDefaultSmsSubId() {
        return getDefaultSmsSubIdAsUser(BINDER_WRAPPER.getCallingUserHandle().getIdentifier());
    }

    public int getDefaultSmsSubIdAsUser(int i) {
        return getDefaultAsUser(i, this.mDefaultSmsSubId.get());
    }

    public void setDefaultSmsSubId(int i) {
        enforcePermissions("setDefaultSmsSubId", "android.permission.MODIFY_PHONE_STATE");
        if (i == Integer.MAX_VALUE) {
            Phone$$ExternalSyntheticBUOutline1.m("setDefaultSmsSubId called with DEFAULT_SUB_ID");
            return;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setDefaultSmsSubId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mDefaultSmsSubId.set(i)) {
                broadcastSubId("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED", i);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void broadcastSubId(String str, int i) {
        Intent intent = new Intent(str);
        intent.addFlags(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
        SubscriptionManager.putSubscriptionIdExtra(intent, i);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        log("broadcastSubId action: " + str + " subId= " + i);
    }

    public int[] getActiveSubIdList(boolean z) {
        enforcePermissions("getActiveSubIdList", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "getActiveSubIdList");
        }
        return getActiveSubIdListAsUser(z, UserHandle.ALL);
    }

    private int[] getActiveLocalSubIdList() {
        return filterSubIdStreamVisibilityAsUser(this.mSlotIndexToSubId.values().stream(), true, UserHandle.ALL);
    }

    private int[] getActiveRemoteSubIdList() {
        return filterSubIdStreamVisibilityAsUser(this.mRemoteSubIds.stream(), true, UserHandle.ALL);
    }

    private int[] filterSubIdStreamVisibilityAsUser(Stream stream, final boolean z, final UserHandle userHandle) {
        return stream.filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda27
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$filterSubIdStreamVisibilityAsUser$30(z, userHandle, (Integer) obj);
            }
        }).mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda28
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((Integer) obj).intValue();
            }
        }).toArray();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$filterSubIdStreamVisibilityAsUser$30(boolean z, UserHandle userHandle, Integer num) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(num.intValue());
        if (subscriptionInfoInternal != null) {
            return (!z || subscriptionInfoInternal.isVisible()) && isSubscriptionAssociatedWithUserInternal(subscriptionInfoInternal, userHandle.getIdentifier());
        }
        return false;
    }

    private int[] getActiveSubIdListAsUser(boolean z, UserHandle userHandle) {
        return filterSubIdStreamVisibilityAsUser(Stream.concat(this.mSlotIndexToSubId.values().stream(), this.mRemoteSubIds.stream()), z, userHandle);
    }

    public void setSubscriptionProperty(int i, String str, String str2) {
        enforcePermissions("setSubscriptionProperty", "android.permission.MODIFY_PHONE_STATE");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            logl("setSubscriptionProperty: subId=" + i + ", columnName=" + str + ", value=" + str2 + ", calling package=" + getCallingPackage());
            onCheckSetSubscriptionPropertyColumnName(str);
            this.mSubscriptionDatabaseManager.setSubscriptionProperty(i, str, str2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getSubscriptionProperty(int i, String str, String str2, String str3) {
        Objects.requireNonNull(str);
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str2, str3, "getSubscriptionProperty")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return null;
        }
        onCheckGetSubscriptionPropertyColumnName(str);
        enforceTelephonyFeatureWithException(str2, "getSubscriptionProperty");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Object subscriptionProperty = this.mSubscriptionDatabaseManager.getSubscriptionProperty(i, str);
                if (!(subscriptionProperty instanceof Integer) && !(subscriptionProperty instanceof Long)) {
                    if (subscriptionProperty instanceof String) {
                        String str4 = (String) subscriptionProperty;
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return str4;
                    }
                    if (subscriptionProperty instanceof byte[]) {
                        String strEncodeToString = Base64.encodeToString((byte[]) subscriptionProperty, 0);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return strEncodeToString;
                    }
                    throw new RuntimeException("Unexpected type " + subscriptionProperty.getClass().getTypeName() + " was returned from SubscriptionDatabaseManager for column " + str);
                }
                String strValueOf = String.valueOf(subscriptionProperty);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return strValueOf;
            } catch (IllegalArgumentException unused) {
                loge("getSubscriptionProperty: Invalid subId " + i + ", columnName=" + str);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return null;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isSubscriptionEnabled(int i) {
        enforcePermissions("isSubscriptionEnabled", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        boolean z = false;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid subscription id ", i);
            return false;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "isSubscriptionEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal != null && subscriptionInfoInternal.isActive()) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getEnabledSubscriptionId(final int i) {
        enforcePermissions("getEnabledSubscriptionId", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid slot index ", i);
            return 0;
        }
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "getEnabledSubscriptionId");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return SubscriptionManagerService.$r8$lambda$PfplkF1FdSeRMKPXHpoUcqh9Wxo(i, (SubscriptionInfoInternal) obj);
                }
            }).mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((SubscriptionInfoInternal) obj).getSubscriptionId();
                }
            }).findFirst().orElse(-1);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static /* synthetic */ boolean $r8$lambda$PfplkF1FdSeRMKPXHpoUcqh9Wxo(int i, SubscriptionInfoInternal subscriptionInfoInternal) {
        return subscriptionInfoInternal.isActive() && subscriptionInfoInternal.getSimSlotIndex() == i;
    }

    public boolean isActiveSubId(int i, String str, String str2) {
        boolean z = false;
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str, str2, "isActiveSubId")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return false;
        }
        if (!this.mContext.getResources().getBoolean(R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled)) {
            enforceTelephonyFeatureWithException(str, "isActiveSubId");
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal != null && subscriptionInfoInternal.isActive()) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getActiveDataSubscriptionId() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
            if (phoneSwitcher != null) {
                int activeDataSubId = phoneSwitcher.getActiveDataSubId();
                if (SubscriptionManager.isUsableSubscriptionId(activeDataSubId)) {
                    return activeDataSubId;
                }
            }
            return getDefaultDataSubId();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean canDisablePhysicalSubscription() {
        enforcePermissions("canDisablePhysicalSubscription", "android.permission.READ_PRIVILEGED_PHONE_STATE");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "canDisablePhysicalSubscription");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone defaultPhone = PhoneFactory.getDefaultPhone();
            return defaultPhone != null && defaultPhone.canDisablePhysicalSubscription();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setUiccApplicationsEnabled(boolean z, final int i) {
        enforcePermissions("setUiccApplicationsEnabled", "android.permission.MODIFY_PHONE_STATE");
        logl("setUiccApplicationsEnabled: subId=" + i + ", enabled=" + z + ", calling package=" + getCallingPackage());
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setUiccApplicationsEnabled");
        MiuiSubscriptionControllerStub.get().setUiccApplicationsEnabled(this.mContext, z, i);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal == null) {
                throw new IllegalArgumentException("setUiccApplicationsEnabled: Subscription doesn't exist. subId=" + i);
            }
            if (subscriptionInfoInternal.areUiccApplicationsEnabled() != z) {
                this.mSubscriptionDatabaseManager.setUiccApplicationsEnabled(i, z);
                this.mSubscriptionManagerServiceCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda43
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        SubscriptionManagerService.SubscriptionManagerServiceCallback subscriptionManagerServiceCallback = (SubscriptionManagerService.SubscriptionManagerServiceCallback) obj;
                        subscriptionManagerServiceCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda58
                            @Override // java.lang.Runnable
                            public final void run() {
                                subscriptionManagerServiceCallback.onUiccApplicationsEnabledChanged(i);
                            }
                        });
                    }
                });
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setDeviceToDeviceStatusSharing(int i, int i2) {
        enforcePermissions("setDeviceToDeviceStatusSharing", "android.permission.MODIFY_PHONE_STATE");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setDeviceToDeviceStatusSharing");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i < 0 || i > 3) {
                throw new IllegalArgumentException("invalid sharing " + i);
            }
            this.mSubscriptionDatabaseManager.setDeviceToDeviceStatusSharingPreference(i2, i);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return 1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setDeviceToDeviceStatusSharingContacts(String str, int i) {
        enforcePermissions("setDeviceToDeviceStatusSharingContacts", "android.permission.MODIFY_PHONE_STATE");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "setDeviceToDeviceStatusSharingContacts");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Objects.requireNonNull(str, "contacts");
            this.mSubscriptionDatabaseManager.setDeviceToDeviceStatusSharingContacts(i, str);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getPhoneNumber(int i, int i2, String str, String str2) {
        if (i2 == 4 && !TelephonyPermissions.isSystemOrPhone(BINDER_WRAPPER.getCallingUid()) && !TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, i)) {
            SmsController$$ExternalSyntheticBUOutline0.m("getPhoneNumber(TS43) is restricted to privileged system components or carrier privileged apps.");
            return null;
        }
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mContext, i, Binder.getCallingUid(), "getPhoneNumber", new String[]{"android.permission.READ_PHONE_NUMBERS", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        enforceTelephonyFeatureWithException(str, "getPhoneNumber");
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(this.mContext, i, str, str2, "getPhoneNumber")) {
            return "";
        }
        checkPhoneNumberSource(i2);
        int iCheckAndGetSubId = checkAndGetSubId(i);
        if (iCheckAndGetSubId == -1) {
            return "";
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhoneNumberFromSourceInternal(iCheckAndGetSubId, i2, true);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private int checkAndGetSubId(int i) {
        if (i != -1) {
            if (i == Integer.MAX_VALUE) {
                return getDefaultSubId();
            }
            if (!SubscriptionManager.isValidSubscriptionId(i)) {
                RILUtils$$ExternalSyntheticBUOutline0.m("Invalid SubId=", i);
                return 0;
            }
        }
        return i;
    }

    private void checkPhoneNumberSource(int i) {
        if (i == 1 || i == 2 || i == 3 || i == 4) {
            return;
        }
        RILUtils$$ExternalSyntheticBUOutline0.m("Invalid number source ", i);
    }

    private String getPhoneNumberFromSourceInternal(int i, int i2, boolean z) {
        TelephonyManager telephonyManagerCreateForSubscriptionId;
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            loge("No SubscriptionInfo found for subId=" + i);
            return "";
        }
        if (i2 == 1) {
            Phone phone = PhoneFactory.getPhone(getSlotIndex(i));
            if (phone != null) {
                return TextUtils.emptyIfNull(phone.getLine1Number());
            }
            return subscriptionInfoInternal.getNumber();
        }
        if (i2 == 2) {
            return subscriptionInfoInternal.getNumberFromCarrier();
        }
        if (i2 == 3) {
            return (!z || ((telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i)) != null && telephonyManagerCreateForSubscriptionId.isImsRegistered() && ((Boolean) this.mImsNumberUpdateStatus.getOrDefault(Integer.valueOf(i), Boolean.FALSE)).booleanValue())) ? subscriptionInfoInternal.getNumberFromIms() : "";
        }
        if (i2 == 4) {
            return subscriptionInfoInternal.getNumberFromTs43();
        }
        loge("No SubscriptionInfo found for subId=" + i);
        return "";
    }

    public String getPhoneNumberFromFirstAvailableSource(int i, String str, String str2) {
        int iCheckAndGetSubId;
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mContext, i, Binder.getCallingUid(), "getPhoneNumberFromFirstAvailableSource", new String[]{"android.permission.READ_PHONE_NUMBERS", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        int callingUid = BINDER_WRAPPER.getCallingUid();
        enforceTelephonyFeatureWithException(str, "getPhoneNumberFromFirstAvailableSource");
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(this.mContext, i, str, str2, "getPhoneNumberFromFirstAvailableSource") || (iCheckAndGetSubId = checkAndGetSubId(i)) == -1) {
            return "";
        }
        boolean zCheckCarrierPrivilegeForSubId = TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, iCheckAndGetSubId);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String phoneNumberFromSourceInternal = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 2, false);
            if (!TextUtils.isEmpty(phoneNumberFromSourceInternal)) {
                return phoneNumberFromSourceInternal;
            }
            String phoneNumberFromSourceInternal2 = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 1, false);
            if (!TextUtils.isEmpty(phoneNumberFromSourceInternal2)) {
                return phoneNumberFromSourceInternal2;
            }
            if (TelephonyPermissions.isSystemOrPhone(callingUid) || zCheckCarrierPrivilegeForSubId) {
                String phoneNumberFromSourceInternal3 = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 4, false);
                if (!TextUtils.isEmpty(phoneNumberFromSourceInternal3)) {
                    return phoneNumberFromSourceInternal3;
                }
            }
            return TextUtils.emptyIfNull(getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 3, true));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getLastKnownPhoneNumberFromFirstAvailableSource(int i, String str, String str2) {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mContext, i, Binder.getCallingUid(), "getLastKnownPhoneNumberFromFirstAvailableSource", new String[]{"android.permission.READ_PHONE_NUMBERS", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        int callingUid = BINDER_WRAPPER.getCallingUid();
        enforceTelephonyFeatureWithException(str, "getLastKnownPhoneNumberFromFirstAvailableSource");
        int iCheckAndGetSubId = checkAndGetSubId(i);
        if (iCheckAndGetSubId == -1) {
            return "";
        }
        boolean zCheckCarrierPrivilegeForSubId = TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, iCheckAndGetSubId);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String phoneNumberFromSourceInternal = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 2, false);
            if (!TextUtils.isEmpty(phoneNumberFromSourceInternal)) {
                return phoneNumberFromSourceInternal;
            }
            String phoneNumberFromSourceInternal2 = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 1, false);
            if (!TextUtils.isEmpty(phoneNumberFromSourceInternal2)) {
                return phoneNumberFromSourceInternal2;
            }
            if (TelephonyPermissions.isSystemOrPhone(callingUid) || zCheckCarrierPrivilegeForSubId) {
                String phoneNumberFromSourceInternal3 = getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 4, false);
                if (!TextUtils.isEmpty(phoneNumberFromSourceInternal3)) {
                    return phoneNumberFromSourceInternal3;
                }
            }
            return TextUtils.emptyIfNull(getPhoneNumberFromSourceInternal(iCheckAndGetSubId, 3, false));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setImsNumberUpdateStatus(int i, boolean z) {
        Boolean bool = (Boolean) this.mImsNumberUpdateStatus.put(Integer.valueOf(i), Boolean.valueOf(z));
        if (bool == null || bool.booleanValue() != z) {
            log("setImsNumberUpdateStatus: subId=" + i + " status changed to " + z + ". notifying.");
            notifySubscriptionChanged(i);
        }
    }

    public void clearImsNumberUpdateStatus(int i) {
        if (this.mImsNumberUpdateStatus.remove(Integer.valueOf(i)) != null) {
            log("clearImsNumberUpdateStatus: subId=" + i + ". notifying.");
            notifySubscriptionChanged(i);
        }
    }

    public void setPhoneNumber(int i, int i2, String str, String str2, String str3) {
        logl("setPhoneNumber: subId=" + i + ", source=" + i2 + ", number=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + ", calling package=" + str2);
        if (i2 == 2 && !TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, i)) {
            SmsController$$ExternalSyntheticBUOutline0.m("setPhoneNumber for CARRIER needs carrier privilege.");
            return;
        }
        if (i2 == 4) {
            enforcePermissions("setPhoneNumber", "android.permission.MODIFY_PHONE_STATE");
        }
        if (i2 != 2 && i2 != 4) {
            Inet4AddressUtils$$ExternalSyntheticBUOutline0.m("setPhoneNumber doesn't accept source ", SubscriptionManager.phoneNumberSourceToString(i2));
            return;
        }
        enforceTelephonyFeatureWithException(str2, "setPhoneNumber");
        Objects.requireNonNull(str, IccProvider.STR_NUMBER);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i2 == 2) {
                this.mSubscriptionDatabaseManager.setNumberFromCarrier(i, str);
            } else {
                if (i2 == 4) {
                    this.mSubscriptionDatabaseManager.setNumberFromTs43(i, str);
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int setUsageSetting(int i, int i2, String str) {
        TelephonyPermissions.enforceAnyPermissionGrantedOrCarrierPrivileges(this.mContext, Binder.getCallingUid(), i2, true, "setUsageSetting", new String[]{"android.permission.MODIFY_PHONE_STATE"});
        if (i < 0 || i > 2) {
            RILUtils$$ExternalSyntheticBUOutline0.m("setUsageSetting: Invalid usage setting: ", i);
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSubscriptionDatabaseManager.setUsageSetting(i2, i);
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int setSubscriptionUserHandle(UserHandle userHandle, int i) {
        enforcePermissions("setSubscriptionUserHandle", "android.permission.MANAGE_SUBSCRIPTION_USER_ASSOCIATION");
        if (userHandle == null) {
            userHandle = UserHandle.of(-10000);
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSubscriptionDatabaseManager.setUserId(i, userHandle.getIdentifier());
            return 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public UserHandle getSubscriptionUserHandle(int i) {
        enforcePermissions("getSubscriptionUserHandle", "android.permission.MANAGE_SUBSCRIPTION_USER_ASSOCIATION");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
            if (subscriptionInfoInternal != null) {
                UserHandle userHandleOf = UserHandle.of(subscriptionInfoInternal.getUserId());
                if (userHandleOf.getIdentifier() == -10000) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return null;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return userHandleOf;
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return null;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isSubscriptionAssociatedWithCallingUser(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str, str2, "isSubscriptionAssociatedWithCallingUser")) {
            SmsController$$ExternalSyntheticBUOutline0.m("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
            return false;
        }
        return isSubscriptionAssociatedWithUserNoCheck(i, UserHandle.of(UserHandle.getCallingUserId()));
    }

    public boolean isSubscriptionAssociatedWithUser(int i, UserHandle userHandle) {
        enforcePermissions("isSubscriptionAssociatedWithUser", "android.permission.MANAGE_SUBSCRIPTION_USER_ASSOCIATION");
        return isSubscriptionAssociatedWithUserNoCheck(i, userHandle);
    }

    private boolean isSubscriptionAssociatedWithUserNoCheck(int i, UserHandle userHandle) {
        if (this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i) == null) {
            RILUtils$$ExternalSyntheticBUOutline0.m("[isSubscriptionAssociatedWithUser]: Subscription doesn't exist: ", i);
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Iterator<SubscriptionInfo> it = getSubscriptionInfoListAssociatedWithUser(userHandle).iterator();
            while (it.hasNext()) {
                if (it.next().getSubscriptionId() == i) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public List<SubscriptionInfo> getSubscriptionInfoListAssociatedWithUser(UserHandle userHandle) {
        enforcePermissions("getSubscriptionInfoListAssociatedWithUser", "android.permission.MANAGE_SUBSCRIPTION_USER_ASSOCIATION");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            List<SubscriptionInfoInternal> allSubscriptions = this.mSubscriptionDatabaseManager.getAllSubscriptions();
            if (allSubscriptions.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return arrayList;
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (SubscriptionInfoInternal subscriptionInfoInternal : allSubscriptions) {
                if (subscriptionInfoInternal.getUserId() == userHandle.getIdentifier()) {
                    arrayList2.add(subscriptionInfoInternal.toSubscriptionInfo());
                } else if (subscriptionInfoInternal.getUserId() == -10000) {
                    arrayList3.add(subscriptionInfoInternal.toSubscriptionInfo());
                }
            }
            UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            if (userManager == null || !userManager.isManagedProfile(userHandle.getIdentifier())) {
                if (arrayList2.isEmpty()) {
                    arrayList2 = arrayList3;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return arrayList2;
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return arrayList2;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    private Stream getSubscriptionInfoStreamAsUser(final UserHandle userHandle) {
        return this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda62
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getSubscriptionInfoStreamAsUser$35(userHandle, (SubscriptionInfoInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSubscriptionInfoStreamAsUser$35(UserHandle userHandle, SubscriptionInfoInternal subscriptionInfoInternal) {
        return isSubscriptionAssociatedWithUserInternal(subscriptionInfoInternal, userHandle.getIdentifier());
    }

    public void restoreAllSimSpecificSettingsFromBackup(byte[] bArr) {
        enforcePermissions("restoreAllSimSpecificSettingsFromBackup", "android.permission.MODIFY_PHONE_STATE");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "restoreAllSimSpecificSettingsFromBackup");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Bundle bundle = new Bundle();
            bundle.putByteArray("KEY_SIM_SPECIFIC_SETTINGS_DATA", bArr);
            logl("restoreAllSimSpecificSettingsFromBackup");
            Bundle bundleCall = this.mContext.getContentResolver().call(SubscriptionManager.SIM_INFO_BACKUP_AND_RESTORE_CONTENT_URI, "restoreSimSpecificSettings", (String) null, bundle);
            if (bundleCall != null && bundleCall.getBoolean("restoreSimSpecificSettingsDatabaseUpdated")) {
                logl("Sim specific settings changed the database.");
                this.mSubscriptionDatabaseManager.reloadDatabaseSync();
                Arrays.stream(PhoneFactory.getPhones()).forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((Phone) obj).loadAllowedNetworksFromSubscriptionDatabase();
                    }
                });
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerCallback(SubscriptionManagerServiceCallback subscriptionManagerServiceCallback) {
        this.mSubscriptionManagerServiceCallbacks.add(subscriptionManagerServiceCallback);
    }

    public void unregisterCallback(SubscriptionManagerServiceCallback subscriptionManagerServiceCallback) {
        this.mSubscriptionManagerServiceCallbacks.remove(subscriptionManagerServiceCallback);
    }

    private void enforcePermissions(String str, String... strArr) {
        if (hasPermissions(strArr)) {
            return;
        }
        throw new SecurityException(str + ". Does not have any of the following permissions. " + Arrays.toString(strArr));
    }

    private boolean hasPermissions(String... strArr) {
        for (String str : strArr) {
            if (this.mContext.checkCallingOrSelfPermission(str) == 0) {
                return true;
            }
        }
        return false;
    }

    public SubscriptionInfoInternal getSubscriptionInfoInternal(int i) {
        return this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
    }

    public SubscriptionInfo getSubscriptionInfo(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal != null) {
            return subscriptionInfoInternal.toSubscriptionInfo();
        }
        return null;
    }

    public void updateSimStateForInactivePort(final int i, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSimStateForInactivePort$36(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$updateSimStateForInactivePort$36(int i, String str) {
        int iInsertSubscriptionInfo;
        logl("updateSimStateForInactivePort: slotIndex=" + i + ", iccId=" + SubscriptionInfo.getPrintableId(str));
        if (this.mSlotIndexToSubId.containsKey(Integer.valueOf(i))) {
            this.mSubscriptionDatabaseManager.setUiccApplicationsEnabled(((Integer) this.mSlotIndexToSubId.get(Integer.valueOf(i))).intValue(), true);
            updateSubscription(i);
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SubscriptionInfoInternal subscriptionInfoInternalByIccId = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(getStrippedIccid(str));
        if (subscriptionInfoInternalByIccId != null) {
            iInsertSubscriptionInfo = subscriptionInfoInternalByIccId.getSubscriptionId();
            log("updateSimStateForInactivePort: Found existing subscription. subId=" + iInsertSubscriptionInfo);
        } else {
            iInsertSubscriptionInfo = insertSubscriptionInfo(getStrippedIccid(str), -1, "", 0);
            this.mSubscriptionDatabaseManager.setDisplayName(iInsertSubscriptionInfo, this.mContext.getResources().getString(R.string.faceunlock_multiple_failures, Integer.valueOf(iInsertSubscriptionInfo)));
            log("updateSimStateForInactivePort: Insert a new subscription for inactive SIM. subId=" + iInsertSubscriptionInfo);
        }
        if (SubscriptionManager.isValidSubscriptionId(iInsertSubscriptionInfo)) {
            this.mSubscriptionDatabaseManager.setPortIndex(iInsertSubscriptionInfo, getPortIndex(str));
        }
    }

    public void updateSimState(final int i, final int i2, final Executor executor, final Runnable runnable) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSimState$37(i, i2, executor, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSimState$37(int i, int i2, Executor executor, Runnable runnable) {
        this.mSimState[i] = i2;
        logl("updateSimState: slot " + i + " " + TelephonyManager.simStateToString(i2));
        switch (i2) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 7:
            case 8:
            case 10:
            case 12:
                updateSubscription(i);
                break;
            case 5:
            case 6:
                updateEmbeddedSubscriptions();
                updateSubscription(i);
                break;
        }
        if (executor == null || runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    private String getCallingPackage() {
        if (UserHandle.isSameApp(Binder.getCallingUid(), 1001)) {
            return "com.android.phone";
        }
        return Arrays.toString(this.mContext.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager().getPackagesForUid(Binder.getCallingUid()));
    }

    public void updateGroupDisabled() {
        List list = (List) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new SubscriptionManagerService$$ExternalSyntheticLambda2()).map(new SubscriptionManagerService$$ExternalSyntheticLambda3()).collect(Collectors.toList());
        for (final SubscriptionInfo subscriptionInfo : getOpportunisticSubscriptions(this.mContext.getOpPackageName(), this.mContext.getFeatureId())) {
            this.mSubscriptionDatabaseManager.setGroupDisabled(subscriptionInfo.getSubscriptionId(), subscriptionInfo.getGroupUuid() != null && list.stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return SubscriptionManagerService.$r8$lambda$x4UEok3rJPG3IVtyi1O_ETOHmV8(subscriptionInfo, (SubscriptionInfo) obj);
                }
            }));
        }
    }

    public static /* synthetic */ boolean $r8$lambda$x4UEok3rJPG3IVtyi1O_ETOHmV8(SubscriptionInfo subscriptionInfo, SubscriptionInfo subscriptionInfo2) {
        return !subscriptionInfo2.isOpportunistic() && Objects.equals(subscriptionInfo.getGroupUuid(), subscriptionInfo2.getGroupUuid());
    }

    public void setTransferStatus(int i, int i2) {
        setTransferStatus_enforcePermission();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS") != 0) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS tosetTransferStatus");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mSubscriptionDatabaseManager.setTransferStatus(i, i2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setEnrollableSubscriptionPlans(final int i, final SubscriptionPlan[] subscriptionPlanArr, final long j, final String str) {
        enforceEnrollableSubscriptionPlanAccess(i, Binder.getCallingUid(), str, "setEnrollableSubscriptionPlans", true);
        for (SubscriptionPlan subscriptionPlan : subscriptionPlanArr) {
            Objects.requireNonNull(subscriptionPlan);
        }
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setEnrollableSubscriptionPlans$40(i, subscriptionPlanArr, j, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setEnrollableSubscriptionPlansInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setEnrollableSubscriptionPlans$40(int i, SubscriptionPlan[] subscriptionPlanArr, long j, String str) {
        long jCurrentTimeMillis = j > 0 ? System.currentTimeMillis() + j : 0L;
        log("set enrollable SubscriptionPlans for subid:" + i + " duration:" + j + "ms");
        this.mEnrollableSubscriptionPlans.put(Integer.valueOf(i), subscriptionPlanArr);
        this.mEnrollableSubscriptionPlansOwner.put(Integer.valueOf(i), str);
        this.mEnrollablePlanExpirationTime.put(Integer.valueOf(i), Long.valueOf(jCurrentTimeMillis));
        rescheduleAllPlanExpirations();
        writeEnrollableSubscriptionPlans();
        broadcastEnrollableSubscriptionPlansChanged(i);
    }

    public SubscriptionPlan[] getEnrollableSubscriptionPlans(int i, String str) {
        enforceEnrollableSubscriptionPlanAccess(i, Binder.getCallingUid(), str, "getEnrollableSubscriptionPlans", false);
        return (SubscriptionPlan[]) this.mEnrollableSubscriptionPlans.get(Integer.valueOf(i));
    }

    public String getEnrollableSubscriptionPlansOwner(int i) {
        if (UserHandle.getCallingAppId() != 1000) {
            throw new SecurityException();
        }
        return (String) this.mEnrollableSubscriptionPlansOwner.get(Integer.valueOf(i));
    }

    private void enforceEnrollableSubscriptionPlanAccess(int i, int i2, String str, String str2, boolean z) {
        int appId = UserHandle.getAppId(i2);
        if (appId == 1000 || appId == 1001) {
            return;
        }
        try {
            if (!isSameAppIncludingPccUid(this.mPackageManager.getPackageUid(str, 0), i2)) {
                throw new SecurityException("Package " + str + " does not belong to uid " + i2);
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
                if (telephonyManagerCreateForSubscriptionId != null && telephonyManagerCreateForSubscriptionId.checkCarrierPrivilegesForPackage(str) == 1) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return;
                }
                CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
                PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(i, "config_plans_package_override_string") : null;
                if (configForSubId != null) {
                    String string = configForSubId.getString("config_plans_package_override_string", null);
                    if (!TextUtils.isEmpty(string) && Objects.equals(string, str)) {
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                }
                if (carrierConfigManager != null) {
                    String defaultCarrierServicePackageName = carrierConfigManager.getDefaultCarrierServicePackageName();
                    if (!TextUtils.isEmpty(defaultCarrierServicePackageName) && Objects.equals(defaultCarrierServicePackageName, str)) {
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                String str3 = (String) this.mEnrollableSubscriptionPlansOwner.get(Integer.valueOf(i));
                if ((str3 == null || !str3.equals(str)) && this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_SUBSCRIPTION_PLANS") != 0) {
                    if (z || this.mContext.checkCallingOrSelfPermission("android.permission.READ_SUBSCRIPTION_PLANS") != 0) {
                        throw new SecurityException(str2 + ": Caller " + str + " does not meet required permissions (Carrier Privilege, Plan Owner, or MANAGE_SUBSCRIPTION_PLANS)");
                    }
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            throw new SecurityException("Package " + str + " not found");
        }
    }

    private boolean isSameAppIncludingPccUid(int i, int i2) {
        if (Process.isPrivateComputeCoreUid(i)) {
            i = this.mPackageManager.getAppUidForPrivateComputeCoreUid(i);
        }
        if (Process.isPrivateComputeCoreUid(i2)) {
            i2 = this.mPackageManager.getAppUidForPrivateComputeCoreUid(i2);
        }
        return UserHandle.isSameApp(i, i2);
    }

    private void broadcastEnrollableSubscriptionPlansChanged(int i) {
        Intent intent = new Intent("android.telephony.action.ENROLLABLE_SUBSCRIPTION_PLANS_CHANGED");
        SubscriptionManager.putSubscriptionIdExtra(intent, i);
        log("broadcastEnrollableSubscriptionPlansChanged for subId:" + i);
        this.mContext.sendBroadcast(intent, "android.permission.READ_SUBSCRIPTION_PLANS");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rescheduleAllPlanExpirations() {
        log("reschedule all enrollable SubscriptionPlans");
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mEnrollablePlanExpirationRunnables.values().iterator();
        while (it.hasNext()) {
            this.mHandler.removeCallbacks((Runnable) it.next());
        }
        this.mEnrollablePlanExpirationRunnables.clear();
        for (Integer num : this.mEnrollableSubscriptionPlans.keySet()) {
            final int iIntValue = num.intValue();
            Long l = (Long) this.mEnrollablePlanExpirationTime.get(num);
            if (l != null && l.longValue() != 0) {
                long jLongValue = l.longValue() - jCurrentTimeMillis;
                if (jLongValue <= 0) {
                    logl("Plan for subId=" + iIntValue + " expired (duration=" + jLongValue + ")");
                    removeEnrollablePlansInMemory(iIntValue);
                    arrayList.add(num);
                } else {
                    logl("Rescheduling expiration for subId=" + iIntValue + " in " + jLongValue + "ms");
                    Runnable runnable = new Runnable() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda59
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$rescheduleAllPlanExpirations$41(iIntValue);
                        }
                    };
                    this.mEnrollablePlanExpirationRunnables.put(num, runnable);
                    this.mHandler.postDelayed(runnable, jLongValue);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        writeEnrollableSubscriptionPlans();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            broadcastEnrollableSubscriptionPlansChanged(((Integer) obj).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handlePlanExpiration, reason: merged with bridge method [inline-methods] */
    public void lambda$rescheduleAllPlanExpirations$41(int i) {
        if (removeEnrollablePlansInMemory(i)) {
            logl("Plan for subId=" + i + " expired.");
            writeEnrollableSubscriptionPlans();
            broadcastEnrollableSubscriptionPlansChanged(i);
        }
    }

    private boolean removeEnrollablePlansInMemory(int i) {
        if (!this.mEnrollableSubscriptionPlans.containsKey(Integer.valueOf(i))) {
            return false;
        }
        logl("removing enrollable SubscriptionPlans for subId=" + i);
        this.mEnrollableSubscriptionPlans.remove(Integer.valueOf(i));
        this.mEnrollableSubscriptionPlansOwner.remove(Integer.valueOf(i));
        this.mEnrollablePlanExpirationTime.remove(Integer.valueOf(i));
        Runnable runnable = (Runnable) this.mEnrollablePlanExpirationRunnables.remove(Integer.valueOf(i));
        if (runnable == null) {
            return true;
        }
        this.mHandler.removeCallbacks(runnable);
        return true;
    }

    private File getProtectedStorageDir() {
        Context contextCreateDeviceProtectedStorageContext = this.mContext.createDeviceProtectedStorageContext();
        if (contextCreateDeviceProtectedStorageContext == null) {
            return null;
        }
        return contextCreateDeviceProtectedStorageContext.getFilesDir();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readEnrollableSubscriptionPlans() {
        AtomicFile atomicFile = this.mEnrollablePlansFile;
        if (atomicFile == null) {
            loge("can't read enrollableSubscriptionPlans. File is null");
            return;
        }
        synchronized (atomicFile) {
            try {
                if (!this.mEnrollablePlansFile.exists()) {
                    logl("No persisted enrollable plans file found.");
                    return;
                }
                logl("read EnrollableSubscriptionPlans");
                this.mEnrollableSubscriptionPlans.clear();
                this.mEnrollableSubscriptionPlansOwner.clear();
                this.mEnrollablePlanExpirationTime.clear();
                try {
                    FileInputStream fileInputStreamOpenRead = this.mEnrollablePlansFile.openRead();
                    try {
                        TypedXmlPullParser binaryXmlPullParser = new BinaryXmlPullParser();
                        binaryXmlPullParser.setInput(fileInputStreamOpenRead, null);
                        while (true) {
                            int next = binaryXmlPullParser.next();
                            if (next == 1) {
                                break;
                            } else if (next == 2 && "sub-plans".equals(binaryXmlPullParser.getName())) {
                                readEnrollableSubscriptionPlansForSubscriptionLocked(binaryXmlPullParser);
                            }
                        }
                        if (fileInputStreamOpenRead != null) {
                            fileInputStreamOpenRead.close();
                        }
                    } catch (Throwable th) {
                        if (fileInputStreamOpenRead != null) {
                            try {
                                fileInputStreamOpenRead.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    loge("Failed to read enrollable plans: " + e);
                }
                rescheduleAllPlanExpirations();
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    private void readEnrollableSubscriptionPlansForSubscriptionLocked(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        SubscriptionPlan singleSubscriptionPlan;
        long jCurrentTimeMillis = System.currentTimeMillis();
        int attributeInt = typedXmlPullParser.getAttributeInt(null, CallWaitingController.KEY_SUB_ID);
        String attributeValue = typedXmlPullParser.getAttributeValue(null, "owner");
        long attributeLong = typedXmlPullParser.getAttributeLong(null, "expirationTime", 0L);
        if (attributeLong - jCurrentTimeMillis <= 0) {
            logl("Skipping reading expired/volatile plans for subId=" + attributeInt);
            return;
        }
        ArrayList arrayList = new ArrayList();
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4 && "plan".equals(typedXmlPullParser.getName()) && (singleSubscriptionPlan = readSingleSubscriptionPlan(typedXmlPullParser)) != null) {
                arrayList.add(singleSubscriptionPlan);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.mEnrollableSubscriptionPlans.put(Integer.valueOf(attributeInt), (SubscriptionPlan[]) arrayList.toArray(new SubscriptionPlan[0]));
        if (attributeValue != null) {
            this.mEnrollableSubscriptionPlansOwner.put(Integer.valueOf(attributeInt), attributeValue);
        }
        if (attributeLong > 0) {
            this.mEnrollablePlanExpirationTime.put(Integer.valueOf(attributeInt), Long.valueOf(attributeLong));
        }
    }

    private SubscriptionPlan readSingleSubscriptionPlan(TypedXmlPullParser typedXmlPullParser) {
        SubscriptionPlan.Builder builderCreateNonrecurring;
        try {
            String attributeValue = typedXmlPullParser.getAttributeValue(null, "cycleStart");
            String attributeValue2 = typedXmlPullParser.getAttributeValue(null, "cycleEnd");
            String attributeValue3 = typedXmlPullParser.getAttributeValue(null, "cyclePeriod");
            ZonedDateTime zonedDateTimeConvertZonedDateTime = attributeValue != null ? RecurrenceRule.convertZonedDateTime(attributeValue) : null;
            ZonedDateTime zonedDateTimeConvertZonedDateTime2 = attributeValue2 != null ? RecurrenceRule.convertZonedDateTime(attributeValue2) : null;
            Period periodConvertPeriod = attributeValue3 != null ? RecurrenceRule.convertPeriod(attributeValue3) : null;
            if (periodConvertPeriod != null) {
                builderCreateNonrecurring = SubscriptionPlan.Builder.createRecurring(zonedDateTimeConvertZonedDateTime, periodConvertPeriod);
            } else {
                builderCreateNonrecurring = SubscriptionPlan.Builder.createNonrecurring(zonedDateTimeConvertZonedDateTime, zonedDateTimeConvertZonedDateTime2);
            }
            String attributeValue4 = typedXmlPullParser.getAttributeValue(null, "title");
            if (attributeValue4 != null) {
                builderCreateNonrecurring.setTitle(attributeValue4);
            }
            String attributeValue5 = typedXmlPullParser.getAttributeValue(null, "summary");
            if (attributeValue5 != null) {
                builderCreateNonrecurring.setSummary(attributeValue5);
            }
            long attributeLong = typedXmlPullParser.getAttributeLong(null, "limitBytes", -1L);
            int attributeInt = typedXmlPullParser.getAttributeInt(null, "limitBehavior", -1);
            if (attributeLong != -1 || attributeInt != -1) {
                builderCreateNonrecurring.setDataLimit(attributeLong, attributeInt);
            }
            long attributeLong2 = typedXmlPullParser.getAttributeLong(null, "usageBytes", -1L);
            long attributeLong3 = typedXmlPullParser.getAttributeLong(null, "usageTime", -1L);
            if (attributeLong2 != -1 || attributeLong3 != -1) {
                builderCreateNonrecurring.setDataUsage(attributeLong2, attributeLong3);
            }
            String attributeValue6 = typedXmlPullParser.getAttributeValue(null, "networkTypes");
            if (attributeValue6 != null && !attributeValue6.isEmpty()) {
                try {
                    builderCreateNonrecurring.setNetworkTypes(Arrays.stream(attributeValue6.split(",")).mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda60
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            return Integer.parseInt((String) obj);
                        }
                    }).toArray());
                } catch (NumberFormatException unused) {
                    loge("Failed to parse network types: ".concat(attributeValue6));
                }
            }
            int attributeInt2 = typedXmlPullParser.getAttributeInt(null, "subscriptionStatus", 0);
            if (attributeInt2 != 0) {
                builderCreateNonrecurring.setSubscriptionStatus(attributeInt2);
            }
            int attributeInt3 = typedXmlPullParser.getAttributeInt(null, "planId", -1);
            if (attributeInt3 != -1) {
                builderCreateNonrecurring.setId(attributeInt3);
            }
            String attributeValue7 = typedXmlPullParser.getAttributeValue(null, "planTypes");
            if (attributeValue7 != null && !attributeValue7.isEmpty()) {
                try {
                    builderCreateNonrecurring.setTypes(Arrays.stream(attributeValue7.split(",")).mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda60
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            return Integer.parseInt((String) obj);
                        }
                    }).toArray());
                } catch (NumberFormatException unused2) {
                    loge("Failed to parse plan types: ".concat(attributeValue7));
                }
            }
            String attributeValue8 = typedXmlPullParser.getAttributeValue(null, "resetTime");
            if (attributeValue8 != null) {
                builderCreateNonrecurring.setDataUsageResetTime(RecurrenceRule.convertZonedDateTime(attributeValue8));
            }
            long attributeLong4 = typedXmlPullParser.getAttributeLong(null, "downlinkKbps", -1L);
            if (attributeLong4 != -1) {
                builderCreateNonrecurring.setStreamingAppMaxDownlinkKbps(attributeLong4);
            }
            long attributeLong5 = typedXmlPullParser.getAttributeLong(null, "uplinkKbps", -1L);
            if (attributeLong5 != -1) {
                builderCreateNonrecurring.setStreamingAppMaxUplinkKbps(attributeLong5);
            }
            return builderCreateNonrecurring.build();
        } catch (Exception e) {
            loge("Failed to reconstruct SubscriptionPlan from XML: " + e);
            return null;
        }
    }

    private void writeEnrollableSubscriptionPlans() {
        FileOutputStream fileOutputStreamStartWrite;
        Exception e;
        AtomicFile atomicFile = this.mEnrollablePlansFile;
        if (atomicFile == null) {
            loge("can't write enrollableSubscriptionPlans. File is null");
            return;
        }
        synchronized (atomicFile) {
            try {
                logl("write EnrollableSubscriptionPlans");
                try {
                    fileOutputStreamStartWrite = this.mEnrollablePlansFile.startWrite();
                    try {
                        TypedXmlSerializer binaryXmlSerializer = new BinaryXmlSerializer();
                        binaryXmlSerializer.setOutput(fileOutputStreamStartWrite, "utf-8");
                        binaryXmlSerializer.startDocument(null, Boolean.TRUE);
                        binaryXmlSerializer.startTag(null, "enrollable-plans");
                        for (Integer num : this.mEnrollableSubscriptionPlans.keySet()) {
                            SubscriptionPlan[] subscriptionPlanArr = (SubscriptionPlan[]) this.mEnrollableSubscriptionPlans.get(num);
                            String str = (String) this.mEnrollableSubscriptionPlansOwner.get(num);
                            Long l = (Long) this.mEnrollablePlanExpirationTime.get(num);
                            if (l == null || l.longValue() != 0) {
                                if (subscriptionPlanArr != null && subscriptionPlanArr.length > 0) {
                                    binaryXmlSerializer.startTag(null, "sub-plans");
                                    binaryXmlSerializer.attributeInt(null, CallWaitingController.KEY_SUB_ID, num.intValue());
                                    if (str != null) {
                                        binaryXmlSerializer.attribute(null, "owner", str);
                                    }
                                    if (l != null) {
                                        binaryXmlSerializer.attributeLong(null, "expirationTime", l.longValue());
                                    }
                                    for (SubscriptionPlan subscriptionPlan : subscriptionPlanArr) {
                                        writeSingleSubscriptionPlan(binaryXmlSerializer, subscriptionPlan);
                                    }
                                    binaryXmlSerializer.endTag(null, "sub-plans");
                                }
                            }
                        }
                        binaryXmlSerializer.endTag(null, "enrollable-plans");
                        binaryXmlSerializer.endDocument();
                        this.mEnrollablePlansFile.finishWrite(fileOutputStreamStartWrite);
                    } catch (Exception e2) {
                        e = e2;
                        loge("Failed to write enrollable plans: " + e);
                        if (fileOutputStreamStartWrite != null) {
                            this.mEnrollablePlansFile.failWrite(fileOutputStreamStartWrite);
                        }
                    }
                } catch (Exception e3) {
                    fileOutputStreamStartWrite = null;
                    e = e3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void writeSingleSubscriptionPlan(TypedXmlSerializer typedXmlSerializer, SubscriptionPlan subscriptionPlan) throws IOException {
        typedXmlSerializer.startTag(null, "plan");
        RecurrenceRule cycleRule = subscriptionPlan.getCycleRule();
        ZonedDateTime zonedDateTime = cycleRule.start;
        if (zonedDateTime != null) {
            typedXmlSerializer.attribute(null, "cycleStart", RecurrenceRule.convertZonedDateTime(zonedDateTime));
        }
        ZonedDateTime zonedDateTime2 = cycleRule.end;
        if (zonedDateTime2 != null) {
            typedXmlSerializer.attribute(null, "cycleEnd", RecurrenceRule.convertZonedDateTime(zonedDateTime2));
        }
        Period period = cycleRule.period;
        if (period != null) {
            typedXmlSerializer.attribute(null, "cyclePeriod", RecurrenceRule.convertPeriod(period));
        }
        if (subscriptionPlan.getTitle() != null) {
            typedXmlSerializer.attribute(null, "title", subscriptionPlan.getTitle().toString());
        }
        if (subscriptionPlan.getSummary() != null) {
            typedXmlSerializer.attribute(null, "summary", subscriptionPlan.getSummary().toString());
        }
        if (subscriptionPlan.getDataLimitBytes() != -1) {
            typedXmlSerializer.attributeLong(null, "limitBytes", subscriptionPlan.getDataLimitBytes());
        }
        if (subscriptionPlan.getDataLimitBehavior() != -1) {
            typedXmlSerializer.attributeInt(null, "limitBehavior", subscriptionPlan.getDataLimitBehavior());
        }
        if (subscriptionPlan.getDataUsageBytes() != -1) {
            typedXmlSerializer.attributeLong(null, "usageBytes", subscriptionPlan.getDataUsageBytes());
        }
        if (subscriptionPlan.getDataUsageTime() != -1) {
            typedXmlSerializer.attributeLong(null, "usageTime", subscriptionPlan.getDataUsageTime());
        }
        int[] networkTypes = subscriptionPlan.getNetworkTypes();
        if (networkTypes != null && networkTypes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < networkTypes.length; i++) {
                sb.append(networkTypes[i]);
                if (i < networkTypes.length - 1) {
                    sb.append(",");
                }
            }
            typedXmlSerializer.attribute(null, "networkTypes", sb.toString());
        }
        if (subscriptionPlan.getSubscriptionStatus() != 0) {
            typedXmlSerializer.attributeInt(null, "subscriptionStatus", subscriptionPlan.getSubscriptionStatus());
        }
        if (subscriptionPlan.getId() != -1) {
            typedXmlSerializer.attributeInt(null, "planId", subscriptionPlan.getId());
        }
        typedXmlSerializer.attribute(null, "planTypes", TextUtils.join(",", subscriptionPlan.getTypes()));
        if (subscriptionPlan.getDataUsageResetTime() != null) {
            typedXmlSerializer.attribute(null, "resetTime", RecurrenceRule.convertZonedDateTime(subscriptionPlan.getDataUsageResetTime()));
        }
        if (subscriptionPlan.getStreamingAppMaxDownlinkKbps() != -1) {
            typedXmlSerializer.attributeLong(null, "downlinkKbps", subscriptionPlan.getStreamingAppMaxDownlinkKbps());
        }
        if (subscriptionPlan.getStreamingAppMaxUplinkKbps() != -1) {
            typedXmlSerializer.attributeLong(null, "uplinkKbps", subscriptionPlan.getStreamingAppMaxUplinkKbps());
        }
        typedXmlSerializer.endTag(null, "plan");
    }

    public void setSatelliteEntitlementInfo(int i, List<String> list, List<String> list2, Map<String, Integer> map, Map<String, List<Integer>> map2, Map<String, Integer> map3, Map<String, Integer> map4) {
        int i2;
        try {
            i2 = i;
            try {
                this.mSubscriptionDatabaseManager.setSatelliteEntitlementInfo(i2, list, list2, map, map2, map3, map4);
            } catch (IllegalArgumentException unused) {
                loge("setSatelliteEntitlementPlmnList: invalid subId=" + i2);
            }
        } catch (IllegalArgumentException unused2) {
            i2 = i;
        }
    }

    public List<String> getSatelliteEntitlementPlmnList(int i) {
        return (List) Optional.ofNullable(this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i)).map(new SubscriptionDatabaseManager$$ExternalSyntheticLambda182()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda44
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SubscriptionManagerService.m1914$r8$lambda$RuhrlwPJ3EW3lUUkelidRIpMAU((String) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda45
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SubscriptionManagerService.$r8$lambda$oJyeiMNIZGt2WweafoMAhgOPwFY((String) obj);
            }
        }).orElse(new ArrayList());
    }

    /* JADX INFO: renamed from: $r8$lambda$RuhrlwPJ3EW3lU-UkelidRIpMAU, reason: not valid java name */
    public static /* synthetic */ boolean m1914$r8$lambda$RuhrlwPJ3EW3lUUkelidRIpMAU(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ List $r8$lambda$oJyeiMNIZGt2WweafoMAhgOPwFY(String str) {
        return (List) Arrays.stream(str.split(",")).collect(Collectors.toList());
    }

    public void setSatelliteESOSSupported(int i, boolean z) {
        try {
            this.mSubscriptionDatabaseManager.setSatelliteESOSSupported(i, z ? 1 : 0);
        } catch (IllegalArgumentException unused) {
            loge("setSatelliteESOSSupported: invalid subId=" + i);
        }
    }

    public void setIsSatelliteProvisionedForNonIpDatagram(int i, boolean z) {
        try {
            this.mSubscriptionDatabaseManager.setIsSatelliteProvisionedForNonIpDatagram(i, z ? 1 : 0);
        } catch (IllegalArgumentException unused) {
            loge("setIsSatelliteProvisionedForNonIpDatagram: invalid subId=" + i);
        }
    }

    public boolean getSatelliteESOSSupported(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.getSatelliteESOSSupported() == 1;
    }

    public List<String> getSatelliteEntitlementBarredPlmnList(int i) {
        return (List) Optional.ofNullable(this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i)).map(new SubscriptionDatabaseManager$$ExternalSyntheticLambda187()).filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SubscriptionManagerService.$r8$lambda$n5c_H6VLDo56FrNi98iKPRq_IRo((String) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SubscriptionManagerService.$r8$lambda$ug2i0_rmx_Fnlh04iXg_aY_OlMs((String) obj);
            }
        }).orElse(new ArrayList());
    }

    public static /* synthetic */ boolean $r8$lambda$n5c_H6VLDo56FrNi98iKPRq_IRo(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ List $r8$lambda$ug2i0_rmx_Fnlh04iXg_aY_OlMs(String str) {
        return (List) Arrays.stream(str.split(",")).collect(Collectors.toList());
    }

    public Map<String, Integer> getSatelliteEntitlementDataPlanForPlmns(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            return new HashMap();
        }
        return deSerializeCVToMap(subscriptionInfoInternal.getSatelliteEntitlementDataPlanForPlmns());
    }

    public Map<String, List<Integer>> getSatelliteEntitlementPlmnServiceTypeMap(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            return new HashMap();
        }
        return deSerializeCVToMapList(subscriptionInfoInternal.getSatelliteEntitlementPlmnsServiceTypes());
    }

    public Map<String, Integer> getSatelliteEntitlementPlmnDataServicePolicy(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            return new HashMap();
        }
        return deSerializeCVToMap(subscriptionInfoInternal.getSatellitePlmnsDataServicePolicy());
    }

    public Map<String, Integer> getSatelliteEntitlementPlmnVoiceServicePolicy(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            return new HashMap();
        }
        return deSerializeCVToMap(subscriptionInfoInternal.getSatellitePlmnsVoiceServicePolicy());
    }

    public boolean isSatelliteProvisionedForNonIpDatagram(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.getIsSatelliteProvisionedForNonIpDatagram() == 1;
    }

    public boolean isEsimBootStrapProvisioningActivated() {
        return getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true).stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SubscriptionManagerService.$r8$lambda$gT3TwP9NyYUTbV7XkSv42vsytjY((SubscriptionInfo) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$gT3TwP9NyYUTbV7XkSv42vsytjY(SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo != null && subscriptionInfo.getProfileClass() == 1;
    }

    public boolean isEsimBootStrapProvisioningActiveForSubId(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.getProfileClass() == 1;
    }

    private String getCurrentPackageName() {
        String[] packagesForUid;
        PackageManager packageManager = this.mContext.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(Binder.getCallingUid())) == null) {
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

    private String slotMappingToString() {
        return "[" + ((String) this.mSlotIndexToSubId.entrySet().stream().map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SubscriptionManagerService.m1906$r8$lambda$5K4THpEyfme7SRT7lfKv3A_l7I((Map.Entry) obj);
            }
        }).collect(Collectors.joining(", "))) + "]";
    }

    /* JADX INFO: renamed from: $r8$lambda$5K4THpEyfm-e7SRT7lfKv3A_l7I, reason: not valid java name */
    public static /* synthetic */ String m1906$r8$lambda$5K4THpEyfme7SRT7lfKv3A_l7I(Map.Entry entry) {
        return "slot " + entry.getKey() + ": subId=" + entry.getValue();
    }

    private boolean isSatellitePlmn(String str) {
        String string;
        try {
            string = this.mContext.getResources().getString(R.string.dump_heap_ready_notification);
        } catch (Resources.NotFoundException e) {
            loge("isSatellitePlmn: id= 17040240, ex=" + e);
            string = null;
        }
        if (TextUtils.isEmpty(string) && isMockModemAllowed()) {
            log("isSatellitePlmn: Read config_satellite_sim_plmn_identifier from device config");
            string = DeviceConfig.getString("telephony", "config_satellite_sim_plmn_identifier", "");
        }
        log("isSatellitePlmn: overlayMccMnc=" + string + ", mccMnc=" + str);
        return TextUtils.equals(str, string);
    }

    private boolean isSatelliteSpn(String str) {
        String string;
        try {
            string = this.mContext.getResources().getString(R.string.dump_heap_ready_text);
        } catch (Resources.NotFoundException e) {
            loge("isSatelliteSpn: id= 17040241, ex=" + e);
            string = null;
        }
        if (TextUtils.isEmpty(string) && isMockModemAllowed()) {
            log("isSatelliteSpn: Read config_satellite_sim_spn_identifier from device config");
            string = DeviceConfig.getString("telephony", "config_satellite_sim_spn_identifier", "");
        }
        log("isSatelliteSpn: overlaySpn=" + string + ", spn=" + str);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(string)) {
            return false;
        }
        return TextUtils.equals(str, string);
    }

    public byte[] getAllPlatformManagedPinsForBackup() {
        getAllPlatformManagedPinsForBackup_enforcePermission();
        return this.mUiccController.getPinStorage().getPlatformManagedPinsForBackup();
    }

    public void restorePlatformManagedSimPins(byte[] bArr) {
        restorePlatformManagedSimPins_enforcePermission();
        this.mUiccController.getPinStorage().restorePlatformManagedPinsFromBackup(bArr);
    }

    private boolean isMockModemAllowed() {
        return SystemProperties.getBoolean("persist.radio.allow_mock_modem", true) || SystemProperties.getBoolean("ro.boot.radio.allow_mock_modem", false);
    }

    protected int getCardNumber(int i) {
        for (int i2 = i - 1; i2 > 0; i2--) {
            SubscriptionInfoInternal subscriptionInfoInternal = getSubscriptionInfoInternal(i2);
            if (subscriptionInfoInternal != null && !subscriptionInfoInternal.isVisible()) {
                i--;
            }
        }
        return i;
    }

    private boolean canManageSubscription(SubscriptionInfo subscriptionInfo, String str) {
        boolean zIsHeadlessSystemUserMode = UserManager.isHeadlessSystemUserMode();
        SubscriptionManager subscriptionManager = this.mSubscriptionManager;
        if (zIsHeadlessSystemUserMode) {
            return subscriptionManager.canManageSubscriptionAsUser(subscriptionInfo, str, UserHandle.of(ActivityManager.getCurrentUser()));
        }
        return subscriptionManager.canManageSubscription(subscriptionInfo, str);
    }

    public boolean canManageSubscriptionAsUser(final SubscriptionInfo subscriptionInfo, final String str, final UserHandle userHandle) {
        Objects.requireNonNull(subscriptionInfo);
        Objects.requireNonNull(str);
        Objects.requireNonNull(userHandle);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        }
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda39
            public final Object getOrThrow() {
                return this.f$0.lambda$canManageSubscriptionAsUser$49(subscriptionInfo, str, userHandle);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$canManageSubscriptionAsUser$49(SubscriptionInfo subscriptionInfo, String str, UserHandle userHandle) {
        return Boolean.valueOf(canManageSubscriptionAsUserInternal(subscriptionInfo, str, userHandle));
    }

    private boolean canManageSubscriptionAsUserInternal(SubscriptionInfo subscriptionInfo, String str, UserHandle userHandle) {
        int[] activeSubIdListAsUser = getActiveSubIdListAsUser(false, userHandle);
        int[] activeSubIdListAsUser2 = getActiveSubIdListAsUser(false, new UserHandle(-10000));
        int length = activeSubIdListAsUser.length + activeSubIdListAsUser2.length;
        int[] iArr = new int[length];
        System.arraycopy(activeSubIdListAsUser, 0, iArr, 0, activeSubIdListAsUser.length);
        System.arraycopy(activeSubIdListAsUser2, 0, iArr, activeSubIdListAsUser.length, activeSubIdListAsUser2.length);
        for (int i = 0; i < length; i++) {
            int i2 = iArr[i];
            TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i2);
            if (telephonyManagerCreateForSubscriptionId.checkCarrierPrivilegesForPackage(str) == 1) {
                if (subscriptionInfo.getSubscriptionId() == i2) {
                    return true;
                }
                if (subscriptionInfo.getCarrierId() != -1 && telephonyManagerCreateForSubscriptionId.getSimCarrierId() == subscriptionInfo.getCarrierId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, Integer> deSerializeCVToMap(String str) {
        HashMap map = new HashMap();
        try {
            if (!TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str);
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, Integer.valueOf(jSONObject.getInt(next)));
                }
            }
            return map;
        } catch (JSONException e) {
            loge("deSerializeCVToMap JSON error: " + e.getMessage());
            return map;
        }
    }

    public Map<String, List<Integer>> deSerializeCVToMapList(String str) {
        HashMap map = new HashMap();
        try {
            if (!TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str);
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    JSONArray jSONArray = jSONObject.getJSONArray(next);
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        arrayList.add(Integer.valueOf(jSONArray.getInt(i)));
                    }
                    map.put(next, arrayList);
                }
            }
            return map;
        } catch (JSONException e) {
            loge("deSerializeCVToMapList JSON error: " + e.getMessage());
            return map;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("SMSVC", str);
    }

    private void loge(String str) {
        Rlog.e("SMSVC", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Requires android.Manifest.permission.DUMP");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
            androidUtilIndentingPrintWriter.println(SubscriptionManagerService.class.getSimpleName().concat(":"));
            androidUtilIndentingPrintWriter.println("Active modem count=" + this.mTelephonyManager.getActiveModemCount());
            androidUtilIndentingPrintWriter.println("Logical SIM slot sub id mapping:");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mSlotIndexToSubId.forEach(new BiConsumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda9
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    androidUtilIndentingPrintWriter.println("Logical SIM slot " + ((Integer) obj) + ": subId=" + ((Integer) obj2));
                }
            });
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println("Remote SIM sub IDs: " + this.mRemoteSubIds);
            androidUtilIndentingPrintWriter.println("ICCID:");
            androidUtilIndentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mTelephonyManager.getActiveModemCount(); i++) {
                androidUtilIndentingPrintWriter.println("slot " + i + ": " + SubscriptionInfo.getPrintableId(getIccId(i)));
            }
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("defaultSubId=" + getDefaultSubId());
            androidUtilIndentingPrintWriter.println("defaultVoiceSubId=" + getDefaultVoiceSubId());
            androidUtilIndentingPrintWriter.println("defaultDataSubId=" + getDefaultDataSubId());
            androidUtilIndentingPrintWriter.println("activeDataSubId=" + getActiveDataSubscriptionId());
            androidUtilIndentingPrintWriter.println("defaultSmsSubId=" + getDefaultSmsSubId());
            androidUtilIndentingPrintWriter.println("areAllSubscriptionsLoaded=" + areAllSubscriptionsLoaded());
            androidUtilIndentingPrintWriter.println("mUserIdToAvailableSubs=" + this.mUserIdToAvailableSubs);
            androidUtilIndentingPrintWriter.println();
            for (int i2 = 0; i2 < this.mSimState.length; i2++) {
                androidUtilIndentingPrintWriter.println("mSimState[" + i2 + "]=" + TelephonyManager.simStateToString(this.mSimState[i2]));
            }
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("Active subscriptions:");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new SubscriptionManagerService$$ExternalSyntheticLambda2()).forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    androidUtilIndentingPrintWriter.println((SubscriptionInfoInternal) obj);
                }
            });
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("All subscriptions:");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mSubscriptionDatabaseManager.getAllSubscriptions().forEach(new Consumer() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    androidUtilIndentingPrintWriter.println((SubscriptionInfoInternal) obj);
                }
            });
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.print("Embedded subscriptions: [");
            androidUtilIndentingPrintWriter.println(((String) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new SubscriptionManagerService$$ExternalSyntheticLambda11()).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda12
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf(((SubscriptionInfoInternal) obj).getSubscriptionId());
                }
            }).collect(Collectors.joining(", "))) + "]");
            androidUtilIndentingPrintWriter.print("Opportunistic subscriptions: [");
            androidUtilIndentingPrintWriter.println(((String) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((SubscriptionInfoInternal) obj).isOpportunistic();
                }
            }).map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda14
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf(((SubscriptionInfoInternal) obj).getSubscriptionId());
                }
            }).collect(Collectors.joining(", "))) + "]");
            androidUtilIndentingPrintWriter.print("getAvailableSubscriptionInfoList: [");
            androidUtilIndentingPrintWriter.println(((String) getAvailableSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getFeatureId()).stream().map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda15
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf(((SubscriptionInfo) obj).getSubscriptionId());
                }
            }).collect(Collectors.joining(", "))) + "]");
            androidUtilIndentingPrintWriter.print("getSelectableSubscriptionInfoList: [");
            androidUtilIndentingPrintWriter.println(((String) this.mSubscriptionManager.getSelectableSubscriptionInfoList().stream().map(new Function() { // from class: com.android.internal.telephony.subscription.SubscriptionManagerService$$ExternalSyntheticLambda16
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf(((SubscriptionInfo) obj).getSubscriptionId());
                }
            }).collect(Collectors.joining(", "))) + "]");
            if (this.mEuiccManager != null) {
                androidUtilIndentingPrintWriter.println("Euicc enabled=" + this.mEuiccManager.isEnabled());
            }
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("Enrollable Subscription Plans:");
            androidUtilIndentingPrintWriter.increaseIndent();
            if (this.mEnrollableSubscriptionPlans.isEmpty()) {
                androidUtilIndentingPrintWriter.println("None");
            } else {
                for (Map.Entry entry : this.mEnrollableSubscriptionPlans.entrySet()) {
                    Integer num = (Integer) entry.getKey();
                    androidUtilIndentingPrintWriter.println("SubId: " + num.intValue());
                    androidUtilIndentingPrintWriter.increaseIndent();
                    String str = (String) this.mEnrollableSubscriptionPlansOwner.get(num);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Owner: ");
                    if (str == null) {
                        str = "null";
                    }
                    sb.append(str);
                    androidUtilIndentingPrintWriter.println(sb.toString());
                    Long l = (Long) this.mEnrollablePlanExpirationTime.get(num);
                    if (l != null) {
                        androidUtilIndentingPrintWriter.println("Expiration: " + l + " (" + Instant.ofEpochMilli(l.longValue()) + ")");
                    } else {
                        androidUtilIndentingPrintWriter.println("Expiration: Never");
                    }
                    SubscriptionPlan[] subscriptionPlanArr = (SubscriptionPlan[]) entry.getValue();
                    if (subscriptionPlanArr != null) {
                        for (SubscriptionPlan subscriptionPlan : subscriptionPlanArr) {
                            androidUtilIndentingPrintWriter.println(subscriptionPlan);
                        }
                    } else {
                        androidUtilIndentingPrintWriter.println("Plans: null");
                    }
                    androidUtilIndentingPrintWriter.decreaseIndent();
                }
            }
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            androidUtilIndentingPrintWriter.println("Local log:");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.decreaseIndent();
            androidUtilIndentingPrintWriter.println();
            this.mSubscriptionDatabaseManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    protected SubscriptionDatabaseManager onCreateSubscriptionDatabaseManager(Context context, Looper looper, FeatureFlags featureFlags, SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback subscriptionDatabaseManagerCallback) {
        return TelephonyComponentFactory.getInstance().inject(SubscriptionDatabaseManager.class.getName()).makeSubscriptionDatabaseManager(context, looper, featureFlags, subscriptionDatabaseManagerCallback);
    }

    protected String onGetIccId(int i) {
        UiccPort uiccPort;
        if (this.mUiccController.getUiccSlotForPhone(i) == null || (uiccPort = this.mUiccController.getUiccPort(i)) == null) {
            return null;
        }
        return uiccPort.getIccId();
    }

    protected void onCheckSetSubscriptionPropertyColumnName(String str) {
        if (!Telephony.SimInfo.getAllColumns().contains(str)) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Invalid column name ", str);
        } else {
            if (DIRECT_ACCESS_SUBSCRIPTION_COLUMNS.contains(str)) {
                return;
            }
            throw new SecurityException("Column " + str + " is not allowed be directly accessed through setSubscriptionProperty.");
        }
    }

    protected void onCheckGetSubscriptionPropertyColumnName(String str) {
        if (!Telephony.SimInfo.getAllColumns().contains(str)) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Invalid column name ", str);
        } else {
            if (DIRECT_ACCESS_SUBSCRIPTION_COLUMNS.contains(str)) {
                return;
            }
            throw new SecurityException("Column " + str + " is not allowed be directly accessed through getSubscriptionProperty.");
        }
    }

    protected boolean isValidIccId(String str) {
        return !TextUtils.isEmpty(str);
    }
}
