package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ChangedPackages;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.permission.LegacyPermissionManager;
import android.telephony.AnomalyReporter;
import android.telephony.ims.ImsService;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsServiceController;
import android.telephony.ims.aidl.ISipTransport;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.ims.ImsFeatureBinderRepository;
import com.android.ims.ImsFeatureContainer;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceController {
    private final UUID mAnomalyUUID;
    private ExponentialBackoff mBackoff;
    private UserHandle mBoundUser;
    private ImsServiceControllerCallbacks mCallbacks;
    private ChangedPackages mChangedPackages;
    private final ComponentName mComponentName;
    protected final Context mContext;
    protected final ExecutorService mExecutor;
    private ImsService.Listener mFeatureChangedListener;
    private Set mFeatureStatusCallbacks;
    protected final Handler mHandler;
    private final HandlerThread mHandlerThread;
    private IImsServiceController mIImsServiceController;
    private final ImsEnablementTracker mImsEnablementTracker;
    private Set mImsFeatures;
    protected ImsServiceConnection mImsServiceConnection;
    private boolean mIsBinding;
    private boolean mIsBound;
    private int mLastSequenceNumber;
    private final LocalLog mLocalLog;
    protected final Object mLock;
    private PackageManager mPackageManager;
    private final LegacyPermissionManager mPermissionManager;
    private RebindRetry mRebindRetry;
    private ImsFeatureBinderRepository mRepo;
    private Runnable mRestartImsServiceRunnable;
    private long mServiceCapabilities;
    private SparseIntArray mSlotIdToSubIdMap;

    public interface ImsServiceControllerCallbacks {
        void imsServiceBindPermanentError(ComponentName componentName, UserHandle userHandle);

        void imsServiceFeatureCreated(int i, int i2, int i3, ImsServiceController imsServiceController);

        void imsServiceFeatureRemoved(int i, int i2, ImsServiceController imsServiceController);

        void imsServiceFeaturesChanged(ImsFeatureConfiguration imsFeatureConfiguration, ImsServiceController imsServiceController);
    }

    public interface RebindRetry {
        long getMaximumDelay();

        long getStartDelay();
    }

    class ImsServiceConnection implements ServiceConnection {
        private boolean mIsServiceConnectionDead = false;

        ImsServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(final ComponentName componentName, final IBinder iBinder) {
            if (ImsServiceController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onServiceConnected$0(componentName, iBinder);
            } else {
                ImsServiceController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$ImsServiceConnection$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceConnected$0(componentName, iBinder);
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(final ComponentName componentName) {
            if (ImsServiceController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onServiceDisconnected$1(componentName);
            } else {
                ImsServiceController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$ImsServiceConnection$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onServiceDisconnected$1(componentName);
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(final ComponentName componentName) {
            if (ImsServiceController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onBindingDied$2(componentName);
            } else {
                ImsServiceController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$ImsServiceConnection$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBindingDied$2(componentName);
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(final ComponentName componentName) {
            if (ImsServiceController.this.mHandler.getLooper().isCurrentThread()) {
                lambda$onNullBinding$3(componentName);
            } else {
                ImsServiceController.this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$ImsServiceConnection$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onNullBinding$3(componentName);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onServiceConnectedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onServiceConnected$0(ComponentName componentName, IBinder iBinder) {
            synchronized (ImsServiceController.this.mLock) {
                ImsServiceController.this.mBackoff.stop();
                ImsServiceController.this.mIsBound = true;
                ImsServiceController.this.mIsBinding = false;
                ImsServiceController.this.mLocalLog.log("onServiceConnectedInternal");
                Log.d("ImsServiceController", "ImsService(" + componentName + "): onServiceConnectedInternal with binder: " + iBinder);
                ImsServiceController.this.setServiceController(iBinder);
                ImsServiceController.this.notifyImsServiceReady();
            }
        }

        protected void updateCapabilityAndServiceFeature() {
            synchronized (ImsServiceController.this.mLock) {
                try {
                    ImsServiceController.this.retrieveStaticImsServiceCapabilities();
                    for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair : ImsServiceController.this.mImsFeatures) {
                        ImsServiceController imsServiceController = ImsServiceController.this;
                        long jModifyCapabilitiesForSlot = imsServiceController.modifyCapabilitiesForSlot(imsServiceController.mImsFeatures, featureSlotPair.slotId, ImsServiceController.this.mServiceCapabilities);
                        ImsServiceController imsServiceController2 = ImsServiceController.this;
                        imsServiceController2.addImsServiceFeature(featureSlotPair, jModifyCapabilitiesForSlot, imsServiceController2.mSlotIdToSubIdMap.get(featureSlotPair.slotId));
                    }
                } catch (RemoteException e) {
                    cleanAndRestart(e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanAndRestart(RemoteException remoteException) {
            ImsServiceController.this.mIsBound = false;
            ImsServiceController.this.mIsBinding = false;
            cleanupConnection();
            ImsServiceController.this.unbindService();
            ImsServiceController.this.startDelayedRebindToService();
            ImsServiceController.this.mLocalLog.log("onConnected exception=" + remoteException.getMessage() + ", retry in " + ImsServiceController.this.mBackoff.getCurrentDelay() + " mS");
            StringBuilder sb = new StringBuilder("ImsService RemoteException:");
            sb.append(remoteException.getMessage());
            Log.e("ImsServiceController", sb.toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onServiceDisconnectedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onServiceDisconnected$1(ComponentName componentName) {
            synchronized (ImsServiceController.this.mLock) {
                ImsServiceController.this.mIsBinding = false;
                cleanupConnection();
            }
            ImsServiceController.this.mLocalLog.log("onServiceDisconnectedInternal");
            Log.w("ImsServiceController", "ImsService(" + componentName + "): onServiceDisconnectedInternal. Waiting...");
            ImsServiceController.this.checkAndReportAnomaly(componentName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onBindingDiedInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onBindingDied$2(ComponentName componentName) {
            this.mIsServiceConnectionDead = true;
            synchronized (ImsServiceController.this.mLock) {
                ImsServiceController.this.mIsBinding = false;
                ImsServiceController.this.mIsBound = false;
                cleanupConnection();
                ImsServiceController.this.unbindService();
                ImsServiceController.this.startDelayedRebindToService();
            }
            Log.w("ImsServiceController", "ImsService(" + componentName + "): onBindingDiedInternal. Starting rebind...");
            ImsServiceController.this.mLocalLog.log("onBindingDiedInternal, retrying in " + ImsServiceController.this.mBackoff.getCurrentDelay() + " mS");
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onNullBindingInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$onNullBinding$3(ComponentName componentName) {
            Log.w("ImsServiceController", "ImsService(" + componentName + "): onNullBindingInternal. Is service dead = " + this.mIsServiceConnectionDead);
            LocalLog localLog = ImsServiceController.this.mLocalLog;
            StringBuilder sb = new StringBuilder("onNullBindingInternal, is service dead = ");
            sb.append(this.mIsServiceConnectionDead);
            localLog.log(sb.toString());
            if (this.mIsServiceConnectionDead) {
                return;
            }
            synchronized (ImsServiceController.this.mLock) {
                ImsServiceController.this.mIsBinding = false;
                ImsServiceController.this.mIsBound = true;
                cleanupConnection();
            }
            if (ImsServiceController.this.mCallbacks != null) {
                ImsServiceController.this.mCallbacks.imsServiceBindPermanentError(ImsServiceController.this.getComponentName(), ImsServiceController.this.mBoundUser);
            }
        }

        private void cleanupConnection() {
            ImsServiceController.this.cleanupAllFeatures();
            ImsServiceController.this.setServiceController(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ImsFeatureStatusCallback {
        private final IImsFeatureStatusCallback mCallback = new IImsFeatureStatusCallback.Stub() { // from class: com.android.internal.telephony.ims.ImsServiceController.ImsFeatureStatusCallback.1
            public void notifyImsFeatureStatus(int i) throws RemoteException {
                Log.i("ImsServiceController", "notifyImsFeatureStatus: slot=" + ImsFeatureStatusCallback.this.mSlotId + ", feature=" + ((String) ImsFeature.FEATURE_LOG_MAP.get(Integer.valueOf(ImsFeatureStatusCallback.this.mFeatureType))) + ", status=" + ((String) ImsFeature.STATE_LOG_MAP.get(Integer.valueOf(i))));
                ImsServiceController.this.mRepo.notifyFeatureStateChanged(ImsFeatureStatusCallback.this.mSlotId, ImsFeatureStatusCallback.this.mFeatureType, i);
            }
        };
        private int mFeatureType;
        private int mSlotId;

        ImsFeatureStatusCallback(int i, int i2) {
            this.mSlotId = i;
            this.mFeatureType = i2;
        }

        public IImsFeatureStatusCallback getCallback() {
            return this.mCallback;
        }
    }

    public ImsServiceController(Context context, ComponentName componentName, ImsServiceControllerCallbacks imsServiceControllerCallbacks, ImsFeatureBinderRepository imsFeatureBinderRepository) {
        this.mAnomalyUUID = UUID.fromString("e93b05e4-6d0a-4755-a6da-a2d2dbfb10d6");
        this.mLastSequenceNumber = 0;
        this.mIsBound = false;
        this.mIsBinding = false;
        this.mBoundUser = null;
        this.mFeatureStatusCallbacks = new HashSet();
        this.mLocalLog = new LocalLog(8);
        this.mLock = new Object();
        this.mFeatureChangedListener = new ImsService.Listener() { // from class: com.android.internal.telephony.ims.ImsServiceController.1
            public void onUpdateSupportedImsFeatures(ImsFeatureConfiguration imsFeatureConfiguration) {
                if (ImsServiceController.this.mCallbacks == null) {
                    return;
                }
                ImsServiceController.this.mLocalLog.log("onUpdateSupportedImsFeatures to " + imsFeatureConfiguration.getServiceFeatures());
                ImsServiceController.this.mCallbacks.imsServiceFeaturesChanged(imsFeatureConfiguration, ImsServiceController.this);
            }
        };
        this.mRestartImsServiceRunnable = new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (ImsServiceController.this.mLock) {
                    try {
                        if (ImsServiceController.this.mIsBound) {
                            return;
                        }
                        ImsServiceController imsServiceController = ImsServiceController.this;
                        imsServiceController.bind(imsServiceController.mBoundUser, ImsServiceController.this.mImsFeatures, ImsServiceController.this.mSlotIdToSubIdMap);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        };
        this.mRebindRetry = new RebindRetry(this) { // from class: com.android.internal.telephony.ims.ImsServiceController.3
            @Override // com.android.internal.telephony.ims.ImsServiceController.RebindRetry
            public long getMaximumDelay() {
                return 60000L;
            }

            @Override // com.android.internal.telephony.ims.ImsServiceController.RebindRetry
            public long getStartDelay() {
                return 2000L;
            }
        };
        this.mContext = context;
        this.mComponentName = componentName;
        this.mCallbacks = imsServiceControllerCallbacks;
        HandlerThread handlerThread = new HandlerThread("ImsServiceControllerHandler");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        this.mHandler = handler;
        Looper looper = handlerThread.getLooper();
        this.mBackoff = new ExponentialBackoff(this.mRebindRetry.getStartDelay(), this.mRebindRetry.getMaximumDelay(), 2, handler, this.mRestartImsServiceRunnable);
        this.mPermissionManager = (LegacyPermissionManager) context.getSystemService("legacy_permission");
        this.mRepo = imsFeatureBinderRepository;
        this.mImsEnablementTracker = new ImsEnablementTracker(looper, componentName);
        PackageManager packageManager = context.getPackageManager();
        this.mPackageManager = packageManager;
        if (packageManager != null) {
            ChangedPackages changedPackages = packageManager.getChangedPackages(this.mLastSequenceNumber);
            this.mChangedPackages = changedPackages;
            if (changedPackages != null) {
                this.mLastSequenceNumber = changedPackages.getSequenceNumber();
            }
        }
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public ImsServiceController(Context context, ComponentName componentName, ImsServiceControllerCallbacks imsServiceControllerCallbacks, Handler handler, RebindRetry rebindRetry, ImsFeatureBinderRepository imsFeatureBinderRepository, ExecutorService executorService) {
        this.mAnomalyUUID = UUID.fromString("e93b05e4-6d0a-4755-a6da-a2d2dbfb10d6");
        this.mLastSequenceNumber = 0;
        this.mIsBound = false;
        this.mIsBinding = false;
        this.mBoundUser = null;
        this.mFeatureStatusCallbacks = new HashSet();
        this.mLocalLog = new LocalLog(8);
        this.mLock = new Object();
        this.mFeatureChangedListener = new ImsService.Listener() { // from class: com.android.internal.telephony.ims.ImsServiceController.1
            public void onUpdateSupportedImsFeatures(ImsFeatureConfiguration imsFeatureConfiguration) {
                if (ImsServiceController.this.mCallbacks == null) {
                    return;
                }
                ImsServiceController.this.mLocalLog.log("onUpdateSupportedImsFeatures to " + imsFeatureConfiguration.getServiceFeatures());
                ImsServiceController.this.mCallbacks.imsServiceFeaturesChanged(imsFeatureConfiguration, ImsServiceController.this);
            }
        };
        this.mRestartImsServiceRunnable = new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (ImsServiceController.this.mLock) {
                    try {
                        if (ImsServiceController.this.mIsBound) {
                            return;
                        }
                        ImsServiceController imsServiceController = ImsServiceController.this;
                        imsServiceController.bind(imsServiceController.mBoundUser, ImsServiceController.this.mImsFeatures, ImsServiceController.this.mSlotIdToSubIdMap);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        };
        this.mRebindRetry = new RebindRetry(this) { // from class: com.android.internal.telephony.ims.ImsServiceController.3
            @Override // com.android.internal.telephony.ims.ImsServiceController.RebindRetry
            public long getMaximumDelay() {
                return 60000L;
            }

            @Override // com.android.internal.telephony.ims.ImsServiceController.RebindRetry
            public long getStartDelay() {
                return 2000L;
            }
        };
        this.mContext = context;
        this.mComponentName = componentName;
        this.mCallbacks = imsServiceControllerCallbacks;
        this.mHandler = handler;
        this.mBackoff = new ExponentialBackoff(rebindRetry.getStartDelay(), rebindRetry.getMaximumDelay(), 2, handler, this.mRestartImsServiceRunnable);
        this.mPermissionManager = null;
        this.mRepo = imsFeatureBinderRepository;
        this.mImsEnablementTracker = new ImsEnablementTracker(handler.getLooper(), componentName);
        this.mHandlerThread = null;
        this.mExecutor = executorService;
    }

    public boolean bind(UserHandle userHandle, Set<ImsFeatureConfiguration.FeatureSlotPair> set, SparseIntArray sparseIntArray) {
        synchronized (this.mLock) {
            if (this.mIsBound || this.mIsBinding) {
                return false;
            }
            this.mIsBinding = true;
            this.mBoundUser = userHandle;
            sanitizeFeatureConfig(set);
            this.mImsFeatures = set;
            this.mSlotIdToSubIdMap = sparseIntArray;
            this.mImsEnablementTracker.setNumOfSlots(sparseIntArray.size());
            grantPermissionsToService(userHandle);
            Intent component = new Intent(getServiceInterface()).setComponent(this.mComponentName);
            this.mImsServiceConnection = new ImsServiceConnection();
            this.mLocalLog.log("binding " + set);
            Log.i("ImsServiceController", "Binding ImsService:" + this.mComponentName);
            try {
                boolean zBindServiceAsUser = this.mContext.bindServiceAsUser(component, this.mImsServiceConnection, 67108929, userHandle);
                if (!zBindServiceAsUser) {
                    this.mLocalLog.log("    binding failed, retrying in " + this.mBackoff.getCurrentDelay() + " mS");
                    this.mIsBinding = false;
                    this.mBackoff.notifyFailed();
                }
                return zBindServiceAsUser;
            } catch (Exception e) {
                this.mBackoff.notifyFailed();
                this.mLocalLog.log("    binding exception=" + e.getMessage() + ", retrying in " + this.mBackoff.getCurrentDelay() + " mS");
                Log.e("ImsServiceController", "Error binding (" + this.mComponentName + ") with exception: " + e.getMessage() + ", rebinding in " + this.mBackoff.getCurrentDelay() + " ms");
                return false;
            }
        }
    }

    public void updateSlotIdToSubIdMap(int i, int i2) {
        synchronized (this.mSlotIdToSubIdMap) {
            this.mSlotIdToSubIdMap.put(i, i2);
        }
    }

    private void sanitizeFeatureConfig(Set set) {
        for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair : (Set) set.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsServiceController.$r8$lambda$LLemOMUF5n3Dke_YfbQqH3ljOCg((ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        }).collect(Collectors.toSet())) {
            if (!set.contains(new ImsFeatureConfiguration.FeatureSlotPair(featureSlotPair.slotId, 1))) {
                set.remove(featureSlotPair);
            }
        }
    }

    public static /* synthetic */ boolean $r8$lambda$LLemOMUF5n3Dke_YfbQqH3ljOCg(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.featureType == 0;
    }

    public void unbind() throws RemoteException {
        synchronized (this.mLock) {
            this.mBackoff.stop();
            changeImsServiceFeatures(new HashSet(), this.mSlotIdToSubIdMap);
            this.mIsBound = false;
            this.mIsBinding = false;
            this.mBoundUser = null;
            setServiceController(null);
            unbindService();
        }
    }

    public void changeImsServiceFeatures(Set<ImsFeatureConfiguration.FeatureSlotPair> set, SparseIntArray sparseIntArray) throws RemoteException {
        int i;
        sanitizeFeatureConfig(set);
        synchronized (this.mLock) {
            try {
                HashSet<Integer> hashSet = (HashSet) set.stream().map(new Function() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda4
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return Integer.valueOf(((ImsFeatureConfiguration.FeatureSlotPair) obj).slotId);
                    }
                }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda5
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        return new HashSet();
                    }
                }));
                this.mImsEnablementTracker.setNumOfSlots(hashSet.size());
                SparseIntArray sparseIntArray2 = new SparseIntArray(hashSet.size());
                for (Integer num : hashSet) {
                    int i2 = this.mSlotIdToSubIdMap.get(num.intValue(), -2);
                    int i3 = sparseIntArray.get(num.intValue());
                    if (i2 != i3) {
                        sparseIntArray2.put(num.intValue(), i3);
                        this.mLocalLog.log("subId changed for slot: " + num + ", " + i2 + " -> " + i3);
                        Log.i("ImsServiceController", "subId changed for slot: " + num + ", " + i2 + " -> " + i3);
                        if (i3 == -1) {
                            this.mImsEnablementTracker.subIdChangedToInvalid(num.intValue());
                        }
                    }
                }
                this.mSlotIdToSubIdMap = sparseIntArray;
                if (this.mImsFeatures.equals(set) && sparseIntArray2.size() == 0) {
                    Log.i("ImsServiceController", "xiaomi_issue return as there is no change mImsFeatures : " + this.mImsFeatures + "newImsFeatures :" + set);
                    return;
                }
                this.mLocalLog.log("Features (" + this.mImsFeatures + "->" + set + ")");
                Log.i("ImsServiceController", "Features (" + this.mImsFeatures + "->" + set + ") for ImsService: " + this.mComponentName);
                HashSet hashSet2 = new HashSet(this.mImsFeatures);
                this.mImsFeatures = set;
                if (this.mIsBound) {
                    HashSet<ImsFeatureConfiguration.FeatureSlotPair> hashSet3 = new HashSet(this.mImsFeatures);
                    hashSet3.removeAll(hashSet2);
                    for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair : hashSet3) {
                        addImsServiceFeature(featureSlotPair, modifyCapabilitiesForSlot(this.mImsFeatures, featureSlotPair.slotId, this.mServiceCapabilities), this.mSlotIdToSubIdMap.get(featureSlotPair.slotId));
                    }
                    HashSet hashSet4 = new HashSet(hashSet2);
                    hashSet4.removeAll(this.mImsFeatures);
                    Iterator it = hashSet4.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        } else {
                            removeImsServiceFeature((ImsFeatureConfiguration.FeatureSlotPair) it.next(), false);
                        }
                    }
                    HashSet<ImsFeatureConfiguration.FeatureSlotPair> hashSet5 = new HashSet(this.mImsFeatures);
                    hashSet5.removeAll(hashSet4);
                    hashSet5.removeAll(hashSet3);
                    if (sparseIntArray2.size() > 0) {
                        for (final int i4 : sparseIntArray2.copyKeys()) {
                            int i5 = sparseIntArray2.get(i4, -1);
                            HashSet<ImsFeatureConfiguration.FeatureSlotPair> hashSet6 = (HashSet) hashSet5.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda6
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    return ImsServiceController.$r8$lambda$aidQFL7FUKEOm5Dw3v21K2VDaZQ(i4, (ImsFeatureConfiguration.FeatureSlotPair) obj);
                                }
                            }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda5
                                @Override // java.util.function.Supplier
                                public final Object get() {
                                    return new HashSet();
                                }
                            }));
                            Iterator it2 = hashSet6.iterator();
                            while (it2.hasNext()) {
                                removeImsServiceFeature((ImsFeatureConfiguration.FeatureSlotPair) it2.next(), true);
                            }
                            for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair2 : hashSet6) {
                                addImsServiceFeature(featureSlotPair2, modifyCapabilitiesForSlot(this.mImsFeatures, featureSlotPair2.slotId, this.mServiceCapabilities), i5);
                            }
                            hashSet5.removeAll(hashSet6);
                        }
                    }
                    for (ImsFeatureConfiguration.FeatureSlotPair featureSlotPair3 : hashSet5) {
                        this.mRepo.notifyFeatureCapabilitiesChanged(featureSlotPair3.slotId, featureSlotPair3.featureType, modifyCapabilitiesForSlot(this.mImsFeatures, featureSlotPair3.slotId, this.mServiceCapabilities));
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static /* synthetic */ boolean $r8$lambda$aidQFL7FUKEOm5Dw3v21K2VDaZQ(int i, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.slotId == i;
    }

    public IImsServiceController getImsServiceController() {
        return this.mIImsServiceController;
    }

    public long getRebindDelay() {
        return this.mBackoff.getCurrentDelay();
    }

    public void stopBackoffTimerForTesting() {
        this.mBackoff.stop();
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public UserHandle getBoundUser() {
        return this.mBoundUser;
    }

    public void enableIms(int i, int i2) {
        this.mImsEnablementTracker.enableIms(i, i2);
    }

    public void disableIms(int i, int i2) {
        this.mImsEnablementTracker.disableIms(i, i2);
    }

    public void resetIms(int i, int i2) {
        this.mImsEnablementTracker.resetIms(i, i2);
    }

    public IImsRegistration getRegistration(int i, int i2) throws RemoteException {
        IImsRegistration registration;
        synchronized (this.mLock) {
            try {
                registration = isServiceControllerAvailable() ? this.mIImsServiceController.getRegistration(i, i2) : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        return registration;
    }

    public IImsConfig getConfig(int i, int i2) throws RemoteException {
        IImsConfig config;
        synchronized (this.mLock) {
            try {
                config = isServiceControllerAvailable() ? this.mIImsServiceController.getConfig(i, i2) : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        return config;
    }

    public ISipTransport getSipTransport(int i) throws RemoteException {
        ISipTransport sipTransport;
        synchronized (this.mLock) {
            try {
                sipTransport = isServiceControllerAvailable() ? this.mIImsServiceController.getSipTransport(i) : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        return sipTransport;
    }

    protected long getStaticServiceCapabilities() throws RemoteException {
        long imsServiceCapabilities;
        synchronized (this.mLock) {
            try {
                imsServiceCapabilities = isServiceControllerAvailable() ? this.mIImsServiceController.getImsServiceCapabilities() : 0L;
            } catch (Throwable th) {
                throw th;
            }
        }
        return imsServiceCapabilities;
    }

    protected void notifyImsServiceReady() {
        final IImsServiceController iImsServiceController;
        synchronized (this.mLock) {
            iImsServiceController = this.mIImsServiceController;
        }
        if (iImsServiceController != null) {
            final ImsService.Listener listener = this.mFeatureChangedListener;
            this.mExecutor.execute(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyImsServiceReady$5(iImsServiceController, listener);
                }
            });
        } else {
            Log.w("ImsServiceController", "ServiceController is not available, skip notifyImsServiceReady");
            this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyImsServiceReady$6();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyImsServiceReady$5(IImsServiceController iImsServiceController, ImsService.Listener listener) {
        Log.d("ImsServiceController", "notifyImsServiceReady");
        try {
            iImsServiceController.setListener(listener);
            iImsServiceController.notifyImsServiceReadyForFeatureCreation();
            Log.d("ImsServiceController", "notifyImsServiceReady(Async) is done.");
            this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyImsServiceReady$3();
                }
            });
        } catch (RemoteException e) {
            Log.d("ImsServiceController", "notifyImsServiceReady exception");
            this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyImsServiceReady$4(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyImsServiceReady$3() {
        ImsServiceConnection imsServiceConnection = this.mImsServiceConnection;
        if (imsServiceConnection != null) {
            imsServiceConnection.updateCapabilityAndServiceFeature();
        } else {
            Log.e("ImsServiceController", "ImsServiceConnection is not available");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyImsServiceReady$4(RemoteException remoteException) {
        ImsServiceConnection imsServiceConnection = this.mImsServiceConnection;
        if (imsServiceConnection != null) {
            imsServiceConnection.cleanAndRestart(remoteException);
        } else {
            Log.e("ImsServiceController", "ImsServiceConnection is not available");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyImsServiceReady$6() {
        this.mImsServiceConnection.updateCapabilityAndServiceFeature();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveStaticImsServiceCapabilities() throws RemoteException {
        long staticServiceCapabilities = getStaticServiceCapabilities();
        Log.i("ImsServiceController", "retrieveStaticImsServiceCapabilities: " + ImsService.getCapabilitiesString(staticServiceCapabilities));
        this.mLocalLog.log("retrieveStaticImsServiceCapabilities: " + ImsService.getCapabilitiesString(staticServiceCapabilities));
        synchronized (this.mLock) {
            this.mServiceCapabilities = staticServiceCapabilities;
        }
    }

    protected String getServiceInterface() {
        return "android.telephony.ims.ImsService";
    }

    protected void setServiceController(IBinder iBinder) {
        this.mIImsServiceController = IImsServiceController.Stub.asInterface(iBinder);
        this.mImsEnablementTracker.setServiceController(iBinder);
    }

    protected boolean isServiceControllerAvailable() {
        return this.mIImsServiceController != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDelayedRebindToService() {
        this.mBackoff.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService() {
        synchronized (this.mLock) {
            try {
                if (this.mImsServiceConnection != null) {
                    Log.i("ImsServiceController", "Unbinding ImsService: " + this.mComponentName);
                    this.mLocalLog.log("unbinding: " + this.mComponentName);
                    this.mContext.unbindService(this.mImsServiceConnection);
                    this.mImsServiceConnection = null;
                } else {
                    Log.i("ImsServiceController", "unbindService called on already unbound ImsService: " + this.mComponentName);
                    this.mLocalLog.log("Note: unbindService called with no ServiceConnection on " + this.mComponentName);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long modifyCapabilitiesForSlot(Set set, int i, long j) {
        if (getFeaturesForSlot(i, set).contains(0)) {
            j |= 1;
        }
        Log.i("ImsServiceController", "skipping single service enforce check...");
        return j;
    }

    private void grantPermissionsToService(UserHandle userHandle) {
        this.mLocalLog.log("grant permissions to " + getComponentName());
        Log.i("ImsServiceController", "Granting Runtime permissions to:" + getComponentName());
        String[] strArr = {this.mComponentName.getPackageName()};
        try {
            if (this.mPermissionManager != null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                this.mPermissionManager.grantDefaultPermissionsToEnabledImsServices(strArr, userHandle, new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new Consumer() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ImsServiceController.m1088$r8$lambda$Zw5zsT6TVnrWOEncQyqzSBtBf8(countDownLatch, (Boolean) obj);
                    }
                });
                TelephonyUtils.waitUntilReady(countDownLatch, 15000L);
            }
        } catch (RuntimeException unused) {
            Log.w("ImsServiceController", "Unable to grant permissions, binder died.");
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$Zw5zsT6T-VnrWOEncQyqzSBtBf8, reason: not valid java name */
    public static /* synthetic */ void m1088$r8$lambda$Zw5zsT6TVnrWOEncQyqzSBtBf8(CountDownLatch countDownLatch, Boolean bool) {
        if (bool.booleanValue()) {
            countDownLatch.countDown();
        } else {
            Log.e("ImsServiceController", "Failed to grant permissions to service.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addImsServiceFeature(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair, long j, int i) throws RemoteException {
        if (!isServiceControllerAvailable() || this.mCallbacks == null) {
            Log.w("ImsServiceController", "addImsServiceFeature called with null values.");
            return;
        }
        int i2 = featureSlotPair.featureType;
        int i3 = featureSlotPair.slotId;
        if (i2 != 0) {
            addImsFeatureBinder(featureSlotPair.slotId, i, featureSlotPair.featureType, createImsFeature(i3, i, i2, j), j);
            addImsFeatureStatusCallback(featureSlotPair.slotId, featureSlotPair.featureType);
        } else {
            Log.i("ImsServiceController", "supports emergency calling on slot " + i3);
        }
        this.mCallbacks.imsServiceFeatureCreated(featureSlotPair.slotId, i, featureSlotPair.featureType, this);
    }

    private void removeImsServiceFeature(ImsFeatureConfiguration.FeatureSlotPair featureSlotPair, boolean z) {
        ImsServiceControllerCallbacks imsServiceControllerCallbacks;
        if (!isServiceControllerAvailable() || (imsServiceControllerCallbacks = this.mCallbacks) == null) {
            Log.w("ImsServiceController", "removeImsServiceFeature called with null values.");
            return;
        }
        imsServiceControllerCallbacks.imsServiceFeatureRemoved(featureSlotPair.slotId, featureSlotPair.featureType, this);
        int i = featureSlotPair.featureType;
        int i2 = featureSlotPair.slotId;
        if (i != 0) {
            removeImsFeatureStatusCallback(i2, i);
            removeImsFeatureBinder(featureSlotPair.slotId, featureSlotPair.featureType);
            try {
                removeImsFeature(featureSlotPair.slotId, featureSlotPair.featureType, z);
                return;
            } catch (RemoteException e) {
                Log.i("ImsServiceController", "Couldn't remove feature {" + ((String) ImsFeature.FEATURE_LOG_MAP.get(Integer.valueOf(featureSlotPair.featureType))) + "}, connection is down: " + e.getMessage());
                return;
            }
        }
        Log.i("ImsServiceController", "doesn't support emergency calling on slot " + i2);
    }

    protected IInterface createImsFeature(int i, int i2, int i3, long j) throws RemoteException {
        if (i3 != 1) {
            if (i3 != 2) {
                return null;
            }
            return this.mIImsServiceController.createRcsFeature(i, i2);
        }
        if (i2 != -1) {
            return this.mIImsServiceController.createMmTelFeature(i, i2);
        }
        if ((1 & j) > 0) {
            return this.mIImsServiceController.createEmergencyOnlyMmTelFeature(i);
        }
        return null;
    }

    private void addImsFeatureStatusCallback(int i, int i2) throws RemoteException {
        ImsFeatureStatusCallback imsFeatureStatusCallback = new ImsFeatureStatusCallback(i, i2);
        this.mFeatureStatusCallbacks.add(imsFeatureStatusCallback);
        registerImsFeatureStatusCallback(i, i2, imsFeatureStatusCallback.getCallback());
    }

    private void removeImsFeatureStatusCallback(final int i, final int i2) {
        ImsFeatureStatusCallback imsFeatureStatusCallback = (ImsFeatureStatusCallback) this.mFeatureStatusCallbacks.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsServiceController.$r8$lambda$cHiDrw8Q2SEeZvplD8fHG9lV6C0(i, i2, (ImsServiceController.ImsFeatureStatusCallback) obj);
            }
        }).findFirst().orElse(null);
        if (imsFeatureStatusCallback != null) {
            this.mFeatureStatusCallbacks.remove(imsFeatureStatusCallback);
            unregisterImsFeatureStatusCallback(i, i2, imsFeatureStatusCallback.getCallback());
        }
    }

    public static /* synthetic */ boolean $r8$lambda$cHiDrw8Q2SEeZvplD8fHG9lV6C0(int i, int i2, ImsFeatureStatusCallback imsFeatureStatusCallback) {
        return imsFeatureStatusCallback.mSlotId == i && imsFeatureStatusCallback.mFeatureType == i2;
    }

    protected void registerImsFeatureStatusCallback(int i, int i2, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException {
        this.mIImsServiceController.addFeatureStatusCallback(i, i2, iImsFeatureStatusCallback);
    }

    protected void unregisterImsFeatureStatusCallback(int i, int i2, IImsFeatureStatusCallback iImsFeatureStatusCallback) {
        try {
            this.mIImsServiceController.removeFeatureStatusCallback(i, i2, iImsFeatureStatusCallback);
        } catch (RemoteException unused) {
            this.mLocalLog.log("unregisterImsFeatureStatusCallback - couldn't remove " + iImsFeatureStatusCallback);
        }
    }

    protected void removeImsFeature(int i, int i2, boolean z) throws RemoteException {
        this.mIImsServiceController.removeImsFeature(i, i2, z);
    }

    private void addImsFeatureBinder(int i, int i2, int i3, IInterface iInterface, long j) throws RemoteException {
        if (iInterface == null) {
            StringBuilder sb = new StringBuilder("addImsFeatureBinder: null IInterface reported for ");
            Map map = ImsFeature.FEATURE_LOG_MAP;
            sb.append((String) map.get(Integer.valueOf(i3)));
            Log.w("ImsServiceController", sb.toString());
            this.mLocalLog.log("addImsFeatureBinder: null IInterface reported for " + ((String) map.get(Integer.valueOf(i3))));
            return;
        }
        this.mRepo.addConnection(i, i2, i3, createFeatureContainer(i, i2, iInterface.asBinder(), j));
    }

    private void removeImsFeatureBinder(int i, int i2) {
        this.mRepo.removeConnection(i, i2);
    }

    private ImsFeatureContainer createFeatureContainer(int i, int i2, IBinder iBinder, long j) throws RemoteException {
        IImsConfig config = getConfig(i, i2);
        IImsRegistration registration = getRegistration(i, i2);
        if (config == null || registration == null) {
            Log.w("ImsServiceController", "createFeatureContainer: invalid state. Reporting as not available. componentName= " + getComponentName());
            this.mLocalLog.log("createFeatureContainer: invalid state. Reporting as not available.");
            return null;
        }
        return new ImsFeatureContainer(iBinder, config, registration, getSipTransport(i), j);
    }

    public static /* synthetic */ boolean $r8$lambda$Msb3DR859ae69gTMmLXzJJk4BTs(int i, ImsFeatureConfiguration.FeatureSlotPair featureSlotPair) {
        return featureSlotPair.slotId == i;
    }

    private List getFeaturesForSlot(final int i, Set set) {
        return (List) set.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsServiceController.$r8$lambda$Msb3DR859ae69gTMmLXzJJk4BTs(i, (ImsFeatureConfiguration.FeatureSlotPair) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.ims.ImsServiceController$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((ImsFeatureConfiguration.FeatureSlotPair) obj).featureType);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupAllFeatures() {
        synchronized (this.mLock) {
            try {
                Iterator it = this.mImsFeatures.iterator();
                while (it.hasNext()) {
                    removeImsServiceFeature((ImsFeatureConfiguration.FeatureSlotPair) it.next(), false);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndReportAnomaly(ComponentName componentName) {
        PackageManager packageManager = this.mPackageManager;
        if (packageManager == null) {
            Log.w("ImsServiceController", "mPackageManager null");
            return;
        }
        ChangedPackages changedPackages = packageManager.getChangedPackages(this.mLastSequenceNumber);
        if (changedPackages != null) {
            this.mLastSequenceNumber = changedPackages.getSequenceNumber();
            if (changedPackages.getPackageNames().contains(componentName.getPackageName())) {
                Log.d("ImsServiceController", "Ignore due to updated, package: " + componentName.getPackageName());
                return;
            }
        }
        AnomalyReporter.reportAnomaly(this.mAnomalyUUID, "IMS Service Crashed");
    }

    public String toString() {
        return "[ImsServiceController: componentName=" + getComponentName() + ", boundUser=" + this.mBoundUser + ", features=" + this.mImsFeatures + ", isBinding=" + this.mIsBinding + ", isBound=" + this.mIsBound + ", serviceController=" + getImsServiceController() + ", rebindDelay=" + getRebindDelay() + ", slotToSubIdMap=" + this.mSlotIdToSubIdMap + "]";
    }

    public void dump(PrintWriter printWriter) {
        this.mLocalLog.dump(printWriter);
    }
}
