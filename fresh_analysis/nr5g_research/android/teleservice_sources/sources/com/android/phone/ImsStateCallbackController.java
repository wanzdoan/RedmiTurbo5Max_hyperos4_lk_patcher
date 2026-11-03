package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.ims.feature.ImsFeature;
import android.util.Log;
import android.util.SparseArray;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsManager;
import com.android.ims.RcsFeatureManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IImsStateCallback;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.util.HandlerExecutor;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class ImsStateCallbackController {
    private static ImsStateCallbackController sInstance;
    private static final LocalLog sLocalLog = new LocalLog(50);
    private final PhoneGlobals mApp;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private final ImsResolver mImsResolver;
    private MmTelFeatureConnectorFactory mMmTelFeatureFactory;
    private int mNumSlots;
    private PhoneFactoryProxy mPhoneFactoryProxy;
    private RcsFeatureConnectorFactory mRcsFeatureFactory;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubChangedListener;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyRegistryManager mTelephonyRegistryManager;
    private final SparseArray mMmTelFeatureListeners = new SparseArray();
    private final SparseArray mRcsFeatureListeners = new SparseArray();
    private final ConcurrentHashMap mSubIdToImsManagerCache = new ConcurrentHashMap();
    private HashMap mWrappers = new HashMap();
    private final Object mDumpLock = new Object();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.ImsStateCallbackController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            if (intent == null || !"android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction()) || (extras = intent.getExtras()) == null) {
                return;
            }
            int i = extras.getInt("android.telephony.extra.SLOT_INDEX", -1);
            int i2 = extras.getInt("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            if (i <= -1) {
                ImsStateCallbackController.loge("onReceive ACTION_CARRIER_CONFIG_CHANGED invalid slotId");
            } else if (i2 <= -1) {
                ImsStateCallbackController.loge("onReceive ACTION_CARRIER_CONFIG_CHANGED invalid subId");
            } else {
                ImsStateCallbackController.this.notifyCarrierConfigChanged(i);
            }
        }
    };

    public interface MmTelFeatureConnectorFactory {
        FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor);
    }

    public interface PhoneFactoryProxy {
        Phone getPhone(int i);
    }

    public interface RcsFeatureConnectorFactory {
        FeatureConnector create(Context context, int i, FeatureConnector.Listener listener, Executor executor, String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int convertReasonType(int i) {
        if (i != 1) {
            return i != 2 ? 3 : 4;
        }
        return 6;
    }

    public static ImsStateCallbackController getInstance() {
        ImsStateCallbackController imsStateCallbackController;
        synchronized (ImsStateCallbackController.class) {
            imsStateCallbackController = sInstance;
        }
        return imsStateCallbackController;
    }

    final class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (ImsStateCallbackController.this.mDumpLock) {
                try {
                    switch (message.what) {
                        case 1:
                            ImsStateCallbackController.this.onSubChanged();
                            break;
                        case 2:
                            ImsStateCallbackController.this.onRegisterCallback((CallbackWrapper) message.obj);
                            break;
                        case 3:
                            ImsStateCallbackController.this.onUnregisterCallback((IImsStateCallback) message.obj);
                            break;
                        case 4:
                            ImsStateCallbackController.this.onCarrierConfigChanged(message.arg1);
                            break;
                        case 5:
                            Object obj = message.obj;
                            if (obj != null) {
                                ImsStateCallbackController.this.onExternalRcsStateChanged((ExternalRcsFeatureState) obj);
                            }
                            break;
                        case 6:
                            Integer num = (Integer) ((AsyncResult) message.obj).result;
                            if (num == null) {
                                Log.w("ImsStateCallbackController", "msim config change with null num slots");
                            } else {
                                ImsStateCallbackController.this.updateFeatureControllerSize(num.intValue());
                            }
                            break;
                        default:
                            ImsStateCallbackController.loge("Unhandled event " + message.what);
                            break;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    final class MmTelFeatureListener implements FeatureConnector.Listener {
        private FeatureConnector mConnector;
        private String mLogPrefix;
        private int mSlotId;
        private int mSubId = -1;
        private int mState = 0;
        private int mReason = 3;
        private boolean mHasConfig = true;

        MmTelFeatureListener(int i) {
            this.mLogPrefix = "";
            this.mSlotId = i;
            this.mLogPrefix = "[" + i + ", MMTEL] ";
            FeatureConnector featureConnectorCreate = ImsStateCallbackController.this.mMmTelFeatureFactory.create(ImsStateCallbackController.this.mApp, i, "ImsStateCallbackController", this, new HandlerExecutor(ImsStateCallbackController.this.mHandler));
            this.mConnector = featureConnectorCreate;
            featureConnectorCreate.connect();
        }

        void setSubId(int i) {
            if (this.mSubId == i) {
                return;
            }
            ImsStateCallbackController.logd(this.mLogPrefix + "setSubId changed subId=" + i);
            this.mSubId = i;
        }

        void destroy() {
            this.mConnector.disconnect();
            this.mConnector = null;
        }

        public void connectionReady(ImsManager imsManager, int i) {
            ImsStateCallbackController.logd(this.mLogPrefix + "connectionReady " + i);
            this.mSubId = i;
            if (i == -1) {
                return;
            }
            if (imsManager != null) {
                ImsStateCallbackController.this.mSubIdToImsManagerCache.put(Integer.valueOf(i), imsManager);
            }
            this.mState = 2;
            this.mReason = 0;
            this.mHasConfig = true;
            ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 1, 2, 0);
        }

        public void connectionUnavailable(int i) {
            ImsStateCallbackController.logd(this.mLogPrefix + "connectionUnavailable reason=" + ImsStateCallbackController.connectorReasonToString(i));
            int iConvertReasonType = ImsStateCallbackController.convertReasonType(i);
            if (this.mReason == iConvertReasonType) {
                return;
            }
            ImsStateCallbackController.this.mSubIdToImsManagerCache.remove(Integer.valueOf(this.mSubId));
            connectionUnavailableInternal(iConvertReasonType);
        }

        private void connectionUnavailableInternal(int i) {
            this.mState = 0;
            this.mReason = i;
            if (this.mHasConfig || i == 4) {
                ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 1, 0, i);
            }
        }

        void notifyConfigChanged(boolean z) {
            if (this.mHasConfig == z) {
                return;
            }
            ImsStateCallbackController.logd(this.mLogPrefix + "notifyConfigChanged " + z);
            this.mHasConfig = z;
            if (z) {
                if (this.mState != 2) {
                    int i = this.mReason;
                    if (i == 4) {
                        connectionUnavailable(0);
                        return;
                    } else {
                        connectionUnavailableInternal(i);
                        return;
                    }
                }
                return;
            }
            connectionUnavailable(2);
        }

        boolean notifyState(CallbackWrapper callbackWrapper) {
            return callbackWrapper.notifyState(this.mSubId, 1, this.mState, this.mReason);
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("Listener={slotId=" + this.mSlotId + ", subId=" + this.mSubId + ", state=" + ((String) ImsFeature.STATE_LOG_MAP.get(Integer.valueOf(this.mState))) + ", reason=" + ImsStateCallbackController.imsStateReasonToString(this.mReason) + ", hasConfig=" + this.mHasConfig + "}");
        }
    }

    final class RcsFeatureListener implements FeatureConnector.Listener {
        private FeatureConnector mConnector;
        private String mLogPrefix;
        private int mSlotId;
        private int mSubId = -1;
        private int mState = 0;
        private int mReason = 3;
        private boolean mHasConfig = true;
        private ExternalRcsFeatureState mExternalState = null;

        RcsFeatureListener(int i) {
            this.mLogPrefix = "";
            this.mSlotId = i;
            this.mLogPrefix = "[" + i + ", RCS] ";
            FeatureConnector featureConnectorCreate = ImsStateCallbackController.this.mRcsFeatureFactory.create(ImsStateCallbackController.this.mApp, i, this, new HandlerExecutor(ImsStateCallbackController.this.mHandler), "ImsStateCallbackController");
            this.mConnector = featureConnectorCreate;
            featureConnectorCreate.connect();
        }

        void setSubId(int i) {
            if (this.mSubId == i) {
                return;
            }
            ImsStateCallbackController.logd(this.mLogPrefix + "setSubId changed subId=" + i);
            this.mSubId = i;
        }

        void destroy() {
            this.mConnector.disconnect();
            this.mConnector = null;
        }

        public void connectionReady(RcsFeatureManager rcsFeatureManager, int i) {
            ImsStateCallbackController.logd(this.mLogPrefix + "connectionReady " + i);
            this.mSubId = i;
            if (i == -1) {
                return;
            }
            this.mState = 2;
            this.mReason = 0;
            this.mHasConfig = true;
            ExternalRcsFeatureState externalRcsFeatureState = this.mExternalState;
            if (externalRcsFeatureState == null || !externalRcsFeatureState.isReady()) {
                return;
            }
            ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 2, this.mState, this.mReason);
        }

        public void connectionUnavailable(int i) {
            ImsStateCallbackController.logd(this.mLogPrefix + "connectionUnavailable reason=" + ImsStateCallbackController.connectorReasonToString(i));
            int iConvertReasonType = ImsStateCallbackController.convertReasonType(i);
            if (this.mReason == iConvertReasonType) {
                return;
            }
            connectionUnavailableInternal(iConvertReasonType);
        }

        private void connectionUnavailableInternal(int i) {
            this.mState = 0;
            this.mReason = i;
            if (this.mHasConfig || i == 4) {
                ExternalRcsFeatureState externalRcsFeatureState = this.mExternalState;
                if (externalRcsFeatureState != null || i == 4) {
                    if (externalRcsFeatureState == null || externalRcsFeatureState.hasActiveFeatures()) {
                        ExternalRcsFeatureState externalRcsFeatureState2 = this.mExternalState;
                        if ((externalRcsFeatureState2 == null || !externalRcsFeatureState2.hasActiveFeatures()) && this.mReason != 4) {
                            return;
                        }
                        ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 2, this.mState, this.mReason);
                    }
                }
            }
        }

        void notifyConfigChanged(boolean z) {
            if (this.mHasConfig == z) {
                return;
            }
            ImsStateCallbackController.logd(this.mLogPrefix + "notifyConfigChanged " + z);
            this.mHasConfig = z;
            if (z) {
                if (this.mState != 2) {
                    int i = this.mReason;
                    if (i == 4) {
                        connectionUnavailable(0);
                        return;
                    } else {
                        connectionUnavailableInternal(i);
                        return;
                    }
                }
                return;
            }
            connectionUnavailable(2);
        }

        void notifyExternalRcsState(ExternalRcsFeatureState externalRcsFeatureState) {
            ExternalRcsFeatureState externalRcsFeatureState2 = this.mExternalState;
            if (externalRcsFeatureState.mState == -1) {
                if (externalRcsFeatureState2 != null) {
                    externalRcsFeatureState.mState = externalRcsFeatureState2.mState;
                } else {
                    externalRcsFeatureState.mState = 0;
                }
            }
            this.mExternalState = externalRcsFeatureState;
            if (this.mHasConfig) {
                if (externalRcsFeatureState.hasActiveFeatures()) {
                    if (this.mState == 2) {
                        if ((externalRcsFeatureState2 == null || !externalRcsFeatureState2.isReady()) && externalRcsFeatureState.isReady()) {
                            ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 2, this.mState, this.mReason);
                            return;
                        } else {
                            externalRcsFeatureState.isReady();
                            return;
                        }
                    }
                    return;
                }
                if ((externalRcsFeatureState2 == null || externalRcsFeatureState2.hasActiveFeatures()) && this.mReason != 4) {
                    ImsStateCallbackController.this.onFeatureStateChange(this.mSubId, 2, 0, 4);
                }
            }
        }

        boolean notifyState(CallbackWrapper callbackWrapper) {
            if (this.mHasConfig) {
                ExternalRcsFeatureState externalRcsFeatureState = this.mExternalState;
                if (externalRcsFeatureState == null) {
                    return callbackWrapper.notifyState(this.mSubId, 2, 0, 3);
                }
                if (!externalRcsFeatureState.hasActiveFeatures()) {
                    return callbackWrapper.notifyState(this.mSubId, 2, 0, 4);
                }
            }
            return callbackWrapper.notifyState(this.mSubId, 2, this.mState, this.mReason);
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            StringBuilder sb = new StringBuilder("Listener={slotId=");
            sb.append(this.mSlotId);
            sb.append(", subId=");
            sb.append(this.mSubId);
            sb.append(", state=");
            sb.append((String) ImsFeature.STATE_LOG_MAP.get(Integer.valueOf(this.mState)));
            sb.append(", reason=");
            sb.append(ImsStateCallbackController.imsStateReasonToString(this.mReason));
            sb.append(", hasConfig=");
            sb.append(this.mHasConfig);
            sb.append(", isReady=");
            ExternalRcsFeatureState externalRcsFeatureState = this.mExternalState;
            sb.append(externalRcsFeatureState == null ? false : externalRcsFeatureState.isReady());
            sb.append(", hasFeatures=");
            ExternalRcsFeatureState externalRcsFeatureState2 = this.mExternalState;
            sb.append(externalRcsFeatureState2 != null ? externalRcsFeatureState2.hasActiveFeatures() : false);
            sb.append("}");
            indentingPrintWriter.println(sb.toString());
        }
    }

    class CallbackWrapper {
        private final IBinder mBinder;
        private final IImsStateCallback mCallback;
        private final String mCallingPackage;
        private int mLastReason = -1;
        private final int mRequiredFeature;
        private final int mSubId;

        CallbackWrapper(int i, int i2, IImsStateCallback iImsStateCallback, String str) {
            this.mSubId = i;
            this.mRequiredFeature = i2;
            this.mCallback = iImsStateCallback;
            this.mBinder = iImsStateCallback.asBinder();
            this.mCallingPackage = str;
        }

        boolean notifyState(int i, int i2, int i3, int i4) {
            IImsStateCallback iImsStateCallback = this.mCallback;
            try {
                if (i3 == 2) {
                    iImsStateCallback.onAvailable();
                } else {
                    iImsStateCallback.onUnavailable(i4);
                }
                this.mLastReason = i4;
                return true;
            } catch (Exception e) {
                ImsStateCallbackController.loge("CallbackWrapper notifyState e=" + e);
                return false;
            }
        }

        void notifyInactive() {
            ImsStateCallbackController.logd("CallbackWrapper notifyInactive subId=" + this.mSubId);
            try {
                this.mCallback.onUnavailable(5);
            } catch (Exception unused) {
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("CallbackWrapper={subId=" + this.mSubId + ", feature=" + ((String) ImsFeature.FEATURE_LOG_MAP.get(Integer.valueOf(this.mRequiredFeature))) + ", reason=" + ImsStateCallbackController.imsStateReasonToString(this.mLastReason) + ", pkg=" + this.mCallingPackage + "}");
        }
    }

    class ExternalRcsFeatureState {
        private int mReason;
        private int mSlotId;
        private int mState;

        ExternalRcsFeatureState(int i, int i2, int i3) {
            this.mSlotId = i;
            this.mState = i2;
            this.mReason = i3;
        }

        boolean hasActiveFeatures() {
            return this.mReason != 4;
        }

        boolean isReady() {
            return this.mState == 2;
        }
    }

    public static ImsStateCallbackController make(PhoneGlobals phoneGlobals, int i, FeatureFlags featureFlags) {
        synchronized (ImsStateCallbackController.class) {
            try {
                if (sInstance == null) {
                    logd("ImsStateCallbackController created");
                    HandlerThread handlerThread = new HandlerThread("ImsStateCallbackController");
                    handlerThread.start();
                    sInstance = new ImsStateCallbackController(phoneGlobals, handlerThread.getLooper(), i, new MmTelFeatureConnectorFactory() { // from class: com.android.phone.ImsStateCallbackController$$ExternalSyntheticLambda1
                        @Override // com.android.phone.ImsStateCallbackController.MmTelFeatureConnectorFactory
                        public final FeatureConnector create(Context context, int i2, String str, FeatureConnector.Listener listener, Executor executor) {
                            return ImsManager.getConnector(context, i2, str, listener, executor);
                        }
                    }, new RcsFeatureConnectorFactory() { // from class: com.android.phone.ImsStateCallbackController$$ExternalSyntheticLambda2
                        @Override // com.android.phone.ImsStateCallbackController.RcsFeatureConnectorFactory
                        public final FeatureConnector create(Context context, int i2, FeatureConnector.Listener listener, Executor executor, String str) {
                            return RcsFeatureManager.getConnector(context, i2, listener, executor, str);
                        }
                    }, ImsResolver.getInstance(), featureFlags);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public ImsStateCallbackController(PhoneGlobals phoneGlobals, Looper looper, int i, MmTelFeatureConnectorFactory mmTelFeatureConnectorFactory, RcsFeatureConnectorFactory rcsFeatureConnectorFactory, ImsResolver imsResolver, FeatureFlags featureFlags) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.ImsStateCallbackController.2
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                if (ImsStateCallbackController.this.mHandler.hasMessages(1)) {
                    return;
                }
                ImsStateCallbackController.this.mHandler.sendEmptyMessage(1);
            }
        };
        this.mSubChangedListener = onSubscriptionsChangedListener;
        this.mPhoneFactoryProxy = new PhoneFactoryProxy(this) { // from class: com.android.phone.ImsStateCallbackController.3
            @Override // com.android.phone.ImsStateCallbackController.PhoneFactoryProxy
            public Phone getPhone(int i2) {
                return PhoneFactory.getPhone(i2);
            }
        };
        this.mApp = phoneGlobals;
        MyHandler myHandler = new MyHandler(looper);
        this.mHandler = myHandler;
        this.mImsResolver = imsResolver;
        this.mSubscriptionManager = (SubscriptionManager) phoneGlobals.getSystemService(SubscriptionManager.class);
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) phoneGlobals.getSystemService(TelephonyRegistryManager.class);
        this.mTelephonyRegistryManager = telephonyRegistryManager;
        this.mMmTelFeatureFactory = mmTelFeatureConnectorFactory;
        this.mRcsFeatureFactory = rcsFeatureConnectorFactory;
        this.mFeatureFlags = featureFlags;
        updateFeatureControllerSize(i);
        Objects.requireNonNull(myHandler);
        telephonyRegistryManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener, new ConcurrencyHelpers$$ExternalSyntheticLambda0(myHandler));
        PhoneConfigurationManager.registerForMultiSimConfigChange(myHandler, 6, (Object) null);
        phoneGlobals.registerReceiver(this.mReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
        onSubChanged();
    }

    public void updateFeatureControllerSize(int i) {
        if (this.mNumSlots != i) {
            logd("updateFeatures: oldSlots=" + this.mNumSlots + ", newNumSlots=" + i);
            int i2 = this.mNumSlots;
            if (i2 >= i) {
                while (true) {
                    i2--;
                    if (i2 <= i - 1) {
                        break;
                    }
                    MmTelFeatureListener mmTelFeatureListener = (MmTelFeatureListener) this.mMmTelFeatureListeners.get(i2);
                    if (mmTelFeatureListener != null) {
                        this.mMmTelFeatureListeners.remove(i2);
                        mmTelFeatureListener.destroy();
                    }
                    RcsFeatureListener rcsFeatureListener = (RcsFeatureListener) this.mRcsFeatureListeners.get(i2);
                    if (rcsFeatureListener != null) {
                        this.mRcsFeatureListeners.remove(i2);
                        rcsFeatureListener.destroy();
                    }
                }
            } else {
                while (i2 < i) {
                    this.mMmTelFeatureListeners.put(i2, new MmTelFeatureListener(i2));
                    this.mRcsFeatureListeners.put(i2, new RcsFeatureListener(i2));
                    i2++;
                }
            }
        }
        this.mNumSlots = i;
    }

    public void onSubChanged() {
        for (int i = 0; i < this.mMmTelFeatureListeners.size(); i++) {
            ((MmTelFeatureListener) this.mMmTelFeatureListeners.valueAt(i)).setSubId(getSubId(i));
        }
        for (int i2 = 0; i2 < this.mRcsFeatureListeners.size(); i2++) {
            ((RcsFeatureListener) this.mRcsFeatureListeners.valueAt(i2)).setSubId(getSubId(i2));
        }
        if (this.mWrappers.size() == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int[] activeSubscriptionIdList = this.mSubscriptionManager.getActiveSubscriptionIdList();
        for (IBinder iBinder : this.mWrappers.keySet()) {
            CallbackWrapper callbackWrapper = (CallbackWrapper) this.mWrappers.get(iBinder);
            if (callbackWrapper != null) {
                if (!isActive(activeSubscriptionIdList, callbackWrapper.mSubId)) {
                    arrayList.add(iBinder);
                }
            } else {
                arrayList.add(iBinder);
            }
        }
        removeInactiveCallbacks(arrayList, "onSubChanged");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFeatureStateChange(final int i, final int i2, final int i3, final int i4) {
        final ArrayList arrayList = new ArrayList();
        this.mWrappers.values().forEach(new Consumer() { // from class: com.android.phone.ImsStateCallbackController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsStateCallbackController.m981$r8$lambda$uZhgBQyqBuXwMMEU4nnz5UGAuA(i, i2, i3, i4, arrayList, (ImsStateCallbackController.CallbackWrapper) obj);
            }
        });
        removeInactiveCallbacks(arrayList, "onFeatureStateChange");
    }

    /* JADX INFO: renamed from: $r8$lambda$uZhg-BQyqBuXwMMEU4nnz5UGAuA, reason: not valid java name */
    public static /* synthetic */ void m981$r8$lambda$uZhgBQyqBuXwMMEU4nnz5UGAuA(int i, int i2, int i3, int i4, ArrayList arrayList, CallbackWrapper callbackWrapper) {
        if (i == callbackWrapper.mSubId && i2 == callbackWrapper.mRequiredFeature && !callbackWrapper.notifyState(i, i2, i3, i4)) {
            arrayList.add(callbackWrapper.mBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRegisterCallback(CallbackWrapper callbackWrapper) {
        if (callbackWrapper == null) {
            return;
        }
        this.mWrappers.put(callbackWrapper.mBinder, callbackWrapper);
        int i = 0;
        if (callbackWrapper.mRequiredFeature == 1) {
            while (i < this.mMmTelFeatureListeners.size()) {
                if (callbackWrapper.mSubId == getSubId(i)) {
                    if (((MmTelFeatureListener) this.mMmTelFeatureListeners.valueAt(i)).notifyState(callbackWrapper)) {
                        return;
                    }
                    this.mWrappers.remove(callbackWrapper.mBinder);
                    return;
                }
                i++;
            }
            return;
        }
        if (callbackWrapper.mRequiredFeature == 2) {
            while (i < this.mRcsFeatureListeners.size()) {
                if (callbackWrapper.mSubId == getSubId(i)) {
                    if (((RcsFeatureListener) this.mRcsFeatureListeners.valueAt(i)).notifyState(callbackWrapper)) {
                        return;
                    }
                    this.mWrappers.remove(callbackWrapper.mBinder);
                    return;
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnregisterCallback(IImsStateCallback iImsStateCallback) {
        if (iImsStateCallback == null) {
            return;
        }
        this.mWrappers.remove(iImsStateCallback.asBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigChanged(int i) {
        if (i >= this.mNumSlots) {
            logd("onCarrierConfigChanged invalid slotId " + i + ", mNumSlots=" + this.mNumSlots);
            return;
        }
        logv("onCarrierConfigChanged slotId=" + i);
        boolean zVerifyImsMmTelConfigured = verifyImsMmTelConfigured(i);
        if (i < this.mMmTelFeatureListeners.size()) {
            ((MmTelFeatureListener) this.mMmTelFeatureListeners.valueAt(i)).notifyConfigChanged(zVerifyImsMmTelConfigured);
        }
        boolean zVerifyImsRcsConfigured = verifyImsRcsConfigured(i);
        if (i < this.mRcsFeatureListeners.size()) {
            ((RcsFeatureListener) this.mRcsFeatureListeners.valueAt(i)).notifyConfigChanged(zVerifyImsRcsConfigured);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExternalRcsStateChanged(ExternalRcsFeatureState externalRcsFeatureState) {
        StringBuilder sb = new StringBuilder("onExternalRcsStateChanged slotId=");
        sb.append(externalRcsFeatureState.mSlotId);
        sb.append(", state=");
        sb.append(externalRcsFeatureState.mState == -1 ? "" : (String) ImsFeature.STATE_LOG_MAP.get(Integer.valueOf(externalRcsFeatureState.mState)));
        sb.append(", reason=");
        sb.append(imsStateReasonToString(externalRcsFeatureState.mReason));
        logv(sb.toString());
        RcsFeatureListener rcsFeatureListener = (RcsFeatureListener) this.mRcsFeatureListeners.get(externalRcsFeatureState.mSlotId);
        if (rcsFeatureListener != null) {
            rcsFeatureListener.notifyExternalRcsState(externalRcsFeatureState);
            return;
        }
        loge("onExternalRcsStateChanged slotId=" + externalRcsFeatureState.mSlotId + ", no listener.");
    }

    public void notifyExternalRcsStateChanged(int i, boolean z, boolean z2) {
        int i2;
        int i3 = 0;
        if (z) {
            i3 = 2;
            i2 = 0;
        } else if (z2) {
            i3 = -1;
            i2 = 3;
        } else {
            i2 = 4;
        }
        ExternalRcsFeatureState externalRcsFeatureState = new ExternalRcsFeatureState(i, i3, i2);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(5, externalRcsFeatureState));
    }

    public void notifyCarrierConfigChanged(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(4, i, 0));
    }

    public void registerImsStateCallback(int i, int i2, IImsStateCallback iImsStateCallback, String str) {
        CallbackWrapper callbackWrapper = new CallbackWrapper(i, i2, iImsStateCallback, str);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(2, callbackWrapper));
    }

    public void unregisterImsStateCallback(IImsStateCallback iImsStateCallback) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(3, iImsStateCallback));
    }

    public ImsManager getImsManager(int i) {
        return (ImsManager) this.mSubIdToImsManagerCache.get(Integer.valueOf(i));
    }

    private void removeInactiveCallbacks(ArrayList arrayList, String str) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IBinder iBinder = (IBinder) obj;
            CallbackWrapper callbackWrapper = (CallbackWrapper) this.mWrappers.get(iBinder);
            if (callbackWrapper != null) {
                callbackWrapper.notifyInactive();
                this.mWrappers.remove(iBinder);
            }
        }
        arrayList.clear();
    }

    private int getSubId(int i) {
        Phone phone = this.mPhoneFactoryProxy.getPhone(i);
        if (phone != null) {
            return phone.getSubId();
        }
        return -1;
    }

    private static boolean isActive(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyImsMmTelConfigured(int i) {
        ImsResolver imsResolver = this.mImsResolver;
        if (imsResolver == null) {
            loge("verifyImsMmTelConfigured mImsResolver is null");
            return false;
        }
        return imsResolver.isImsServiceConfiguredForFeature(i, 1);
    }

    private boolean verifyImsRcsConfigured(int i) {
        ImsResolver imsResolver = this.mImsResolver;
        if (imsResolver == null) {
            loge("verifyImsRcsConfigured mImsResolver is null");
            return false;
        }
        return imsResolver.isImsServiceConfiguredForFeature(i, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String connectorReasonToString(int i) {
        if (i == 0) {
            return "DISCONNECTED";
        }
        if (i == 1) {
            return "NOT_READY";
        }
        if (i == 2) {
            return "IMS_UNSUPPORTED";
        }
        if (i == 3) {
            return "SERVER_UNAVAILABLE";
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String imsStateReasonToString(int i) {
        switch (i) {
            case 0:
                return "READY";
            case 1:
                return "UNKNOWN_TEMPORARY_ERROR";
            case 2:
                return "UNKNOWN_PERMANENT_ERROR";
            case 3:
                return "IMS_SERVICE_DISCONNECTED";
            case 4:
                return "NO_IMS_SERVICE_CONFIGURED";
            case 5:
                return "SUBSCRIPTION_INACTIVE";
            case 6:
                return "IMS_SERVICE_NOT_READY";
            default:
                return "";
        }
    }

    private void release() {
        SparseArray sparseArray;
        this.mTelephonyRegistryManager.removeOnSubscriptionsChangedListener(this.mSubChangedListener);
        this.mApp.unregisterReceiver(this.mReceiver);
        int i = 0;
        int i2 = 0;
        while (true) {
            int size = this.mMmTelFeatureListeners.size();
            sparseArray = this.mMmTelFeatureListeners;
            if (i2 >= size) {
                break;
            }
            ((MmTelFeatureListener) sparseArray.valueAt(i2)).destroy();
            i2++;
        }
        sparseArray.clear();
        while (true) {
            int size2 = this.mRcsFeatureListeners.size();
            SparseArray sparseArray2 = this.mRcsFeatureListeners;
            if (i < size2) {
                ((RcsFeatureListener) sparseArray2.valueAt(i)).destroy();
                i++;
            } else {
                sparseArray2.clear();
                return;
            }
        }
    }

    public void destroy() {
        release();
        this.mHandler.getLooper().quit();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isRegistered(IImsStateCallback iImsStateCallback) {
        if (iImsStateCallback == null) {
            return false;
        }
        return this.mWrappers.containsKey(iImsStateCallback.asBinder());
    }

    public void dump(final IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.increaseIndent();
        synchronized (this.mDumpLock) {
            try {
                indentingPrintWriter.println("CallbackWrappers:");
                indentingPrintWriter.increaseIndent();
                this.mWrappers.values().forEach(new Consumer() { // from class: com.android.phone.ImsStateCallbackController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((ImsStateCallbackController.CallbackWrapper) obj).dump(indentingPrintWriter);
                    }
                });
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("MmTelFeatureListeners:");
                indentingPrintWriter.increaseIndent();
                for (int i = 0; i < this.mNumSlots; i++) {
                    MmTelFeatureListener mmTelFeatureListener = (MmTelFeatureListener) this.mMmTelFeatureListeners.get(i);
                    if (mmTelFeatureListener != null) {
                        mmTelFeatureListener.dump(indentingPrintWriter);
                    }
                }
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("RcsFeatureListeners:");
                indentingPrintWriter.increaseIndent();
                for (int i2 = 0; i2 < this.mNumSlots; i2++) {
                    RcsFeatureListener rcsFeatureListener = (RcsFeatureListener) this.mRcsFeatureListeners.get(i2);
                    if (rcsFeatureListener != null) {
                        rcsFeatureListener.dump(indentingPrintWriter);
                    }
                }
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("Most recent logs:");
                indentingPrintWriter.increaseIndent();
                sLocalLog.dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
            } catch (Throwable th) {
                throw th;
            }
        }
        indentingPrintWriter.decreaseIndent();
    }

    private static void logv(String str) {
        Rlog.d("ImsStateCallbackController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Rlog.d("ImsStateCallbackController", str);
        sLocalLog.log(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Rlog.e("ImsStateCallbackController", str);
        sLocalLog.log(str);
    }
}
