package com.android.internal.telephony.uicc;

import android.app.ActivityManager;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CarrierAppUtils;
import com.android.internal.telephony.CarrierPrivilegesTracker;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class UiccProfile extends IccCard {
    protected static final boolean DBG = true;
    public static final int EVENT_APP_READY = 3;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mCarrierConfigManager;
    private UiccCarrierPrivilegeRules mCarrierPrivilegeRules;
    private CatService mCatService;
    private int mCdmaSubscriptionAppIndex;
    protected CommandsInterface mCi;
    protected Context mContext;
    private final FeatureFlags mFlags;
    private int mGsmUmtsSubscriptionAppIndex;
    public final Handler mHandler;
    private int mImsSubscriptionAppIndex;
    private int mLastAppType;
    public int mLastReportedNumOfUiccApplications;
    protected final Object mLock;
    protected final int mPhoneId;
    protected final PinStorage mPinStorage;
    private boolean mProvisionCompleteContentObserverRegistered;
    protected TelephonyManager mTelephonyManager;
    private UiccCarrierPrivilegeRules mTestOverrideCarrierPrivilegeRules;
    protected final UiccCard mUiccCard;
    private IccCardStatus.PinState mUniversalPinState;
    private boolean mUserUnlockReceiverRegistered;
    protected static final String LOG_TAG = "UiccProfile";
    private static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    protected static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};
    private UiccCardApplication[] mUiccApplications = new UiccCardApplication[8];
    protected boolean mDisposed = false;
    protected RegistrantList mOperatorBrandOverrideRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    public int mCurrentAppType = 1;
    private int mRadioTech = 0;
    protected UiccCardApplication mUiccApplication = null;
    protected IccRecords mIccRecords = null;
    protected IccCardConstants.State mExternalState = IccCardConstants.State.UNKNOWN;
    private final ContentObserver mProvisionCompleteContentObserver = new ContentObserver(new Handler()) { // from class: com.android.internal.telephony.uicc.UiccProfile.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            synchronized (UiccProfile.this.mLock) {
                UiccProfile.this.mContext.getContentResolver().unregisterContentObserver(this);
                UiccProfile.this.mProvisionCompleteContentObserverRegistered = false;
                UiccProfile.this.showCarrierAppNotificationsIfPossible();
            }
        }
    };
    private final BroadcastReceiver mUserUnlockReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.uicc.UiccProfile.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (UiccProfile.this.mLock) {
                UiccProfile.this.mContext.unregisterReceiver(this);
                UiccProfile.this.mUserUnlockReceiverRegistered = false;
                UiccProfile.this.showCarrierAppNotificationsIfPossible();
            }
        }
    };

    public void queryIccNetworkLock(int i, Message message) {
    }

    public void setIccNetworkLockEnabled(int i, int i2, String str, String str2, String str3, String str4, Message message) {
    }

    public UiccProfile(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i, UiccCard uiccCard, Object obj, FeatureFlags featureFlags) {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.uicc.UiccProfile.3
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public void onCarrierConfigChanged(int i2, int i3, int i4, int i5) {
                if (i2 == UiccProfile.this.mPhoneId && SubscriptionManager.isValidSubscriptionId(i3) && i3 == SubscriptionManager.getSubscriptionId(UiccProfile.this.mPhoneId)) {
                    UiccProfile.this.log("onCarrierConfigChanged: slotIndex = " + i2 + ", subId=" + i3 + ", carrierId = " + i4);
                    UiccProfile.this.handleCarrierNameOverride();
                    UiccProfile.this.handleSimCountryIsoOverride();
                }
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        Handler handler = new Handler() { // from class: com.android.internal.telephony.uicc.UiccProfile.4
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                CarrierPrivilegesTracker carrierPrivilegesTracker;
                int i2;
                String strEventToString = UiccProfile.eventToString(message.what);
                UiccProfile uiccProfile = UiccProfile.this;
                if (uiccProfile.mDisposed && (i2 = message.what) != 8 && i2 != 9 && i2 != 10 && i2 != 11 && i2 != 12) {
                    uiccProfile.loge("handleMessage: Received " + strEventToString + " after dispose(); ignoring the message");
                    return;
                }
                uiccProfile.logWithLocalLog("handleMessage: Received " + strEventToString + " for phoneId " + UiccProfile.this.mPhoneId);
                switch (message.what) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 6:
                        break;
                    case 5:
                        UiccProfile uiccProfile2 = UiccProfile.this;
                        if (uiccProfile2.mUiccApplication != null) {
                            uiccProfile2.mNetworkLockedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(UiccProfile.this.mUiccApplication.getPersoSubState().ordinal()), (Throwable) null));
                        } else {
                            uiccProfile2.log("EVENT_NETWORK_LOCKED: mUiccApplication is NULL, mNetworkLockedRegistrants not notified.");
                        }
                        break;
                    case 7:
                    case 14:
                    default:
                        UiccProfile.this.loge("handleMessage: Unhandled message with number: " + message.what);
                        return;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult.exception != null) {
                            UiccProfile.this.logWithLocalLog("handleMessage: Error in SIM access with exception " + asyncResult.exception);
                        }
                        Object obj2 = asyncResult.userObj;
                        if (obj2 != null) {
                            AsyncResult.forMessage((Message) obj2, asyncResult.result, asyncResult.exception);
                            ((Message) asyncResult.userObj).sendToTarget();
                            return;
                        }
                        UiccProfile.this.loge("handleMessage: ar.userObj is null in event:" + strEventToString + ", failed to post status back to caller");
                        return;
                    case 13:
                        if (UiccProfile.VDBG) {
                            UiccProfile.this.log("handleMessage: EVENT_CARRIER_PRIVILEGES_LOADED");
                        }
                        Phone phone = PhoneFactory.getPhone(UiccProfile.this.mPhoneId);
                        if (phone != null && (carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker()) != null) {
                            carrierPrivilegesTracker.onUiccAccessRulesLoaded();
                        }
                        UiccProfile.this.onCarrierPrivilegesLoadedMessage();
                        UiccProfile.this.updateExternalState();
                        return;
                    case 15:
                        Object obj3 = message.obj;
                        UiccProfile uiccProfile3 = UiccProfile.this;
                        if (obj3 == null) {
                            uiccProfile3.mTestOverrideCarrierPrivilegeRules = null;
                        } else {
                            uiccProfile3.mTestOverrideCarrierPrivilegeRules = new UiccCarrierPrivilegeRules((List) message.obj);
                        }
                        UiccProfile.this.refresh();
                        return;
                    case 16:
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        Throwable th = asyncResult2.exception;
                        UiccProfile uiccProfile4 = UiccProfile.this;
                        if (th != null) {
                            uiccProfile4.loge("An error occurred during internal PIN verification");
                            UiccProfile uiccProfile5 = UiccProfile.this;
                            uiccProfile5.mPinStorage.clearPin(uiccProfile5.mPhoneId);
                            UiccProfile.this.updateExternalState();
                        } else {
                            uiccProfile4.log("Internal PIN verification was successful!");
                        }
                        TelephonyStatsLog.write(336, asyncResult2.exception != null ? 2 : 1, 1);
                        return;
                }
                if (UiccProfile.VDBG) {
                    UiccProfile.this.log("handleMessage: Received " + strEventToString);
                }
                UiccProfile.this.updateExternalState();
            }
        };
        this.mHandler = handler;
        this.mLastAppType = 1;
        log("Creating profile");
        this.mFlags = featureFlags;
        this.mLock = obj;
        this.mUiccCard = uiccCard;
        this.mPhoneId = i;
        if (PhoneFactory.getPhone(i) != null) {
            setCurrentAppType(true);
        }
        if (uiccCard instanceof EuiccCard) {
            ((EuiccCard) uiccCard).registerForEidReady(handler, 6, null);
        }
        this.mPinStorage = UiccController.getInstance().getPinStorage();
        update(context, commandsInterface, iccCardStatus);
        commandsInterface.registerForOffOrNotAvailable(handler, 1, null);
        resetProperties();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        Objects.requireNonNull(handler);
        carrierConfigManager.registerCarrierConfigChangeListener(new NetworkTypeController$$ExternalSyntheticLambda2(handler), carrierConfigChangeListener);
    }

    public void dispose() {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener;
        log("Disposing profile");
        UiccCard uiccCard = this.mUiccCard;
        if (uiccCard instanceof EuiccCard) {
            ((EuiccCard) uiccCard).unregisterForEidReady(this.mHandler);
        }
        synchronized (this.mLock) {
            try {
                unregisterAllAppEvents();
                unregisterCurrAppEvents();
                if (this.mProvisionCompleteContentObserverRegistered) {
                    this.mContext.getContentResolver().unregisterContentObserver(this.mProvisionCompleteContentObserver);
                    this.mProvisionCompleteContentObserverRegistered = false;
                }
                if (this.mUserUnlockReceiverRegistered) {
                    this.mContext.unregisterReceiver(this.mUserUnlockReceiver);
                    this.mUserUnlockReceiverRegistered = false;
                }
                InstallCarrierAppUtils.hideAllNotifications(this.mContext);
                InstallCarrierAppUtils.unregisterPackageInstallReceiver(this.mContext);
                this.mCi.unregisterForOffOrNotAvailable(this.mHandler);
                CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
                if (carrierConfigManager != null && (carrierConfigChangeListener = this.mCarrierConfigChangeListener) != null) {
                    carrierConfigManager.unregisterCarrierConfigChangeListener(carrierConfigChangeListener);
                }
                CatService catService = this.mCatService;
                if (catService != null) {
                    catService.dispose();
                }
                for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
                    if (uiccCardApplication != null) {
                        uiccCardApplication.dispose();
                    }
                }
                this.mCatService = null;
                this.mUiccApplications = null;
                this.mRadioTech = 0;
                this.mCarrierPrivilegeRules = null;
                this.mContext.getContentResolver().unregisterContentObserver(this.mProvisionCompleteContentObserver);
                this.mDisposed = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setVoiceRadioTech(int i) {
        synchronized (this.mLock) {
            log("Setting radio tech " + ServiceState.rilRadioTechnologyToString(i));
            this.mRadioTech = i;
            setCurrentAppType(true);
            updateIccAvailability(false);
        }
    }

    private void setCurrentAppType(boolean z) {
        this.mLastAppType = this.mCurrentAppType;
        log("setCurrentAppType, isGsm=" + z + " mPhoneId=" + getPhoneId());
        int i = 2;
        if (!z) {
            this.mCurrentAppType = 2;
            return;
        }
        int i2 = 1;
        if (z) {
            i2 = 2;
            i = 1;
        }
        synchronized (this.mLock) {
            try {
                if (getApplication(i) != null || getApplication(i2) == null) {
                    this.mCurrentAppType = i;
                } else {
                    this.mCurrentAppType = i2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        log("setCurrentAppType to be " + this.mCurrentAppType);
    }

    protected void handleCarrierNameOverride() {
        Phone phone;
        Phone phone2;
        int subscriptionId = SubscriptionManager.getSubscriptionId(this.mPhoneId);
        if (subscriptionId == -1) {
            loge("subId not valid for Phone " + this.mPhoneId);
            return;
        }
        if (((CarrierConfigManager) this.mContext.getSystemService("carrier_config")) == null) {
            loge("Failed to load a Carrier Config");
            return;
        }
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mContext, subscriptionId, new String[]{"carrier_name_override_bool", "carrier_name_string"});
        if (carrierConfigSubset.isEmpty()) {
            loge("handleCarrierNameOverride: fail to get carrier configs.");
            return;
        }
        int i = 0;
        boolean z = carrierConfigSubset.getBoolean("carrier_name_override_bool", false);
        String string = carrierConfigSubset.getString("carrier_name_string");
        String serviceProviderName = getServiceProviderName();
        if (z || (TextUtils.isEmpty(serviceProviderName) && !TextUtils.isEmpty(string))) {
            i = 3;
        } else if (!TextUtils.isEmpty(serviceProviderName) || (phone2 = PhoneFactory.getPhone(this.mPhoneId)) == null) {
            string = null;
            i = 1;
        } else {
            String pnnHomeNetworkName = phone2.getPnnHomeNetworkName();
            if (!TextUtils.isEmpty(pnnHomeNetworkName)) {
                string = MiuiNetworkControllerStub.get(this.mPhoneId).handleCarrierNameOverride(pnnHomeNetworkName, this.mPhoneId, this.mIccRecords);
                i = 4;
            } else {
                string = (!TelephonyBaseUtilsStub.isMiuiRom() || this.mIccRecords == null) ? phone2.getCarrierName() : MiuiUiccControllerStub.get(this.mPhoneId).overrideSpnFromConfig(phone2.getPhoneId(), phone2.getCarrierName(), this.mIccRecords);
            }
        }
        if (isUdpateCarrierName(string) || !TextUtils.isEmpty(string)) {
            this.mTelephonyManager.setSimOperatorNameForPhone(this.mPhoneId, MiuiUiccControllerStub.get(this.mPhoneId).onOperatorNumericOrNameSet(this.mPhoneId, "gsm.sim.operator.alpha", string));
        }
        updateCarrierNameForSubscription(subscriptionId, i);
        if (!TelephonyBaseUtilsStub.isMiuiRom() || (phone = PhoneFactory.getPhone(this.mPhoneId)) == null || phone.getServiceStateTracker() == null) {
            return;
        }
        phone.getServiceStateTracker().updateCarrierDisplayName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimCountryIsoOverride() {
        int subscriptionId = SubscriptionManager.getSubscriptionId(this.mPhoneId);
        if (subscriptionId == -1) {
            loge("subId not valid for Phone " + this.mPhoneId);
        } else {
            if (((CarrierConfigManager) this.mContext.getSystemService("carrier_config")) == null) {
                loge("Failed to load a Carrier Config");
                return;
            }
            PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mContext, subscriptionId, new String[]{"sim_country_iso_override_string"});
            if (carrierConfigSubset.isEmpty()) {
                loge("handleSimCountryIsoOverride: fail to get carrier configs.");
                return;
            }
            String string = carrierConfigSubset.getString("sim_country_iso_override_string");
            if (TextUtils.isEmpty(string) || string.equals(TelephonyManager.getSimCountryIsoForPhone(this.mPhoneId))) {
                return;
            }
            this.mTelephonyManager.setSimCountryIsoForPhone(this.mPhoneId, string);
            SubscriptionManagerService.getInstance().setCountryIso(subscriptionId, string);
        }
    }

    protected void updateCarrierNameForSubscription(int i, int i2) {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManagerService.getInstance().getActiveSubscriptionInfo(i, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        if (activeSubscriptionInfo == null) {
            return;
        }
        CharSequence displayName = activeSubscriptionInfo.getDisplayName();
        String subscriptionDisplayName = getSubscriptionDisplayName(i, this.mContext);
        if (TextUtils.isEmpty(subscriptionDisplayName) || subscriptionDisplayName.equals(displayName)) {
            return;
        }
        log("sim name[" + this.mPhoneId + "] = " + subscriptionDisplayName);
        SubscriptionManagerService.getInstance().setDisplayNameUsingSrc(subscriptionDisplayName, i, i2);
    }

    private void updateIccAvailability(boolean z) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication application = getApplication(this.mCurrentAppType);
                IccRecords iccRecords = application != null ? application.getIccRecords() : null;
                if (z) {
                    unregisterAllAppEvents();
                    registerAllAppEvents();
                }
                if (this.mIccRecords != iccRecords || this.mUiccApplication != application) {
                    log("Icc changed. Reregistering.");
                    unregisterCurrAppEvents();
                    this.mUiccApplication = application;
                    this.mIccRecords = iccRecords;
                    registerCurrAppEvents();
                }
                updateExternalState();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void resetProperties() {
        if (this.mCurrentAppType == 1) {
            log("update icc_operator_numeric=");
            this.mTelephonyManager.setSimOperatorNumericForPhone(this.mPhoneId, "");
            this.mTelephonyManager.setSimCountryIsoForPhone(this.mPhoneId, "");
            this.mTelephonyManager.setSimOperatorNameForPhone(this.mPhoneId, "");
            MiuiUiccControllerStub.get(this.mPhoneId).setTelephonyProperty(this.mPhoneId, "gsm.operator.orig.alpha", "");
            MiuiUiccControllerStub.get(this.mPhoneId).setTelephonyProperty(this.mPhoneId, "gsm.sim.operator.orig.alpha", "");
        }
    }

    /* JADX WARN: Code duplicated, block: B:101:0x0196  */
    /* JADX WARN: Code duplicated, block: B:104:0x01a1  */
    /* JADX WARN: Code duplicated, block: B:106:0x01a5  */
    /* JADX WARN: Code duplicated, block: B:49:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:68:0x0118  */
    /* JADX WARN: Code duplicated, block: B:71:0x0123  */
    /* JADX WARN: Code duplicated, block: B:73:0x012d  */
    /* JADX WARN: Code duplicated, block: B:75:0x0130  */
    /* JADX WARN: Code duplicated, block: B:77:0x0133 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:78:0x0134  */
    /* JADX WARN: Code duplicated, block: B:80:0x013d  */
    /* JADX WARN: Code duplicated, block: B:91:0x015c  */
    /* JADX WARN: Code duplicated, block: B:94:0x0183  */
    /* JADX WARN: Code duplicated, block: B:96:0x0187  */
    /* JADX WARN: Code duplicated, block: B:99:0x0192  */
    /* JADX WARN: Instruction removed from duplicated block: B:91:0x015c, please report this as an issue */
    public void updateExternalState() {
        boolean z;
        IccCardConstants.State state;
        IccCardConstants.State state2;
        int i;
        IccRecords iccRecords;
        log("updateExternalState: mUiccCard.getCardState() = " + this.mUiccCard.getCardState());
        if (this.mUiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_ERROR) {
            setExternalState(IccCardConstants.State.CARD_IO_ERROR);
            return;
        }
        if (this.mUiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_RESTRICTED) {
            setExternalState(IccCardConstants.State.CARD_RESTRICTED);
            return;
        }
        UiccCard uiccCard = this.mUiccCard;
        if ((uiccCard instanceof EuiccCard) && ((EuiccCard) uiccCard).getEid() == null) {
            log("EID is not ready yet.");
            return;
        }
        UiccCardApplication uiccCardApplication = this.mUiccApplication;
        if (uiccCardApplication == null) {
            loge("updateExternalState: setting state to NOT_READY because mUiccApplication is null");
            setExternalState(IccCardConstants.State.NOT_READY);
            return;
        }
        IccCardApplicationStatus.AppState state3 = uiccCardApplication.getState();
        if (this.mUiccApplication.getPin1State() == IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
            if (VDBG) {
                log("updateExternalState: PERM_DISABLED");
            }
            state2 = IccCardConstants.State.PERM_DISABLED;
        } else if (state3 == IccCardApplicationStatus.AppState.APPSTATE_PIN) {
            if (VDBG) {
                log("updateExternalState: PIN_REQUIRED");
            }
            state2 = IccCardConstants.State.PIN_REQUIRED;
        } else if (state3 == IccCardApplicationStatus.AppState.APPSTATE_PUK) {
            if (VDBG) {
                log("updateExternalState: PUK_REQUIRED");
            }
            state2 = IccCardConstants.State.PUK_REQUIRED;
        } else {
            if (state3 == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO && IccCardApplicationStatus.PersoSubState.isPersoLocked(this.mUiccApplication.getPersoSubState())) {
                if (VDBG) {
                    log("updateExternalState: PERSOSUBSTATE_SIM_NETWORK");
                }
                state2 = IccCardConstants.State.NETWORK_LOCKED;
            } else {
                z = false;
                state = null;
            }
            if (z) {
                iccRecords = this.mIccRecords;
                if (iccRecords == null && (iccRecords.getLockedRecordsLoaded() || this.mIccRecords.getNetworkLockedRecordsLoaded())) {
                    if (VDBG) {
                        log("updateExternalState: card locked and records loaded; setting state to locked");
                    }
                    if (state == IccCardConstants.State.PIN_REQUIRED) {
                        String pin = this.mPinStorage.getPin(this.mPhoneId, this.mIccRecords.getFullIccId());
                        if (!pin.isEmpty()) {
                            log("PIN_REQUIRED[" + this.mPhoneId + "] - Cache present");
                            this.mCi.supplyIccPin(pin, this.mHandler.obtainMessage(16));
                            return;
                        }
                    }
                    setExternalState(state);
                    return;
                }
                if (VDBG) {
                    log("updateExternalState: card locked but records not loaded; setting state to NOT_READY");
                }
                setExternalState(IccCardConstants.State.NOT_READY);
                return;
            }
            i = AnonymousClass5.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState[state3.ordinal()];
            if (i != 1) {
                if (VDBG) {
                    log("updateExternalState: app state is unknown; setting state to NOT_READY");
                }
                setExternalState(IccCardConstants.State.NOT_READY);
                return;
            }
            if (i != 2) {
                if (VDBG) {
                    log("updateExternalState: app state is detected; setting state to NOT_READY");
                }
                setExternalState(IccCardConstants.State.NOT_READY);
                return;
            }
            if (i != 3) {
                return;
            }
            checkAndUpdateIfAnyAppToBeIgnored();
            if (areAllApplicationsReady()) {
                if (!areAllRecordsLoaded() && areCarrierPrivilegeRulesLoaded()) {
                    if (VDBG) {
                        log("updateExternalState: setting state to LOADED");
                    }
                    setExternalState(IccCardConstants.State.LOADED);
                    return;
                }
                if (VDBG) {
                    log("updateExternalState: setting state to READY; records loaded " + areAllRecordsLoaded() + ", carrier privilige rules loaded " + areCarrierPrivilegeRulesLoaded());
                }
                setExternalState(IccCardConstants.State.READY);
                return;
            }
            if (VDBG) {
                log("updateExternalState: app state is READY but not for all apps; setting state to NOT_READY");
            }
            setExternalState(IccCardConstants.State.NOT_READY);
        }
        state = state2;
        z = true;
        if (z) {
            iccRecords = this.mIccRecords;
            if (iccRecords == null) {
            }
            if (VDBG) {
                log("updateExternalState: card locked but records not loaded; setting state to NOT_READY");
            }
            setExternalState(IccCardConstants.State.NOT_READY);
            return;
        }
        i = AnonymousClass5.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState[state3.ordinal()];
        if (i != 1) {
            if (VDBG) {
                log("updateExternalState: app state is unknown; setting state to NOT_READY");
            }
            setExternalState(IccCardConstants.State.NOT_READY);
            return;
        }
        if (i != 2) {
            if (VDBG) {
                log("updateExternalState: app state is detected; setting state to NOT_READY");
            }
            setExternalState(IccCardConstants.State.NOT_READY);
            return;
        }
        if (i != 3) {
            return;
        }
        checkAndUpdateIfAnyAppToBeIgnored();
        if (areAllApplicationsReady()) {
            if (!areAllRecordsLoaded()) {
            }
            if (VDBG) {
                log("updateExternalState: setting state to READY; records loaded " + areAllRecordsLoaded() + ", carrier privilige rules loaded " + areCarrierPrivilegeRulesLoaded());
            }
            setExternalState(IccCardConstants.State.READY);
            return;
        }
        if (VDBG) {
            log("updateExternalState: app state is READY but not for all apps; setting state to NOT_READY");
        }
        setExternalState(IccCardConstants.State.NOT_READY);
    }

    private void registerAllAppEvents() {
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null) {
                boolean z = VDBG;
                if (z) {
                    log("registerUiccCardEvents: registering for EVENT_APP_READY");
                }
                uiccCardApplication.registerForReady(this.mHandler, 3, null);
                IccRecords iccRecords = uiccCardApplication.getIccRecords();
                if (iccRecords != null) {
                    if (z) {
                        log("registerUiccCardEvents: registering for EVENT_RECORDS_LOADED");
                    }
                    iccRecords.registerForRecordsLoaded(this.mHandler, 4, null);
                }
            }
        }
    }

    private void unregisterAllAppEvents() {
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null) {
                uiccCardApplication.unregisterForReady(this.mHandler);
                IccRecords iccRecords = uiccCardApplication.getIccRecords();
                if (iccRecords != null) {
                    iccRecords.unregisterForRecordsLoaded(this.mHandler);
                }
            }
        }
    }

    protected void registerCurrAppEvents() {
        IccRecords iccRecords = this.mIccRecords;
        if (iccRecords != null) {
            iccRecords.registerForLockedRecordsLoaded(this.mHandler, 2, null);
            this.mIccRecords.registerForNetworkLockedRecordsLoaded(this.mHandler, 5, null);
        }
    }

    protected void unregisterCurrAppEvents() {
        IccRecords iccRecords = this.mIccRecords;
        if (iccRecords != null) {
            iccRecords.unregisterForLockedRecordsLoaded(this.mHandler);
            this.mIccRecords.unregisterForNetworkLockedRecordsLoaded(this.mHandler);
        }
    }

    protected void setExternalState(IccCardConstants.State state, boolean z) {
        IccRecords iccRecords;
        IccCardConstants.State state2;
        synchronized (this.mLock) {
            try {
                if (!SubscriptionManager.isValidSlotIndex(this.mPhoneId)) {
                    loge("setExternalState: mPhoneId=" + this.mPhoneId + " is invalid; Return!!");
                    return;
                }
                log("setExternalState(): mExternalState = " + this.mExternalState + " newState =  " + state + " override = " + z);
                if (!z && state == (state2 = this.mExternalState)) {
                    if (state2 == IccCardConstants.State.LOADED && this.mLastAppType != this.mCurrentAppType && checkCdmaCard(this.mPhoneId) == 2) {
                        log("setExternalState: Update operatorNumeric for CDMA 3G dual mode card");
                    } else {
                        log("setExternalState: !override and newstate unchanged from " + state);
                        return;
                    }
                }
                this.mExternalState = state;
                if (state == IccCardConstants.State.LOADED && (iccRecords = this.mIccRecords) != null) {
                    String operatorNumeric = iccRecords.getOperatorNumeric();
                    log("setExternalState: operator=" + operatorNumeric + " mPhoneId=" + this.mPhoneId);
                    if (!TextUtils.isEmpty(operatorNumeric)) {
                        String strOnOperatorNumericOrNameSet = MiuiUiccControllerStub.get(this.mPhoneId).onOperatorNumericOrNameSet(this.mPhoneId, "gsm.sim.operator.numeric", operatorNumeric);
                        this.mTelephonyManager.setSimOperatorNumericForPhone(this.mPhoneId, strOnOperatorNumericOrNameSet);
                        this.mTelephonyManager.setSimCountryIsoForPhone(this.mPhoneId, MccTable.countryCodeForMcc(strOnOperatorNumericOrNameSet.substring(0, 3)));
                    } else {
                        loge("setExternalState: state LOADED; Operator name is null");
                    }
                }
                log("setExternalState: set mPhoneId=" + this.mPhoneId + " mExternalState=" + this.mExternalState);
                UiccController uiccController = UiccController.getInstance();
                int i = this.mPhoneId;
                IccCardConstants.State state3 = this.mExternalState;
                uiccController.updateSimState(i, state3, getIccStateReason(state3));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void setExternalState(IccCardConstants.State state) {
        setExternalState(state, false);
    }

    public boolean getIccRecordsLoaded() {
        synchronized (this.mLock) {
            try {
                IccRecords iccRecords = this.mIccRecords;
                if (iccRecords == null) {
                    return false;
                }
                return iccRecords.getRecordsLoaded();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.uicc.UiccProfile$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$IccCardConstants$State;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState;

        static {
            int[] iArr = new int[IccCardConstants.State.values().length];
            $SwitchMap$com$android$internal$telephony$IccCardConstants$State = iArr;
            try {
                iArr[IccCardConstants.State.PIN_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PUK_REQUIRED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.NETWORK_LOCKED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PERM_DISABLED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_IO_ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_RESTRICTED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr2 = new int[IccCardApplicationStatus.AppState.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState = iArr2;
            try {
                iArr2[IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState[IccCardApplicationStatus.AppState.APPSTATE_DETECTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState[IccCardApplicationStatus.AppState.APPSTATE_READY.ordinal()] = 3;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    protected String getIccStateReason(IccCardConstants.State state) {
        switch (AnonymousClass5.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[state.ordinal()]) {
            case 1:
                return "PIN";
            case 2:
                return "PUK";
            case 3:
                return "NETWORK";
            case 4:
                return "PERM_DISABLED";
            case 5:
                return "CARD_IO_ERROR";
            case 6:
                return "CARD_RESTRICTED";
            default:
                return null;
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public IccCardConstants.State getState() {
        IccCardConstants.State state;
        synchronized (this.mLock) {
            state = this.mExternalState;
        }
        return state;
    }

    @Override // com.android.internal.telephony.IccCard
    public IccRecords getIccRecords() {
        IccRecords iccRecords;
        synchronized (this.mLock) {
            iccRecords = this.mIccRecords;
        }
        return iccRecords;
    }

    @Override // com.android.internal.telephony.IccCard
    public void registerForNetworkLocked(Handler handler, int i, Object obj) {
        synchronized (this.mLock) {
            try {
                Registrant registrant = new Registrant(handler, i, obj);
                this.mNetworkLockedRegistrants.add(registrant);
                if (getState() == IccCardConstants.State.NETWORK_LOCKED) {
                    if (this.mUiccApplication != null) {
                        registrant.notifyRegistrant(new AsyncResult((Object) null, Integer.valueOf(this.mUiccApplication.getPersoSubState().ordinal()), (Throwable) null));
                    } else {
                        log("registerForNetworkLocked: not notifying registrants, mUiccApplication == null");
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void unregisterForNetworkLocked(Handler handler) {
        synchronized (this.mLock) {
            this.mNetworkLockedRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplyPin(String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplyPin(str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("supplyPin");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplyPuk(String str, String str2, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplyPuk(str, str2, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("supplyPuk");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplyPin2(String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplyPin2(str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("supplyPin2");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplyPuk2(String str, String str2, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplyPuk2(str, str2, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("supplyPuk2");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplyNetworkDepersonalization(String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplyNetworkDepersonalization(str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = new RuntimeException("CommandsInterface is not set.");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void supplySimDepersonalization(IccCardApplicationStatus.PersoSubState persoSubState, String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.supplySimDepersonalization(persoSubState, str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = new RuntimeException("CommandsInterface is not set.");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean getIccLockEnabled() {
        boolean z;
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                z = uiccCardApplication != null && uiccCardApplication.getIccLockEnabled();
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean getIccFdnEnabled() {
        boolean z;
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                z = uiccCardApplication != null && uiccCardApplication.getIccFdnEnabled();
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean getIccFdnAvailable() {
        boolean z;
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                z = uiccCardApplication != null && uiccCardApplication.getIccFdnAvailable();
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean getIccPin2Blocked() {
        UiccCardApplication uiccCardApplication = this.mUiccApplication;
        return uiccCardApplication != null && uiccCardApplication.getIccPin2Blocked();
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean getIccPuk2Blocked() {
        UiccCardApplication uiccCardApplication = this.mUiccApplication;
        return uiccCardApplication != null && uiccCardApplication.getIccPuk2Blocked();
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean isEmptyProfile() {
        return this.mLastReportedNumOfUiccApplications == 0;
    }

    @Override // com.android.internal.telephony.IccCard
    public void setIccLockEnabled(boolean z, String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.setIccLockEnabled(z, str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("setIccLockEnabled");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void setIccFdnEnabled(boolean z, String str, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.setIccFdnEnabled(z, str, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("setIccFdnEnabled");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void changeIccLockPassword(String str, String str2, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.changeIccLockPassword(str, str2, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("changeIccLockPassword");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public void changeIccFdnPassword(String str, String str2, Message message) {
        synchronized (this.mLock) {
            try {
                UiccCardApplication uiccCardApplication = this.mUiccApplication;
                if (uiccCardApplication != null) {
                    uiccCardApplication.changeIccFdnPassword(str, str2, message);
                } else if (message != null) {
                    AsyncResult.forMessage(message).exception = covertException("changeIccFdnPassword");
                    message.sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public String getServiceProviderName() {
        synchronized (this.mLock) {
            try {
                IccRecords iccRecords = this.mIccRecords;
                if (iccRecords == null) {
                    return null;
                }
                return iccRecords.getServiceProviderName();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean hasIccCard() {
        if (this.mUiccCard.getCardState() != IccCardStatus.CardState.CARDSTATE_ABSENT) {
            return true;
        }
        loge("hasIccCard: UiccProfile is not null but UiccCard is null or card state is ABSENT");
        return false;
    }

    public void update(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus) {
        UiccProfile uiccProfile;
        synchronized (this.mLock) {
            try {
                this.mUniversalPinState = iccCardStatus.mUniversalPinState;
                this.mGsmUmtsSubscriptionAppIndex = iccCardStatus.mGsmUmtsSubscriptionAppIndex;
                this.mCdmaSubscriptionAppIndex = iccCardStatus.mCdmaSubscriptionAppIndex;
                this.mImsSubscriptionAppIndex = iccCardStatus.mImsSubscriptionAppIndex;
                this.mContext = context;
                this.mCi = commandsInterface;
                this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
                log(iccCardStatus.mApplications.length + " applications");
                this.mLastReportedNumOfUiccApplications = iccCardStatus.mApplications.length;
                int i = 0;
                while (true) {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i >= uiccCardApplicationArr.length) {
                        break;
                    }
                    UiccCardApplication uiccCardApplication = uiccCardApplicationArr[i];
                    IccCardApplicationStatus[] iccCardApplicationStatusArr = iccCardStatus.mApplications;
                    if (uiccCardApplication == null) {
                        if (i < iccCardApplicationStatusArr.length) {
                            uiccProfile = this;
                            uiccCardApplicationArr[i] = uiccProfile.makeUiccApplication(this, iccCardApplicationStatusArr[i], this.mContext, this.mCi, this.mFlags);
                        } else {
                            uiccProfile = this;
                        }
                    } else {
                        uiccProfile = this;
                        if (i >= iccCardApplicationStatusArr.length) {
                            uiccCardApplication.dispose();
                            uiccProfile.mUiccApplications[i] = null;
                        } else {
                            uiccCardApplication.update(iccCardApplicationStatusArr[i], uiccProfile.mContext, uiccProfile.mCi);
                        }
                    }
                    i++;
                    this = uiccProfile;
                }
                UiccProfile uiccProfile2 = this;
                uiccProfile2.createAndUpdateCatServiceLocked();
                uiccProfile2.log("Before privilege rules: " + uiccProfile2.mCarrierPrivilegeRules + " : " + iccCardStatus.mCardState);
                UiccCarrierPrivilegeRules uiccCarrierPrivilegeRules = uiccProfile2.mCarrierPrivilegeRules;
                if (uiccCarrierPrivilegeRules == null && iccCardStatus.mCardState == IccCardStatus.CardState.CARDSTATE_PRESENT && uiccProfile2.mLastReportedNumOfUiccApplications > 0) {
                    uiccProfile2.mCarrierPrivilegeRules = new UiccCarrierPrivilegeRules(uiccProfile2, uiccProfile2.mHandler.obtainMessage(13));
                } else if (uiccCarrierPrivilegeRules != null && iccCardStatus.mCardState != IccCardStatus.CardState.CARDSTATE_PRESENT) {
                    uiccProfile2.mCarrierPrivilegeRules = null;
                    uiccProfile2.mContext.getContentResolver().unregisterContentObserver(uiccProfile2.mProvisionCompleteContentObserver);
                }
                uiccProfile2.sanitizeApplicationIndexesLocked();
                if (uiccProfile2.mRadioTech != 0) {
                    uiccProfile2.setCurrentAppType(true);
                }
                uiccProfile2.updateIccAvailability(true);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void createAndUpdateCatServiceLocked() {
        UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
        if (uiccCardApplicationArr.length > 0 && uiccCardApplicationArr[0] != null) {
            CatService catService = this.mCatService;
            CommandsInterface commandsInterface = this.mCi;
            if (catService == null) {
                this.mCatService = CatService.getInstance(commandsInterface, this.mContext, this, this.mPhoneId, this.mFlags);
                return;
            } else {
                catService.update(commandsInterface, this.mContext, this);
                return;
            }
        }
        CatService catService2 = this.mCatService;
        if (catService2 != null) {
            catService2.dispose();
        }
        this.mCatService = null;
    }

    protected void finalize() {
        log("UiccProfile finalized");
    }

    private void sanitizeApplicationIndexesLocked() {
        this.mGsmUmtsSubscriptionAppIndex = checkIndexLocked(this.mGsmUmtsSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_SIM, IccCardApplicationStatus.AppType.APPTYPE_USIM);
        this.mCdmaSubscriptionAppIndex = checkIndexLocked(this.mCdmaSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_RUIM, IccCardApplicationStatus.AppType.APPTYPE_CSIM);
        this.mImsSubscriptionAppIndex = checkIndexLocked(this.mImsSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_ISIM, null);
    }

    private boolean isSupportedApplication(UiccCardApplication uiccCardApplication) {
        return uiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM || uiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_SIM;
    }

    private void checkAndUpdateIfAnyAppToBeIgnored() {
        boolean[] zArr = new boolean[IccCardApplicationStatus.AppType.APPTYPE_ISIM.ordinal() + 1];
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null && isSupportedApplication(uiccCardApplication) && uiccCardApplication.isReady()) {
                zArr[uiccCardApplication.getType().ordinal()] = true;
            }
        }
        for (UiccCardApplication uiccCardApplication2 : this.mUiccApplications) {
            if (uiccCardApplication2 != null && isSupportedApplication(uiccCardApplication2) && !uiccCardApplication2.isReady() && zArr[uiccCardApplication2.getType().ordinal()]) {
                uiccCardApplication2.setAppIgnoreState(true);
            }
        }
    }

    private boolean areAllApplicationsReady() {
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null && isSupportedApplication(uiccCardApplication) && !uiccCardApplication.isReady() && !uiccCardApplication.isAppIgnored()) {
                if (VDBG) {
                    log("areAllApplicationsReady: return false");
                }
                return false;
            }
        }
        if (VDBG) {
            StringBuilder sb = new StringBuilder("areAllApplicationsReady: outside loop, return ");
            sb.append(this.mUiccApplication != null);
            log(sb.toString());
        }
        return this.mUiccApplication != null;
    }

    private boolean areAllRecordsLoaded() {
        IccRecords iccRecords;
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null && isSupportedApplication(uiccCardApplication) && !uiccCardApplication.isAppIgnored() && ((iccRecords = uiccCardApplication.getIccRecords()) == null || !iccRecords.isLoaded())) {
                if (VDBG) {
                    log("areAllRecordsLoaded: return false");
                }
                return false;
            }
        }
        if (VDBG) {
            StringBuilder sb = new StringBuilder("areAllRecordsLoaded: outside loop, return ");
            sb.append(this.mUiccApplication != null);
            log(sb.toString());
        }
        return this.mUiccApplication != null;
    }

    private int checkIndexLocked(int i, IccCardApplicationStatus.AppType appType, IccCardApplicationStatus.AppType appType2) {
        UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
        if (uiccCardApplicationArr == null || i >= uiccCardApplicationArr.length) {
            loge("App index " + i + " is invalid since there are no applications");
            return -1;
        }
        if (i < 0) {
            return -1;
        }
        UiccCardApplication uiccCardApplication = uiccCardApplicationArr[i];
        if (uiccCardApplication == null) {
            loge("App index " + i + " is invalid");
            return -1;
        }
        if (uiccCardApplication.getType() == appType || this.mUiccApplications[i].getType() == appType2) {
            return i;
        }
        loge("App index " + i + " is invalid since it's not " + appType + " and not " + appType2);
        return -1;
    }

    static boolean isPackageBundled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
            Rlog.d(LOG_TAG, str + " is installed.");
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            Rlog.d(LOG_TAG, str + " is not installed.");
            return false;
        }
    }

    private void promptInstallCarrierApp(String str) {
        Intent intent = InstallCarrierAppTrampolineActivity.get(this.mContext, str);
        intent.addFlags(268435456);
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierPrivilegesLoadedMessage() {
        CarrierAppUtils.disableCarrierAppsUntilPrivileged(this.mContext.getOpPackageName(), this.mTelephonyManager, ActivityManager.getCurrentUser(), this.mContext);
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.mContext.getSystemService("usagestats");
        if (usageStatsManager != null) {
            usageStatsManager.onCarrierPrivilegedAppsChanged();
        }
        InstallCarrierAppUtils.hideAllNotifications(this.mContext);
        InstallCarrierAppUtils.unregisterPackageInstallReceiver(this.mContext);
        synchronized (this.mLock) {
            try {
                boolean zIsProvisioned = isProvisioned();
                boolean zIsUserUnlocked = isUserUnlocked();
                if (zIsProvisioned && zIsUserUnlocked) {
                    Iterator it = getUninstalledCarrierPackages().iterator();
                    while (it.hasNext()) {
                        promptInstallCarrierApp((String) it.next());
                    }
                } else {
                    if (!zIsProvisioned) {
                        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.mProvisionCompleteContentObserver);
                        this.mProvisionCompleteContentObserverRegistered = true;
                    }
                    if (!zIsUserUnlocked) {
                        this.mContext.registerReceiver(this.mUserUnlockReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                        this.mUserUnlockReceiverRegistered = true;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean isProvisioned() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 1) == 1;
    }

    private boolean isUserUnlocked() {
        return ((UserManager) this.mContext.getSystemService(UserManager.class)).isUserUnlocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCarrierAppNotificationsIfPossible() {
        if (isProvisioned() && isUserUnlocked()) {
            Iterator it = getUninstalledCarrierPackages().iterator();
            while (it.hasNext()) {
                InstallCarrierAppUtils.showNotification(this.mContext, (String) it.next());
                InstallCarrierAppUtils.registerPackageInstallReceiver(this.mContext);
            }
        }
    }

    private Set getUninstalledCarrierPackages() {
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "carrier_app_whitelist");
        if (TextUtils.isEmpty(string)) {
            return Collections.EMPTY_SET;
        }
        Map<String, String> toCertificateToPackageMap = parseToCertificateToPackageMap(string);
        if (toCertificateToPackageMap.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        UiccCarrierPrivilegeRules carrierPrivilegeRules = getCarrierPrivilegeRules();
        if (carrierPrivilegeRules == null) {
            return Collections.EMPTY_SET;
        }
        ArraySet arraySet = new ArraySet();
        Iterator<UiccAccessRule> it = carrierPrivilegeRules.getAccessRules().iterator();
        while (it.hasNext()) {
            String str = toCertificateToPackageMap.get(it.next().getCertificateHexString().toUpperCase(Locale.ROOT));
            if (!TextUtils.isEmpty(str) && !isPackageBundled(this.mContext, str)) {
                arraySet.add(str);
            }
        }
        return arraySet;
    }

    public static Map<String, String> parseToCertificateToPackageMap(String str) {
        List listAsList = Arrays.asList(str.split("\\s*;\\s*"));
        if (listAsList.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        ArrayMap arrayMap = new ArrayMap(listAsList.size());
        Iterator it = listAsList.iterator();
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split("\\s*:\\s*");
            if (strArrSplit.length == 2) {
                arrayMap.put(strArrSplit[0].toUpperCase(Locale.ROOT), strArrSplit[1]);
            } else {
                Rlog.d(LOG_TAG, "Incorrect length of key-value pair in carrier app allow list map. Length should be exactly 2");
            }
        }
        return arrayMap;
    }

    @Override // com.android.internal.telephony.IccCard
    public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType appType) {
        synchronized (this.mLock) {
            int i = 0;
            while (true) {
                try {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i >= uiccCardApplicationArr.length) {
                        return false;
                    }
                    UiccCardApplication uiccCardApplication = uiccCardApplicationArr[i];
                    if (uiccCardApplication != null && uiccCardApplication.getType() == appType) {
                        return true;
                    }
                    i++;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public IccCardStatus.PinState getUniversalPinState() {
        IccCardStatus.PinState pinState;
        synchronized (this.mLock) {
            pinState = this.mUniversalPinState;
        }
        return pinState;
    }

    public UiccCardApplication getApplication(int i) {
        int i2;
        synchronized (this.mLock) {
            try {
                if (i == 1) {
                    i2 = this.mGsmUmtsSubscriptionAppIndex;
                } else {
                    if (i == 2) {
                        return null;
                    }
                    i2 = i != 3 ? 8 : this.mImsSubscriptionAppIndex;
                }
                if (i2 >= 0) {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i2 < uiccCardApplicationArr.length) {
                        return uiccCardApplicationArr[i2];
                    }
                }
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccCardApplication getApplicationIndex(int i) {
        synchronized (this.mLock) {
            if (i >= 0) {
                try {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i < uiccCardApplicationArr.length) {
                        return uiccCardApplicationArr[i];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return null;
        }
    }

    public UiccCardApplication getApplicationByType(int i) {
        synchronized (this.mLock) {
            int i2 = 0;
            while (true) {
                try {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i2 >= uiccCardApplicationArr.length) {
                        return null;
                    }
                    UiccCardApplication uiccCardApplication = uiccCardApplicationArr[i2];
                    if (uiccCardApplication != null && uiccCardApplication.getType().ordinal() == i) {
                        return this.mUiccApplications[i2];
                    }
                    i2++;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public boolean resetAppWithAid(String str, boolean z) {
        boolean z2;
        synchronized (this.mLock) {
            z2 = false;
            int i = 0;
            boolean z3 = false;
            boolean z4 = false;
            while (true) {
                try {
                    UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
                    if (i >= uiccCardApplicationArr.length) {
                        break;
                    }
                    if (uiccCardApplicationArr[i] != null && (TextUtils.isEmpty(str) || str.equals(this.mUiccApplications[i].getAid()))) {
                        if (!TextUtils.isEmpty(str) && this.mUiccApplications[i].getType() == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
                            z4 = true;
                        }
                        this.mUiccApplications[i].dispose();
                        this.mUiccApplications[i] = null;
                        z3 = true;
                    }
                    i++;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z && TextUtils.isEmpty(str)) {
                if (this.mCarrierPrivilegeRules != null) {
                    this.mCarrierPrivilegeRules = null;
                    this.mContext.getContentResolver().unregisterContentObserver(this.mProvisionCompleteContentObserver);
                    z3 = true;
                }
                CatService catService = this.mCatService;
                if (catService != null) {
                    catService.dispose();
                    this.mCatService = null;
                    z3 = true;
                }
            }
            if (z3 && !z4) {
                z2 = true;
            }
        }
        return z2;
    }

    public void iccOpenLogicalChannel(String str, int i, Message message) {
        logWithLocalLog("iccOpenLogicalChannel: " + str + " , " + i + " by pid:" + Binder.getCallingPid() + " uid:" + Binder.getCallingUid());
        this.mCi.iccOpenLogicalChannel(str, i, this.mHandler.obtainMessage(8, message));
    }

    public void iccCloseLogicalChannel(int i, boolean z, Message message) {
        logWithLocalLog("iccCloseLogicalChannel: " + i);
        this.mCi.iccCloseLogicalChannel(i, z, this.mHandler.obtainMessage(9, message));
    }

    public void iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z, Message message) {
        this.mCi.iccTransmitApduLogicalChannel(i, i2, i3, i4, i5, i6, str, z, this.mHandler.obtainMessage(10, message));
    }

    public void iccTransmitApduBasicChannel(int i, int i2, int i3, int i4, int i5, String str, Message message) {
        this.mCi.iccTransmitApduBasicChannel(i, i2, i3, i4, i5, str, this.mHandler.obtainMessage(11, message));
    }

    public void iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, String str, Message message) {
        this.mCi.iccIO(i2, i, str, i3, i4, i5, null, null, this.mHandler.obtainMessage(12, message));
    }

    public void sendEnvelopeWithStatus(String str, Message message) {
        this.mCi.sendEnvelopeWithStatus(str, message);
    }

    public int getNumApplications() {
        return this.mLastReportedNumOfUiccApplications;
    }

    public int getPhoneId() {
        return this.mPhoneId;
    }

    public boolean areCarrierPrivilegeRulesLoaded() {
        UiccCarrierPrivilegeRules carrierPrivilegeRules = getCarrierPrivilegeRules();
        return carrierPrivilegeRules == null || carrierPrivilegeRules.areCarrierPriviligeRulesLoaded();
    }

    public List<String> getCertsFromCarrierPrivilegeAccessRules() {
        ArrayList arrayList = new ArrayList();
        UiccCarrierPrivilegeRules carrierPrivilegeRules = getCarrierPrivilegeRules();
        if (carrierPrivilegeRules != null) {
            Iterator<UiccAccessRule> it = carrierPrivilegeRules.getAccessRules().iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getCertificateHexString());
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    public List<UiccAccessRule> getCarrierPrivilegeAccessRules() {
        UiccCarrierPrivilegeRules carrierPrivilegeRules = getCarrierPrivilegeRules();
        if (carrierPrivilegeRules == null) {
            return Collections.EMPTY_LIST;
        }
        return new ArrayList(carrierPrivilegeRules.getAccessRules());
    }

    private UiccCarrierPrivilegeRules getCarrierPrivilegeRules() {
        synchronized (this.mLock) {
            try {
                UiccCarrierPrivilegeRules uiccCarrierPrivilegeRules = this.mTestOverrideCarrierPrivilegeRules;
                if (uiccCarrierPrivilegeRules != null) {
                    return uiccCarrierPrivilegeRules;
                }
                return this.mCarrierPrivilegeRules;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean setOperatorBrandOverride(String str) {
        log("setOperatorBrandOverride: " + str);
        log("current iccId: " + SubscriptionInfo.getPrintableId(getIccId()));
        final String iccId = getIccId();
        if (TextUtils.isEmpty(iccId)) {
            return false;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(getPhoneId());
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(subscriptionId);
        if (subscriptionInfoInternal == null) {
            loge("setOperatorBrandOverride: Cannot find subscription info for sub " + subscriptionId);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(subscriptionInfoInternal.toSubscriptionInfo());
        String groupUuid = subscriptionInfoInternal.getGroupUuid();
        if (!TextUtils.isEmpty(groupUuid)) {
            arrayList.addAll(SubscriptionManagerService.getInstance().getSubscriptionsInGroup(ParcelUuid.fromString(groupUuid), this.mContext.getOpPackageName(), this.mContext.getFeatureId()));
        }
        if (arrayList.stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.uicc.UiccProfile$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TextUtils.equals(IccUtils.stripTrailingFs(((SubscriptionInfo) obj).getIccId()), IccUtils.stripTrailingFs(iccId));
            }
        })) {
            loge("iccId doesn't match current active subId.");
            return false;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        String str2 = "operator_branding_" + iccId;
        if (str == null) {
            editorEdit.remove(str2).commit();
            return true;
        }
        editorEdit.putString(str2, str).commit();
        return true;
    }

    public String getOperatorBrandOverride() {
        String iccId = getIccId();
        if (TextUtils.isEmpty(iccId)) {
            return null;
        }
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("operator_branding_" + iccId, null);
    }

    public String getIccId() {
        IccRecords iccRecords;
        UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
        if (uiccCardApplicationArr == null) {
            return null;
        }
        for (UiccCardApplication uiccCardApplication : uiccCardApplicationArr) {
            if (uiccCardApplication != null && (iccRecords = uiccCardApplication.getIccRecords()) != null && iccRecords.getIccId() != null) {
                return iccRecords.getIccId();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String eventToString(int i) {
        switch (i) {
            case 1:
                return "RADIO_OFF_OR_UNAVAILABLE";
            case 2:
                return "ICC_LOCKED";
            case 3:
                return "APP_READY";
            case 4:
                return "RECORDS_LOADED";
            case 5:
                return "NETWORK_LOCKED";
            case 6:
                return "EID_READY";
            case 7:
            case 14:
            default:
                return "UNKNOWN(" + i + ")";
            case 8:
                return "OPEN_LOGICAL_CHANNEL_DONE";
            case 9:
                return "CLOSE_LOGICAL_CHANNEL_DONE";
            case 10:
                return "TRANSMIT_APDU_LOGICAL_CHANNEL_DONE";
            case 11:
                return "TRANSMIT_APDU_BASIC_CHANNEL_DONE";
            case 12:
                return "SIM_IO_DONE";
            case 13:
                return "CARRIER_PRIVILEGES_LOADED";
            case 15:
                return "CARRIER_PRIVILEGES_TEST_OVERRIDE_SET";
            case 16:
                return "SUPPLY_ICC_PIN_DONE";
        }
    }

    protected void log(String str) {
        Rlog.d("UiccProfile [" + this.mPhoneId + "]", str);
    }

    protected void loge(String str) {
        Rlog.e("UiccProfile [" + this.mPhoneId + "]", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWithLocalLog(String str) {
        Rlog.d("UiccProfile [" + this.mPhoneId + "]", str);
        UiccController.addLocalLog("UiccProfile[" + this.mPhoneId + "]: " + str);
    }

    public void refresh() {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(13), 20L);
    }

    public void setTestOverrideCarrierPrivilegeRules(List<UiccAccessRule> list) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(15, list));
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IccRecords iccRecords;
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mCatService=" + this.mCatService);
        androidUtilIndentingPrintWriter.println("mUniversalPinState=" + this.mUniversalPinState);
        androidUtilIndentingPrintWriter.println("mGsmUmtsSubscriptionAppIndex=" + this.mGsmUmtsSubscriptionAppIndex);
        androidUtilIndentingPrintWriter.println("mCdmaSubscriptionAppIndex=" + this.mCdmaSubscriptionAppIndex);
        androidUtilIndentingPrintWriter.println("mImsSubscriptionAppIndex=" + this.mImsSubscriptionAppIndex);
        androidUtilIndentingPrintWriter.println("mUiccApplications: length=" + this.mUiccApplications.length);
        androidUtilIndentingPrintWriter.increaseIndent();
        int i = 0;
        while (true) {
            UiccCardApplication[] uiccCardApplicationArr = this.mUiccApplications;
            if (i >= uiccCardApplicationArr.length) {
                break;
            }
            if (uiccCardApplicationArr[i] == null) {
                androidUtilIndentingPrintWriter.println("mUiccApplications[" + i + "]=null");
            } else {
                androidUtilIndentingPrintWriter.println("mUiccApplications[" + i + "]=" + this.mUiccApplications[i].getType() + " " + this.mUiccApplications[i]);
            }
            i++;
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println();
        for (UiccCardApplication uiccCardApplication : this.mUiccApplications) {
            if (uiccCardApplication != null) {
                uiccCardApplication.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                androidUtilIndentingPrintWriter.println();
            }
        }
        for (UiccCardApplication uiccCardApplication2 : this.mUiccApplications) {
            if (uiccCardApplication2 != null && (iccRecords = uiccCardApplication2.getIccRecords()) != null) {
                iccRecords.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                androidUtilIndentingPrintWriter.println();
            }
        }
        if (this.mCarrierPrivilegeRules == null) {
            androidUtilIndentingPrintWriter.println("mCarrierPrivilegeRules: null");
        } else {
            androidUtilIndentingPrintWriter.println("mCarrierPrivilegeRules: ");
            androidUtilIndentingPrintWriter.increaseIndent();
            this.mCarrierPrivilegeRules.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
            androidUtilIndentingPrintWriter.decreaseIndent();
        }
        if (this.mTestOverrideCarrierPrivilegeRules != null) {
            androidUtilIndentingPrintWriter.println("mTestOverrideCarrierPrivilegeRules: " + this.mTestOverrideCarrierPrivilegeRules);
            this.mTestOverrideCarrierPrivilegeRules.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        }
        androidUtilIndentingPrintWriter.flush();
        androidUtilIndentingPrintWriter.println("mNetworkLockedRegistrants: size=" + this.mNetworkLockedRegistrants.size());
        for (int i2 = 0; i2 < this.mNetworkLockedRegistrants.size(); i2++) {
            androidUtilIndentingPrintWriter.println("  mNetworkLockedRegistrants[" + i2 + "]=" + ((Registrant) this.mNetworkLockedRegistrants.get(i2)).getHandler());
        }
        androidUtilIndentingPrintWriter.println("mCurrentAppType=" + this.mCurrentAppType);
        androidUtilIndentingPrintWriter.println("mUiccCard=" + this.mUiccCard);
        androidUtilIndentingPrintWriter.println("mUiccApplication=" + this.mUiccApplication);
        androidUtilIndentingPrintWriter.println("mIccRecords=" + this.mIccRecords);
        androidUtilIndentingPrintWriter.println("mExternalState=" + this.mExternalState);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.flush();
    }

    private static int checkCdmaCard(int i) {
        int i2 = -1;
        if (i >= 0) {
            String[] strArr = PROPERTY_RIL_FULL_UICC_TYPE;
            if (i < strArr.length) {
                String str = SystemProperties.get(strArr[i]);
                String[] strArrSplit = (str == null || str.length() <= 0) ? null : str.split(",");
                if (strArrSplit != null) {
                    i2 = (Arrays.asList(strArrSplit).contains("RUIM") && (Arrays.asList(strArrSplit).contains("SIM") || Arrays.asList(strArrSplit).contains("USIM"))) ? 2 : 0;
                }
                StringBuilder sb = new StringBuilder("checkCdmaCard slotId ");
                sb.append(i);
                sb.append(", prop value = ");
                sb.append(str);
                sb.append(", size = ");
                sb.append(strArrSplit != null ? strArrSplit.length : 0);
                sb.append(", cdma3gCardType = ");
                sb.append(i2);
                Rlog.d(LOG_TAG, sb.toString());
                return i2;
            }
        }
        Rlog.d(LOG_TAG, "checkCdmaCard: invalid slotId " + i);
        return -1;
    }

    protected Exception covertException(String str) {
        return new RuntimeException("ICC card is absent.");
    }

    protected UiccCardApplication makeUiccApplication(UiccProfile uiccProfile, IccCardApplicationStatus iccCardApplicationStatus, Context context, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        return new UiccCardApplication(uiccProfile, iccCardApplicationStatus, context, commandsInterface, featureFlags);
    }

    protected String getSubscriptionDisplayName(int i, Context context) {
        return this.mTelephonyManager.getSimOperatorName(i);
    }

    protected boolean isUdpateCarrierName(String str) {
        return !TextUtils.isEmpty(str);
    }

    protected int getSubscriptionAppIndex(IccCardApplicationStatus.AppType appType) {
        if (appType == IccCardApplicationStatus.AppType.APPTYPE_USIM || appType == IccCardApplicationStatus.AppType.APPTYPE_SIM) {
            return this.mGsmUmtsSubscriptionAppIndex;
        }
        if (appType == IccCardApplicationStatus.AppType.APPTYPE_RUIM || appType == IccCardApplicationStatus.AppType.APPTYPE_CSIM) {
            return this.mCdmaSubscriptionAppIndex;
        }
        if (appType == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
            return this.mImsSubscriptionAppIndex;
        }
        return 8;
    }

    protected String overrideSpnFromConfig() {
        return "";
    }

    public void setServiceProviderNameForEsim(IccRecords iccRecords, String str) {
        if (iccRecords == null) {
            return;
        }
        iccRecords.setServiceProviderName(str);
        if (this.mExternalState == IccCardConstants.State.LOADED) {
            SubscriptionManagerService.getInstance().onHandleSimLoaded(this.mPhoneId);
            Rlog.d(LOG_TAG, "update eSIM displayName");
        }
    }
}
