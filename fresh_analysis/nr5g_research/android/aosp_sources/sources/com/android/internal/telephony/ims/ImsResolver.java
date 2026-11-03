package com.android.internal.telephony.ims;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.ims.ImsFeatureBinderRepository;
import com.android.ims.ImsFeatureContainer;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.telephony.GbaManager;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.TelephonyComponentFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class ImsResolver implements ImsServiceController.ImsServiceControllerCallbacks {
    public static final String METADATA_EMERGENCY_MMTEL_FEATURE = "android.telephony.ims.EMERGENCY_MMTEL_FEATURE";
    public static final String METADATA_MMTEL_FEATURE = "android.telephony.ims.MMTEL_FEATURE";
    public static final String METADATA_RCS_FEATURE = "android.telephony.ims.RCS_FEATURE";
    public static final String MTK_IMS_PACKAGE_NAME = "com.mediatek.ims";
    private static HandlerThread sHandlerThread;
    private static ImsResolver sInstance;
    private final SparseArray mBoundImsServicesByFeature;
    private final CarrierConfigManager mCarrierConfigManager;
    private final SparseArray mCarrierServices;
    private final Context mContext;
    private ImsServiceFeatureQueryManager mFeatureQueryManager;
    private final Handler mHandler;
    private int mNumSlots;
    private final SparseArray mOverrideServices;
    private final Context mReceiverContext;
    private final ImsFeatureBinderRepository mRepo;
    private final HandlerExecutor mRunnableExecutor;
    private final SparseIntArray mSlotIdToSubIdMap;
    private final BroadcastReceiver mUserReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ims.ImsResolver.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UserHandle userHandle = (UserHandle) intent.getParcelableExtra("android.intent.extra.USER", UserHandle.class);
            action.hashCode();
            if (action.equals("android.intent.action.USER_SWITCHED")) {
                ImsResolver.this.mHandler.obtainMessage(9, userHandle).sendToTarget();
            }
        }
    };
    private final BroadcastReceiver mAppChangedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ims.ImsResolver.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            action.hashCode();
            switch (action) {
                case "android.intent.action.PACKAGE_REPLACED":
                case "android.intent.action.PACKAGE_CHANGED":
                case "android.intent.action.PACKAGE_ADDED":
                    ImsResolver.this.mHandler.obtainMessage(0, schemeSpecificPart).sendToTarget();
                    break;
                case "android.intent.action.PACKAGE_REMOVED":
                    if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                        Log.i("ImsResolver", "onReceive: " + intent.getIntExtra("android.intent.extra.user_handle", -1));
                        if (intent.getIntExtra("android.intent.extra.user_handle", -1) != 999) {
                            ImsResolver.this.mHandler.obtainMessage(1, schemeSpecificPart).sendToTarget();
                        }
                        break;
                    } else {
                        ImsResolver.this.mHandler.obtainMessage(1, schemeSpecificPart).sendToTarget();
                        break;
                    }
                    break;
            }
        }
    };
    private final BroadcastReceiver mConfigChangedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ims.ImsResolver.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
            if (intExtra == -1) {
                Log.i("ImsResolver", "Received CCC for invalid slot id.");
                return;
            }
            int intExtra2 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            int simState = ImsResolver.this.mTelephonyManagerProxy.getSimState(ImsResolver.this.mContext, intExtra);
            if (intExtra2 == -1 && simState != 1 && simState != 6) {
                Log.i("ImsResolver", "xiaomi_issue setting subId slot " + intExtra + " and subId " + intExtra2);
                ImsResolver.this.setSubId(intExtra, intExtra2);
                ImsResolver.this.updateServiceContollerMap(intExtra, intExtra2);
                Log.i("ImsResolver", "Received CCC for slot " + intExtra + " and sim state " + simState + ", ignoring.");
                return;
            }
            Log.i("ImsResolver", "Received Carrier Config Changed for SlotId: " + intExtra + ", SubId: " + intExtra2 + ", sim state: " + simState);
            ImsResolver.this.mHandler.obtainMessage(2, intExtra, intExtra2).sendToTarget();
        }
    };
    private final BroadcastReceiver mBootCompleted = new BroadcastReceiver() { // from class: com.android.internal.telephony.ims.ImsResolver.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.i("ImsResolver", "Received BOOT_COMPLETED");
            ImsResolver.this.mHandler.obtainMessage(6, null).sendToTarget();
        }
    };
    private TelephonyManagerProxy mTelephonyManagerProxy = new TelephonyManagerProxy(this) { // from class: com.android.internal.telephony.ims.ImsResolver.5
        @Override // com.android.internal.telephony.ims.ImsResolver.TelephonyManagerProxy
        public int getSimState(Context context, int i) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (telephonyManager == null) {
                return 0;
            }
            return telephonyManager.getSimState(i);
        }
    };
    private SubscriptionManagerProxy mSubscriptionManagerProxy = new SubscriptionManagerProxy(this) { // from class: com.android.internal.telephony.ims.ImsResolver.6
        @Override // com.android.internal.telephony.ims.ImsResolver.SubscriptionManagerProxy
        public int getSubId(int i) {
            return SubscriptionManager.getSubscriptionId(i);
        }

        @Override // com.android.internal.telephony.ims.ImsResolver.SubscriptionManagerProxy
        public int getSlotIndex(int i) {
            return SubscriptionManager.getSlotIndex(i);
        }
    };
    private ActivityManagerProxy mActivityManagerProxy = new ActivityManagerProxy(this) { // from class: com.android.internal.telephony.ims.ImsResolver.7
        @Override // com.android.internal.telephony.ims.ImsResolver.ActivityManagerProxy
        public UserHandle getCurrentUser() {
            return UserHandle.of(ActivityManager.getCurrentUser());
        }
    };
    private ImsServiceControllerFactory mImsServiceControllerFactory = new ImsServiceControllerFactory(this) { // from class: com.android.internal.telephony.ims.ImsResolver.8
        @Override // com.android.internal.telephony.ims.ImsResolver.ImsServiceControllerFactory
        public String getServiceInterface() {
            return "android.telephony.ims.ImsService";
        }

        @Override // com.android.internal.telephony.ims.ImsResolver.ImsServiceControllerFactory
        public ImsServiceController create(Context context, ComponentName componentName, ImsServiceController.ImsServiceControllerCallbacks imsServiceControllerCallbacks, ImsFeatureBinderRepository imsFeatureBinderRepository) {
            return new ImsServiceController(context, componentName, imsServiceControllerCallbacks, imsFeatureBinderRepository);
        }
    };
    private final ImsServiceControllerFactory mImsServiceControllerFactoryCompat = new ImsServiceControllerFactory(this) { // from class: com.android.internal.telephony.ims.ImsResolver.9
        @Override // com.android.internal.telephony.ims.ImsResolver.ImsServiceControllerFactory
        public String getServiceInterface() {
            return "android.telephony.ims.compat.ImsService";
        }

        @Override // com.android.internal.telephony.ims.ImsResolver.ImsServiceControllerFactory
        public ImsServiceController create(Context context, ComponentName componentName, ImsServiceController.ImsServiceControllerCallbacks imsServiceControllerCallbacks, ImsFeatureBinderRepository imsFeatureBinderRepository) {
            return new ImsServiceControllerCompat(context, componentName, imsServiceControllerCallbacks, imsFeatureBinderRepository);
        }
    };
    private ImsDynamicQueryManagerFactory mDynamicQueryManagerFactory = new ImsDynamicQueryManagerFactory() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda4
        @Override // com.android.internal.telephony.ims.ImsResolver.ImsDynamicQueryManagerFactory
        public final ImsServiceFeatureQueryManager create(Context context, ImsServiceFeatureQueryManager.Listener listener) {
            return new ImsServiceFeatureQueryManager(context, listener);
        }
    };
    private final Object mBoundServicesLock = new Object();
    private final Map mDeviceServices = new ArrayMap();
    private final LocalLog mEventLog = new LocalLog(32);
    private boolean mBootCompletedHandlerRan = false;
    private boolean mCarrierConfigReceived = false;
    private final ImsServiceFeatureQueryManager.Listener mDynamicQueryListener = new ImsServiceFeatureQueryManager.Listener() { // from class: com.android.internal.telephony.ims.ImsResolver.10
        @Override // com.android.internal.telephony.ims.ImsServiceFeatureQueryManager.Listener
        public void onComplete(ComponentName componentName, Set<ImsFeatureConfiguration.FeatureSlotPair> set) {
            Log.d("ImsResolver", "onComplete called for name: " + componentName + ImsResolver.printFeatures(set));
            ImsResolver.this.handleFeaturesChanged(componentName, set);
        }

        @Override // com.android.internal.telephony.ims.ImsServiceFeatureQueryManager.Listener
        public void onError(ComponentName componentName) {
            Log.w("ImsResolver", "onError: " + componentName + "returned with an error result");
            ImsResolver.this.mEventLog.log("onError - dynamic query error for " + componentName);
            ImsResolver.this.scheduleQueryForFeatures(componentName, GbaManager.REQUEST_TIMEOUT_MS);
        }

        @Override // com.android.internal.telephony.ims.ImsServiceFeatureQueryManager.Listener
        public void onPermanentError(ComponentName componentName, UserHandle userHandle) {
            Log.w("ImsResolver", "onPermanentError: component=" + componentName);
            ImsResolver.this.mEventLog.log("onPermanentError - error for " + componentName);
            ImsResolver.this.mHandler.obtainMessage(10, new Pair(componentName.getPackageName(), userHandle)).sendToTarget();
        }
    };
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.ims.ImsResolver.11
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            boolean z = false;
            for (int i = 0; i < ImsResolver.this.mNumSlots; i++) {
                if (ImsResolver.this.getSubId(i) != ImsResolver.this.mSubscriptionManagerProxy.getSubId(i)) {
                    Log.i("ImsResolver", "onSubscriptionsChanged for slotId : " + i + " subId: " + ImsResolver.this.getSubId(i) + " to: " + ImsResolver.this.mSubscriptionManagerProxy.getSubId(i));
                    ImsResolver imsResolver = ImsResolver.this;
                    imsResolver.setSubId(i, imsResolver.mSubscriptionManagerProxy.getSubId(i));
                    z = true;
                }
            }
            if (z) {
                ImsResolver.this.calculateFeatureConfigurationChange();
            }
        }
    };
    private final Map mImsServiceTestUserRestrictions = new HashMap();
    private final Map mInstalledServicesCache = new HashMap();
    private final Map mActiveControllers = new HashMap();
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.ims.ImsResolver.12
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            for (int i = 0; i < ImsResolver.this.mNumSlots; i++) {
                int subIdUsingPhoneId = ImsResolver.this.getSubIdUsingPhoneId(i);
                if (ImsResolver.this.getSubId(i) != subIdUsingPhoneId) {
                    Log.d("ImsResolver", "mSubscriptionsChangedListener slotId: " + i + ", SubId: " + subIdUsingPhoneId);
                    ImsResolver.this.mHandler.obtainMessage(2, i, subIdUsingPhoneId).sendToTarget();
                }
            }
        }
    };

    public interface ActivityManagerProxy {
        UserHandle getCurrentUser();
    }

    public interface ImsDynamicQueryManagerFactory {
        ImsServiceFeatureQueryManager create(Context context, ImsServiceFeatureQueryManager.Listener listener);
    }

    public interface ImsServiceControllerFactory {
        ImsServiceController create(Context context, ComponentName componentName, ImsServiceController.ImsServiceControllerCallbacks imsServiceControllerCallbacks, ImsFeatureBinderRepository imsFeatureBinderRepository);

        String getServiceInterface();
    }

    public interface SubscriptionManagerProxy {
        int getSlotIndex(int i);

        int getSubId(int i);
    }

    public interface TelephonyManagerProxy {
        int getSimState(Context context, int i);
    }

    public static void make(Context context, String str, String str2, int i, ImsFeatureBinderRepository imsFeatureBinderRepository) {
        if (sInstance == null) {
            HandlerThread handlerThread = new HandlerThread("ImsResolver");
            sHandlerThread = handlerThread;
            handlerThread.start();
            sInstance = TelephonyComponentFactory.getInstance().inject(ImsResolver.class.getName()).makeImsResolver(context, str, str2, i, imsFeatureBinderRepository, sHandlerThread.getLooper());
        }
    }

    public static ImsResolver getInstance() {
        return sInstance;
    }

    private static class OverrideConfig {
        public final int[] featureTypes;
        public final boolean isCarrierService;
        public final String packageName;
        public final int slotId;
        public final int userId;

        OverrideConfig(String str, int i, int i2, boolean z, int[] iArr) {
            this.packageName = str;
            this.slotId = i;
            this.userId = i2;
            this.isCarrierService = z;
            this.featureTypes = iArr;
        }
    }

    public static class ImsServiceInfo {
        public ImsServiceControllerFactory controllerFactory;
        public final ComponentName name;
        public final Set<UserHandle> users = new HashSet();
        public boolean featureFromMetadata = true;
        private final HashSet mSupportedFeatures = new HashSet();

        public ImsServiceInfo(ComponentName componentName) {
            this.name = componentName;
        }

        void addFeatureForAllSlots(int i, int i2) {
            for (int i3 = 0; i3 < i; i3++) {
                this.mSupportedFeatures.add(new ImsFeatureConfiguration.FeatureSlotPair(i3, i2));
            }
        }

        void replaceFeatures(Set set) {
            this.mSupportedFeatures.clear();
            this.mSupportedFeatures.addAll(set);
        }

        public Set<ImsFeatureConfiguration.FeatureSlotPair> getSupportedFeatures() {
            return this.mSupportedFeatures;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ImsServiceInfo imsServiceInfo = (ImsServiceInfo) obj;
                if (Objects.equals(this.name, imsServiceInfo.name) && Objects.equals(this.users, imsServiceInfo.users)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.name, this.users);
        }

        public String toString() {
            return "[ImsServiceInfo] name=" + this.name + ", user=" + this.users + ",featureFromMetadata=" + this.featureFromMetadata + "," + ImsResolver.printFeatures(this.mSupportedFeatures);
        }
    }

    private class ResolverHandler extends Handler {
        ResolverHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ImsResolver.this.maybeAddedImsService((String) message.obj);
                    break;
                case 1:
                    ImsResolver.this.maybeRemovedImsService((String) message.obj);
                    break;
                case 2:
                    int i = message.arg1;
                    int i2 = message.arg2;
                    if (i < ImsResolver.this.mNumSlots) {
                        ImsResolver.this.mCarrierConfigReceived = true;
                        ImsResolver.this.carrierConfigChanged(i, i2);
                    } else {
                        Log.w("ImsResolver", "HANDLER_CONFIG_CHANGED for invalid slotid=" + i);
                    }
                    break;
                case 3:
                    ImsResolver.this.startDynamicQuery((ImsServiceInfo) message.obj);
                    break;
                case 4:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    ComponentName componentName = (ComponentName) someArgs.arg1;
                    Set set = (Set) someArgs.arg2;
                    someArgs.recycle();
                    ImsResolver.this.dynamicQueryComplete(componentName, set);
                    break;
                case 5:
                    OverrideConfig overrideConfig = (OverrideConfig) message.obj;
                    ImsResolver.this.setPackageNameUserOverride(overrideConfig.packageName, overrideConfig.userId);
                    HashMap map = new HashMap();
                    for (int i3 : overrideConfig.featureTypes) {
                        map.put(Integer.valueOf(i3), overrideConfig.packageName);
                    }
                    boolean z = overrideConfig.isCarrierService;
                    ImsResolver imsResolver = ImsResolver.this;
                    if (z) {
                        imsResolver.overrideCarrierService(overrideConfig.slotId, map);
                    } else {
                        imsResolver.overrideDeviceService(map);
                    }
                    break;
                case 6:
                    if (!ImsResolver.this.mBootCompletedHandlerRan) {
                        ImsResolver.this.mBootCompletedHandlerRan = true;
                        ImsResolver.this.mEventLog.log("handling BOOT_COMPLETE");
                        boolean z2 = ImsResolver.this.mCarrierConfigReceived;
                        ImsResolver imsResolver2 = ImsResolver.this;
                        if (z2) {
                            imsResolver2.mEventLog.log("boot complete - reeval");
                            ImsResolver.this.maybeAddedImsService(null);
                        } else {
                            imsResolver2.mEventLog.log("boot complete - update cache");
                            ImsResolver.this.updateInstalledServicesCache();
                        }
                    }
                    break;
                case 7:
                    ImsResolver.this.handleMsimConfigChange((Integer) ((AsyncResult) message.obj).result);
                    break;
                case 8:
                    ImsResolver.this.clearCarrierServiceOverrides(message.arg1);
                    break;
                case 9:
                    Log.i("ImsResolver", "onUserSwitched=" + ((UserHandle) message.obj));
                    ImsResolver.this.maybeAddedImsService(null);
                    break;
                case 10:
                    Pair pair = (Pair) message.obj;
                    ImsResolver.this.maybeRemovedImsServiceForUser((String) pair.first, (UserHandle) pair.second);
                    break;
            }
        }
    }

    public ImsResolver(Context context, String str, String str2, int i, ImsFeatureBinderRepository imsFeatureBinderRepository, Looper looper) {
        Log.i("ImsResolver", "device MMTEL package: " + str + ", device RCS package:" + str2);
        this.mContext = context;
        this.mNumSlots = i;
        this.mRepo = imsFeatureBinderRepository;
        this.mReceiverContext = context.createContextAsUser(UserHandle.ALL, 0);
        ResolverHandler resolverHandler = new ResolverHandler(looper);
        this.mHandler = resolverHandler;
        this.mRunnableExecutor = new HandlerExecutor(resolverHandler);
        this.mCarrierServices = new SparseArray(this.mNumSlots);
        setDeviceConfiguration(str, 0);
        setDeviceConfiguration(str, 1);
        setDeviceConfiguration(str2, 2);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        this.mOverrideServices = new SparseArray(0);
        this.mBoundImsServicesByFeature = new SparseArray(this.mNumSlots);
        this.mSlotIdToSubIdMap = new SparseIntArray(this.mNumSlots);
        for (int i2 = 0; i2 < this.mNumSlots; i2++) {
            this.mSlotIdToSubIdMap.put(i2, -1);
        }
    }

    public void setTelephonyManagerProxy(TelephonyManagerProxy telephonyManagerProxy) {
        this.mTelephonyManagerProxy = telephonyManagerProxy;
    }

    public void setSubscriptionManagerProxy(SubscriptionManagerProxy subscriptionManagerProxy) {
        this.mSubscriptionManagerProxy = subscriptionManagerProxy;
    }

    public void setImsServiceControllerFactory(ImsServiceControllerFactory imsServiceControllerFactory) {
        this.mImsServiceControllerFactory = imsServiceControllerFactory;
    }

    public void setActivityManagerProxy(ActivityManagerProxy activityManagerProxy) {
        this.mActivityManagerProxy = activityManagerProxy;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setImsDynamicQueryManagerFactory(ImsDynamicQueryManagerFactory imsDynamicQueryManagerFactory) {
        this.mDynamicQueryManagerFactory = imsDynamicQueryManagerFactory;
    }

    public void initialize() {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initialize$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: initializeInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$initialize$0() {
        this.mEventLog.log("Initializing");
        Log.i("ImsResolver", "Initializing cache.");
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mHandler, 7, null);
        this.mFeatureQueryManager = this.mDynamicQueryManagerFactory.create(this.mContext, this.mDynamicQueryListener);
        updateInstalledServicesCache();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        this.mReceiverContext.registerReceiver(this.mAppChangedReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mReceiverContext.registerReceiver(this.mUserReceiver, new IntentFilter("android.intent.action.USER_SWITCHED"));
        this.mReceiverContext.registerReceiver(this.mConfigChangedReceiver, intentFilter2, 2);
        SubscriptionManager.from(this.mContext).addOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener);
        UserManager userManager = (UserManager) this.mContext.getSystemService("user");
        if (userManager.isUserUnlocked()) {
            this.mHandler.obtainMessage(6, null).sendToTarget();
        } else {
            this.mReceiverContext.registerReceiver(this.mBootCompleted, new IntentFilter("android.intent.action.BOOT_COMPLETED"));
            if (userManager.isUserUnlocked()) {
                this.mHandler.obtainMessage(6, null).sendToTarget();
            }
        }
        bindCarrierServicesIfAvailable();
        if (MTK_IMS_PACKAGE_NAME.equals(getDeviceConfiguration(0))) {
            Log.d("ImsResolver", "Initializing for binding ims service earlier");
            for (int i = 0; i < this.mNumSlots; i++) {
                int subIdUsingPhoneId = getSubIdUsingPhoneId(i);
                Log.d("ImsResolver", "Initializing slotId: " + i + ", SubId: " + subIdUsingPhoneId);
                this.mHandler.obtainMessage(2, i, subIdUsingPhoneId).sendToTarget();
            }
        }
        this.mHandler.obtainMessage(0, null).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInstalledServicesCache() {
        for (ImsServiceInfo imsServiceInfo : getImsServiceInfo(null)) {
            if (!this.mInstalledServicesCache.containsKey(imsServiceInfo.name)) {
                this.mInstalledServicesCache.put(imsServiceInfo.name, imsServiceInfo);
            }
        }
    }

    public void destroy() {
        PhoneConfigurationManager.unregisterForMultiSimConfigChange(this.mHandler);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void bindCarrierServicesIfAvailable() {
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < this.mNumSlots; i++) {
            int subId = this.mSubscriptionManagerProxy.getSubId(i);
            Map imsPackageOverrideConfig = getImsPackageOverrideConfig(subId);
            for (int i2 = 0; i2 < 3; i2++) {
                String str = (String) imsPackageOverrideConfig.getOrDefault(Integer.valueOf(i2), "");
                if (!TextUtils.isEmpty(str)) {
                    Log.d("ImsResolver", "bindCarrierServicesIfAvailable - carrier package found: " + str + ", feature " + ((String) ImsFeature.FEATURE_LOG_MAP.getOrDefault(Integer.valueOf(i2), "invalid")) + " on slot " + i);
                    LocalLog localLog = this.mEventLog;
                    StringBuilder sb = new StringBuilder("bindCarrierServicesIfAvailable - carrier package found: ");
                    sb.append(str);
                    sb.append(" on slot ");
                    sb.append(i);
                    localLog.log(sb.toString());
                    this.mCarrierConfigReceived = true;
                    setSubId(i, subId);
                    setCarrierConfiguredPackageName(str, i, i2);
                    ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(str);
                    if (visibleImsServiceInfoFromCache == null || !visibleImsServiceInfoFromCache.featureFromMetadata) {
                        scheduleQueryForFeatures(visibleImsServiceInfoFromCache);
                        if (visibleImsServiceInfoFromCache != null) {
                            z2 = true;
                        }
                    } else {
                        z = true;
                    }
                }
            }
        }
        if (z || (this.mCarrierConfigReceived && !z2)) {
            calculateFeatureConfigurationChange();
        }
    }

    public void enableIms(final int i) {
        getImsServiceControllers(i).forEach(new Consumer() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$enableIms$1(i, (ImsServiceController) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableIms$1(int i, ImsServiceController imsServiceController) {
        imsServiceController.enableIms(i, getSubId(i));
    }

    public void disableIms(final int i) {
        getImsServiceControllers(i).forEach(new Consumer() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$disableIms$2(i, (ImsServiceController) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disableIms$2(int i, ImsServiceController imsServiceController) {
        imsServiceController.disableIms(i, getSubId(i));
    }

    public void resetIms(final int i) {
        getImsServiceControllers(i).forEach(new Consumer() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$resetIms$3(i, (ImsServiceController) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetIms$3(int i, ImsServiceController imsServiceController) {
        imsServiceController.resetIms(i, getSubId(i));
    }

    public IImsRegistration getImsRegistration(int i, int i2) {
        ImsFeatureContainer imsFeatureContainer = (ImsFeatureContainer) this.mRepo.getIfExists(i, i2).orElse(null);
        if (imsFeatureContainer != null) {
            return imsFeatureContainer.imsRegistration;
        }
        return null;
    }

    public IImsConfig getImsConfig(int i, int i2) {
        ImsFeatureContainer imsFeatureContainer = (ImsFeatureContainer) this.mRepo.getIfExists(i, i2).orElse(null);
        if (imsFeatureContainer != null) {
            return imsFeatureContainer.imsConfig;
        }
        return null;
    }

    private Set getImsServiceControllers(int i) {
        SparseArray sparseArray;
        if (i < 0 || i >= this.mNumSlots) {
            return Collections.EMPTY_SET;
        }
        synchronized (this.mBoundServicesLock) {
            sparseArray = (SparseArray) this.mBoundImsServicesByFeature.get(i);
        }
        if (sparseArray == null) {
            Log.w("ImsResolver", "getImsServiceControllers: couldn't find any active ImsServiceControllers");
            return Collections.EMPTY_SET;
        }
        ArraySet arraySet = new ArraySet(2);
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            ImsServiceController imsServiceController = (ImsServiceController) sparseArray.get(sparseArray.keyAt(i2));
            if (imsServiceController != null) {
                arraySet.add(imsServiceController);
            }
        }
        return arraySet;
    }

    public void listenForFeature(int i, int i2, IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        this.mRepo.registerForConnectionUpdates(i, i2, iImsServiceFeatureCallback, this.mRunnableExecutor);
    }

    public void unregisterImsFeatureCallback(IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        this.mRepo.unregisterForConnectionUpdates(iImsServiceFeatureCallback);
    }

    public boolean clearCarrierImsServiceConfiguration(int i) {
        if (i < 0 || i >= this.mNumSlots) {
            Log.w("ImsResolver", "clearCarrierImsServiceConfiguration: invalid slotId!");
            return false;
        }
        Message.obtain(this.mHandler, 8, i, 0).sendToTarget();
        return true;
    }

    public boolean overrideImsServiceConfiguration(String str, int i, int i2, boolean z, int[] iArr) {
        if (i < 0 || i >= this.mNumSlots) {
            Log.w("ImsResolver", "overrideImsServiceConfiguration: invalid slotId!");
            return false;
        }
        Message.obtain(this.mHandler, 5, new OverrideConfig(str, i, i2, z, iArr)).sendToTarget();
        return true;
    }

    private String getDeviceConfiguration(int i) {
        String str;
        synchronized (this.mDeviceServices) {
            str = (String) this.mDeviceServices.getOrDefault(Integer.valueOf(i), "");
        }
        return str;
    }

    public void setDeviceConfiguration(String str, int i) {
        synchronized (this.mDeviceServices) {
            this.mDeviceServices.put(Integer.valueOf(i), str);
        }
    }

    private void setCarrierConfiguredPackageName(String str, int i, int i2) {
        getCarrierConfiguredPackageNames(i).put(Integer.valueOf(i2), str);
    }

    private String getCarrierConfiguredPackageName(int i, int i2) {
        return (String) getCarrierConfiguredPackageNames(i).getOrDefault(Integer.valueOf(i2), "");
    }

    private Map getCarrierConfiguredPackageNames(int i) {
        Map map = (Map) this.mCarrierServices.get(i);
        if (map != null) {
            return map;
        }
        ArrayMap arrayMap = new ArrayMap();
        this.mCarrierServices.put(i, arrayMap);
        return arrayMap;
    }

    private Set removeOverridePackageName(int i) {
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < 3; i2++) {
            String str = (String) getOverridePackageName(i).removeReturnOld(i2);
            if (str != null) {
                hashSet.add(str);
            }
        }
        return hashSet;
    }

    private void setOverridePackageName(String str, int i, int i2) {
        getOverridePackageName(i).put(i2, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPackageNameUserOverride(String str, int i) {
        if (str == null || str.isEmpty() || i == -10000) {
            return;
        }
        Log.i("ImsResolver", "setPackageNameUserOverride: set for " + str + ", user= " + i);
        this.mImsServiceTestUserRestrictions.put(str, UserHandle.of(i));
    }

    private void clearPackageNameUserOverride(String str) {
        UserHandle userHandle = (UserHandle) this.mImsServiceTestUserRestrictions.remove(str);
        if (userHandle != null) {
            Log.i("ImsResolver", "clearPackageNameUserOverride: cleared for " + str + "on user " + userHandle);
        }
    }

    private String getOverridePackageName(int i, int i2) {
        return (String) getOverridePackageName(i).get(i2);
    }

    private SparseArray getOverridePackageName(int i) {
        SparseArray sparseArray = (SparseArray) this.mOverrideServices.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        SparseArray sparseArray2 = new SparseArray();
        this.mOverrideServices.put(i, sparseArray2);
        return sparseArray2;
    }

    private boolean doesCarrierConfigurationExist(int i, int i2) {
        String carrierConfiguredPackageName = getCarrierConfiguredPackageName(i, i2);
        if (TextUtils.isEmpty(carrierConfiguredPackageName)) {
            return false;
        }
        return doesCachedImsServiceExist(carrierConfiguredPackageName, i, i2);
    }

    private boolean doesCachedImsServiceExist(String str, final int i, final int i2) {
        ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(str);
        return visibleImsServiceInfoFromCache != null && visibleImsServiceInfoFromCache.getSupportedFeatures().stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsResolver.m1054$r8$lambda$3zbiVi_2CDXiaJzrhLgziqeCN4(i, i2, (ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$3zbiVi_2CDXiaJzrhLgziqeCN-4, reason: not valid java name */
    public static /* synthetic */ boolean m1054$r8$lambda$3zbiVi_2CDXiaJzrhLgziqeCN4(int i, int i2, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.slotId == i && featureSlotPair.featureType == i2;
    }

    public String getImsServiceConfiguration(final int i, final boolean z, final int i2) {
        if (i < 0 || i >= this.mNumSlots) {
            Log.w("ImsResolver", "getImsServiceConfiguration: invalid slotId!");
            return "";
        }
        final LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(1);
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getImsServiceConfiguration$5(linkedBlockingQueue, z, i, i2);
            }
        });
        try {
            return (String) linkedBlockingQueue.poll(5000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.w("ImsResolver", "getImsServiceConfiguration: exception=" + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getImsServiceConfiguration$5(LinkedBlockingQueue linkedBlockingQueue, boolean z, int i, int i2) {
        String deviceConfiguration;
        if (z) {
            deviceConfiguration = getCarrierConfiguredPackageName(i, i2);
        } else {
            deviceConfiguration = getDeviceConfiguration(i2);
        }
        linkedBlockingQueue.offer(deviceConfiguration);
    }

    public boolean isImsServiceConfiguredForFeature(int i, int i2) {
        if (TextUtils.isEmpty(getDeviceConfiguration(i2))) {
            return !TextUtils.isEmpty(getConfiguredImsServicePackageName(i, i2));
        }
        return true;
    }

    public String getConfiguredImsServicePackageName(final int i, final int i2) {
        String str;
        if (i < 0 || i >= this.mNumSlots || i2 <= -1 || i2 >= 3) {
            Log.w("ImsResolver", "getResolvedImsServicePackageName received invalid parameters - slot: " + i + ", feature: " + i2);
            return null;
        }
        final CompletableFuture completableFuture = new CompletableFuture();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.mHandler.getLooper().isCurrentThread()) {
            completableFuture.complete(getConfiguredImsServicePackageNameInternal(i, i2));
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getConfiguredImsServicePackageName$6(completableFuture, i, i2);
                }
            });
        }
        try {
            String str2 = (String) completableFuture.get();
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            if (jCurrentTimeMillis2 <= 50) {
                return str2;
            }
            LocalLog localLog = this.mEventLog;
            StringBuilder sb = new StringBuilder("getResolvedImsServicePackageName - [");
            sb.append(i);
            sb.append(", ");
            Map map = ImsFeature.FEATURE_LOG_MAP;
            str = null;
            try {
                sb.append((String) map.get(Integer.valueOf(i2)));
                sb.append("], async query complete, took ");
                sb.append(jCurrentTimeMillis2);
                sb.append(" ms with package name: ");
                sb.append(str2);
                localLog.log(sb.toString());
                Log.w("ImsResolver", "getResolvedImsServicePackageName: [" + i + ", " + ((String) map.get(Integer.valueOf(i2))) + "], async query complete, took " + jCurrentTimeMillis2 + " ms with package name: " + str2);
                return str2;
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            str = null;
        }
        LocalLog localLog2 = this.mEventLog;
        StringBuilder sb2 = new StringBuilder("getResolvedImsServicePackageName - [");
        sb2.append(i);
        sb2.append(", ");
        Map map2 = ImsFeature.FEATURE_LOG_MAP;
        sb2.append((String) map2.get(Integer.valueOf(i2)));
        sb2.append("] -> Exception: ");
        sb2.append(e);
        localLog2.log(sb2.toString());
        Log.w("ImsResolver", "getResolvedImsServicePackageName: [" + i + ", " + ((String) map2.get(Integer.valueOf(i2))) + "] returned Exception: " + e);
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getConfiguredImsServicePackageName$6(CompletableFuture completableFuture, int i, int i2) {
        try {
            completableFuture.complete(getConfiguredImsServicePackageNameInternal(i, i2));
        } catch (Exception e) {
            completableFuture.completeExceptionally(e);
        }
    }

    private String getConfiguredImsServicePackageNameInternal(int i, int i2) {
        String carrierConfiguredPackageName = getCarrierConfiguredPackageName(i, i2);
        if (!TextUtils.isEmpty(carrierConfiguredPackageName) && doesCachedImsServiceExist(carrierConfiguredPackageName, i, i2)) {
            return carrierConfiguredPackageName;
        }
        String deviceConfiguration = getDeviceConfiguration(i2);
        if (TextUtils.isEmpty(deviceConfiguration) || !doesCachedImsServiceExist(deviceConfiguration, i, i2)) {
            return null;
        }
        return deviceConfiguration;
    }

    private void putImsController(int i, int i2, int i3, ImsServiceController imsServiceController) {
        if (i < 0 || i >= this.mNumSlots || i3 <= -1 || i3 >= 3) {
            Log.w("ImsResolver", "putImsController received invalid parameters - slot: " + i + ", feature: " + i3);
            return;
        }
        synchronized (this.mBoundServicesLock) {
            try {
                SparseArray sparseArray = (SparseArray) this.mBoundImsServicesByFeature.get(i);
                if (sparseArray == null) {
                    sparseArray = new SparseArray();
                    this.mBoundImsServicesByFeature.put(i, sparseArray);
                }
                LocalLog localLog = this.mEventLog;
                StringBuilder sb = new StringBuilder("putImsController - [");
                sb.append(i);
                sb.append(", ");
                Map map = ImsFeature.FEATURE_LOG_MAP;
                sb.append((String) map.get(Integer.valueOf(i3)));
                sb.append("] -> ");
                sb.append(imsServiceController);
                localLog.log(sb.toString());
                Log.i("ImsResolver", "ImsServiceController added on slot: " + i + ", subId: " + i2 + " with feature: " + ((String) map.get(Integer.valueOf(i3))) + " using package: " + imsServiceController.getComponentName());
                sparseArray.put(i3, imsServiceController);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private ImsServiceController removeImsController(int i, int i2) {
        if (i < 0 || i2 <= -1 || i2 >= 3) {
            Log.w("ImsResolver", "removeImsController received invalid parameters - slot: " + i + ", feature: " + i2);
            return null;
        }
        synchronized (this.mBoundServicesLock) {
            try {
                SparseArray sparseArray = (SparseArray) this.mBoundImsServicesByFeature.get(i);
                if (sparseArray == null) {
                    return null;
                }
                ImsServiceController imsServiceController = (ImsServiceController) sparseArray.get(i2, null);
                if (imsServiceController != null) {
                    LocalLog localLog = this.mEventLog;
                    StringBuilder sb = new StringBuilder("removeImsController - [");
                    sb.append(i);
                    sb.append(", ");
                    Map map = ImsFeature.FEATURE_LOG_MAP;
                    sb.append((String) map.get(Integer.valueOf(i2)));
                    sb.append("] -> ");
                    sb.append(imsServiceController);
                    localLog.log(sb.toString());
                    Log.i("ImsResolver", "ImsServiceController removed on slot: " + i + " with feature: " + ((String) map.get(Integer.valueOf(i2))) + " using package: " + imsServiceController.getComponentName());
                    sparseArray.remove(i2);
                }
                return imsServiceController;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAddedImsService(String str) {
        Log.d("ImsResolver", "maybeAddedImsService, packageName: " + str);
        boolean z = false;
        for (ImsServiceInfo imsServiceInfo : getImsServiceInfo(str)) {
            ImsServiceInfo infoByComponentName = getInfoByComponentName(this.mInstalledServicesCache, imsServiceInfo.name);
            if (infoByComponentName != null) {
                infoByComponentName.users.clear();
                infoByComponentName.users.addAll(imsServiceInfo.users);
                boolean z2 = imsServiceInfo.featureFromMetadata;
                LocalLog localLog = this.mEventLog;
                if (z2) {
                    localLog.log("maybeAddedImsService - updating features for " + imsServiceInfo.name + ": " + printFeatures(infoByComponentName.getSupportedFeatures()) + " -> " + printFeatures(imsServiceInfo.getSupportedFeatures()));
                    Log.d("ImsResolver", "Updating features in cached ImsService: " + imsServiceInfo.name + ", old features: " + infoByComponentName + " new features: " + imsServiceInfo);
                    infoByComponentName.replaceFeatures(imsServiceInfo.getSupportedFeatures());
                    z = true;
                } else {
                    localLog.log("maybeAddedImsService - scheduling query for " + imsServiceInfo);
                    scheduleQueryForFeatures(imsServiceInfo);
                }
            } else {
                Log.i("ImsResolver", "Adding newly added ImsService to cache: " + imsServiceInfo.name);
                this.mEventLog.log("maybeAddedImsService - adding new ImsService: " + imsServiceInfo);
                this.mInstalledServicesCache.put(imsServiceInfo.name, imsServiceInfo);
                if (imsServiceInfo.featureFromMetadata) {
                    z = true;
                } else {
                    scheduleQueryForFeatures(imsServiceInfo);
                }
            }
        }
        if (z) {
            calculateFeatureConfigurationChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeRemovedImsService(String str) {
        ImsServiceInfo infoByPackageName = getInfoByPackageName(this.mInstalledServicesCache, str);
        if (infoByPackageName == null) {
            return false;
        }
        List listSearchForImsServices = searchForImsServices(str, infoByPackageName.controllerFactory);
        ImsServiceInfo imsServiceInfo = listSearchForImsServices.isEmpty() ? null : (ImsServiceInfo) listSearchForImsServices.getFirst();
        if (imsServiceInfo == null) {
            clearPackageNameUserOverride(infoByPackageName.name.getPackageName());
            this.mInstalledServicesCache.remove(infoByPackageName.name);
            this.mEventLog.log("maybeRemovedImsService - removing ImsService: " + infoByPackageName);
            Log.i("ImsResolver", "maybeRemovedImsService Removing ImsService for all users: " + infoByPackageName.name);
            unbindImsService(infoByPackageName);
        } else {
            infoByPackageName.users.clear();
            infoByPackageName.users.addAll(imsServiceInfo.users);
            this.mEventLog.log("maybeRemovedImsService - modifying ImsService users: " + infoByPackageName);
            Log.i("ImsResolver", "maybeRemovedImsService - Modifying ImsService users " + infoByPackageName);
            unbindImsService(infoByPackageName);
        }
        calculateFeatureConfigurationChange();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeRemovedImsServiceForUser(String str, UserHandle userHandle) {
        ImsServiceInfo infoByPackageName = getInfoByPackageName(this.mInstalledServicesCache, str);
        if (infoByPackageName == null) {
            return false;
        }
        this.mEventLog.log("maybeRemovedImsServiceForUser - removing ImsService " + infoByPackageName + "for user " + userHandle);
        Log.i("ImsResolver", "maybeRemovedImsServiceForUser: Removing ImsService " + infoByPackageName + "for user " + userHandle);
        unbindImsService(infoByPackageName);
        infoByPackageName.users.remove(userHandle);
        if (infoByPackageName.users.isEmpty()) {
            this.mEventLog.log("maybeRemovedImsServiceForUser - no more users, removing ImsService " + infoByPackageName);
            Log.i("ImsResolver", "maybeRemovedImsServiceForUser - no more users, removing ImsService " + infoByPackageName);
            this.mInstalledServicesCache.remove(infoByPackageName.name);
        }
        calculateFeatureConfigurationChange();
        return true;
    }

    private boolean isDeviceService(ImsServiceInfo imsServiceInfo) {
        boolean zContainsValue;
        if (imsServiceInfo == null) {
            return false;
        }
        synchronized (this.mDeviceServices) {
            zContainsValue = this.mDeviceServices.containsValue(imsServiceInfo.name.getPackageName());
        }
        return zContainsValue;
    }

    private List getSlotsForActiveCarrierService(final ImsServiceInfo imsServiceInfo) {
        if (imsServiceInfo == null) {
            return Collections.EMPTY_LIST;
        }
        if (getUserForBind(imsServiceInfo) == null) {
            Log.d("ImsResolver", "getSlotsForActiveCarrierService: ImsService " + imsServiceInfo.name + "is not configured to run for any users, skipping...");
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList(this.mNumSlots);
        for (int i = 0; i < this.mNumSlots; i++) {
            if (!TextUtils.isEmpty((CharSequence) getCarrierConfiguredPackageNames(i).values().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda22
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((String) obj).equals(imsServiceInfo.name.getPackageName());
                }
            }).findAny().orElse(""))) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    private ImsServiceController getControllerByServiceInfo(Map map, final ImsServiceInfo imsServiceInfo) {
        return (ImsServiceController) map.values().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.equals(((ImsServiceController) obj).getComponentName(), imsServiceInfo.name);
            }
        }).findFirst().orElse(null);
    }

    private ImsServiceInfo getInfoByPackageName(Map map, final String str) {
        return (ImsServiceInfo) map.values().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.equals(((ImsResolver.ImsServiceInfo) obj).name.getPackageName(), str);
            }
        }).findFirst().orElse(null);
    }

    private ImsServiceInfo getInfoByComponentName(Map map, ComponentName componentName) {
        return (ImsServiceInfo) map.get(componentName);
    }

    private void bindImsServiceWithFeatures(ImsServiceInfo imsServiceInfo, UserHandle userHandle, Set set) {
        if (shouldFeaturesCauseBind(set)) {
            ImsServiceController controllerByServiceInfo = getControllerByServiceInfo(this.mActiveControllers, imsServiceInfo);
            SparseIntArray sparseIntArrayClone = this.mSlotIdToSubIdMap.clone();
            if (controllerByServiceInfo != null) {
                try {
                    if (Objects.equals(userHandle, controllerByServiceInfo.getBoundUser())) {
                        Log.i("ImsResolver", "ImsService connection exists for " + imsServiceInfo.name + ", updating features " + set);
                        controllerByServiceInfo.changeImsServiceFeatures(set, sparseIntArrayClone);
                    } else {
                        Log.i("ImsResolver", "ImsService user changed for " + imsServiceInfo.name + ", rebinding on user " + userHandle + ", features " + set);
                        controllerByServiceInfo.unbind();
                        controllerByServiceInfo.bind(userHandle, set, sparseIntArrayClone);
                    }
                } catch (RemoteException e) {
                    Log.w("ImsResolver", "bindImsService: error=" + e.getMessage());
                }
            } else {
                controllerByServiceInfo = imsServiceInfo.controllerFactory.create(this.mContext, imsServiceInfo.name, this, this.mRepo);
                Log.i("ImsResolver", "Binding ImsService: " + controllerByServiceInfo.getComponentName() + "on user " + userHandle + " with features: " + set + ", subIdMap: " + sparseIntArrayClone);
                controllerByServiceInfo.bind(userHandle, set, sparseIntArrayClone);
                LocalLog localLog = this.mEventLog;
                StringBuilder sb = new StringBuilder("bindImsServiceWithFeatures - create new controller: ");
                sb.append(controllerByServiceInfo);
                localLog.log(sb.toString());
            }
            this.mActiveControllers.put(imsServiceInfo.name, controllerByServiceInfo);
        }
    }

    private void unbindImsService(ImsServiceInfo imsServiceInfo) {
        ImsServiceController controllerByServiceInfo;
        if (imsServiceInfo == null || (controllerByServiceInfo = getControllerByServiceInfo(this.mActiveControllers, imsServiceInfo)) == null) {
            return;
        }
        try {
            Log.i("ImsResolver", "Unbinding ImsService: " + controllerByServiceInfo.getComponentName());
            this.mEventLog.log("unbindImsService - unbinding and removing " + controllerByServiceInfo);
            controllerByServiceInfo.unbind();
        } catch (RemoteException e) {
            Log.e("ImsResolver", "unbindImsService: Remote Exception: " + e.getMessage());
        }
        this.mActiveControllers.remove(imsServiceInfo.name);
    }

    private HashSet calculateFeaturesToCreate(final ImsServiceInfo imsServiceInfo) {
        HashSet hashSet = new HashSet();
        if (!getSlotsForActiveCarrierService(imsServiceInfo).isEmpty()) {
            hashSet.addAll(imsServiceInfo.getSupportedFeatures().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda14
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$calculateFeaturesToCreate$10(imsServiceInfo, (ImsFeatureConfiguration.FeatureSlotPair) obj);
                }
            }).toList());
            return hashSet;
        }
        if (isDeviceService(imsServiceInfo)) {
            hashSet.addAll(imsServiceInfo.getSupportedFeatures().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda15
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$calculateFeaturesToCreate$11(imsServiceInfo, (ImsFeatureConfiguration.FeatureSlotPair) obj);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda16
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$calculateFeaturesToCreate$12((ImsFeatureConfiguration.FeatureSlotPair) obj);
                }
            }).toList());
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$calculateFeaturesToCreate$10(ImsServiceInfo imsServiceInfo, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return imsServiceInfo.name.getPackageName().equals(getCarrierConfiguredPackageName(featureSlotPair.slotId, featureSlotPair.featureType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$calculateFeaturesToCreate$11(ImsServiceInfo imsServiceInfo, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return imsServiceInfo.name.getPackageName().equals(getDeviceConfiguration(featureSlotPair.featureType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$calculateFeaturesToCreate$12(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return !doesCarrierConfigurationExist(featureSlotPair.slotId, featureSlotPair.featureType);
    }

    @Override // com.android.internal.telephony.ims.ImsServiceController.ImsServiceControllerCallbacks
    public void imsServiceFeatureCreated(int i, int i2, int i3, ImsServiceController imsServiceController) {
        putImsController(i, i2, i3, imsServiceController);
    }

    @Override // com.android.internal.telephony.ims.ImsServiceController.ImsServiceControllerCallbacks
    public void imsServiceFeatureRemoved(int i, int i2, ImsServiceController imsServiceController) {
        removeImsController(i, i2);
    }

    @Override // com.android.internal.telephony.ims.ImsServiceController.ImsServiceControllerCallbacks
    public void imsServiceFeaturesChanged(ImsFeatureConfiguration imsFeatureConfiguration, ImsServiceController imsServiceController) {
        if (imsServiceController == null || imsFeatureConfiguration == null) {
            return;
        }
        Log.i("ImsResolver", "imsServiceFeaturesChanged: config=" + imsFeatureConfiguration.getServiceFeatures() + ", ComponentName=" + imsServiceController.getComponentName());
        this.mEventLog.log("imsServiceFeaturesChanged - for " + imsServiceController + ", new config " + imsFeatureConfiguration.getServiceFeatures());
        handleFeaturesChanged(imsServiceController.getComponentName(), imsFeatureConfiguration.getServiceFeatures());
    }

    @Override // com.android.internal.telephony.ims.ImsServiceController.ImsServiceControllerCallbacks
    public void imsServiceBindPermanentError(ComponentName componentName, UserHandle userHandle) {
        if (componentName == null) {
            return;
        }
        Log.w("ImsResolver", "imsServiceBindPermanentError: component=" + componentName + ", user=" + userHandle);
        this.mEventLog.log("imsServiceBindPermanentError - for " + componentName + ", user " + userHandle);
        this.mHandler.obtainMessage(10, new Pair(componentName.getPackageName(), userHandle)).sendToTarget();
    }

    private boolean shouldFeaturesCauseBind(Set set) {
        return set.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsResolver.$r8$lambda$t4o51oVdXPrC2nSVzEojrLqhrPA((ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        }).count() > 0;
    }

    public static /* synthetic */ boolean $r8$lambda$t4o51oVdXPrC2nSVzEojrLqhrPA(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.featureType != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void overrideCarrierService(int i, Map map) {
        for (Integer num : map.keySet()) {
            String str = (String) map.get(num);
            this.mEventLog.log("overriding carrier ImsService to " + str + " on slot " + i + " for feature " + ((String) ImsFeature.FEATURE_LOG_MAP.getOrDefault(num, "invalid")));
            setOverridePackageName(str, i, num.intValue());
        }
        updateBoundServices(i, Collections.EMPTY_MAP);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCarrierServiceOverrides(int i) {
        Log.i("ImsResolver", "clearing carrier ImsService overrides");
        this.mEventLog.log("clearing carrier ImsService overrides");
        Iterator it = removeOverridePackageName(i).iterator();
        while (it.hasNext()) {
            clearPackageNameUserOverride((String) it.next());
        }
        carrierConfigChanged(i, getSubId(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void overrideDeviceService(Map map) {
        boolean z = false;
        for (Integer num : map.keySet()) {
            String str = (String) map.get(num);
            this.mEventLog.log("overriding device ImsService to " + str + " for feature " + ((String) ImsFeature.FEATURE_LOG_MAP.getOrDefault(num, "invalid")));
            String deviceConfiguration = getDeviceConfiguration(num.intValue());
            if (!TextUtils.equals(deviceConfiguration, str)) {
                Log.i("ImsResolver", "overrideDeviceService - device package changed (override): " + deviceConfiguration + " -> " + str);
                this.mEventLog.log("overrideDeviceService - device package changed (override): " + deviceConfiguration + " -> " + str);
                clearPackageNameUserOverride(deviceConfiguration);
                setDeviceConfiguration(str, num.intValue());
                ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(str);
                if (visibleImsServiceInfoFromCache == null || visibleImsServiceInfoFromCache.featureFromMetadata) {
                    z = true;
                } else {
                    scheduleQueryForFeatures(visibleImsServiceInfoFromCache);
                }
            }
        }
        if (z) {
            calculateFeatureConfigurationChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void carrierConfigChanged(int i, int i2) {
        setSubId(i, i2);
        updateBoundDeviceServices();
        if (i <= -1) {
            for (int i3 = 0; i3 < this.mNumSlots; i3++) {
                updateBoundServices(i3, getImsPackageOverrideConfig(getSubId(i3)));
            }
        }
        updateBoundServices(i, getImsPackageOverrideConfig(i2));
    }

    private void updateBoundDeviceServices() {
        Log.d("ImsResolver", "updateBoundDeviceServices: called");
        ArrayMap arrayMap = new ArrayMap();
        for (int i = 0; i < 3; i++) {
            String deviceConfiguration = getDeviceConfiguration(i);
            ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(deviceConfiguration);
            if (visibleImsServiceInfoFromCache != null && !visibleImsServiceInfoFromCache.featureFromMetadata && !arrayMap.containsKey(deviceConfiguration)) {
                arrayMap.put(deviceConfiguration, visibleImsServiceInfoFromCache);
                Log.d("ImsResolver", "updateBoundDeviceServices: Schedule query for package=" + deviceConfiguration);
                scheduleQueryForFeatures((ImsServiceInfo) arrayMap.get(deviceConfiguration));
            }
        }
    }

    private void updateBoundServices(int i, Map map) {
        if (i <= -1 || i >= this.mNumSlots) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        for (int i2 = 0; i2 < 3; i2++) {
            String overridePackageName = getOverridePackageName(i, i2);
            String carrierConfiguredPackageName = getCarrierConfiguredPackageName(i, i2);
            String str = (String) map.getOrDefault(Integer.valueOf(i2), "");
            if (TextUtils.isEmpty(overridePackageName)) {
                overridePackageName = str;
            } else {
                if (TextUtils.isEmpty(str)) {
                    str = "(none)";
                }
                Log.i("ImsResolver", String.format("updateBoundServices: overriding %s with %s for feature %s on slot %d", str, overridePackageName, ImsFeature.FEATURE_LOG_MAP.getOrDefault(Integer.valueOf(i2), "invalid"), Integer.valueOf(i)));
            }
            setCarrierConfiguredPackageName(overridePackageName, i, i2);
            ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(overridePackageName);
            if (visibleImsServiceInfoFromCache == null || visibleImsServiceInfoFromCache.featureFromMetadata) {
                z = true;
            } else {
                scheduleQueryForFeatures(visibleImsServiceInfoFromCache);
                z2 = true;
            }
            Log.i("ImsResolver", "updateBoundServices - carrier package changed: " + carrierConfiguredPackageName + " -> " + overridePackageName + " on slot " + i + ", hasConfigChanged=" + z);
            this.mEventLog.log("updateBoundServices - carrier package changed: " + carrierConfiguredPackageName + " -> " + overridePackageName + " on slot " + i + ", hasConfigChanged=" + z);
        }
        if (z) {
            calculateFeatureConfigurationChange();
        }
        if (z && z2) {
            this.mEventLog.log("[warning] updateBoundServices - both hasConfigChange and query scheduled on slot " + i);
        }
    }

    private Map getImsPackageOverrideConfig(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        if (configForSubId == null && MTK_IMS_PACKAGE_NAME.equals(getDeviceConfiguration(0))) {
            HashMap map = new HashMap();
            map.put(0, MTK_IMS_PACKAGE_NAME);
            map.put(1, MTK_IMS_PACKAGE_NAME);
            map.put(2, MTK_IMS_PACKAGE_NAME);
            Log.i("ImsResolver", "getImsPackageOverrideConfig, ims/rcs config-1:com.mediatek.ims");
            return map;
        }
        if (configForSubId == null) {
            return Collections.EMPTY_MAP;
        }
        String string = configForSubId.getString("config_ims_package_override_string", null);
        String string2 = configForSubId.getString("config_ims_mmtel_package_override_string", string);
        String string3 = configForSubId.getString("config_ims_rcs_package_override_string", string);
        ArrayMap arrayMap = new ArrayMap();
        Log.d("ImsResolver", "getImsPackageOverrideConfig, ims/rcs config-2:" + string2 + ", " + string3);
        if (!TextUtils.isEmpty(string2)) {
            arrayMap.put(0, string2);
            arrayMap.put(1, string2);
        }
        if (!TextUtils.isEmpty(string3)) {
            arrayMap.put(2, string3);
        }
        return arrayMap;
    }

    private void scheduleQueryForFeatures(ImsServiceInfo imsServiceInfo, int i) {
        if (imsServiceInfo == null) {
            return;
        }
        Message messageObtain = Message.obtain(this.mHandler, 3, imsServiceInfo);
        boolean zHasMessages = this.mHandler.hasMessages(3, imsServiceInfo);
        ComponentName componentName = imsServiceInfo.name;
        if (zHasMessages) {
            Log.d("ImsResolver", "scheduleQueryForFeatures: dynamic query for " + componentName + " already scheduled");
            return;
        }
        Log.d("ImsResolver", "scheduleQueryForFeatures: starting dynamic query for " + componentName + " in " + i + "ms.");
        this.mHandler.sendMessageDelayed(messageObtain, (long) i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleQueryForFeatures(ComponentName componentName, int i) {
        ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(componentName.getPackageName());
        if (visibleImsServiceInfoFromCache == null) {
            Log.w("ImsResolver", "scheduleQueryForFeatures: Couldn't find cached info for name: " + componentName);
            return;
        }
        scheduleQueryForFeatures(visibleImsServiceInfoFromCache, i);
    }

    private void scheduleQueryForFeatures(ImsServiceInfo imsServiceInfo) {
        scheduleQueryForFeatures(imsServiceInfo, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFeaturesChanged(ComponentName componentName, Set set) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = componentName;
        someArgsObtain.arg2 = set;
        this.mHandler.obtainMessage(4, someArgsObtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMsimConfigChange(final Integer num) {
        int i = this.mNumSlots;
        if (i == num.intValue()) {
            return;
        }
        this.mNumSlots = num.intValue();
        Log.i("ImsResolver", "handleMsimConfigChange: oldLen=" + i + ", newLen=" + num);
        this.mEventLog.log("MSIM config change: " + i + " -> " + num);
        if (num.intValue() < i) {
            for (int iIntValue = num.intValue(); iIntValue < i; iIntValue++) {
                Iterator it = getCarrierConfiguredPackageNames(iIntValue).keySet().iterator();
                while (it.hasNext()) {
                    setCarrierConfiguredPackageName("", iIntValue, ((Integer) it.next()).intValue());
                }
                SparseArray overridePackageName = getOverridePackageName(iIntValue);
                for (int i2 = 0; i2 < overridePackageName.size(); i2++) {
                    setOverridePackageName("", iIntValue, overridePackageName.keyAt(i2));
                }
                removeSlotId(iIntValue);
            }
        } else if (num.intValue() > i) {
            for (int i3 = i; i3 < num.intValue(); i3++) {
                int subIdUsingPhoneId = getSubIdUsingPhoneId(i3);
                Log.d("ImsResolver", "handleMsimConfigChange slotId: " + i3 + ", SubId: " + subIdUsingPhoneId);
                setSubId(i3, subIdUsingPhoneId);
            }
        }
        for (ImsServiceInfo imsServiceInfo : getImsServiceInfo(null)) {
            ImsServiceInfo imsServiceInfo2 = (ImsServiceInfo) this.mInstalledServicesCache.get(imsServiceInfo.name);
            if (imsServiceInfo2 != null) {
                if (imsServiceInfo.featureFromMetadata) {
                    imsServiceInfo2.replaceFeatures(imsServiceInfo.getSupportedFeatures());
                } else {
                    imsServiceInfo2.getSupportedFeatures().removeIf(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda13
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return ImsResolver.$r8$lambda$OsDdu4V2CfEwpXM9ysLhTWArF0o(num, (ImsFeatureConfiguration.FeatureSlotPair) obj);
                        }
                    });
                }
            } else {
                this.mEventLog.log("handleMsimConfigChange: detected untracked service - " + imsServiceInfo);
                Log.w("ImsResolver", "handleMsimConfigChange: detected untracked package, queueing to add " + imsServiceInfo);
                this.mHandler.obtainMessage(0, imsServiceInfo.name.getPackageName()).sendToTarget();
            }
        }
        if (num.intValue() < i) {
            calculateFeatureConfigurationChange();
        }
    }

    public static /* synthetic */ boolean $r8$lambda$OsDdu4V2CfEwpXM9ysLhTWArF0o(Integer num, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.slotId >= num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDynamicQuery(ImsServiceInfo imsServiceInfo) {
        UserHandle userForBind = getUserForBind(imsServiceInfo);
        if (userForBind == null) {
            Log.i("ImsResolver", "scheduleQueryForFeatures: skipping query for ImsService that is not running: " + imsServiceInfo);
        } else {
            if (!isDeviceService(imsServiceInfo) && getSlotsForActiveCarrierService(imsServiceInfo).isEmpty()) {
                Log.i("ImsResolver", "scheduleQueryForFeatures: skipping query for ImsService that is not set as carrier/device ImsService.");
                return;
            }
            this.mEventLog.log("startDynamicQuery - starting query for " + imsServiceInfo);
            if (!this.mFeatureQueryManager.startQuery(imsServiceInfo.name, userForBind, imsServiceInfo.controllerFactory.getServiceInterface())) {
                Log.w("ImsResolver", "startDynamicQuery: service could not connect. Retrying after delay.");
                this.mEventLog.log("startDynamicQuery - query failed. Retrying in 5000 mS");
                scheduleQueryForFeatures(imsServiceInfo, GbaManager.REQUEST_TIMEOUT_MS);
                return;
            }
            Log.d("ImsResolver", "startDynamicQuery: Service queried, waiting for response.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dynamicQueryComplete(ComponentName componentName, Set set) {
        ImsServiceInfo visibleImsServiceInfoFromCache = getVisibleImsServiceInfoFromCache(componentName.getPackageName());
        if (visibleImsServiceInfoFromCache == null) {
            Log.w("ImsResolver", "dynamicQueryComplete: Couldn't find cached info for name: " + componentName);
            return;
        }
        sanitizeFeatureConfig(set);
        this.mEventLog.log("dynamicQueryComplete: for package " + componentName + ", features: " + printFeatures(visibleImsServiceInfoFromCache.getSupportedFeatures()) + " -> " + printFeatures(set));
        visibleImsServiceInfoFromCache.replaceFeatures(set);
        if (this.mFeatureQueryManager.isQueryInProgress()) {
            return;
        }
        if (this.mHandler.hasMessages(4)) {
            this.mEventLog.log("[warning] dynamicQueryComplete - HANDLER_DYNAMIC_FEATURE_CHANGE pending with calculateFeatureConfigurationChange()");
        }
        calculateFeatureConfigurationChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$sanitizeFeatureConfig$15(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.slotId >= this.mNumSlots;
    }

    private void sanitizeFeatureConfig(Set set) {
        set.removeIf(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$sanitizeFeatureConfig$15((ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        });
        for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair : (Set) set.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsResolver.m1055$r8$lambda$vwgU6xq694VOVm_WPQRaxsNcK4((ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        }).collect(Collectors.toSet())) {
            if (!set.contains(new ImsFeatureConfiguration.FeatureSlotPair(featureSlotPair.slotId, 1))) {
                set.remove(featureSlotPair);
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$vwgU6xq694V-OVm_WPQRaxsNcK4, reason: not valid java name */
    public static /* synthetic */ boolean m1055$r8$lambda$vwgU6xq694VOVm_WPQRaxsNcK4(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.featureType == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateFeatureConfigurationChange() {
        for (ImsServiceInfo imsServiceInfo : this.mInstalledServicesCache.values()) {
            HashSet hashSetCalculateFeaturesToCreate = calculateFeaturesToCreate(imsServiceInfo);
            UserHandle userForBind = getUserForBind(imsServiceInfo);
            if (!shouldFeaturesCauseBind(hashSetCalculateFeaturesToCreate) || userForBind == null) {
                unbindImsService(imsServiceInfo);
            }
        }
        for (ImsServiceInfo imsServiceInfo2 : this.mInstalledServicesCache.values()) {
            HashSet hashSetCalculateFeaturesToCreate2 = calculateFeaturesToCreate(imsServiceInfo2);
            UserHandle userForBind2 = getUserForBind(imsServiceInfo2);
            if (shouldFeaturesCauseBind(hashSetCalculateFeaturesToCreate2) && userForBind2 != null) {
                bindImsServiceWithFeatures(imsServiceInfo2, userForBind2, hashSetCalculateFeaturesToCreate2);
            }
        }
    }

    private UserHandle getUserForBind(ImsServiceInfo imsServiceInfo) {
        final UserHandle currentUser = this.mActivityManagerProxy.getCurrentUser();
        Stream stream = getActiveUsers().stream();
        final Set<UserHandle> set = imsServiceInfo.users;
        Objects.requireNonNull(set);
        List list = stream.filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return set.contains((UserHandle) obj);
            }
        }).toList();
        if (list.isEmpty()) {
            return null;
        }
        final UserHandle userHandle = (UserHandle) this.mImsServiceTestUserRestrictions.getOrDefault(imsServiceInfo.name.getPackageName(), null);
        if (userHandle != null && list.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.equals((UserHandle) obj, userHandle);
            }
        })) {
            return userHandle;
        }
        if (list.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getUserForBind$18((UserHandle) obj);
            }
        })) {
            return this.mContext.getUser();
        }
        if (list.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.equals((UserHandle) obj, currentUser);
            }
        })) {
            return currentUser;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getUserForBind$18(UserHandle userHandle) {
        return Objects.equals(userHandle, this.mContext.getUser());
    }

    private Set getActiveUsers() {
        HashSet hashSet = new HashSet();
        hashSet.add(this.mContext.getUser());
        hashSet.add(this.mActivityManagerProxy.getCurrentUser());
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String printFeatures(Set set) {
        StringBuilder sb = new StringBuilder(" features: [");
        if (set != null) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                ImsFeatureConfiguration.FeatureSlotPair featureSlotPair = (ImsFeatureConfiguration.FeatureSlotPair) it.next();
                sb.append("{");
                sb.append(featureSlotPair.slotId);
                sb.append(",");
                sb.append((String) ImsFeature.FEATURE_LOG_MAP.get(Integer.valueOf(featureSlotPair.featureType)));
                sb.append("}");
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public ImsServiceInfo getVisibleImsServiceInfoFromCache(String str) {
        ImsServiceInfo imsServiceInfoFromCache = getImsServiceInfoFromCache(str);
        if (imsServiceInfoFromCache == null) {
            return null;
        }
        UserHandle userForBind = getUserForBind(imsServiceInfoFromCache);
        Log.d("ImsResolver", "getVisibleImsServiceInfoFromCache: " + str + ", match=" + imsServiceInfoFromCache + ", targetUser=" + userForBind);
        if (userForBind != null) {
            return imsServiceInfoFromCache;
        }
        return null;
    }

    public ImsServiceInfo getImsServiceInfoFromCache(String str) {
        ImsServiceInfo infoByPackageName;
        if (TextUtils.isEmpty(str) || (infoByPackageName = getInfoByPackageName(this.mInstalledServicesCache, str)) == null) {
            return null;
        }
        return infoByPackageName;
    }

    private List getImsServiceInfo(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(searchForImsServices(str, this.mImsServiceControllerFactory));
        arrayList.addAll(searchForImsServices(str, this.mImsServiceControllerFactoryCompat));
        return arrayList;
    }

    private ImsServiceInfo getInfoFromCache(List list, final ComponentName componentName) {
        return (ImsServiceInfo) list.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsResolver$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.equals(((ImsResolver.ImsServiceInfo) obj).name, componentName);
            }
        }).findFirst().orElse(null);
    }

    private List searchForImsServices(String str, ImsServiceControllerFactory imsServiceControllerFactory) {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent(imsServiceControllerFactory.getServiceInterface());
        intent.setPackage(str);
        Set<UserHandle> activeUsers = getActiveUsers();
        Log.v("ImsResolver", "searchForImsServices: package=" + str + ", users=" + activeUsers);
        PackageManager packageManager = this.mContext.getPackageManager();
        for (UserHandle userHandle : activeUsers) {
            Iterator it = packageManager.queryIntentServicesAsUser(intent, 128, userHandle).iterator();
            while (it.hasNext()) {
                ServiceInfo serviceInfo = ((ResolveInfo) it.next()).serviceInfo;
                if (serviceInfo != null) {
                    ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                    ImsServiceInfo infoFromCache = getInfoFromCache(arrayList, componentName);
                    if (infoFromCache != null) {
                        infoFromCache.users.add(userHandle);
                        Log.d("ImsResolver", "service modify users:" + infoFromCache);
                    } else {
                        ImsServiceInfo imsServiceInfo = new ImsServiceInfo(componentName);
                        imsServiceInfo.users.add(userHandle);
                        imsServiceInfo.controllerFactory = imsServiceControllerFactory;
                        if (isDeviceService(imsServiceInfo) || this.mImsServiceControllerFactoryCompat == imsServiceControllerFactory) {
                            Bundle bundle = serviceInfo.metaData;
                            if (bundle != null) {
                                if (bundle.getBoolean(METADATA_MMTEL_FEATURE, false)) {
                                    imsServiceInfo.addFeatureForAllSlots(this.mNumSlots, 1);
                                    if (serviceInfo.metaData.getBoolean(METADATA_EMERGENCY_MMTEL_FEATURE, false)) {
                                        imsServiceInfo.addFeatureForAllSlots(this.mNumSlots, 0);
                                    }
                                }
                                if (serviceInfo.metaData.getBoolean(METADATA_RCS_FEATURE, false)) {
                                    imsServiceInfo.addFeatureForAllSlots(this.mNumSlots, 2);
                                }
                            }
                            if (this.mImsServiceControllerFactoryCompat != imsServiceControllerFactory && imsServiceInfo.getSupportedFeatures().isEmpty()) {
                                imsServiceInfo.featureFromMetadata = false;
                            }
                        } else {
                            imsServiceInfo.featureFromMetadata = false;
                        }
                        Log.d("ImsResolver", "service name: " + imsServiceInfo.name + ", manifest query: " + imsServiceInfo.featureFromMetadata + ", users: " + imsServiceInfo.users);
                        if (TextUtils.equals(serviceInfo.permission, "android.permission.BIND_IMS_SERVICE") || serviceInfo.metaData.getBoolean("override_bind_check", false)) {
                            arrayList.add(imsServiceInfo);
                        } else {
                            Log.w("ImsResolver", "ImsService is not protected with BIND_IMS_SERVICE permission: " + imsServiceInfo.name);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubIdUsingPhoneId(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length < 1) {
            return -1;
        }
        return subId[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceContollerMap(int i, int i2) {
        Iterator it = getImsServiceControllers(i).iterator();
        while (it.hasNext()) {
            ((ImsServiceController) it.next()).updateSlotIdToSubIdMap(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSubId(int i, int i2) {
        synchronized (this.mSlotIdToSubIdMap) {
            this.mSlotIdToSubIdMap.put(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubId(int i) {
        int i2;
        synchronized (this.mSlotIdToSubIdMap) {
            i2 = this.mSlotIdToSubIdMap.get(i, -1);
        }
        return i2;
    }

    private void removeSlotId(int i) {
        synchronized (this.mSlotIdToSubIdMap) {
            this.mSlotIdToSubIdMap.delete(i);
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("ImsResolver:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Configurations:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Device:");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mDeviceServices) {
            try {
                for (Integer num : this.mDeviceServices.keySet()) {
                    indentingPrintWriter.println(((String) ImsFeature.FEATURE_LOG_MAP.get(num)) + " -> " + ((String) this.mDeviceServices.get(num)));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Carrier: ");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mNumSlots; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                indentingPrintWriter.print("slot=");
                indentingPrintWriter.print(i);
                indentingPrintWriter.print(", feature=");
                indentingPrintWriter.print((String) ImsFeature.FEATURE_LOG_MAP.getOrDefault(Integer.valueOf(i2), "?"));
                indentingPrintWriter.println(": ");
                indentingPrintWriter.increaseIndent();
                String carrierConfiguredPackageName = getCarrierConfiguredPackageName(i, i2);
                if (TextUtils.isEmpty(carrierConfiguredPackageName)) {
                    carrierConfiguredPackageName = "none";
                }
                indentingPrintWriter.println(carrierConfiguredPackageName);
                indentingPrintWriter.decreaseIndent();
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Cached ImsServices:");
        indentingPrintWriter.increaseIndent();
        Iterator it = this.mInstalledServicesCache.values().iterator();
        while (it.hasNext()) {
            indentingPrintWriter.println((ImsServiceInfo) it.next());
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Active controllers:");
        indentingPrintWriter.increaseIndent();
        for (ImsServiceController imsServiceController : this.mActiveControllers.values()) {
            indentingPrintWriter.println(imsServiceController);
            indentingPrintWriter.increaseIndent();
            imsServiceController.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Connection Repository Log:");
        indentingPrintWriter.increaseIndent();
        this.mRepo.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Event Log:");
        indentingPrintWriter.increaseIndent();
        this.mEventLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }
}
