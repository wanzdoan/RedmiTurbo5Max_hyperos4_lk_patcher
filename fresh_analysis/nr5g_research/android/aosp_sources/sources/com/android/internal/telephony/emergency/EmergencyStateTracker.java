package com.android.internal.telephony.emergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telecom.Connection;
import android.telephony.CarrierConfigManager;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyStateTracker {
    public static final int EMERGENCY_TYPE_CALL = 1;
    public static final int EMERGENCY_TYPE_SMS = 2;
    private static EmergencyStateTracker INSTANCE = null;
    public static final int MSG_EXIT_EMERGENCY_MODE_DONE = 2;
    public static final int MSG_NEW_RINGING_CONNECTION = 5;
    public static final int MSG_SET_EMERGENCY_CALLBACK_MODE_DONE = 3;
    public static final int MSG_SET_EMERGENCY_MODE_DONE = 1;
    public static final int MSG_VOICE_REG_STATE_CHANGED = 6;
    private CompletableFuture mCallEmergencyModeFuture;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mConfigManager;
    private final Context mContext;
    private final BroadcastReceiver mEcmExitReceiver;
    private final long mEcmExitTimeoutMs;
    private CompletableFuture mEmergencyTransportChangedFuture;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private boolean mIsCallInEmergencyMode;
    private boolean mIsEmergencyCallStartedDuringEmergencySms;
    private boolean mIsEmergencyCallWaitingForEmergencyModeCompletion;
    private boolean mIsEmergencyModeInProgress;
    private boolean mIsEmergencySmsStartedDuringScbm;
    private boolean mIsInEcm;
    private boolean mIsInEmergencyCall;
    private boolean mIsInScbm;
    private boolean mIsSmsInEmergencyMode;
    private final boolean mIsSuplDdsSwitchRequiredForEmergencyCall;
    private boolean mIsTestEmergencyNumber;
    private boolean mIsTestEmergencyNumberForSms;
    private boolean mIsWaitingForRadioOff;
    private EmergencyRegistrationResult mLastEmergencyRegistrationResult;
    private Connection mNormalRoutingEmergencyConnection;
    private Runnable mOnEcmExitCompleteRunnable;
    private int mOngoingCallProperties;
    private Connection mOngoingConnection;
    private Phone mPhone;
    private PhoneFactoryProxy mPhoneFactoryProxy;
    private PhoneSwitcherProxy mPhoneSwitcherProxy;
    private RadioOnHelper mRadioOnHelper;
    private boolean mSentEmergencyCallState;
    private CompletableFuture mSmsEmergencyModeFuture;
    private Phone mSmsPhone;
    private final TelephonyManagerProxy mTelephonyManagerProxy;
    private final boolean mTurnOffNonEmergencyNbIotNtnSatelliteForEmergencyCall;
    private final boolean mTurnOffOemEnabledSatelliteDuringEmergencyCall;
    private final int mWaitForInServiceTimeoutMs;
    private final PowerManager.WakeLock mWakeLock;
    private boolean mWasEmergencyModeSetOnModem;
    private int mEmergencyMode = 0;
    private final Runnable mExitEcmRunnable = new Runnable() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final ArraySet mActiveEmergencyCalls = new ArraySet();
    private int mEmergencyCallDomain = 0;
    private int mEmergencyCallPhoneType = 0;
    private final Set mOngoingEmergencySmsIds = new ArraySet();
    private final Object mRegistrantidentifier = new Object();
    private final ArrayMap mNoSimEcbmSupported = new ArrayMap();
    private final ArrayMap mBroadcastEmergencyCallStateChanges = new ArrayMap();

    public interface PhoneFactoryProxy {
        Phone[] getPhones();
    }

    public interface PhoneSwitcherProxy {
        PhoneSwitcher getPhoneSwitcher();
    }

    public interface TelephonyManagerProxy {
        int getPhoneCount();

        int getSimState(int i);
    }

    private static boolean isVoWiFi(int i) {
        return (i & 8) > 0 || (i & NetworkStackConstants.IPV4_FLAG_MF) > 0;
    }

    public boolean isInCdmaEcm() {
        return false;
    }

    private static class OnDisconnectListener extends com.android.internal.telephony.Connection.ListenerBase {
        private final CompletableFuture mFuture;

        OnDisconnectListener(CompletableFuture completableFuture) {
            this.mFuture = completableFuture;
        }

        @Override // com.android.internal.telephony.Connection.ListenerBase, com.android.internal.telephony.Connection.Listener
        public void onDisconnect(int i) {
            this.mFuture.complete(Boolean.TRUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        exitEmergencyCallbackMode(5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, int i2, int i3, int i4) {
        onCarrierConfigurationChanged(i, i2);
    }

    private static class TelephonyManagerProxyImpl implements TelephonyManagerProxy {
        private final TelephonyManager mTelephonyManager;

        TelephonyManagerProxyImpl(Context context) {
            this.mTelephonyManager = new TelephonyManager(context);
        }

        @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.TelephonyManagerProxy
        public int getPhoneCount() {
            return this.mTelephonyManager.getActiveModemCount();
        }

        @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.TelephonyManagerProxy
        public int getSimState(int i) {
            return this.mTelephonyManager.getSimState(i);
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    private class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Code duplicated, block: B:78:0x025e  */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    Integer num = (Integer) asyncResult.userObj;
                    Rlog.v("EmergencyStateTracker", "MSG_SET_EMERGENCY_MODE_DONE for " + EmergencyStateTracker.emergencyTypeToString(num.intValue()));
                    Throwable th = asyncResult.exception;
                    EmergencyStateTracker emergencyStateTracker = EmergencyStateTracker.this;
                    if (th == null) {
                        emergencyStateTracker.mLastEmergencyRegistrationResult = (EmergencyRegistrationResult) asyncResult.result;
                    } else {
                        emergencyStateTracker.mLastEmergencyRegistrationResult = null;
                        Rlog.w("EmergencyStateTracker", "LastEmergencyRegistrationResult not set. AsyncResult.exception: " + asyncResult.exception);
                    }
                    EmergencyStateTracker.this.setEmergencyModeInProgress(false);
                    EmergencyStateTracker.this.maybeNotifyTransportChangeCompleted(num.intValue(), false);
                    if (num.intValue() == 1) {
                        if (EmergencyStateTracker.this.mLastEmergencyRegistrationResult == null && EmergencyStateTracker.this.mPhone != null && !SubscriptionManager.isValidSubscriptionId(EmergencyStateTracker.this.mPhone.getSubId())) {
                            EmergencyStateTracker emergencyStateTracker2 = EmergencyStateTracker.this;
                            if (emergencyStateTracker2.needToSwitchPhone(emergencyStateTracker2.mPhone)) {
                                Rlog.i("EmergencyStateTracker", "setEmergencyMode failed: need to switch stacks.");
                                EmergencyStateTracker.this.mEmergencyMode = 0;
                                EmergencyStateTracker.this.completeEmergencyMode(num.intValue(), 64);
                            } else {
                                EmergencyStateTracker.this.setIsInEmergencyCall(true);
                                EmergencyStateTracker.this.completeEmergencyMode(num.intValue());
                            }
                        } else {
                            EmergencyStateTracker.this.setIsInEmergencyCall(true);
                            EmergencyStateTracker.this.completeEmergencyMode(num.intValue());
                        }
                        if (EmergencyStateTracker.this.mSmsPhone != null) {
                            EmergencyStateTracker.this.completeEmergencyMode(2);
                        }
                        break;
                    } else if (num.intValue() == 2) {
                        if (EmergencyStateTracker.this.mPhone != null && EmergencyStateTracker.this.mSmsPhone != null) {
                            boolean z = EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms;
                            EmergencyStateTracker emergencyStateTracker3 = EmergencyStateTracker.this;
                            if (z) {
                                boolean zIsSamePhone = EmergencyStateTracker.isSamePhone(emergencyStateTracker3.mPhone, EmergencyStateTracker.this.mSmsPhone);
                                EmergencyStateTracker emergencyStateTracker4 = EmergencyStateTracker.this;
                                if (!zIsSamePhone) {
                                    emergencyStateTracker4.exitEmergencyMode(emergencyStateTracker4.mSmsPhone, num.intValue());
                                    EmergencyStateTracker.this.completeEmergencyMode(num.intValue(), 80);
                                    EmergencyStateTracker.this.exitEmergencySmsCallbackMode(3);
                                } else {
                                    boolean zIsInScbm = emergencyStateTracker4.isInScbm();
                                    EmergencyStateTracker emergencyStateTracker5 = EmergencyStateTracker.this;
                                    if (!zIsInScbm) {
                                        Phone phone = emergencyStateTracker5.mPhone;
                                        EmergencyStateTracker.this.mPhone = null;
                                        EmergencyStateTracker emergencyStateTracker6 = EmergencyStateTracker.this;
                                        emergencyStateTracker6.exitEmergencyMode(emergencyStateTracker6.mSmsPhone, num.intValue());
                                        EmergencyStateTracker.this.mPhone = phone;
                                    } else {
                                        emergencyStateTracker5.completeEmergencyMode(num.intValue());
                                        EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms = false;
                                        EmergencyStateTracker.this.exitEmergencySmsCallbackMode(3);
                                        EmergencyStateTracker emergencyStateTracker7 = EmergencyStateTracker.this;
                                        emergencyStateTracker7.turnOnRadioAndSwitchDds(emergencyStateTracker7.mPhone, 1, EmergencyStateTracker.this.mIsTestEmergencyNumber);
                                    }
                                }
                            } else {
                                emergencyStateTracker3.completeEmergencyMode(num.intValue());
                                if (EmergencyStateTracker.this.mIsEmergencyCallWaitingForEmergencyModeCompletion) {
                                    EmergencyStateTracker.this.mIsEmergencyCallWaitingForEmergencyModeCompletion = false;
                                    EmergencyStateTracker.this.setIsInEmergencyCall(true);
                                    EmergencyStateTracker.this.completeEmergencyMode(1);
                                }
                            }
                            break;
                        } else {
                            EmergencyStateTracker.this.completeEmergencyMode(num.intValue());
                            boolean z2 = EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms;
                            EmergencyStateTracker emergencyStateTracker8 = EmergencyStateTracker.this;
                            if (z2) {
                                emergencyStateTracker8.mIsEmergencyCallStartedDuringEmergencySms = false;
                                EmergencyStateTracker.this.exitEmergencySmsCallbackMode(3);
                                EmergencyStateTracker emergencyStateTracker9 = EmergencyStateTracker.this;
                                emergencyStateTracker9.turnOnRadioAndSwitchDds(emergencyStateTracker9.mPhone, 1, EmergencyStateTracker.this.mIsTestEmergencyNumber);
                                break;
                            } else if (emergencyStateTracker8.mPhone != null && EmergencyStateTracker.this.mIsEmergencyCallWaitingForEmergencyModeCompletion) {
                                EmergencyStateTracker.this.mIsEmergencyCallWaitingForEmergencyModeCompletion = false;
                                EmergencyStateTracker.this.setIsInEmergencyCall(true);
                                EmergencyStateTracker.this.completeEmergencyMode(1);
                                break;
                            }
                        }
                    }
                    break;
                case 2:
                    Integer num2 = (Integer) ((AsyncResult) message.obj).userObj;
                    Rlog.v("EmergencyStateTracker", "MSG_EXIT_EMERGENCY_MODE_DONE for " + EmergencyStateTracker.emergencyTypeToString(num2.intValue()));
                    EmergencyStateTracker.this.setEmergencyModeInProgress(false);
                    if (num2.intValue() == 1) {
                        EmergencyStateTracker.this.setIsInEmergencyCall(false);
                        if (EmergencyStateTracker.this.mOnEcmExitCompleteRunnable != null) {
                            EmergencyStateTracker.this.mOnEcmExitCompleteRunnable.run();
                            EmergencyStateTracker.this.mOnEcmExitCompleteRunnable = null;
                        }
                        if (EmergencyStateTracker.this.mPhone != null && EmergencyStateTracker.this.mEmergencyMode == 1) {
                            EmergencyStateTracker.this.setEmergencyModeInProgress(true);
                            EmergencyStateTracker.this.mWasEmergencyModeSetOnModem = true;
                            EmergencyStateTracker.this.mPhone.setEmergencyMode(1, EmergencyStateTracker.this.mHandler.obtainMessage(1, 1));
                            break;
                        }
                    } else {
                        if (num2.intValue() == 2) {
                            boolean z3 = EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms;
                            EmergencyStateTracker emergencyStateTracker10 = EmergencyStateTracker.this;
                            if (z3) {
                                emergencyStateTracker10.mIsEmergencyCallStartedDuringEmergencySms = false;
                                EmergencyStateTracker emergencyStateTracker11 = EmergencyStateTracker.this;
                                emergencyStateTracker11.turnOnRadioAndSwitchDds(emergencyStateTracker11.mPhone, 1, EmergencyStateTracker.this.mIsTestEmergencyNumber);
                            } else if (emergencyStateTracker10.mPhone != null && EmergencyStateTracker.this.mEmergencyMode == 1) {
                                EmergencyStateTracker.this.setEmergencyModeInProgress(true);
                                EmergencyStateTracker.this.mWasEmergencyModeSetOnModem = true;
                                EmergencyStateTracker.this.mPhone.setEmergencyMode(1, EmergencyStateTracker.this.mHandler.obtainMessage(1, 1));
                            } else if (EmergencyStateTracker.this.mIsEmergencySmsStartedDuringScbm) {
                                EmergencyStateTracker.this.mIsEmergencySmsStartedDuringScbm = false;
                                EmergencyStateTracker emergencyStateTracker12 = EmergencyStateTracker.this;
                                emergencyStateTracker12.setEmergencyMode(emergencyStateTracker12.mSmsPhone, num2.intValue(), 1, 1);
                            }
                        }
                        break;
                    }
                    break;
                case 3:
                    Integer num3 = (Integer) ((AsyncResult) message.obj).userObj;
                    Rlog.v("EmergencyStateTracker", "MSG_SET_EMERGENCY_CALLBACK_MODE_DONE for " + EmergencyStateTracker.emergencyTypeToString(num3.intValue()));
                    EmergencyStateTracker.this.setEmergencyModeInProgress(false);
                    if (num3.intValue() == 1) {
                        if (EmergencyStateTracker.this.mSmsPhone != null) {
                            EmergencyStateTracker.this.completeEmergencyMode(2);
                        }
                        break;
                    } else if (num3.intValue() == 2 && EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms) {
                        Phone phone2 = EmergencyStateTracker.this.mSmsPhone;
                        EmergencyStateTracker.this.exitEmergencySmsCallbackMode(3);
                        if (EmergencyStateTracker.this.mPhone != null && phone2 != null && !EmergencyStateTracker.isSamePhone(EmergencyStateTracker.this.mPhone, phone2)) {
                            Phone phone3 = EmergencyStateTracker.this.mPhone;
                            EmergencyStateTracker.this.mPhone = null;
                            EmergencyStateTracker.this.exitEmergencyMode(phone2, num3.intValue());
                            EmergencyStateTracker.this.mPhone = phone3;
                        } else {
                            EmergencyStateTracker.this.mIsEmergencyCallStartedDuringEmergencySms = false;
                            EmergencyStateTracker emergencyStateTracker13 = EmergencyStateTracker.this;
                            emergencyStateTracker13.turnOnRadioAndSwitchDds(emergencyStateTracker13.mPhone, 1, EmergencyStateTracker.this.mIsTestEmergencyNumber);
                        }
                        break;
                    }
                    break;
                case 4:
                    EmergencyStateTracker.this.exitEmergencySmsCallbackModeAndEmergencyMode(5, false);
                    break;
                case 5:
                    EmergencyStateTracker.this.handleNewRingingConnection(message);
                    break;
                case 6:
                    if (EmergencyStateTracker.this.mIsWaitingForRadioOff && EmergencyStateTracker.this.isPowerOff()) {
                        EmergencyStateTracker.this.unregisterForVoiceRegStateOrRatChanged();
                        if (EmergencyStateTracker.this.mPhone != null) {
                            EmergencyStateTracker emergencyStateTracker14 = EmergencyStateTracker.this;
                            emergencyStateTracker14.turnOnRadioAndSwitchDds(emergencyStateTracker14.mPhone, 1, EmergencyStateTracker.this.mIsTestEmergencyNumber);
                        }
                        break;
                    }
                    break;
            }
        }
    }

    public static void make(Context context, boolean z, int i, boolean z2, boolean z3, FeatureFlags featureFlags) {
        if (INSTANCE == null) {
            INSTANCE = new EmergencyStateTracker(context, Looper.myLooper(), z, i, z2, z3, featureFlags);
        }
    }

    public static EmergencyStateTracker getInstance() {
        EmergencyStateTracker emergencyStateTracker = INSTANCE;
        if (emergencyStateTracker != null) {
            return emergencyStateTracker;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("EmergencyStateTracker is not ready!");
        return null;
    }

    private EmergencyStateTracker(Context context, Looper looper, boolean z, int i, boolean z2, boolean z3, FeatureFlags featureFlags) {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda2
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i2, int i3, int i4, int i5) {
                this.f$0.lambda$new$1(i2, i3, i4, i5);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                    boolean booleanExtra = intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false);
                    Rlog.d("EmergencyStateTracker", "Received ACTION_EMERGENCY_CALLBACK_MODE_CHANGED isInEcm = " + booleanExtra);
                    if (booleanExtra) {
                        return;
                    }
                    EmergencyStateTracker.this.exitEmergencyCallbackMode();
                }
            }
        };
        this.mEcmExitReceiver = broadcastReceiver;
        this.mPhoneFactoryProxy = new PhoneFactoryProxy() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda3
            @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.PhoneFactoryProxy
            public final Phone[] getPhones() {
                return PhoneFactory.getPhones();
            }
        };
        this.mPhoneSwitcherProxy = new PhoneSwitcherProxy() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda4
            @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.PhoneSwitcherProxy
            public final PhoneSwitcher getPhoneSwitcher() {
                return PhoneSwitcher.getInstance();
            }
        };
        this.mEcmExitTimeoutMs = 300000L;
        this.mContext = context;
        MyHandler myHandler = new MyHandler(looper);
        this.mHandler = myHandler;
        this.mIsSuplDdsSwitchRequiredForEmergencyCall = z;
        this.mWaitForInServiceTimeoutMs = i;
        this.mTurnOffOemEnabledSatelliteDuringEmergencyCall = z2;
        this.mTurnOffNonEmergencyNbIotNtnSatelliteForEmergencyCall = z3;
        this.mFeatureFlags = featureFlags;
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mWakeLock = powerManager != null ? powerManager.newWakeLock(1, "telephony:EmergencyStateTracker") : null;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mConfigManager = carrierConfigManager;
        if (carrierConfigManager != null) {
            Objects.requireNonNull(myHandler);
            carrierConfigManager.registerCarrierConfigChangeListener(new NetworkTypeController$$ExternalSyntheticLambda2(myHandler), carrierConfigChangeListener);
        } else {
            Rlog.e("EmergencyStateTracker", "CarrierConfigLoader is not available.");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter, null, myHandler);
        this.mTelephonyManagerProxy = new TelephonyManagerProxyImpl(context);
        registerForNewRingingConnection();
        maybeResetEmergencyCallStateChangedIntent();
    }

    public EmergencyStateTracker(Context context, Looper looper, boolean z, int i, boolean z2, boolean z3, PhoneFactoryProxy phoneFactoryProxy, PhoneSwitcherProxy phoneSwitcherProxy, TelephonyManagerProxy telephonyManagerProxy, RadioOnHelper radioOnHelper, long j, FeatureFlags featureFlags) {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda2
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i2, int i3, int i4, int i5) {
                this.f$0.lambda$new$1(i2, i3, i4, i5);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                    boolean booleanExtra = intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false);
                    Rlog.d("EmergencyStateTracker", "Received ACTION_EMERGENCY_CALLBACK_MODE_CHANGED isInEcm = " + booleanExtra);
                    if (booleanExtra) {
                        return;
                    }
                    EmergencyStateTracker.this.exitEmergencyCallbackMode();
                }
            }
        };
        this.mEcmExitReceiver = broadcastReceiver;
        this.mPhoneFactoryProxy = new PhoneFactoryProxy() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda3
            @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.PhoneFactoryProxy
            public final Phone[] getPhones() {
                return PhoneFactory.getPhones();
            }
        };
        this.mPhoneSwitcherProxy = new PhoneSwitcherProxy() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda4
            @Override // com.android.internal.telephony.emergency.EmergencyStateTracker.PhoneSwitcherProxy
            public final PhoneSwitcher getPhoneSwitcher() {
                return PhoneSwitcher.getInstance();
            }
        };
        this.mContext = context;
        MyHandler myHandler = new MyHandler(looper);
        this.mHandler = myHandler;
        this.mIsSuplDdsSwitchRequiredForEmergencyCall = z;
        this.mWaitForInServiceTimeoutMs = i;
        this.mTurnOffOemEnabledSatelliteDuringEmergencyCall = z2;
        this.mTurnOffNonEmergencyNbIotNtnSatelliteForEmergencyCall = z3;
        this.mPhoneFactoryProxy = phoneFactoryProxy;
        this.mPhoneSwitcherProxy = phoneSwitcherProxy;
        this.mTelephonyManagerProxy = telephonyManagerProxy;
        this.mRadioOnHelper = radioOnHelper;
        this.mEcmExitTimeoutMs = j;
        this.mFeatureFlags = featureFlags;
        this.mWakeLock = null;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mConfigManager = carrierConfigManager;
        Objects.requireNonNull(myHandler);
        carrierConfigManager.registerCarrierConfigChangeListener(new NetworkTypeController$$ExternalSyntheticLambda2(myHandler), carrierConfigChangeListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter, null, myHandler);
        registerForNewRingingConnection();
    }

    public boolean hasActiveCall(Connection connection) {
        return this.mActiveEmergencyCalls.contains(connection);
    }

    public CompletableFuture<Integer> startEmergencyCall(Phone phone, Connection connection, boolean z) {
        Rlog.i("EmergencyStateTracker", "startEmergencyCall: phoneId=" + phone.getPhoneId() + ", callId=" + connection.getTelecomCallId());
        if (needToSwitchPhone(phone)) {
            Rlog.e("EmergencyStateTracker", "startEmergencyCall failed. need to switch stacks.");
            return CompletableFuture.completedFuture(64);
        }
        Phone phone2 = this.mPhone;
        if (phone2 != null) {
            if (!isSamePhone(phone2, phone) || (this.mActiveEmergencyCalls.isEmpty() && !isInEcm())) {
                Rlog.e("EmergencyStateTracker", "startEmergencyCall failed. Existing emergency call in progress.");
                return CompletableFuture.completedFuture(36);
            }
            exitEmergencySmsCallbackMode(3);
            this.mOngoingConnection = connection;
            this.mIsTestEmergencyNumber = z;
            if (isInEcm()) {
                this.mHandler.removeCallbacks(this.mExitEcmRunnable);
                releaseWakeLock();
                ((GsmCdmaPhone) this.mPhone).notifyEcbmTimerReset(Boolean.TRUE);
                this.mPhone.stopEmergencyCallbackMode(1, 3);
                this.mOngoingCallProperties = 0;
                this.mCallEmergencyModeFuture = new CompletableFuture();
                setEmergencyMode(this.mPhone, 1, 1, 1);
                return this.mCallEmergencyModeFuture;
            }
            setUnknownEmergencyRegistrationResultIfNotAvailable();
            return CompletableFuture.completedFuture(0);
        }
        this.mOngoingCallProperties = 0;
        this.mCallEmergencyModeFuture = new CompletableFuture();
        if (this.mSmsPhone != null) {
            this.mIsEmergencyCallStartedDuringEmergencySms = true;
            if (isInEmergencyMode() && !isEmergencyModeInProgress()) {
                if (!isSamePhone(this.mSmsPhone, phone)) {
                    exitEmergencyMode(this.mSmsPhone, 2);
                } else {
                    this.mIsEmergencyCallStartedDuringEmergencySms = false;
                }
                exitEmergencySmsCallbackMode(3);
            }
            if (this.mIsEmergencyCallStartedDuringEmergencySms) {
                this.mPhone = phone;
                this.mOngoingConnection = connection;
                this.mIsTestEmergencyNumber = z;
                sendEmergencyCallStateChange(phone, true);
                maybeRejectIncomingCall(null);
                return this.mCallEmergencyModeFuture;
            }
        }
        this.mPhone = phone;
        this.mOngoingConnection = connection;
        this.mIsTestEmergencyNumber = z;
        sendEmergencyCallStateChange(phone, true);
        final Connection connection2 = this.mOngoingConnection;
        maybeRejectIncomingCall(new Consumer() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$startEmergencyCall$2(connection2, (Boolean) obj);
            }
        });
        return this.mCallEmergencyModeFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startEmergencyCall$2(Connection connection, Boolean bool) {
        Rlog.i("EmergencyStateTracker", "maybeRejectIncomingCall : result = " + bool);
        if (!Objects.equals(this.mOngoingConnection, connection)) {
            Rlog.i("EmergencyStateTracker", "maybeRejectIncomingCall " + connection.getTelecomCallId() + " canceled.");
            return;
        }
        turnOnRadioAndSwitchDds(this.mPhone, 1, this.mIsTestEmergencyNumber);
    }

    public void endCall(Connection connection) {
        boolean zRemove = this.mActiveEmergencyCalls.remove(connection);
        if (Objects.equals(this.mOngoingConnection, connection)) {
            if (this.mActiveEmergencyCalls.isEmpty()) {
                this.mOngoingConnection = null;
                sendEmergencyCallStateChange(this.mPhone, false);
            } else {
                this.mOngoingConnection = (Connection) this.mActiveEmergencyCalls.valueAt(0);
            }
            this.mOngoingCallProperties = 0;
            unregisterForVoiceRegStateOrRatChanged();
        }
        if (zRemove && this.mActiveEmergencyCalls.isEmpty() && isEmergencyCallbackModeSupported(this.mPhone, true)) {
            enterEmergencyCallbackMode();
            if (this.mOngoingConnection == null) {
                this.mIsEmergencyCallStartedDuringEmergencySms = false;
                this.mCallEmergencyModeFuture = null;
            }
        } else if (this.mOngoingConnection == null) {
            if (isInEcm()) {
                this.mIsEmergencyCallStartedDuringEmergencySms = false;
                this.mCallEmergencyModeFuture = null;
                if (this.mActiveEmergencyCalls.isEmpty()) {
                    if (isEmergencyCallbackModeSupported(this.mPhone, true)) {
                        enterEmergencyCallbackMode();
                    } else {
                        exitEmergencyCallbackMode();
                        clearEmergencyCallInfo();
                    }
                }
            } else {
                if (isInScbm()) {
                    setIsInEmergencyCall(false);
                    setEmergencyCallbackMode(this.mSmsPhone, 2);
                } else {
                    exitEmergencyMode(this.mPhone, 1);
                }
                clearEmergencyCallInfo();
            }
        }
        maybeNotifyTransportChangeCompleted(1, true);
    }

    private void clearEmergencyCallInfo() {
        this.mIsEmergencyCallWaitingForEmergencyModeCompletion = false;
        this.mEmergencyCallDomain = 0;
        this.mEmergencyCallPhoneType = 0;
        this.mIsTestEmergencyNumber = false;
        this.mIsEmergencyCallStartedDuringEmergencySms = false;
        this.mCallEmergencyModeFuture = null;
        this.mOngoingConnection = null;
        this.mOngoingCallProperties = 0;
        this.mPhone = null;
    }

    private void setUnknownEmergencyRegistrationResultIfNotAvailable() {
        if (this.mLastEmergencyRegistrationResult == null) {
            this.mLastEmergencyRegistrationResult = new EmergencyRegistrationResult(0, 4, 0, false, false, 0, 0, "", "", "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchDdsAndSetEmergencyMode(final Phone phone, final int i) {
        switchDdsDelayed(phone, new Consumer() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$switchDdsAndSetEmergencyMode$3(phone, i, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchDdsAndSetEmergencyMode$3(Phone phone, int i, Boolean bool) {
        Rlog.i("EmergencyStateTracker", "switchDdsDelayed: result = " + bool);
        if (!bool.booleanValue()) {
            Rlog.e("EmergencyStateTracker", "DDS Switch failed.");
        }
        if (this.mEmergencyMode != 1) {
            setEmergencyMode(phone, i, 1, 1);
            return;
        }
        if (!isEmergencyModeInProgress()) {
            setUnknownEmergencyRegistrationResultIfNotAvailable();
            if (i == 1) {
                setIsInEmergencyCall(true);
            }
            completeEmergencyMode(i);
            return;
        }
        this.mIsEmergencyCallWaitingForEmergencyModeCompletion = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEmergencyMode(Phone phone, int i, int i2, int i3) {
        Rlog.i("EmergencyStateTracker", "setEmergencyMode from " + this.mEmergencyMode + " to " + i2 + " for " + emergencyTypeToString(i));
        if (this.mEmergencyMode == i2) {
            maybeNotifyTransportChangeCompleted(i, false);
            return;
        }
        if (i == 1 && i2 == 1 && isEmergencyModeInProgress() && !isInEmergencyMode()) {
            this.mEmergencyMode = i2;
            Rlog.i("EmergencyStateTracker", "setEmergencyMode wait for the completion of exitEmergencyMode");
            return;
        }
        this.mEmergencyMode = i2;
        setEmergencyModeInProgress(true);
        Message messageObtainMessage = this.mHandler.obtainMessage(i3, Integer.valueOf(i));
        this.mWasEmergencyModeSetOnModem = true;
        phone.setEmergencyMode(i2, messageObtainMessage);
    }

    private void setEmergencyCallbackMode(Phone phone, int i) {
        if (i == 1 || (i == 2 && this.mActiveEmergencyCalls.isEmpty() && this.mOngoingConnection == null && this.mOngoingEmergencySmsIds.isEmpty())) {
            setEmergencyMode(phone, i, 3, 3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeEmergencyMode(int i) {
        completeEmergencyMode(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeEmergencyMode(int i, int i2) {
        Phone phone;
        CompletableFuture completableFuture;
        CompletableFuture completableFuture2 = null;
        phone = null;
        Phone phone2 = null;
        if (i == 1) {
            completableFuture = this.mCallEmergencyModeFuture;
            if (i2 != 0) {
                clearEmergencyCallInfo();
            } else {
                phone2 = this.mPhone;
            }
        } else {
            if (i == 2) {
                completableFuture = this.mSmsEmergencyModeFuture;
                if (i2 != 0) {
                    clearEmergencySmsInfo();
                } else {
                    phone2 = this.mSmsPhone;
                }
            } else {
                phone = null;
            }
            if (completableFuture2 != null || completableFuture2.isDone()) {
            }
            if (phone != null) {
                updateEmergencyModeChanged(phone, i, true);
            }
            completableFuture2.complete(Integer.valueOf(i2));
            return;
        }
        CompletableFuture completableFuture3 = completableFuture;
        phone = phone2;
        completableFuture2 = completableFuture3;
        if (completableFuture2 != null) {
        }
    }

    public boolean isInEmergencyMode() {
        return this.mEmergencyMode != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEmergencyModeInProgress(boolean z) {
        this.mIsEmergencyModeInProgress = z;
    }

    private boolean isEmergencyModeInProgress() {
        return this.mIsEmergencyModeInProgress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsInEmergencyCall(boolean z) {
        this.mIsInEmergencyCall = z;
    }

    public boolean isInEmergencyCall() {
        return this.mIsInEmergencyCall;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitEmergencyMode(Phone phone, int i) {
        Phone phone2;
        Rlog.i("EmergencyStateTracker", "exitEmergencyMode for " + emergencyTypeToString(i));
        if (i == 1) {
            Phone phone3 = this.mSmsPhone;
            if (phone3 != null && isSamePhone(phone, phone3)) {
                Rlog.i("EmergencyStateTracker", "exitEmergencyMode: waits for emergency SMS end.");
                setIsInEmergencyCall(false);
                updateEmergencyModeChanged(phone, i, false);
                return;
            }
        } else if (i == 2 && (phone2 = this.mPhone) != null && isSamePhone(phone, phone2)) {
            Rlog.i("EmergencyStateTracker", "exitEmergencyMode: waits for emergency call end.");
            updateEmergencyModeChanged(phone, i, false);
            return;
        }
        if (this.mEmergencyMode == 0) {
            return;
        }
        this.mEmergencyMode = 0;
        setEmergencyModeInProgress(true);
        this.mLastEmergencyRegistrationResult = null;
        Message messageObtainMessage = this.mHandler.obtainMessage(2, Integer.valueOf(i));
        if (!this.mWasEmergencyModeSetOnModem) {
            Rlog.d("EmergencyStateTracker", "Emergency mode was not set on modem: Skipping exiting emergency mode.");
            AsyncResult.forMessage(messageObtainMessage, (Object) null, (Throwable) null);
            messageObtainMessage.sendToTarget();
            updateEmergencyModeChanged(phone, i, false);
            return;
        }
        this.mWasEmergencyModeSetOnModem = false;
        phone.exitEmergencyMode(messageObtainMessage);
        updateEmergencyModeChanged(phone, i, false);
    }

    public EmergencyRegistrationResult getEmergencyRegistrationResult() {
        return this.mLastEmergencyRegistrationResult;
    }

    private void waitForTransportChangeCompleted(CompletableFuture completableFuture) {
        if (completableFuture != null) {
            synchronized (completableFuture) {
                if (this.mEmergencyMode != 0 && !this.mHandler.getLooper().isCurrentThread()) {
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    long j = 1000 + jElapsedRealtime;
                    while (!completableFuture.isDone() && jElapsedRealtime < j) {
                        try {
                            completableFuture.wait(j - jElapsedRealtime);
                        } catch (Exception e) {
                            Rlog.e("EmergencyStateTracker", "waitForTransportChangeCompleted wait e=" + e);
                        }
                        jElapsedRealtime = SystemClock.elapsedRealtime();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:18:0x001e A[Catch: all -> 0x001a, TryCatch #0 {all -> 0x001a, blocks: (B:8:0x0009, B:10:0x000f, B:12:0x0015, B:18:0x001e, B:19:0x0026), top: B:24:0x0009 }] */
    public void maybeNotifyTransportChangeCompleted(int i, boolean z) {
        CompletableFuture completableFuture;
        if (i == 1 && (completableFuture = this.mEmergencyTransportChangedFuture) != null) {
            synchronized (completableFuture) {
                try {
                    if (!completableFuture.isDone()) {
                        if (!isEmergencyModeInProgress() && this.mEmergencyMode == 1) {
                            completableFuture.complete(Boolean.TRUE);
                            completableFuture.notifyAll();
                        } else if (z) {
                            completableFuture.complete(Boolean.TRUE);
                            completableFuture.notifyAll();
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void onEmergencyTransportChangedAndWait(int i, int i2) {
        if (i == 1 && i2 == 1) {
            CompletableFuture completableFuture = new CompletableFuture();
            synchronized (completableFuture) {
                this.mEmergencyTransportChangedFuture = completableFuture;
                lambda$onEmergencyTransportChanged$4(i, i2);
                waitForTransportChangeCompleted(completableFuture);
            }
            return;
        }
        lambda$onEmergencyTransportChanged$4(i, i2);
    }

    /* JADX INFO: renamed from: onEmergencyTransportChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onEmergencyTransportChanged$4(final int i, final int i2) {
        Phone phone;
        if (!this.mHandler.getLooper().isCurrentThread()) {
            this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onEmergencyTransportChanged$4(i, i2);
                }
            });
            return;
        }
        if (i == 1) {
            phone = this.mPhone;
        } else {
            phone = i == 2 ? this.mSmsPhone : null;
        }
        if (phone != null) {
            setEmergencyMode(phone, i, i2, 1);
        }
    }

    public void onEmergencyCallDomainUpdated(int i, Connection connection) {
        Rlog.d("EmergencyStateTracker", "domain update for callId: " + connection.getTelecomCallId());
        int i2 = 1;
        if (i != 1 && i != 2) {
            if (i == 5) {
                i2 = 2;
            } else if (i != 6) {
                Rlog.w("EmergencyStateTracker", "domain updated: Unexpected phoneType:" + i);
                i2 = -1;
            }
        }
        if (this.mEmergencyCallPhoneType != i) {
            Rlog.i("EmergencyStateTracker", "phoneType updated: from " + this.mEmergencyCallPhoneType + " to " + i);
            this.mEmergencyCallPhoneType = i;
        }
        if (this.mEmergencyCallDomain != i2) {
            Rlog.i("EmergencyStateTracker", "domain updated: from " + this.mEmergencyCallDomain + " to " + i2);
            this.mEmergencyCallDomain = i2;
        }
    }

    public void onEmergencyCallStateChanged(Call.State state, Connection connection) {
        if (state == Call.State.ACTIVE) {
            this.mActiveEmergencyCalls.add(connection);
            if (Objects.equals(this.mOngoingConnection, connection)) {
                Rlog.i("EmergencyStateTracker", "call connected " + connection.getTelecomCallId());
                if (this.mPhone != null && isVoWiFi(this.mOngoingCallProperties) && this.mEmergencyMode == 2) {
                    this.mPhone.cancelEmergencyNetworkScan(true, null);
                }
            }
        }
    }

    public void onEmergencyCallPropertiesChanged(int i, Connection connection) {
        if (Objects.equals(this.mOngoingConnection, connection)) {
            this.mOngoingCallProperties = i;
        }
    }

    public void onCellularRadioPowerOffRequested() {
        exitEmergencySmsCallbackModeAndEmergencyMode(0, true);
        exitEmergencyCallbackMode();
    }

    public boolean isEmergencyCallbackModeSupported(Phone phone, boolean z) {
        int i;
        if (z && ((i = this.mEmergencyCallPhoneType) == 1 || i == 0)) {
            Rlog.d("EmergencyStateTracker", "ecbmUnavailableRat");
            return false;
        }
        if (phone == null) {
            return false;
        }
        int subId = phone.getSubId();
        int phoneId = phone.getPhoneId();
        if (!isSimReady(phoneId, subId)) {
            Boolean boolValueOf = (Boolean) this.mNoSimEcbmSupported.get(Integer.valueOf(phoneId));
            if (boolValueOf == null) {
                boolValueOf = Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("no_sim_ecbm_support" + phoneId, false));
                Rlog.i("EmergencyStateTracker", "ECBM value not cached, load from preference");
                this.mNoSimEcbmSupported.put(Integer.valueOf(phoneId), boolValueOf);
            }
            Rlog.i("EmergencyStateTracker", "isEmergencyCallbackModeSupported savedConfig=" + boolValueOf);
            return boolValueOf.booleanValue();
        }
        return getConfig(subId, "imsemergency.emergency_callback_mode_supported_bool", true);
    }

    private void enterEmergencyCallbackMode() {
        Rlog.d("EmergencyStateTracker", "enter ECBM");
        setIsInEmergencyCall(false);
        if (!isInEcm()) {
            setIsInEcm(true);
            if (!this.mPhone.getUnitTestMode()) {
                TelephonyProperties.in_ecm_mode(Boolean.TRUE);
            }
            sendEmergencyCallbackModeChange();
            if (isInImsEcm()) {
                ((GsmCdmaPhone) this.mPhone).notifyEmergencyCallRegistrants(true);
            }
        } else {
            ((GsmCdmaPhone) this.mPhone).notifyEcbmTimerReset(Boolean.FALSE);
        }
        setEmergencyCallbackMode(this.mPhone, 1);
        long jLongValue = TelephonyProperties.ecm_exit_timer().orElse(Long.valueOf(this.mEcmExitTimeoutMs)).longValue();
        this.mPhone.startEmergencyCallbackMode(1, jLongValue);
        this.mHandler.postDelayed(this.mExitEcmRunnable, jLongValue);
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            wakeLock.acquire(jLongValue);
        }
    }

    public void exitEmergencyCallbackMode() {
        exitEmergencyCallbackMode(0);
    }

    public void exitEmergencyCallbackMode(int i) {
        Rlog.d("EmergencyStateTracker", "exit ECBM");
        this.mHandler.removeCallbacks(this.mExitEcmRunnable);
        if (isInEcm()) {
            setIsInEcm(false);
            if (!this.mPhone.getUnitTestMode()) {
                TelephonyProperties.in_ecm_mode(Boolean.FALSE);
            }
            releaseWakeLock();
            GsmCdmaPhone gsmCdmaPhone = (GsmCdmaPhone) this.mPhone;
            sendEmergencyCallbackModeChange();
            gsmCdmaPhone.notifyEmergencyCallRegistrants(false);
            gsmCdmaPhone.stopEmergencyCallbackMode(1, i);
            exitEmergencyMode(gsmCdmaPhone, 1);
        }
        if (this.mOngoingConnection == null) {
            this.mEmergencyCallDomain = 0;
            this.mEmergencyCallPhoneType = 0;
            this.mIsTestEmergencyNumber = false;
            this.mPhone = null;
        }
    }

    private void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        try {
            this.mWakeLock.release();
        } catch (Exception e) {
            Rlog.d("EmergencyStateTracker", "WakeLock already released: " + e.toString());
        }
    }

    public void exitEmergencyCallbackMode(Runnable runnable, int i) {
        this.mOnEcmExitCompleteRunnable = runnable;
        exitEmergencyCallbackMode(i);
    }

    private void sendEmergencyCallbackModeChange() {
        Rlog.d("EmergencyStateTracker", "sendEmergencyCallbackModeChange: isInEcm=" + isInEcm());
        Intent intent = new Intent("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        intent.putExtra("android.telephony.extra.PHONE_IN_ECM_STATE", isInEcm());
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    public boolean isInEcm() {
        return this.mIsInEcm;
    }

    private void setIsInEcm(boolean z) {
        this.mIsInEcm = z;
    }

    public boolean isInImsEcm() {
        return this.mEmergencyCallDomain == 2 && isInEcm();
    }

    public boolean isInEcm(Phone phone) {
        return isInEcm() && isSamePhone(this.mPhone, phone);
    }

    private void sendEmergencyCallStateChange(Phone phone, boolean z) {
        if (!(z && !this.mSentEmergencyCallState && getBroadcastEmergencyCallStateChanges(phone)) && (z || !this.mSentEmergencyCallState)) {
            return;
        }
        this.mSentEmergencyCallState = z;
        Rlog.i("EmergencyStateTracker", "sendEmergencyCallStateChange: " + z);
        Intent intent = new Intent("android.intent.action.EMERGENCY_CALL_STATE_CHANGED");
        intent.putExtra("android.telephony.extra.PHONE_IN_EMERGENCY_CALL", z);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phone.getPhoneId());
        this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    public CompletableFuture<Integer> startEmergencySms(Phone phone, String str, boolean z) {
        boolean z2 = false;
        Rlog.i("EmergencyStateTracker", "startEmergencySms: phoneId=" + phone.getPhoneId() + ", smsId=" + str + ", scbm=" + isInScbm());
        Phone phone2 = this.mPhone;
        if (phone2 != null && !isSamePhone(phone2, phone)) {
            Rlog.e("EmergencyStateTracker", "Emergency call is in progress on the other slot.");
            return CompletableFuture.completedFuture(36);
        }
        boolean zIsInScbm = isInScbm();
        Phone phone3 = this.mSmsPhone;
        if (phone3 != null && !isSamePhone(phone3, phone)) {
            if (!zIsInScbm) {
                Rlog.e("EmergencyStateTracker", "Emergency SMS is in progress on the other slot.");
                return CompletableFuture.completedFuture(36);
            }
            this.mIsEmergencySmsStartedDuringScbm = true;
            exitEmergencySmsCallbackModeAndEmergencyMode(4, false);
            z2 = true;
        }
        if (this.mSmsPhone != null && isInEmergencyMode() && isEmergencyModeInProgress() && !zIsInScbm) {
            Rlog.e("EmergencyStateTracker", "Existing emergency SMS is in progress and not in SCBM.");
            return CompletableFuture.completedFuture(36);
        }
        if (!phone.isRadioOn() || !phone.getServiceStateTracker().getDesiredPowerState()) {
            Rlog.e("EmergencyStateTracker", "Emergency SMS is not allowed while radio is/will be turned off.");
            return CompletableFuture.completedFuture(17);
        }
        this.mSmsPhone = phone;
        this.mIsTestEmergencyNumberForSms = z;
        this.mOngoingEmergencySmsIds.add(str);
        if (zIsInScbm) {
            if (!z2) {
                return CompletableFuture.completedFuture(0);
            }
            this.mSmsEmergencyModeFuture = new CompletableFuture();
        } else {
            if (isInEmergencyMode() && !isEmergencyModeInProgress()) {
                return CompletableFuture.completedFuture(0);
            }
            this.mSmsEmergencyModeFuture = new CompletableFuture();
            if (!isInEmergencyMode()) {
                setEmergencyMode(this.mSmsPhone, 2, 1, 1);
            }
        }
        return this.mSmsEmergencyModeFuture;
    }

    public void endSms(String str, boolean z, int i, boolean z2) {
        if (z && !z2) {
            Rlog.i("EmergencyStateTracker", "endSms: wait for additional SMS parts to be sent.");
        } else {
            this.mOngoingEmergencySmsIds.remove(str);
        }
        if (this.mOngoingEmergencySmsIds.isEmpty()) {
            this.mSmsEmergencyModeFuture = null;
            this.mIsEmergencySmsStartedDuringScbm = false;
            if (isInEcm() && this.mActiveEmergencyCalls.isEmpty() && this.mOngoingConnection == null) {
                setEmergencyCallbackMode(this.mPhone, 1);
            }
            if (z && i == 2 && (isInScbm() || isEmergencyCallbackModeSupported(this.mSmsPhone, false))) {
                enterEmergencySmsCallbackMode();
                return;
            }
            boolean zIsInScbm = isInScbm();
            Phone phone = this.mSmsPhone;
            if (zIsInScbm) {
                setEmergencyCallbackMode(phone, 2);
                return;
            }
            if (phone != null) {
                exitEmergencyMode(phone, 2);
            }
            clearEmergencySmsInfo();
        }
    }

    public void onEmergencySmsReceived() {
        if (isInScbm()) {
            Rlog.d("EmergencyStateTracker", "Emergency SMS received, re-initiate SCBM timer");
            enterEmergencySmsCallbackMode();
        }
    }

    private void clearEmergencySmsInfo() {
        this.mOngoingEmergencySmsIds.clear();
        this.mIsEmergencySmsStartedDuringScbm = false;
        this.mIsTestEmergencyNumberForSms = false;
        this.mSmsEmergencyModeFuture = null;
        this.mSmsPhone = null;
    }

    public boolean isInScbm() {
        return this.mIsInScbm;
    }

    private void setIsInScbm(boolean z) {
        this.mIsInScbm = z;
    }

    private void enterEmergencySmsCallbackMode() {
        StringBuilder sb = new StringBuilder("enter SCBM while ");
        sb.append(isInScbm() ? "in" : "not in");
        sb.append(" SCBM");
        Rlog.d("EmergencyStateTracker", sb.toString());
        boolean zIsInScbm = isInScbm();
        this.mHandler.removeMessages(4);
        if (!isInScbm()) {
            setIsInScbm(true);
        }
        setEmergencyCallbackMode(this.mSmsPhone, 2);
        int iIntValue = Long.valueOf(this.mEcmExitTimeoutMs).intValue();
        int subId = this.mSmsPhone.getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId) && (iIntValue = getConfig(subId, "emergency_sms_mode_timer_ms_int", iIntValue)) == 0) {
            iIntValue = Long.valueOf(this.mEcmExitTimeoutMs).intValue();
        }
        long j = iIntValue;
        this.mHandler.sendEmptyMessageDelayed(4, j);
        Phone phone = this.mSmsPhone;
        if (zIsInScbm) {
            phone.restartEmergencyCallbackMode(2, j);
        } else {
            phone.startEmergencyCallbackMode(2, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitEmergencySmsCallbackModeAndEmergencyMode(int i, boolean z) {
        Rlog.d("EmergencyStateTracker", "exit SCBM and emergency mode");
        Phone phone = this.mSmsPhone;
        boolean zIsInScbm = isInScbm();
        exitEmergencySmsCallbackMode(i);
        if ((phone == null || !z) && !(zIsInScbm && this.mOngoingEmergencySmsIds.isEmpty())) {
            return;
        }
        exitEmergencyMode(phone, 2);
        if (z) {
            clearEmergencySmsInfo();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitEmergencySmsCallbackMode(int i) {
        this.mHandler.removeMessages(4);
        boolean zIsInScbm = isInScbm();
        if (zIsInScbm) {
            Rlog.i("EmergencyStateTracker", "exit SCBM");
            this.mSmsPhone.stopEmergencyCallbackMode(2, i);
            setIsInScbm(false);
        }
        if (this.mOngoingEmergencySmsIds.isEmpty()) {
            if (zIsInScbm) {
                updateEmergencyModeChanged(this.mSmsPhone, 2, false);
            }
            this.mIsTestEmergencyNumberForSms = false;
            this.mSmsPhone = null;
        }
    }

    private boolean isRadioOn() {
        boolean zIsRadioOn = false;
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            zIsRadioOn |= phone.isRadioOn();
        }
        return zIsRadioOn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPowerOff() {
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            if (phone.getServiceStateTracker().getServiceState().getState() != 3) {
                return false;
            }
        }
        return true;
    }

    private void registerForVoiceRegStateOrRatChanged() {
        if (this.mIsWaitingForRadioOff) {
            return;
        }
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            phone.getServiceStateTracker().registerForVoiceRegStateOrRatChanged(this.mHandler, 6, null);
        }
        this.mIsWaitingForRadioOff = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForVoiceRegStateOrRatChanged() {
        if (this.mIsWaitingForRadioOff) {
            for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
                phone.getServiceStateTracker().unregisterForVoiceRegStateOrRatChanged(this.mHandler);
            }
            this.mIsWaitingForRadioOff = false;
        }
    }

    private boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void turnOnRadioAndSwitchDds(final Phone phone, final int i, boolean z) {
        boolean zIsAirplaneModeOn = isAirplaneModeOn(this.mContext);
        boolean z2 = !isRadioOn() || zIsAirplaneModeOn;
        boolean zShouldExitSatelliteMode = shouldExitSatelliteMode();
        if (zIsAirplaneModeOn && !isPowerOff() && !phone.getServiceStateTracker().getDesiredPowerState()) {
            Rlog.i("EmergencyStateTracker", "turnOnRadioAndSwitchDds: wait for the delayed power off");
            registerForVoiceRegStateOrRatChanged();
            return;
        }
        if (z2 || zShouldExitSatelliteMode) {
            Rlog.i("EmergencyStateTracker", "turnOnRadioAndSwitchDds: phoneId=" + phone.getPhoneId() + " for " + emergencyTypeToString(i));
            if (this.mRadioOnHelper == null) {
                this.mRadioOnHelper = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeRadioOnHelper(this.mContext);
            }
            boolean z3 = z2;
            final Connection connection = this.mOngoingConnection;
            final int i2 = z3 ? this.mWaitForInServiceTimeoutMs : 0;
            Rlog.i("EmergencyStateTracker", "turnOnRadioAndSwitchDds: timeout=" + i2);
            this.mRadioOnHelper.triggerRadioOnAndListen(new RadioOnStateListener.Callback() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker.2
                @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
                public void onComplete(RadioOnStateListener radioOnStateListener, boolean z4) {
                    if (connection.getState() == 6) {
                        Rlog.i("EmergencyStateTracker", "Call disconnected before the outgoing call was placed.Skipping call placement.");
                        for (Phone phone2 : EmergencyStateTracker.this.mPhoneFactoryProxy.getPhones()) {
                            phone2.setRadioPower(true, false, false, true);
                        }
                        return;
                    }
                    EmergencyStateTracker emergencyStateTracker = EmergencyStateTracker.this;
                    if (!z4) {
                        if (emergencyStateTracker.shouldExitSatelliteMode()) {
                            Rlog.e("EmergencyStateTracker", "Failed to turn off satellite modem.");
                            EmergencyStateTracker.this.completeEmergencyMode(i, 82);
                            return;
                        } else {
                            Rlog.e("EmergencyStateTracker", "Failed to turn on radio.");
                            EmergencyStateTracker.this.completeEmergencyMode(i, 17);
                            return;
                        }
                    }
                    if (!Objects.equals(emergencyStateTracker.mOngoingConnection, connection)) {
                        Rlog.i("EmergencyStateTracker", "onComplete " + connection.getTelecomCallId() + " canceled.");
                        return;
                    }
                    EmergencyStateTracker.this.switchDdsAndSetEmergencyMode(phone, i);
                }

                @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
                public boolean isOkToCall(Phone phone2, int i3, boolean z4) {
                    if (Objects.equals(EmergencyStateTracker.this.mOngoingConnection, connection)) {
                        return (phone2 != phone || i2 <= 0 || EmergencyStateTracker.isNetworkRegistered(phone2)) && phone2.getServiceStateTracker().isRadioOn() && !EmergencyStateTracker.this.shouldExitSatelliteMode();
                    }
                    Rlog.i("EmergencyStateTracker", "isOkToCall " + connection.getTelecomCallId() + " canceled.");
                    return true;
                }

                @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
                public boolean onTimeout(Phone phone2, int i3, boolean z4) {
                    if (Objects.equals(EmergencyStateTracker.this.mOngoingConnection, connection)) {
                        return phone2.getServiceStateTracker().isRadioOn() && !EmergencyStateTracker.this.shouldExitSatelliteMode();
                    }
                    Rlog.i("EmergencyStateTracker", "onTimeout " + connection.getTelecomCallId() + " canceled.");
                    return true;
                }
            }, z ^ true, phone, z, i2);
            return;
        }
        switchDdsAndSetEmergencyMode(phone, i);
    }

    public void switchDdsDelayed(Phone phone, Consumer<Boolean> consumer) {
        if (phone == null) {
            consumer.accept(Boolean.FALSE);
        }
        try {
            CompletableFuture completableFuturePossiblyOverrideDefaultDataForEmergencyCall = possiblyOverrideDefaultDataForEmergencyCall(phone);
            final CompletableFuture completableFuture = new CompletableFuture();
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    completableFuture.complete(Boolean.FALSE);
                }
            }, 1000L);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            completableFuturePossiblyOverrideDefaultDataForEmergencyCall.acceptEitherAsync((CompletionStage) completableFuture, (Consumer) consumer, (Executor) new NetworkTypeController$$ExternalSyntheticLambda2(handler));
        } catch (Exception e) {
            Rlog.w("EmergencyStateTracker", "switchDdsDelayed - exception= " + e.getMessage());
        }
    }

    private CompletableFuture possiblyOverrideDefaultDataForEmergencyCall(Phone phone) {
        boolean z = true;
        if (this.mTelephonyManagerProxy.getPhoneCount() <= 1) {
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (!this.mIsSuplDdsSwitchRequiredForEmergencyCall) {
            Rlog.d("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS, does not require DDS switch.");
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (!isAvailableForEmergencyCalls(phone)) {
            Rlog.d("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS");
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        boolean voiceRoaming = phone.getServiceState().getVoiceRoaming();
        String[] config = getConfig(phone.getSubId(), "gps.es_supl_data_plane_only_roaming_plmn_string_array");
        int i = 0;
        if (config != null && Arrays.asList(config).contains(phone.getServiceState().getOperatorNumeric())) {
            z = false;
        }
        if (voiceRoaming && z) {
            Rlog.d("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: roaming network is assumed to support CP fallback, not switching DDS.");
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (getConfig(phone.getSubId(), "gps.es_supl_control_plane_support_int", 0) != 2 && z) {
            Rlog.d("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS, carrier supports CP fallback.");
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        try {
            i = Integer.parseInt(getConfig(phone.getSubId(), "gps.es_extension_sec", "0"));
        } catch (NumberFormatException unused) {
        }
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        try {
            Rlog.d("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: overriding DDS for " + i + "seconds");
            this.mPhoneSwitcherProxy.getPhoneSwitcher().overrideDefaultDataForEmergency(phone.getPhoneId(), i, completableFuture);
            return completableFuture;
        } catch (Exception e) {
            Rlog.w("EmergencyStateTracker", "possiblyOverrideDefaultDataForEmergencyCall: exception = " + e.getMessage());
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }

    private String getConfig(int i, String str, String str2) {
        return getConfigBundle(i, str).getString(str, str2);
    }

    private int getConfig(int i, String str, int i2) {
        return getConfigBundle(i, str).getInt(str, i2);
    }

    private String[] getConfig(int i, String str) {
        return getConfigBundle(i, str).getStringArray(str);
    }

    private boolean getConfig(int i, String str, boolean z) {
        return getConfigBundle(i, str).getBoolean(str, z);
    }

    private PersistableBundle getConfigBundle(int i, String... strArr) {
        CarrierConfigManager carrierConfigManager = this.mConfigManager;
        return carrierConfigManager == null ? new PersistableBundle() : carrierConfigManager.getConfigForSubId(i, strArr);
    }

    private boolean isAvailableForEmergencyCalls(Phone phone) {
        return phone.getServiceState().getState() == 0 || phone.getServiceState().isEmergencyOnly();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isNetworkRegistered(Phone phone) {
        ServiceState serviceState = phone.getServiceStateTracker().getServiceState();
        if (serviceState == null) {
            return false;
        }
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo != null && networkRegistrationInfo.isNetworkRegistered()) {
            return true;
        }
        NetworkRegistrationInfo networkRegistrationInfo2 = serviceState.getNetworkRegistrationInfo(1, 1);
        return networkRegistrationInfo2 != null && networkRegistrationInfo2.isNetworkRegistered();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSamePhone(Phone phone, Phone phone2) {
        return (phone == null || phone2 == null || phone.getPhoneId() != phone2.getPhoneId()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String emergencyTypeToString(int i) {
        if (i == 1) {
            return "CALL";
        }
        if (i == 2) {
            return "SMS";
        }
        return SatelliteConstants.DEFAULT_PLMN;
    }

    private void onCarrierConfigurationChanged(int i, int i2) {
        SharedPreferences defaultSharedPreferences;
        Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged slotIndex=" + i + ", subId=" + i2);
        if (i < 0) {
            return;
        }
        Boolean bool = (Boolean) this.mNoSimEcbmSupported.get(Integer.valueOf(i));
        if (bool == null) {
            SharedPreferences defaultSharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(this.mContext);
            Boolean boolValueOf = Boolean.valueOf(defaultSharedPreferences2.getBoolean("no_sim_ecbm_support" + i, false));
            this.mNoSimEcbmSupported.put(Integer.valueOf(i), boolValueOf);
            Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged load from preference slotIndex=" + i + ", ecbmSupported=" + boolValueOf);
            defaultSharedPreferences = defaultSharedPreferences2;
            bool = boolValueOf;
        } else {
            defaultSharedPreferences = null;
        }
        if (!isSimReady(i, i2)) {
            Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged SIM not ready");
            return;
        }
        PersistableBundle configBundle = getConfigBundle(i2, "imsemergency.emergency_callback_mode_supported_bool", "broadcast_emergency_call_state_changes_bool");
        if (configBundle.isEmpty()) {
            Rlog.e("EmergencyStateTracker", "onCarrierConfigChanged empty result");
            return;
        }
        if (!CarrierConfigManager.isConfigForIdentifiedCarrier(configBundle)) {
            Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged not carrier specific configuration");
            return;
        }
        boolean z = configBundle.getBoolean("broadcast_emergency_call_state_changes_bool");
        this.mBroadcastEmergencyCallStateChanges.put(Integer.valueOf(i), Boolean.valueOf(z));
        Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged slotIndex=" + i + ", broadcastEmergencyCallStateChanges=" + z);
        boolean z2 = configBundle.getBoolean("imsemergency.emergency_callback_mode_supported_bool");
        if (z2 == bool.booleanValue()) {
            return;
        }
        this.mNoSimEcbmSupported.put(Integer.valueOf(i), Boolean.valueOf(z2));
        if (defaultSharedPreferences == null) {
            defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putBoolean("no_sim_ecbm_support" + i, z2);
        editorEdit.apply();
        Rlog.i("EmergencyStateTracker", "onCarrierConfigChanged preference updated slotIndex=" + i + ", ecbmSupported=" + z2);
    }

    private boolean isSimReady(int i, int i2) {
        return SubscriptionManager.isValidSubscriptionId(i2) && this.mTelephonyManagerProxy.getSimState(i) == 5;
    }

    private boolean getBroadcastEmergencyCallStateChanges(Phone phone) {
        Boolean bool = (Boolean) this.mBroadcastEmergencyCallStateChanges.get(Integer.valueOf(phone.getPhoneId()));
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void maybeResetEmergencyCallStateChangedIntent() {
        Intent intentRegisterReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.EMERGENCY_CALL_STATE_CHANGED"), 4);
        if (intentRegisterReceiver == null || !"android.intent.action.EMERGENCY_CALL_STATE_CHANGED".equals(intentRegisterReceiver.getAction())) {
            return;
        }
        boolean booleanExtra = intentRegisterReceiver.getBooleanExtra("android.telephony.extra.PHONE_IN_EMERGENCY_CALL", false);
        Rlog.i("EmergencyStateTracker", "maybeResetEmergencyCallStateChangedIntent isAlive=" + booleanExtra);
        if (booleanExtra) {
            Intent intent = new Intent("android.intent.action.EMERGENCY_CALL_STATE_CHANGED");
            intent.putExtra("android.telephony.extra.PHONE_IN_EMERGENCY_CALL", false);
            this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private Call getRingingCall(Phone phone) {
        if (phone == null) {
            return null;
        }
        Call ringingCall = phone.getRingingCall();
        if (ringingCall != null && ringingCall.getState() != Call.State.IDLE && ringingCall.getState() != Call.State.DISCONNECTED) {
            return ringingCall;
        }
        Phone imsPhone = phone.getImsPhone();
        if (imsPhone != null) {
            ringingCall = imsPhone.getRingingCall();
        }
        if (imsPhone == null || ringingCall == null || ringingCall.getState() == Call.State.IDLE || ringingCall.getState() == Call.State.DISCONNECTED) {
            return null;
        }
        return ringingCall;
    }

    private void maybeRejectIncomingCall(Consumer consumer) {
        Phone[] phones = this.mPhoneFactoryProxy.getPhones();
        if (phones == null) {
            if (consumer != null) {
                consumer.accept(Boolean.TRUE);
                return;
            }
            return;
        }
        Call ringingCall = null;
        for (Phone phone : phones) {
            ringingCall = getRingingCall(phone);
            if (ringingCall != null) {
                Rlog.i("EmergencyStateTracker", "maybeRejectIncomingCall found a ringing call");
                break;
            }
        }
        if (ringingCall == null) {
            if (consumer != null) {
                consumer.accept(Boolean.TRUE);
                return;
            }
            return;
        }
        try {
            ringingCall.hangup();
            if (consumer == null) {
                return;
            }
            CompletableFuture completableFuture = new CompletableFuture();
            ringingCall.getLatestConnection().addListener(new OnDisconnectListener(completableFuture));
            final CompletableFuture completableFuture2 = new CompletableFuture();
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    completableFuture2.complete(Boolean.FALSE);
                }
            }, SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            completableFuture.acceptEitherAsync((CompletionStage) completableFuture2, consumer, (Executor) new NetworkTypeController$$ExternalSyntheticLambda2(handler)).exceptionally(new Function() { // from class: com.android.internal.telephony.emergency.EmergencyStateTracker$$ExternalSyntheticLambda7
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return EmergencyStateTracker.$r8$lambda$LZgH_inD9ydkKdyCzZn5I0V1yrg((Throwable) obj);
                }
            });
        } catch (Exception e) {
            Rlog.w("EmergencyStateTracker", "maybeRejectIncomingCall - exception= " + e.getMessage());
            if (consumer != null) {
                consumer.accept(Boolean.FALSE);
            }
        }
    }

    public static /* synthetic */ Void $r8$lambda$LZgH_inD9ydkKdyCzZn5I0V1yrg(Throwable th) {
        Rlog.w("EmergencyStateTracker", "maybeRejectIncomingCall - exceptionally= " + th);
        return null;
    }

    private void registerForNewRingingConnection() {
        Phone[] phones = this.mPhoneFactoryProxy.getPhones();
        if (phones == null) {
            return;
        }
        for (Phone phone : phones) {
            phone.registerForNewRingingConnection(this.mHandler, 5, this.mRegistrantidentifier);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewRingingConnection(Message message) {
        Connection connection;
        com.android.internal.telephony.Connection connection2 = (com.android.internal.telephony.Connection) ((AsyncResult) message.obj).result;
        if (connection2 == null) {
            return;
        }
        Connection connection3 = this.mNormalRoutingEmergencyConnection;
        if ((connection3 == null || connection3.getState() == 4 || this.mNormalRoutingEmergencyConnection.getState() == 6) && ((connection = this.mOngoingConnection) == null || connection.getState() == 4 || this.mOngoingConnection.getState() == 6)) {
            return;
        }
        if (connection2.getPhoneType() == 5 && ((ImsPhoneConnection) connection2).isIncomingCallAutoRejected()) {
            Rlog.i("EmergencyStateTracker", "handleNewRingingConnection auto rejected call");
            return;
        }
        try {
            Rlog.i("EmergencyStateTracker", "handleNewRingingConnection silently drop incoming call");
            connection2.getCall().hangup();
        } catch (CallStateException e) {
            Rlog.w("EmergencyStateTracker", "handleNewRingingConnection", e);
        }
    }

    public void startNormalRoutingEmergencyCall(Phone phone, Connection connection, Consumer<Boolean> consumer) {
        Rlog.i("EmergencyStateTracker", "startNormalRoutingEmergencyCall: phoneId=" + phone.getPhoneId() + ", callId=" + connection.getTelecomCallId());
        this.mNormalRoutingEmergencyConnection = connection;
        maybeRejectIncomingCall(consumer);
    }

    public void endNormalRoutingEmergencyCall(Connection connection) {
        if (connection != this.mNormalRoutingEmergencyConnection) {
            return;
        }
        Rlog.i("EmergencyStateTracker", "endNormalRoutingEmergencyCall: callId=" + connection.getTelecomCallId());
        this.mNormalRoutingEmergencyConnection = null;
    }

    public void onNormalRoutingEmergencyCallStateChanged(Connection connection, int i) {
        if (connection != this.mNormalRoutingEmergencyConnection) {
            return;
        }
        if (i == 4 || i == 6) {
            endNormalRoutingEmergencyCall(connection);
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0026  */
    public boolean needToSwitchPhone(Phone phone) {
        int subId = phone.getSubId();
        int phoneId = phone.getPhoneId();
        boolean z = false;
        if (isSimReady(phoneId, subId)) {
            return false;
        }
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            Rlog.i("EmergencyStateTracker", "needToSwitchPhone SIM absent");
            if (phoneId != 0 || isThereOtherPhone(phoneId, true)) {
                z = true;
            }
        } else {
            Rlog.i("EmergencyStateTracker", "needToSwitchPhone SIM not ready");
            if ((phoneId == 0 && isThereOtherPhone(phoneId, false)) || (phoneId != 0 && isThereOtherPhone(phoneId, true))) {
                z = true;
            }
        }
        Rlog.i("EmergencyStateTracker", "needToSwitchPhone " + z);
        return z;
    }

    private boolean isThereOtherPhone(int i, boolean z) {
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            int phoneId = phone.getPhoneId();
            if (phoneId != i) {
                int subId = phone.getSubId();
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    int simState = this.mTelephonyManagerProxy.getSimState(phoneId);
                    if (simState == 5 || (z && simState != 1 && simState != 6)) {
                        Rlog.i("EmergencyStateTracker", "isThereOtherPhone found, ignoreLockState=" + z + ", phoneId=" + phoneId + ", simState=" + simState);
                        return true;
                    }
                    Rlog.i("EmergencyStateTracker", "isThereOtherPhone phoneId=" + phoneId + ", simState=" + simState);
                } else {
                    Rlog.i("EmergencyStateTracker", "isThereOtherPhone phoneId=" + phoneId + ", subId=" + subId);
                }
            }
        }
        return false;
    }

    public boolean shouldExitSatelliteMode() {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (!satelliteController.isSatelliteEnabledOrBeingEnabled() || !this.mTurnOffNonEmergencyNbIotNtnSatelliteForEmergencyCall) {
            return false;
        }
        if (satelliteController.isDemoModeEnabled() || !satelliteController.getRequestIsEmergency()) {
            return true;
        }
        int selectedSatelliteSubId = satelliteController.getSelectedSatelliteSubId();
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(selectedSatelliteSubId);
        if (subscriptionInfoInternal == null) {
            Rlog.e("EmergencyStateTracker", "satellite is/being enabled, but satellite sub " + selectedSatelliteSubId + " is null");
            return false;
        }
        if (subscriptionInfoInternal.getOnlyNonTerrestrialNetwork() == 1) {
            return this.mTurnOffOemEnabledSatelliteDuringEmergencyCall;
        }
        return satelliteController.shouldTurnOffCarrierSatelliteForEmergencyCall();
    }

    private void updateEmergencyModeChanged(Phone phone, int i, boolean z) {
        if (i == 1) {
            this.mIsCallInEmergencyMode = notifyEmergencyModeChanged(phone, 1, this.mIsCallInEmergencyMode, z, "Call");
        } else if (i == 2) {
            this.mIsSmsInEmergencyMode = notifyEmergencyModeChanged(phone, 2, this.mIsSmsInEmergencyMode, z, "SMS");
        }
    }

    private boolean notifyEmergencyModeChanged(Phone phone, int i, boolean z, boolean z2, String str) {
        if (z2 != z) {
            StringBuilder sb = new StringBuilder("notifyEmergencyModeChanged: ");
            sb.append(str);
            sb.append(" emergency mode ");
            sb.append(z2 ? "entered" : "exited");
            Rlog.d("EmergencyStateTracker", sb.toString());
            if (z2) {
                phone.notifyDomainSelectionEmergencyModeEntered(i);
                return z2;
            }
            phone.notifyDomainSelectionEmergencyModeExited(i);
        }
        return z2;
    }
}
