package com.android.services.telephony;

import android.app.ActivityManager;
import android.app.PropertyInvalidatedCache;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.AnomalyReporter;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.feature.MmTelFeature;
import android.text.TextUtils;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SimultaneousCallingTracker;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneInterfaceManager;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.xiaomi.internal.telephony.DynamicSarService;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class TelecomAccountRegistry {
    private static final int DEFAULT_SIM_ICON = R.drawable.ic_multi_sim;
    private static TelecomAccountRegistry sInstance;
    private final Runnable mCheckTelecomReadyRunnable;
    private final Context mContext;
    private ExecutorService mExecutor;
    private Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final ImsManager mImsManager;
    private List mIncomingCallNotifiers;
    private Runnable mRegisterOnSubscriptionsChangedListenerRunnable;
    private ExponentialBackoff mRegisterSubscriptionListenerBackoff;
    private List mSubscriptionInfos;
    private final SubscriptionManager mSubscriptionManager;
    private final TelecomManager mTelecomManager;
    private ExponentialBackoff mTelecomReadyBackoff;
    private TelephonyConnectionService mTelephonyConnectionService;
    private final TelephonyManager mTelephonyManager;
    private SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener = new AnonymousClass1();
    private final BroadcastReceiver mReceiver = new AnonymousClass2();
    private BroadcastReceiver mLocaleChangeReceiver = new AnonymousClass3();
    private final TelephonyCallback mTelephonyCallback = new TelecomAccountTelephonyCallback();
    private List mAccounts = new LinkedList();
    private final Object mAccountsLock = new Object();
    private int mSubscriptionListenerState = 0;
    private int mServiceState = 3;
    private int mActiveDataSubscriptionId = -1;
    private boolean mDoesUserSupportVideoCalling = UserHandle.of(ActivityManager.getCurrentUser()).isSystem();

    final class AccountEntry implements PstnPhoneCapabilitiesNotifier.Listener {
        private PhoneAccount mAccount;
        private PersistableBundle mCarrierConfigBundle;
        private boolean mImsMmTelRegistered;
        private RegistrationManager.RegistrationCallback mImsRegistrationCallback;
        private boolean mIsAdhocConfCapable;
        private boolean mIsCallComposerCapable;
        private boolean mIsEmergency;
        private boolean mIsEmergencyPreferred;
        private boolean mIsManageImsConferenceCallSupported;
        private boolean mIsMergeCallSupported;
        private boolean mIsMergeImsCallSupported;
        private boolean mIsMergeOfWifiCallsAllowedWhenVoWifiOff;
        private boolean mIsRttCapable;
        private boolean mIsShowPreciseFailedCause;
        private final boolean mIsTestAccount;
        private boolean mIsUsingSimCallManager;
        private boolean mIsVideoCapable;
        private boolean mIsVideoConferencingSupported;
        private boolean mIsVideoPauseSupported;
        private boolean mIsVideoPresenceSupported;
        private MmTelFeature.MmTelCapabilities mMmTelCapabilities;
        private ImsMmTelManager mMmTelManager;
        private ImsMmTelManager.CapabilityCallback mMmtelCapabilityCallback;
        private final Phone mPhone;
        private final PstnPhoneCapabilitiesNotifier mPhoneCapabilitiesNotifier;
        private SimultaneousCallingTracker mSCT;
        private Set mSimultaneousCallSupportedSubIds;
        private SimultaneousCallingTracker.Listener mSimultaneousCallingTrackerListener;

        AccountEntry(Phone phone, boolean z, boolean z2) {
            this.mPhone = phone;
            this.mIsEmergency = z;
            this.mIsTestAccount = z2;
            this.mIsAdhocConfCapable = phone.isImsRegistered();
            SimultaneousCallingTracker simultaneousCallingTracker = SimultaneousCallingTracker.getInstance();
            this.mSCT = simultaneousCallingTracker;
            this.mSimultaneousCallSupportedSubIds = simultaneousCallingTracker.getSubIdsSupportingSimultaneousCalling(phone.getSubId());
            PhoneAccount phoneAccountRegisterIfChanged = registerIfChanged();
            this.mAccount = phoneAccountRegisterIfChanged;
            Log.i(this, "Registered phoneAccount: %s with handle: %s", phoneAccountRegisterIfChanged, phoneAccountRegisterIfChanged == null ? "null" : phoneAccountRegisterIfChanged.getAccountHandle());
            this.mPhoneCapabilitiesNotifier = new PstnPhoneCapabilitiesNotifier(phone, this);
            if (z2 || z) {
                return;
            }
            try {
                if (phone.getContext().getPackageManager().hasSystemFeature("android.hardware.telephony.ims")) {
                    this.mMmTelManager = ImsMmTelManager.createForSubscriptionId(getSubId());
                }
                this.mMmtelCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.1
                    @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
                    public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                        AccountEntry.this.mMmTelCapabilities = mmTelCapabilities;
                        AccountEntry.this.updateRttCapability();
                        AccountEntry.this.registerImsRegistrationCallback();
                        AccountEntry.this.updateCallComposerCapability(mmTelCapabilities);
                    }
                };
                registerMmTelCapabilityCallback();
                this.mImsRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.2
                    @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
                    public void onRegistered(int i) {
                        AccountEntry.this.updateAdhocConfCapability(true);
                    }

                    @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
                    public void onRegistering(int i) {
                        AccountEntry.this.updateAdhocConfCapability(false);
                    }

                    @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
                    public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                        AccountEntry.this.updateAdhocConfCapability(false);
                    }
                };
                this.mSimultaneousCallingTrackerListener = new SimultaneousCallingTracker.Listener() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.3
                    public void onSimultaneousCallingSupportChanged(Map map) {
                        AccountEntry.this.updateSimultaneousCallSubSupportMap(map);
                    }
                };
                SimultaneousCallingTracker.getInstance().addListener(this.mSimultaneousCallingTrackerListener);
                Log.d("TelecomAccountRegistry", "Finished registering mSimultaneousCallingTrackerListener for phoneId = " + phone.getPhoneId() + "; subId = " + phone.getSubId(), new Object[0]);
            } catch (IllegalArgumentException e) {
                Log.i(this, "Not registering MmTel capabilities listener because the subid '" + getSubId() + "' is invalid: " + e.getMessage(), new Object[0]);
            }
        }

        void teardown() {
            this.mPhoneCapabilitiesNotifier.teardown();
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            if (imsMmTelManager != null) {
                ImsMmTelManager.CapabilityCallback capabilityCallback = this.mMmtelCapabilityCallback;
                if (capabilityCallback != null && this.mImsMmTelRegistered) {
                    imsMmTelManager.unregisterMmTelCapabilityCallback(capabilityCallback);
                    this.mImsMmTelRegistered = false;
                    this.mMmtelCapabilityCallback = null;
                }
                RegistrationManager.RegistrationCallback registrationCallback = this.mImsRegistrationCallback;
                if (registrationCallback != null) {
                    this.mMmTelManager.unregisterImsRegistrationCallback(registrationCallback);
                    this.mImsRegistrationCallback = null;
                }
                this.mMmTelManager = null;
            }
            SimultaneousCallingTracker.getInstance().removeListener(this.mSimultaneousCallingTrackerListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerMmTelCapabilityCallback() {
            ImsMmTelManager.CapabilityCallback capabilityCallback;
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            if (imsMmTelManager == null || (capabilityCallback = this.mMmtelCapabilityCallback) == null) {
                return;
            }
            try {
                if (this.mImsMmTelRegistered) {
                    imsMmTelManager.unregisterMmTelCapabilityCallback(capabilityCallback);
                    this.mImsMmTelRegistered = false;
                }
                this.mMmTelManager.registerMmTelCapabilityCallback(TelecomAccountRegistry.this.mContext.getMainExecutor(), this.mMmtelCapabilityCallback);
                this.mImsMmTelRegistered = true;
            } catch (ImsException e) {
                Log.w(this, "registerMmTelCapabilityCallback: registration failed, no ImsService available. Exception: " + e.getMessage(), new Object[0]);
                this.mImsMmTelRegistered = false;
            } catch (IllegalArgumentException e2) {
                Log.w(this, "registerMmTelCapabilityCallback: registration failed, invalid subscription, Exception" + e2.getMessage(), new Object[0]);
                this.mImsMmTelRegistered = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerImsRegistrationCallback() {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            if (imsMmTelManager == null || this.mImsRegistrationCallback == null) {
                return;
            }
            try {
                imsMmTelManager.registerImsRegistrationCallback(TelecomAccountRegistry.this.mContext.getMainExecutor(), this.mImsRegistrationCallback);
            } catch (ImsException e) {
                Log.w(this, "registerImsRegistrationCallback: registration failed, no ImsService available. Exception: " + e.getMessage(), new Object[0]);
            } catch (IllegalArgumentException e2) {
                Log.w(this, "registerImsRegistrationCallback: registration failed, invalid subscription, Exception" + e2.getMessage(), new Object[0]);
            }
        }

        public void reRegisterPstnPhoneAccount() {
            final PhoneAccount phoneAccountBuildPstnPhoneAccount = buildPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
            if (!phoneAccountBuildPstnPhoneAccount.equals(this.mAccount)) {
                TelecomAccountRegistry.this.mExecutor.submit(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Log.i(this, "reRegisterPstnPhoneAccount: subId: " + AccountEntry.this.getSubId() + " - re-register due to account change.", new Object[0]);
                        try {
                            TelecomAccountRegistry.this.mTelecomManager.registerPhoneAccount(phoneAccountBuildPstnPhoneAccount);
                        } catch (SecurityException e) {
                            Log.e(this, e, "reRegisterPstnPhoneAccount: acct=" + phoneAccountBuildPstnPhoneAccount + " failed", new Object[0]);
                            AnomalyReporter.reportAnomaly(UUID.fromString("5cd718af-4ef1-4835-8e5b-ebf78eb030ee"), "Failed to register a telephony phone account.");
                            throw e;
                        }
                    }
                });
                this.mAccount = phoneAccountBuildPstnPhoneAccount;
            } else {
                Log.i(this, "reRegisterPstnPhoneAccount: subId: " + getSubId() + " - no change", new Object[0]);
            }
        }

        private PhoneAccount registerPstnPhoneAccount(boolean z, boolean z2) {
            final PhoneAccount phoneAccountBuildPstnPhoneAccount = buildPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
            Log.i(this, "registerPstnPhoneAccount: Registering account=%s with Telecom. subId=%d", phoneAccountBuildPstnPhoneAccount, Integer.valueOf(getSubId()));
            TelecomAccountRegistry.this.mExecutor.submit(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.5
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        TelecomAccountRegistry.this.mTelecomManager.registerPhoneAccount(phoneAccountBuildPstnPhoneAccount);
                    } catch (SecurityException e) {
                        Log.e(this, e, "registerPstnPhoneAccount: acct=" + phoneAccountBuildPstnPhoneAccount + " failed", new Object[0]);
                        AnomalyReporter.reportAnomaly(UUID.fromString("5cd718af-4ef1-4835-8e5b-ebf78eb030ee"), "Failed to register a telephony phone account.");
                        throw e;
                    }
                }
            });
            return phoneAccountBuildPstnPhoneAccount;
        }

        /* JADX WARN: Code duplicated, block: B:110:0x035b  */
        /* JADX WARN: Code duplicated, block: B:34:0x0134  */
        /* JADX WARN: Code duplicated, block: B:35:0x0137  */
        /* JADX WARN: Code duplicated, block: B:38:0x014a  */
        /* JADX WARN: Code duplicated, block: B:41:0x015a  */
        /* JADX WARN: Code duplicated, block: B:44:0x0162  */
        /* JADX WARN: Code duplicated, block: B:45:0x0168  */
        /* JADX WARN: Code duplicated, block: B:48:0x0171  */
        /* JADX WARN: Code duplicated, block: B:51:0x0178  */
        /* JADX WARN: Code duplicated, block: B:54:0x01b9  */
        /* JADX WARN: Code duplicated, block: B:57:0x01c8  */
        /* JADX WARN: Code duplicated, block: B:59:0x01cc  */
        /* JADX WARN: Code duplicated, block: B:70:0x01f7  */
        /* JADX WARN: Code duplicated, block: B:79:0x0225  */
        /* JADX WARN: Code duplicated, block: B:87:0x025b  */
        /* JADX WARN: Code duplicated, block: B:93:0x02ae  */
        /* JADX WARN: Code duplicated, block: B:95:0x02b2  */
        /* JADX WARN: Code duplicated, block: B:97:0x02c7  */
        /* JADX WARN: Code duplicated, block: B:98:0x0301  */
        /* JADX WARN: Code duplicated, block: B:99:0x0318  */
        private PhoneAccount buildPstnPhoneAccount(boolean z, boolean z2) {
            Icon iconCreateWithBitmap;
            CharSequence string;
            int simSlotIndex;
            int iconTint;
            String string2;
            String string3;
            String strValueOf;
            int i;
            int i2;
            int i3;
            boolean zIsEmergencyPreferredAccount;
            boolean zIsVtEnabledByPlatform;
            boolean z3;
            Bundle bundle;
            int i4;
            boolean z4;
            String[] mergedSubscriberIds;
            String strConcat;
            Resources resources;
            Drawable drawable;
            String str = z2 ? "Test " : "";
            int subId = this.mPhone.getSubId();
            final UserHandle userHandle = this.mPhone.getUserHandle();
            PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix = PhoneUtils.makePstnPhoneAccountHandleWithPrefix(this.mPhone, str, z, userHandle);
            this.mCarrierConfigBundle = PhoneGlobals.getInstance().getCarrierConfigForSubId(subId);
            String subscriberId = this.mPhone.getSubscriberId();
            String line1Number = TelecomAccountRegistry.this.mTelephonyManager.getLine1Number(subId);
            if (line1Number == null) {
                line1Number = "";
            }
            String line1Number2 = this.mPhone.getLine1Number();
            if (line1Number2 == null) {
                line1Number2 = "";
            }
            SubscriptionInfo cachedSubscriptionInfo = TelecomAccountRegistry.this.getCachedSubscriptionInfo(subId);
            TelecomAccountRegistry.this.mTelephonyManager.createForSubscriptionId(subId);
            TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.this;
            if (z) {
                strValueOf = telecomAccountRegistry.mContext.getResources().getString(R.string.sim_label_emergency_calls);
                string3 = TelecomAccountRegistry.this.mContext.getResources().getString(R.string.sim_description_emergency_calls);
            } else {
                if (telecomAccountRegistry.mTelephonyManager.getPhoneCount() == 1) {
                    if (cachedSubscriptionInfo == null) {
                        strValueOf = "";
                    } else {
                        strValueOf = String.valueOf(cachedSubscriptionInfo.getDisplayName());
                    }
                    string3 = strValueOf;
                } else {
                    if (cachedSubscriptionInfo != null) {
                        string = cachedSubscriptionInfo.getDisplayName();
                        simSlotIndex = cachedSubscriptionInfo.getSimSlotIndex();
                        iconTint = cachedSubscriptionInfo.getIconTint();
                        iconCreateWithBitmap = Icon.createWithBitmap(cachedSubscriptionInfo.createIconBitmap(TelecomAccountRegistry.this.mContext));
                    } else {
                        iconCreateWithBitmap = null;
                        string = null;
                        simSlotIndex = -1;
                        iconTint = 0;
                    }
                    if (SubscriptionManager.isValidSlotIndex(simSlotIndex)) {
                        string2 = Integer.toString(simSlotIndex);
                    } else {
                        string2 = TelecomAccountRegistry.this.mContext.getResources().getString(R.string.unknown);
                    }
                    if (TextUtils.isEmpty(string)) {
                        Log.w(this, "Could not get a display name for subid: %d", Integer.valueOf(subId));
                        string = TelecomAccountRegistry.this.mContext.getResources().getString(R.string.sim_description_default, string2);
                    }
                    String str2 = str + ((Object) string);
                    string3 = str + TelecomAccountRegistry.this.mContext.getResources().getString(R.string.sim_description_default, string2);
                    strValueOf = str2;
                    i = simSlotIndex;
                    i2 = iconTint;
                }
                if (userHandle == null) {
                    i3 = 38;
                } else {
                    i3 = 6;
                }
                if (TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_pstnCanPlaceEmergencyCalls)) {
                    i3 |= 16;
                }
                zIsEmergencyPreferredAccount = isEmergencyPreferredAccount(subId, TelecomAccountRegistry.this.mActiveDataSubscriptionId);
                this.mIsEmergencyPreferred = zIsEmergencyPreferredAccount;
                if (zIsEmergencyPreferredAccount) {
                    i3 |= 8192;
                }
                if (isRttCurrentlySupported()) {
                    i3 |= 4096;
                    this.mIsRttCapable = true;
                } else {
                    this.mIsRttCapable = false;
                }
                if (isCarrierRttDowngradeToAudioSupported()) {
                    i3 |= 1048576;
                }
                if (this.mIsCallComposerCapable) {
                    i3 |= 32768;
                }
                this.mIsVideoCapable = this.mPhone.isVideoEnabled();
                Log.d(this, "buildPstnPhoneAccount, phoneId: %d, mIsVideoCapable: %b, ", Integer.valueOf(this.mPhone.getPhoneId()), Boolean.valueOf(this.mIsVideoCapable));
                zIsVtEnabledByPlatform = com.android.ims.ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId()).isVtEnabledByPlatform();
                if (!TelecomAccountRegistry.this.mDoesUserSupportVideoCalling) {
                    Log.i(this, "Disabling video calling for secondary user.", new Object[0]);
                    this.mIsVideoCapable = false;
                    zIsVtEnabledByPlatform = false;
                }
                if (this.mIsVideoCapable) {
                    i3 |= 8;
                }
                if (zIsVtEnabledByPlatform) {
                    i3 |= SmartNetworkRelayScore.TRAFFIC_UNIT;
                }
                boolean zIsCarrierVideoPresenceSupported = isCarrierVideoPresenceSupported();
                this.mIsVideoPresenceSupported = zIsCarrierVideoPresenceSupported;
                z3 = this.mIsVideoCapable;
                if (z3 && zIsCarrierVideoPresenceSupported) {
                    i3 |= DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER;
                }
                if (z3 && isCarrierEmergencyVideoCallsAllowed()) {
                    i3 |= 512;
                }
                this.mIsVideoPauseSupported = isCarrierVideoPauseSupported();
                bundle = new Bundle();
                if (isCarrierInstantLetteringSupported()) {
                    i3 |= 64;
                    bundle.putAll(getPhoneAccountExtras());
                }
                bundle.putAll(getExtrasForLowBatteryAlert());
                if (this.mIsAdhocConfCapable || !isCarrierAdhocConferenceCallSupported() || (TelecomAccountRegistry.this.mTelephonyConnectionService != null && TelecomAccountRegistry.this.mTelephonyConnectionService.isConferenceActive(phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix))) {
                    i4 = i3 & (-16385);
                } else {
                    i4 = i3 | 16384;
                }
                z4 = TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_handover_from);
                if (z4 && !z) {
                    bundle.putBoolean("android.telecom.extra.SUPPORTS_HANDOVER_FROM", z4);
                }
                bundle.putBoolean("android.telecom.extra.SUPPORTS_VIDEO_CALLING_FALLBACK", TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_video_calling_fallback));
                if (i != -1) {
                    bundle.putInt("android.telecom.extra.SORT_ORDER", i);
                }
                bundle.putInt("mediatek.telecom.extra.PHONE_ACCOUNT_CARRIER_CAPABILITIES", buildPhoneAccountCarrierCapabilties());
                this.mIsMergeCallSupported = isCarrierMergeCallSupported();
                this.mIsMergeImsCallSupported = isCarrierMergeImsCallSupported();
                this.mIsVideoConferencingSupported = isCarrierVideoConferencingSupported();
                this.mIsMergeOfWifiCallsAllowedWhenVoWifiOff = isCarrierMergeOfWifiCallsAllowedWhenVoWifiOff();
                this.mIsManageImsConferenceCallSupported = isCarrierManageImsConferenceCallSupported();
                this.mIsUsingSimCallManager = isCarrierUsingSimCallManager();
                this.mIsShowPreciseFailedCause = isCarrierShowPreciseFailedCause();
                if (isSubscriptionVoiceCapableByCarrierConfig() || (z && TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_emergency_account_emergency_calls_only))) {
                    i4 |= DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
                }
                if (iconCreateWithBitmap != null) {
                    resources = TelecomAccountRegistry.this.mContext.getResources();
                    drawable = resources.getDrawable(TelecomAccountRegistry.DEFAULT_SIM_ICON, null);
                    if (drawable != null) {
                        drawable.setTint(resources.getColor(R.color.default_sim_icon_tint_color, null));
                        drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
                        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmapCreateBitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        iconCreateWithBitmap = Icon.createWithBitmap(bitmapCreateBitmap);
                    } else {
                        Log.i(this, "Load DEFAULT_SIM_ICON faile, set blank content in icon", new Object[0]);
                        iconCreateWithBitmap = Icon.createWithBitmap(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
                    }
                }
                mergedSubscriberIds = TelecomAccountRegistry.this.mTelephonyManager.getMergedSubscriberIds();
                if (mergedSubscriberIds != null || subscriberId == null || z) {
                    strConcat = "";
                } else {
                    for (String str3 : mergedSubscriberIds) {
                        if (str3.equals(subscriberId)) {
                            strConcat = "group_".concat(line1Number);
                            Log.i(this, "Adding Merged Account with group: " + Rlog.pii("TelecomAccountRegistry", strConcat), new Object[0]);
                        }
                    }
                    strConcat = "";
                }
                PhoneAccount.Builder groupId = PhoneAccount.builder(phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix, strValueOf).setAddress(Uri.fromParts(GenericURI.TEL, line1Number, null)).setSubscriptionAddress(Uri.fromParts(GenericURI.TEL, line1Number2, null)).setCapabilities(i4).setIcon(iconCreateWithBitmap).setHighlightColor(i2).setShortDescription(string3).setSupportedUriSchemes(Arrays.asList(GenericURI.TEL, "voicemail")).setExtras(bundle).setGroupId(strConcat);
                groupId.setSimultaneousCallingRestriction((Set) this.mSimultaneousCallSupportedSubIds.stream().map(new Function() { // from class: com.android.services.telephony.TelecomAccountRegistry$AccountEntry$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return PhoneUtils.makePstnPhoneAccountHandleWithId(String.valueOf((Integer) obj), userHandle);
                    }
                }).collect(Collectors.toSet()));
                return groupId.build();
            }
            iconCreateWithBitmap = null;
            i = -1;
            i2 = 0;
            if (userHandle == null) {
                i3 = 38;
            } else {
                i3 = 6;
            }
            if (TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_pstnCanPlaceEmergencyCalls)) {
                i3 |= 16;
            }
            zIsEmergencyPreferredAccount = isEmergencyPreferredAccount(subId, TelecomAccountRegistry.this.mActiveDataSubscriptionId);
            this.mIsEmergencyPreferred = zIsEmergencyPreferredAccount;
            if (zIsEmergencyPreferredAccount) {
                i3 |= 8192;
            }
            if (isRttCurrentlySupported()) {
                i3 |= 4096;
                this.mIsRttCapable = true;
            } else {
                this.mIsRttCapable = false;
            }
            if (isCarrierRttDowngradeToAudioSupported()) {
                i3 |= 1048576;
            }
            if (this.mIsCallComposerCapable) {
                i3 |= 32768;
            }
            this.mIsVideoCapable = this.mPhone.isVideoEnabled();
            Log.d(this, "buildPstnPhoneAccount, phoneId: %d, mIsVideoCapable: %b, ", Integer.valueOf(this.mPhone.getPhoneId()), Boolean.valueOf(this.mIsVideoCapable));
            zIsVtEnabledByPlatform = com.android.ims.ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId()).isVtEnabledByPlatform();
            if (!TelecomAccountRegistry.this.mDoesUserSupportVideoCalling) {
                Log.i(this, "Disabling video calling for secondary user.", new Object[0]);
                this.mIsVideoCapable = false;
                zIsVtEnabledByPlatform = false;
            }
            if (this.mIsVideoCapable) {
                i3 |= 8;
            }
            if (zIsVtEnabledByPlatform) {
                i3 |= SmartNetworkRelayScore.TRAFFIC_UNIT;
            }
            boolean zIsCarrierVideoPresenceSupported2 = isCarrierVideoPresenceSupported();
            this.mIsVideoPresenceSupported = zIsCarrierVideoPresenceSupported2;
            z3 = this.mIsVideoCapable;
            if (z3) {
                i3 |= DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER;
            }
            if (z3) {
                i3 |= 512;
            }
            this.mIsVideoPauseSupported = isCarrierVideoPauseSupported();
            bundle = new Bundle();
            if (isCarrierInstantLetteringSupported()) {
                i3 |= 64;
                bundle.putAll(getPhoneAccountExtras());
            }
            bundle.putAll(getExtrasForLowBatteryAlert());
            if (this.mIsAdhocConfCapable) {
                i4 = i3 & (-16385);
            } else {
                i4 = i3 & (-16385);
            }
            z4 = TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_handover_from);
            if (z4) {
                bundle.putBoolean("android.telecom.extra.SUPPORTS_HANDOVER_FROM", z4);
            }
            bundle.putBoolean("android.telecom.extra.SUPPORTS_VIDEO_CALLING_FALLBACK", TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_video_calling_fallback));
            if (i != -1) {
                bundle.putInt("android.telecom.extra.SORT_ORDER", i);
            }
            bundle.putInt("mediatek.telecom.extra.PHONE_ACCOUNT_CARRIER_CAPABILITIES", buildPhoneAccountCarrierCapabilties());
            this.mIsMergeCallSupported = isCarrierMergeCallSupported();
            this.mIsMergeImsCallSupported = isCarrierMergeImsCallSupported();
            this.mIsVideoConferencingSupported = isCarrierVideoConferencingSupported();
            this.mIsMergeOfWifiCallsAllowedWhenVoWifiOff = isCarrierMergeOfWifiCallsAllowedWhenVoWifiOff();
            this.mIsManageImsConferenceCallSupported = isCarrierManageImsConferenceCallSupported();
            this.mIsUsingSimCallManager = isCarrierUsingSimCallManager();
            this.mIsShowPreciseFailedCause = isCarrierShowPreciseFailedCause();
            if (isSubscriptionVoiceCapableByCarrierConfig()) {
                i4 |= DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
            } else {
                i4 |= DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
            }
            if (iconCreateWithBitmap != null) {
                resources = TelecomAccountRegistry.this.mContext.getResources();
                drawable = resources.getDrawable(TelecomAccountRegistry.DEFAULT_SIM_ICON, null);
                if (drawable != null) {
                    drawable.setTint(resources.getColor(R.color.default_sim_icon_tint_color, null));
                    drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
                    Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas2 = new Canvas(bitmapCreateBitmap2);
                    drawable.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
                    drawable.draw(canvas2);
                    iconCreateWithBitmap = Icon.createWithBitmap(bitmapCreateBitmap2);
                } else {
                    Log.i(this, "Load DEFAULT_SIM_ICON faile, set blank content in icon", new Object[0]);
                    iconCreateWithBitmap = Icon.createWithBitmap(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
                }
            }
            mergedSubscriberIds = TelecomAccountRegistry.this.mTelephonyManager.getMergedSubscriberIds();
            if (mergedSubscriberIds != null) {
                strConcat = "";
            } else {
                strConcat = "";
            }
            PhoneAccount.Builder groupId2 = PhoneAccount.builder(phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix, strValueOf).setAddress(Uri.fromParts(GenericURI.TEL, line1Number, null)).setSubscriptionAddress(Uri.fromParts(GenericURI.TEL, line1Number2, null)).setCapabilities(i4).setIcon(iconCreateWithBitmap).setHighlightColor(i2).setShortDescription(string3).setSupportedUriSchemes(Arrays.asList(GenericURI.TEL, "voicemail")).setExtras(bundle).setGroupId(strConcat);
            groupId2.setSimultaneousCallingRestriction((Set) this.mSimultaneousCallSupportedSubIds.stream().map(new Function() { // from class: com.android.services.telephony.TelecomAccountRegistry$AccountEntry$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return PhoneUtils.makePstnPhoneAccountHandleWithId(String.valueOf((Integer) obj), userHandle);
                }
            }).collect(Collectors.toSet()));
            return groupId2.build();
        }

        public PhoneAccountHandle getPhoneAccountHandle() {
            PhoneAccount phoneAccount = this.mAccount;
            if (phoneAccount != null) {
                return phoneAccount.getAccountHandle();
            }
            return null;
        }

        public int getSubId() {
            return this.mPhone.getSubId();
        }

        private boolean isEmergencyPreferredAccount(int i, int i2) {
            Log.d(this, "isEmergencyPreferredAccount: subId=" + i + ", activeData=" + i2, new Object[0]);
            if (!TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_gnss_supl_requires_default_data_for_emergency)) {
                Log.d(this, "isEmergencyPreferredAccount: Device does not require preference.", new Object[0]);
                return false;
            }
            if (SubscriptionManagerService.getInstance() == null) {
                Log.d(this, "isEmergencyPreferredAccount: SubscriptionManagerService not available.", new Object[0]);
                return false;
            }
            int[] activeSubIdList = SubscriptionManagerService.getInstance().getActiveSubIdList(false);
            if (activeSubIdList == null || activeSubIdList.length <= 1) {
                Log.d(this, "isEmergencyPreferredAccount: one or less active subscriptions.", new Object[0]);
                return false;
            }
            if (!SubscriptionManager.isValidSubscriptionId(i)) {
                Log.d(this, "isEmergencyPreferredAccount: provided subId " + i + "is not valid.", new Object[0]);
                return false;
            }
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(i2);
            SubscriptionInfo subscriptionInfo = SubscriptionManagerService.getInstance().getSubscriptionInfo(i2);
            boolean z = zIsValidSubscriptionId && subscriptionInfo != null && subscriptionInfo.isOpportunistic();
            Log.d(this, "isEmergencyPreferredAccount: userDefaultData=" + defaultDataSubscriptionId + ", isActiveDataOppurtunistic=" + z, new Object[0]);
            if (!zIsValidSubscriptionId || z) {
                i2 = defaultDataSubscriptionId;
            }
            return i == i2;
        }

        private boolean isCarrierVideoPauseSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_pause_ims_video_calls_bool");
        }

        private boolean uceEnabled() {
            return SystemProperties.getInt("persist.vendor.mtk_uce_support", 0) == 1;
        }

        private boolean isCarrierVideoPresenceSupported() {
            boolean zUceEnabled = uceEnabled();
            boolean zIsUserContactDiscoverySettingEnabled = isUserContactDiscoverySettingEnabled();
            if (this.mCarrierConfigBundle != null) {
                Log.i("TelecomAccountRegistry", "isCarrierVideoPresenceSupported, " + zUceEnabled + ", " + zIsUserContactDiscoverySettingEnabled + ", " + this.mCarrierConfigBundle.getBoolean("ims.enable_presence_capability_exchange_bool") + ", " + this.mCarrierConfigBundle.getBoolean("use_rcs_presence_bool"), new Object[0]);
            }
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle != null) {
                if (persistableBundle.getBoolean("ims.enable_presence_capability_exchange_bool") && zUceEnabled) {
                    return true;
                }
                if (this.mCarrierConfigBundle.getBoolean("use_rcs_presence_bool") && zIsUserContactDiscoverySettingEnabled) {
                    return true;
                }
            }
            return false;
        }

        private boolean isUserContactDiscoverySettingEnabled() {
            try {
                return TelecomAccountRegistry.this.mImsManager.getImsRcsManager(this.mPhone.getSubId()).getUceAdapter().isUceSettingEnabled();
            } catch (Exception e) {
                Log.w("TelecomAccountRegistry", "isUserContactDiscoverySettingEnabled caught exception: " + e, new Object[0]);
                return false;
            }
        }

        private boolean isCarrierInstantLetteringSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("carrier_instant_lettering_available_bool");
        }

        private boolean isCarrierAdhocConferenceCallSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_adhoc_conference_calls_bool");
        }

        private boolean isCarrierMergeCallSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_conference_call_bool");
        }

        private boolean isCarrierMergeImsCallSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_ims_conference_call_bool");
        }

        private boolean isCarrierEmergencyVideoCallsAllowed() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("allow_emergency_video_calls_bool");
        }

        private boolean isCarrierVideoConferencingSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_video_conference_call_bool");
        }

        private boolean isCarrierMergeOfWifiCallsAllowedWhenVoWifiOff() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("allow_merge_wifi_calls_when_vowifi_off_bool");
        }

        private boolean isCarrierManageImsConferenceCallSupported() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("support_manage_ims_conference_call_bool");
        }

        private boolean isCarrierUsingSimCallManager() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return !TextUtils.isEmpty(persistableBundle.getString("default_sim_call_manager_string"));
        }

        private boolean isCarrierShowPreciseFailedCause() {
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return false;
            }
            return persistableBundle.getBoolean("show_precise_failed_cause_bool");
        }

        private boolean isCarrierAllowRttWhenRoaming() {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            if (carrierConfigForSubId == null) {
                return false;
            }
            return carrierConfigForSubId.getBoolean("rtt_supported_while_roaming_bool");
        }

        private boolean isCarrierRttDowngradeToAudioSupported() {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            if (carrierConfigForSubId == null) {
                return false;
            }
            return carrierConfigForSubId.getBoolean("rtt_downgrade_supported_bool");
        }

        private Bundle getPhoneAccountExtras() {
            String string;
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            int i = persistableBundle != null ? persistableBundle.getInt("carrier_instant_lettering_length_limit_int") : 64;
            PersistableBundle persistableBundle2 = this.mCarrierConfigBundle;
            if (persistableBundle2 != null) {
                string = persistableBundle2.getString("carrier_instant_lettering_encoding_string");
            } else {
                string = "";
            }
            Bundle bundle = new Bundle();
            bundle.putInt("android.telecom.extra.CALL_SUBJECT_MAX_LENGTH", i);
            bundle.putString("android.telecom.extra.CALL_SUBJECT_CHARACTER_ENCODING", string);
            return bundle;
        }

        private Bundle getExtrasForLowBatteryAlert() {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            if (carrierConfigForSubId == null) {
                return new Bundle();
            }
            int i = carrierConfigForSubId.getInt("low_battery_alert_interval_int", -1);
            int i2 = carrierConfigForSubId.getInt("low_battery_alert_threshold_int", -1);
            if (i == -1 || i2 == -1) {
                return new Bundle();
            }
            Bundle bundle = new Bundle();
            bundle.putInt("android.telecom.extra.LOW_BATTERY_ALERT_INTERVAL_SECONDS", i);
            bundle.putInt("android.telecom.extra.LOW_BATTERY_ALERT_LEVEL_THRESHOLD", i2);
            return bundle;
        }

        private boolean isSubscriptionVoiceCapableByCarrierConfig() {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            if (carrierConfigForSubId == null) {
                return true;
            }
            return Arrays.stream(carrierConfigForSubId.getIntArray("cellular_service_capabilities_int_array")).anyMatch(new IntPredicate() { // from class: com.android.services.telephony.TelecomAccountRegistry$AccountEntry$$ExternalSyntheticLambda1
                @Override // java.util.function.IntPredicate
                public final boolean test(int i) {
                    return TelecomAccountRegistry.AccountEntry.$r8$lambda$jvQrGIQbtRtVJz6VZslr6Iiy28c(i);
                }
            });
        }

        public static /* synthetic */ boolean $r8$lambda$jvQrGIQbtRtVJz6VZslr6Iiy28c(int i) {
            return i == 1;
        }

        @Override // com.android.services.telephony.PstnPhoneCapabilitiesNotifier.Listener
        public void onVideoCapabilitiesChanged(boolean z) {
            this.mIsVideoCapable = z;
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        reRegisterPstnPhoneAccount();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateSimultaneousCallSubSupportMap(Map map) {
            HashSet hashSet = new HashSet(3);
            hashSet.addAll((Collection) map.get(Integer.valueOf(this.mPhone.getSubId())));
            if (hashSet.equals(this.mSimultaneousCallSupportedSubIds)) {
                return;
            }
            this.mSimultaneousCallSupportedSubIds = hashSet;
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateAdhocConfCapability(boolean z) {
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        if (z != this.mIsAdhocConfCapable) {
                            Log.i(this, "updateAdhocConfCapability - changed, new value: " + z, new Object[0]);
                            this.mIsAdhocConfCapable = z;
                            this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateVideoPresenceCapability() {
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        boolean zIsCarrierVideoPresenceSupported = isCarrierVideoPresenceSupported();
                        if (this.mIsVideoPresenceSupported != zIsCarrierVideoPresenceSupported) {
                            Log.i(this, "updateVideoPresenceCapability for subId=" + this.mPhone.getSubId() + ", new value= " + zIsCarrierVideoPresenceSupported, new Object[0]);
                            this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateRttCapability() {
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        boolean zIsRttCurrentlySupported = isRttCurrentlySupported();
                        if (zIsRttCurrentlySupported != this.mIsRttCapable) {
                            Log.i(this, "updateRttCapability - changed, new value: " + zIsRttCurrentlySupported, new Object[0]);
                            this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateCallComposerCapability(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        boolean zIsCapable = mmTelCapabilities.isCapable(16);
                        if (zIsCapable != this.mIsCallComposerCapable) {
                            this.mIsCallComposerCapable = zIsCapable;
                            Log.i(this, "updateCallComposerCapability - changed, new value: " + zIsCapable, new Object[0]);
                            this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void updateDefaultDataSubId(int i) {
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    if (TelecomAccountRegistry.this.mAccounts.contains(this)) {
                        boolean zIsEmergencyPreferredAccount = isEmergencyPreferredAccount(this.mPhone.getSubId(), i);
                        if (zIsEmergencyPreferredAccount != this.mIsEmergencyPreferred) {
                            Log.i(this, "updateDefaultDataSubId - changed, new value: " + zIsEmergencyPreferredAccount, new Object[0]);
                            this.mAccount = registerPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        private boolean isRttCurrentlySupported() {
            if (this.mIsEmergency) {
                boolean z = TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_rtt);
                if (!TelecomAccountRegistry.this.mContext.getResources().getBoolean(R.bool.config_support_simless_emergency_rtt) || !z) {
                    Log.i(this, "isRttCurrentlySupported -- emergency acct and no device support", new Object[0]);
                    return false;
                }
                String lowerCase = this.mPhone.getServiceStateTracker().getLocaleTracker().getLastKnownCountryIso().toLowerCase(Locale.ROOT);
                String[] stringArray = TelecomAccountRegistry.this.mContext.getResources().getStringArray(R.array.config_simless_emergency_rtt_supported_countries);
                if (stringArray != null && !Arrays.stream(stringArray).noneMatch(Predicate.isEqual(lowerCase))) {
                    return true;
                }
                Log.i(this, "isRttCurrentlySupported -- emergency acct and not supported in this country: " + lowerCase, new Object[0]);
                return false;
            }
            boolean zIsImsVoiceAvailable = isImsVoiceAvailable();
            PhoneInterfaceManager phoneInterfaceManager = PhoneGlobals.getInstance().phoneMgr;
            boolean zIsRttEnabled = phoneInterfaceManager != null ? phoneInterfaceManager.isRttEnabled(this.mPhone.getSubId()) : false;
            boolean zIsNetworkRoaming = TelecomAccountRegistry.this.mTelephonyManager.isNetworkRoaming(this.mPhone.getSubId());
            boolean z2 = this.mPhone.getImsRegistrationTech() == 1 || this.mPhone.getImsRegistrationTech() == 2;
            boolean zIsCarrierAllowRttWhenRoaming = isCarrierAllowRttWhenRoaming();
            boolean z3 = (!zIsNetworkRoaming || z2 || zIsCarrierAllowRttWhenRoaming) ? false : true;
            Log.i(this, "isRttCurrentlySupported -- regular acct, hasVoiceAvailability: " + zIsImsVoiceAvailable + "\n isRttSupported: " + zIsRttEnabled + "\n alwaysAllowWhileRoaming: " + zIsCarrierAllowRttWhenRoaming + "\n isRoaming: " + zIsNetworkRoaming + "\n isOnWfc: " + z2 + "\n", new Object[0]);
            return zIsImsVoiceAvailable && zIsRttEnabled && !z3;
        }

        public boolean isVideoPauseSupported() {
            return this.mIsVideoCapable && this.mIsVideoPauseSupported;
        }

        public boolean isMergeCallSupported() {
            return this.mIsMergeCallSupported;
        }

        public boolean isMergeImsCallSupported() {
            return this.mIsMergeImsCallSupported;
        }

        public boolean isVideoConferencingSupported() {
            return this.mIsVideoConferencingSupported;
        }

        public boolean isMergeOfWifiCallsAllowedWhenVoWifiOff() {
            return this.mIsMergeOfWifiCallsAllowedWhenVoWifiOff;
        }

        public boolean isManageImsConferenceCallSupported() {
            return this.mIsManageImsConferenceCallSupported;
        }

        public boolean isUsingSimCallManager() {
            return this.mIsUsingSimCallManager;
        }

        public boolean isShowPreciseFailedCause() {
            return this.mIsShowPreciseFailedCause;
        }

        private boolean isImsVoiceAvailable() {
            MmTelFeature.MmTelCapabilities mmTelCapabilities = this.mMmTelCapabilities;
            if (mmTelCapabilities != null) {
                return mmTelCapabilities.isCapable(1);
            }
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            if (imsMmTelManager == null) {
                return false;
            }
            return imsMmTelManager.isAvailable(0, 1) || this.mMmTelManager.isAvailable(1, 1) || this.mMmTelManager.isAvailable(2, 1) || this.mMmTelManager.isAvailable(3, 1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v14 */
        /* JADX WARN: Type inference failed for: r0v15 */
        /* JADX WARN: Type inference failed for: r0v16 */
        /* JADX WARN: Type inference failed for: r0v17 */
        /* JADX WARN: Type inference failed for: r0v18 */
        /* JADX WARN: Type inference failed for: r0v19 */
        /* JADX WARN: Type inference failed for: r0v2 */
        /* JADX WARN: Type inference failed for: r0v20 */
        /* JADX WARN: Type inference failed for: r0v21 */
        /* JADX WARN: Type inference failed for: r0v3 */
        /* JADX WARN: Type inference failed for: r0v4 */
        /* JADX WARN: Type inference failed for: r0v5, types: [int] */
        private int buildPhoneAccountCarrierCapabilties() {
            ?? r0;
            PersistableBundle persistableBundle = this.mCarrierConfigBundle;
            if (persistableBundle == null) {
                return 0;
            }
            boolean z = persistableBundle.getBoolean("mtk_resume_hold_call_after_active_call_end_by_remote");
            if (this.mCarrierConfigBundle.getBoolean("mtk_disable_vt_over_wifi_bool")) {
                r0 = z;
                r0 = (z ? 1 : 0) | 4;
            }
            r0 = z;
            ?? r1 = r0;
            if (this.mCarrierConfigBundle.getBoolean("mtk_allow_one_video_call_only_bool")) {
                r1 = (r0 == true ? 1 : 0) | 32;
            }
            ?? r2 = r1;
            if (this.mCarrierConfigBundle.getBoolean("mtk_disallow_outgoing_calls_during_video_or_voice_call_bool")) {
                r2 = (r1 == true ? 1 : 0) | 64;
            }
            ?? r3 = r2;
            if (this.mCarrierConfigBundle.getBoolean("mtk_disallow_outgoing_calls_during_conference_bool")) {
                r3 = (r2 == true ? 1 : 0) | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
            }
            return this.mCarrierConfigBundle.getBoolean("mtk_support_enhanced_call_blocking_bool") ? r3 | DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER : r3;
        }

        private PhoneAccount registerIfChanged() {
            final PhoneAccount phoneAccountBuildPstnPhoneAccount = buildPstnPhoneAccount(this.mIsEmergency, this.mIsTestAccount);
            if (phoneAccountBuildPstnPhoneAccount == null) {
                Log.w(this, "cannot build phone account", new Object[0]);
                return null;
            }
            if (!phoneAccountBuildPstnPhoneAccount.equals(this.mAccount)) {
                TelecomAccountRegistry.this.mExecutor.submit(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.AccountEntry.6
                    @Override // java.lang.Runnable
                    public void run() {
                        Log.i(this, "registerIfChanged: subId: " + AccountEntry.this.getSubId() + " - register due to account change.", new Object[0]);
                        TelecomAccountRegistry.this.mTelecomManager.registerPhoneAccount(phoneAccountBuildPstnPhoneAccount);
                    }
                });
                return phoneAccountBuildPstnPhoneAccount;
            }
            Log.i(this, "registerIfChanged: subId: " + getSubId() + " - no change", new Object[0]);
            return phoneAccountBuildPstnPhoneAccount;
        }

        public int getPhoneId() {
            return this.mPhone.getPhoneId();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelecomAccountRegistry$1, reason: invalid class name */
    class AnonymousClass1 extends SubscriptionManager.OnSubscriptionsChangedListener {
        AnonymousClass1() {
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            if (TelecomAccountRegistry.this.mSubscriptionListenerState != 3) {
                TelecomAccountRegistry.this.mRegisterSubscriptionListenerBackoff.stop();
                TelecomAccountRegistry.this.mHandlerThread.quitSafely();
            }
            TelecomAccountRegistry.this.mSubscriptionListenerState = 3;
            Log.i(this, "TelecomAccountRegistry: onSubscriptionsChanged - update accounts", new Object[0]);
            TelecomAccountRegistry.this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSubscriptionsChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSubscriptionsChanged$0() {
            TelecomAccountRegistry.this.tearDownAccounts();
            TelecomAccountRegistry.this.setupAccounts();
        }

        public void onAddListenerFailed() {
            Log.w(this, "TelecomAccountRegistry: onAddListenerFailed - failed to register OnSubscriptionsChangedListener", new Object[0]);
            TelecomAccountRegistry.this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAddListenerFailed$1();
                }
            });
            int i = TelecomAccountRegistry.this.mSubscriptionListenerState;
            TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.this;
            if (i == 0) {
                telecomAccountRegistry.mSubscriptionListenerState = 2;
                TelecomAccountRegistry.this.mRegisterSubscriptionListenerBackoff.start();
            } else {
                telecomAccountRegistry.mRegisterSubscriptionListenerBackoff.notifyFailed();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAddListenerFailed$1() {
            TelecomAccountRegistry.this.tearDownAccounts();
            TelecomAccountRegistry.this.setupAccounts();
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelecomAccountRegistry$2, reason: invalid class name */
    class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            final BroadcastReceiver.PendingResult pendingResultGoAsync = goAsync();
            TelecomAccountRegistry.this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(intent, pendingResultGoAsync);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent, BroadcastReceiver.PendingResult pendingResult) {
            try {
                if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                    Log.i(this, "TelecomAccountRegistry: User changed, re-registering phone accounts.", new Object[0]);
                    UserHandle userHandle = (UserHandle) intent.getParcelableExtra("android.intent.extra.USER");
                    TelecomAccountRegistry.this.mDoesUserSupportVideoCalling = userHandle == null ? true : userHandle.isSystem();
                    TelecomAccountRegistry.this.tearDownAccounts();
                    TelecomAccountRegistry.this.setupAccounts();
                } else if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
                    Log.i(this, "TelecomAccountRegistry: Carrier-config changed, checking for phone account updates.", new Object[0]);
                    TelecomAccountRegistry.this.handleCarrierConfigChange(intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1));
                } else if ("com.android.ims.IMS_SERVICE_UP".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("android:phone_id", -1);
                    Log.d(this, "receive ACTION_IMS_SERVICE_UP, phoneId = " + intExtra, new Object[0]);
                    synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                        try {
                            for (AccountEntry accountEntry : TelecomAccountRegistry.this.mAccounts) {
                                if (intExtra == accountEntry.getPhoneId()) {
                                    accountEntry.registerMmTelCapabilityCallback();
                                }
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } else if ("android.telephony.action.MULTI_SIM_CONFIG_CHANGED".equals(intent.getAction())) {
                    Log.d(this, "Receive ACTION_MULTI_SIM_CONFIG_CHANGED = " + intent.getIntExtra("android.telephony.extra.ACTIVE_SIM_SUPPORTED_COUNT", 0), new Object[0]);
                    TelecomAccountRegistry.this.mIncomingCallNotifiers.clear();
                    for (Phone phone : PhoneFactory.getPhones()) {
                        Log.i(this, "new PstnIncomingCallNotifier for %s", phone);
                        TelecomAccountRegistry.this.mIncomingCallNotifiers.add(new PstnIncomingCallNotifier(phone));
                    }
                }
                if (pendingResult != null) {
                    pendingResult.finish();
                }
            } catch (Throwable th2) {
                if (pendingResult != null) {
                    pendingResult.finish();
                }
                throw th2;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelecomAccountRegistry$3, reason: invalid class name */
    class AnonymousClass3 extends BroadcastReceiver {
        AnonymousClass3() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            final BroadcastReceiver.PendingResult pendingResultGoAsync = goAsync();
            TelecomAccountRegistry.this.mHandler.post(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(pendingResultGoAsync);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(BroadcastReceiver.PendingResult pendingResult) {
            try {
                Log.i(this, "TelecomAccountRegistry: Locale change; re-registering phone accounts.", new Object[0]);
                TelecomAccountRegistry.this.tearDownAccounts();
                TelecomAccountRegistry.this.setupAccounts();
            } finally {
                if (pendingResult != null) {
                    pendingResult.finish();
                }
            }
        }
    }

    class TelecomAccountTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.ServiceStateListener {
        private TelecomAccountTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            int state = serviceState.getState();
            Log.i(this, "TelecomAccountRegistry: onServiceStateChanged: newState=%d, mServiceState=%d", Integer.valueOf(state), Integer.valueOf(TelecomAccountRegistry.this.mServiceState));
            if (state == 0 && TelecomAccountRegistry.this.mServiceState != state) {
                Log.i(this, "TelecomAccountRegistry: onServiceStateChanged: Tearing down and re-setting up accounts.", new Object[0]);
                TelecomAccountRegistry.this.tearDownAccounts();
                TelecomAccountRegistry.this.setupAccounts();
            } else {
                synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                    try {
                        Iterator it = TelecomAccountRegistry.this.mAccounts.iterator();
                        while (it.hasNext()) {
                            ((AccountEntry) it.next()).updateRttCapability();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            TelecomAccountRegistry.this.mServiceState = state;
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            TelecomAccountRegistry.this.mActiveDataSubscriptionId = i;
            synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                try {
                    Iterator it = TelecomAccountRegistry.this.mAccounts.iterator();
                    while (it.hasNext()) {
                        ((AccountEntry) it.next()).updateDefaultDataSubId(TelecomAccountRegistry.this.mActiveDataSubscriptionId);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTelecomReady() {
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager == null) {
            Log.i(this, "TelecomAccountRegistry: isTelecomReady: telecom null", new Object[0]);
            return true;
        }
        try {
            if (telecomManager.getSystemDialerPackage() == null) {
                Log.i(this, "TelecomAccountRegistry: isTelecomReady: telecom not ready", new Object[0]);
                return false;
            }
            Log.i(this, "TelecomAccountRegistry: isTelecomReady: telecom ready", new Object[0]);
            return true;
        } catch (Exception unused) {
            Log.i(this, "TelecomAccountRegistry: isTelecomReady: telecom exception", new Object[0]);
            return true;
        }
    }

    TelecomAccountRegistry(Context context) {
        HandlerThread handlerThread = new HandlerThread("TelecomAccountRegistry");
        this.mHandlerThread = handlerThread;
        this.mIncomingCallNotifiers = new ArrayList();
        this.mRegisterOnSubscriptionsChangedListenerRunnable = new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.4
            @Override // java.lang.Runnable
            public void run() {
                if (TelecomAccountRegistry.this.mSubscriptionListenerState != 3) {
                    Log.i(this, "TelecomAccountRegistry: performing delayed register.", new Object[0]);
                    SubscriptionManager.from(TelecomAccountRegistry.this.mContext).addOnSubscriptionsChangedListener(TelecomAccountRegistry.this.mOnSubscriptionsChangedListener);
                }
            }
        };
        Runnable runnable = new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.5
            @Override // java.lang.Runnable
            public void run() {
                boolean zIsTelecomReady = TelecomAccountRegistry.this.isTelecomReady();
                TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.this;
                if (zIsTelecomReady) {
                    telecomAccountRegistry.setupOnBootInternal();
                    return;
                }
                telecomAccountRegistry.mTelecomReadyBackoff.notifyFailed();
                Log.i(this, "TelecomAccountRegistry: telecom not ready, retrying in " + TelecomAccountRegistry.this.mTelecomReadyBackoff.getCurrentDelay() + " ms", new Object[0]);
            }
        };
        this.mCheckTelecomReadyRunnable = runnable;
        this.mContext = context;
        this.mTelecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        this.mImsManager = (ImsManager) context.getSystemService(ImsManager.class);
        this.mTelephonyManager = TelephonyManager.from(context);
        this.mSubscriptionManager = SubscriptionManager.from(context);
        handlerThread.start();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mRegisterSubscriptionListenerBackoff = new ExponentialBackoff(1000L, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS, 2, handlerThread.getLooper(), this.mRegisterOnSubscriptionsChangedListenerRunnable);
        this.mTelecomReadyBackoff = new ExponentialBackoff(250L, 4000L, 2, context.getMainLooper(), runnable);
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public static synchronized TelecomAccountRegistry getInstance(Context context) {
        try {
            if (sInstance == null && context != null) {
                int i = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
                PackageManager packageManager = context.getPackageManager();
                if (i >= 35) {
                    if (packageManager != null && packageManager.hasSystemFeature("android.hardware.telephony") && packageManager.hasSystemFeature("android.hardware.telephony.calling")) {
                        sInstance = new TelecomAccountRegistry(context);
                    } else {
                        Log.d("TelecomAccountRegistry", "Not initializing TelecomAccountRegistry: missing telephony/calling feature(s)", new Object[0]);
                    }
                } else if (packageManager != null && (packageManager.hasSystemFeature("android.hardware.telephony") || packageManager.hasSystemFeature("android.hardware.telephony.calling"))) {
                    sInstance = new TelecomAccountRegistry(context);
                } else {
                    Log.d("TelecomAccountRegistry", "Not initializing TelecomAccountRegistry: missing telephony or calling feature(s)", new Object[0]);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    void setTelephonyConnectionService(TelephonyConnectionService telephonyConnectionService) {
        this.mTelephonyConnectionService = telephonyConnectionService;
    }

    public TelephonyConnectionService getTelephonyConnectionService() {
        return this.mTelephonyConnectionService;
    }

    boolean isVideoPauseSupported(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isVideoPauseSupported();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isMergeCallSupported(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isMergeCallSupported();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isVideoConferencingSupported(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isVideoConferencingSupported();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isMergeOfWifiCallsAllowedWhenVoWifiOff(final PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                Optional optionalFindFirst = this.mAccounts.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelecomAccountRegistry$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ((TelecomAccountRegistry.AccountEntry) obj).getPhoneAccountHandle().equals(phoneAccountHandle);
                    }
                }).findFirst();
                if (!optionalFindFirst.isPresent()) {
                    return false;
                }
                return ((AccountEntry) optionalFindFirst.get()).isMergeOfWifiCallsAllowedWhenVoWifiOff();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isMergeImsCallSupported(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isMergeImsCallSupported();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean isManageImsConferenceCallSupported(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isManageImsConferenceCallSupported();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    boolean isShowPreciseFailedCause(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isShowPreciseFailedCause();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    SubscriptionManager getSubscriptionManager() {
        return this.mSubscriptionManager;
    }

    public void refreshAdhocConference(boolean z) {
        synchronized (this.mAccountsLock) {
            try {
                Log.v(this, "refreshAdhocConference isEnable = " + z, new Object[0]);
                Iterator it = this.mAccounts.iterator();
                while (it.hasNext()) {
                    lambda$refreshAdhocConference$2((AccountEntry) it.next(), z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void refreshAdhocConference(final boolean z, final PhoneAccountHandle phoneAccountHandle) {
        if (!isSimultaneousCallingEnabled() || phoneAccountHandle == null) {
            refreshAdhocConference(z);
            return;
        }
        synchronized (this.mAccountsLock) {
            Log.v(this, "refreshAdhocConferenceCallSequencing isEnable = " + z + ", for handle = " + phoneAccountHandle, new Object[0]);
            this.mAccounts.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelecomAccountRegistry$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.equals(((TelecomAccountRegistry.AccountEntry) obj).getPhoneAccountHandle(), phoneAccountHandle);
                }
            }).findFirst().ifPresent(new Consumer() { // from class: com.android.services.telephony.TelecomAccountRegistry$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$refreshAdhocConference$2(z, (TelecomAccountRegistry.AccountEntry) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateAdhocCapabilityForAccountEntry, reason: merged with bridge method [inline-methods] */
    public void lambda$refreshAdhocConference$2(AccountEntry accountEntry, boolean z) {
        boolean zHasCapabilities = accountEntry.mAccount.hasCapabilities(16384);
        if (!z && zHasCapabilities) {
            accountEntry.updateAdhocConfCapability(false);
        } else {
            if (!z || zHasCapabilities) {
                return;
            }
            accountEntry.updateAdhocConfCapability(accountEntry.mPhone.isImsRegistered());
        }
    }

    private boolean isSimultaneousCallingEnabled() {
        try {
            return this.mTelephonyManager.getMaxNumberOfSimultaneouslyActiveSims() > 1 || this.mTelephonyManager.getPhoneCapability().getMaxActiveVoiceSubscriptions() > 1;
        } catch (UnsupportedOperationException unused) {
            Log.w(this, "Telephony not supported", new Object[0]);
            return false;
        } catch (Exception e) {
            Log.w(this, "exception in isSimultaneousCallingEnabled(): ", e);
            return false;
        }
    }

    public boolean isUsingSimCallManager(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return accountEntry.isUsingSimCallManager();
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setupOnBoot() {
        if (!isTelecomReady()) {
            Log.i(this, "setupOnBoot: delaying start for Telecom...", new Object[0]);
            this.mTelecomReadyBackoff.start();
        } else {
            setupOnBootInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupOnBootInternal() {
        Log.i(this, "TelecomAccountRegistry: setupOnBootInternal - register subscription listener", new Object[0]);
        SubscriptionManager.from(this.mContext).addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mTelephonyManager.registerTelephonyCallback(0, new HandlerExecutor(this.mHandler), this.mTelephonyCallback);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("com.android.ims.IMS_SERVICE_UP");
        intentFilter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.LOCALE_CHANGED");
        intentFilter2.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mLocaleChangeReceiver, intentFilter2);
        registerContentObservers();
    }

    private void registerContentObservers() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("rtt_calling_mode"), false, new ContentObserver(this.mHandler) { // from class: com.android.services.telephony.TelecomAccountRegistry.6
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                    try {
                        Iterator it = TelecomAccountRegistry.this.mAccounts.iterator();
                        while (it.hasNext()) {
                            ((AccountEntry) it.next()).updateRttCapability();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
        this.mIncomingCallNotifiers.clear();
        for (Phone phone : PhoneFactory.getPhones()) {
            Log.i(this, "new PstnIncomingCallNotifier for %s", phone);
            this.mIncomingCallNotifiers.add(new PstnIncomingCallNotifier(phone));
        }
        this.mContext.getContentResolver().registerContentObserver(Uri.withAppendedPath(Telephony.SimInfo.CONTENT_URI, "ims_rcs_uce_enabled"), true, new ContentObserver(this.mHandler) { // from class: com.android.services.telephony.TelecomAccountRegistry.7
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                synchronized (TelecomAccountRegistry.this.mAccountsLock) {
                    try {
                        Iterator it = TelecomAccountRegistry.this.mAccounts.iterator();
                        while (it.hasNext()) {
                            ((AccountEntry) it.next()).updateVideoPresenceCapability();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
    }

    boolean hasAccountEntryForPhoneAccount(PhoneAccountHandle phoneAccountHandle) {
        synchronized (this.mAccountsLock) {
            try {
                Iterator it = this.mAccounts.iterator();
                while (it.hasNext()) {
                    if (((AccountEntry) it.next()).getPhoneAccountHandle().equals(phoneAccountHandle)) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    PhoneAccountHandle getPhoneAccountHandleForSubId(int i) {
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getSubId() == i) {
                        return accountEntry.getPhoneAccountHandle();
                    }
                }
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPhoneAccounts() {
        List<PhoneAccountHandle> callCapablePhoneAccounts;
        ComponentName componentName = new ComponentName(this.mContext, (Class<?>) TelephonyConnectionService.class);
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_emergency_account_emergency_calls_only);
        TelecomManager telecomManager = this.mTelecomManager;
        if (z) {
            callCapablePhoneAccounts = telecomManager.getAllPhoneAccountHandles();
        } else {
            callCapablePhoneAccounts = telecomManager.getCallCapablePhoneAccounts();
        }
        for (PhoneAccountHandle phoneAccountHandle : callCapablePhoneAccounts) {
            if (componentName.equals(phoneAccountHandle.getComponentName()) && !hasAccountEntryForPhoneAccount(phoneAccountHandle)) {
                Log.i(this, "Unregistering phone account %s.", phoneAccountHandle);
                this.mTelecomManager.unregisterPhoneAccount(phoneAccountHandle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAccounts() {
        PhoneAccountHandle phoneAccountHandleForSubId;
        PhoneAccountHandle phoneAccountHandleForSubId2;
        Phone[] phones = PhoneFactory.getPhones();
        Log.i(this, "setupAccounts: Found %d phones.  Attempting to register.", Integer.valueOf(phones.length));
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_pstn_phone_accounts_enabled);
        synchronized (this.mAccountsLock) {
            boolean z2 = true;
            try {
                if (z) {
                    try {
                        this.mSubscriptionInfos = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoList(false);
                        for (Phone phone : phones) {
                            if (!MiPhoneAppStub.get().isDataOnlySlot(this.mContext, phone)) {
                                int subId = phone.getSubId();
                                Log.i(this, "setupAccounts: Phone with subscription id %d", Integer.valueOf(subId));
                                if (!shouldSkipAccountEntry(subId)) {
                                    if (MtkTelephonyManagerEx.getDefault().getSimOnOffState(phone.getPhoneId()) != 11) {
                                        Log.d(this, "setupAccounts: skipping sim off", new Object[0]);
                                    } else {
                                        this.mAccounts.add(new AccountEntry(phone, false, false));
                                    }
                                }
                            }
                        }
                    } catch (Throwable th) {
                        if (this.mAccounts.isEmpty()) {
                            Log.i(this, "setupAccounts: adding default", new Object[0]);
                            this.mAccounts.add(new AccountEntry(PhoneFactory.getDefaultPhone(), true, false));
                        }
                        int defaultVoiceSubscriptionId = SubscriptionManager.getDefaultVoiceSubscriptionId();
                        if (SubscriptionManager.isValidSubscriptionId(defaultVoiceSubscriptionId) && (phoneAccountHandleForSubId = getPhoneAccountHandleForSubId(defaultVoiceSubscriptionId)) != null) {
                            PhoneAccountHandle userSelectedOutgoingPhoneAccount = this.mTelecomManager.getUserSelectedOutgoingPhoneAccount();
                            if (userSelectedOutgoingPhoneAccount != null && !Objects.equals(phoneAccountHandleForSubId.getComponentName(), userSelectedOutgoingPhoneAccount.getComponentName())) {
                                z2 = false;
                            }
                            if (!phoneAccountHandleForSubId.equals(userSelectedOutgoingPhoneAccount) && z2) {
                                Log.d(this, "setupAccounts: Re-setup phone account again for default voice sub " + defaultVoiceSubscriptionId, new Object[0]);
                                this.mTelecomManager.setUserSelectedOutgoingPhoneAccount(phoneAccountHandleForSubId);
                            }
                        }
                        throw th;
                    }
                }
                if (this.mAccounts.isEmpty()) {
                    Log.i(this, "setupAccounts: adding default", new Object[0]);
                    this.mAccounts.add(new AccountEntry(PhoneFactory.getDefaultPhone(), true, false));
                }
                int defaultVoiceSubscriptionId2 = SubscriptionManager.getDefaultVoiceSubscriptionId();
                if (SubscriptionManager.isValidSubscriptionId(defaultVoiceSubscriptionId2) && (phoneAccountHandleForSubId2 = getPhoneAccountHandleForSubId(defaultVoiceSubscriptionId2)) != null) {
                    PhoneAccountHandle userSelectedOutgoingPhoneAccount2 = this.mTelecomManager.getUserSelectedOutgoingPhoneAccount();
                    if (userSelectedOutgoingPhoneAccount2 != null && !Objects.equals(phoneAccountHandleForSubId2.getComponentName(), userSelectedOutgoingPhoneAccount2.getComponentName())) {
                        z2 = false;
                    }
                    if (!phoneAccountHandleForSubId2.equals(userSelectedOutgoingPhoneAccount2) && z2) {
                        Log.d(this, "setupAccounts: Re-setup phone account again for default voice sub " + defaultVoiceSubscriptionId2, new Object[0]);
                        this.mTelecomManager.setUserSelectedOutgoingPhoneAccount(phoneAccountHandleForSubId2);
                    }
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        this.mExecutor.submit(new Runnable() { // from class: com.android.services.telephony.TelecomAccountRegistry.8
            @Override // java.lang.Runnable
            public void run() {
                TelecomAccountRegistry.this.cleanupPhoneAccounts();
                MiPhoneAppStub.get().syncPhoneAccounts();
            }
        });
    }

    private boolean shouldSkipAccountEntry(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.d(this, "setupAccounts: skipping invalid subid %d", Integer.valueOf(i));
            return true;
        }
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo == null || activeSubscriptionInfo.isOpportunistic()) {
            Log.d(this, "setupAccounts: skipping unknown or opportunistic subid %d", Integer.valueOf(i));
            return true;
        }
        if (activeSubscriptionInfo.isPrivateNetwork()) {
            Log.d(this, "setupAccounts: skipping private network subid %d", Integer.valueOf(i));
            return true;
        }
        if (activeSubscriptionInfo.getProfileClass() == 1) {
            Log.d(this, "setupAccounts: skipping bootstrap sub id " + i, new Object[0]);
            return true;
        }
        if (!activeSubscriptionInfo.isOnlyNonTerrestrialNetwork()) {
            return false;
        }
        Log.d(this, "setupAccounts: skipping satellite sub id " + i, new Object[0]);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tearDownAccounts() {
        synchronized (this.mAccountsLock) {
            try {
                Iterator it = this.mAccounts.iterator();
                while (it.hasNext()) {
                    ((AccountEntry) it.next()).teardown();
                }
                this.mAccounts.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
        PropertyInvalidatedCache.invalidateCache("cache_key.telephony.phone_account_to_subid");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SubscriptionInfo getCachedSubscriptionInfo(int i) {
        List<SubscriptionInfo> list = this.mSubscriptionInfos;
        if (list == null) {
            return null;
        }
        for (SubscriptionInfo subscriptionInfo : list) {
            if (subscriptionInfo != null && subscriptionInfo.getSubscriptionId() == i) {
                return subscriptionInfo;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarrierConfigChange(int i) {
        if (i == -1) {
            return;
        }
        synchronized (this.mAccountsLock) {
            try {
                for (AccountEntry accountEntry : this.mAccounts) {
                    if (accountEntry.getSubId() == i) {
                        Log.d(this, "handleCarrierConfigChange: subId=%d, accountSubId=%d", Integer.valueOf(i), Integer.valueOf(accountEntry.getSubId()));
                        accountEntry.reRegisterPstnPhoneAccount();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
