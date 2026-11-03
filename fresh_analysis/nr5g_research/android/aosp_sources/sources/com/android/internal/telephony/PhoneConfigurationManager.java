package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telephony.PhoneCapability;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class PhoneConfigurationManager {
    public static final String DSDA = "dsda";
    public static final String DSDS = "dsds";
    public static final int EVENT_SIMULTANEOUS_CALLING_SUPPORT_CHANGED = 106;
    public static final String SSSS = "";
    public static final String TSTS = "tsts";
    private static PhoneConfigurationManager sInstance;
    private final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private final DefaultPhoneNotifier mNotifier;
    private Phone[] mPhones;
    private QtiMultiSimVoiceCallTracker mQtiMultiSimVoiceCallTracker;
    private TelephonyManager mTelephonyManager;
    private static final RegistrantList sMultiSimConfigChangeRegistrants = new RegistrantList();
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    private PhoneCapability mStaticCapability = null;
    private final Set mSlotsSupportingSimultaneousCellularCalls = new HashSet(3);
    private final Set mSubIdsSupportingSimultaneousCellularCalls = new HashSet(3);
    private final HashSet mSimultaneousCellularCallingListeners = new HashSet(1);
    private MockableInterface mMi = new MockableInterface();
    public Set<Listener> mListeners = new CopyOnWriteArraySet();
    private final String EXTRAS_MSIM_VOICE_CAPABILITY = QtiMultiSimVoiceCallTracker.EXTRAS_MSIM_VOICE_CAPABILITY;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.PhoneConfigurationManager.1
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            PhoneConfigurationManager.this.updateSimultaneousSubIdsFromPhoneIdMappingAndNotify();
        }
    };
    protected final RadioConfig mRadioConfig = RadioConfig.getInstance();
    protected final Handler mHandler = new ConfigManagerHandler();
    private final Map mPhoneStatusMap = new HashMap();
    private boolean mVirtualDsdaEnabled = DeviceConfig.getBoolean("telephony", "enable_virtual_dsda", false);

    public interface Listener {
        void onDeviceConfigChanged();

        void onPhoneCapabilityChanged();
    }

    public static abstract class ListenerBase implements Listener {
        @Override // com.android.internal.telephony.PhoneConfigurationManager.Listener
        public void onDeviceConfigChanged() {
        }

        @Override // com.android.internal.telephony.PhoneConfigurationManager.Listener
        public void onPhoneCapabilityChanged() {
        }
    }

    protected boolean onMaybeEnableCellularDSDASupport(boolean z, boolean z2) {
        return z2;
    }

    public static PhoneConfigurationManager init(Context context, FeatureFlags featureFlags) {
        PhoneConfigurationManager phoneConfigurationManager;
        synchronized (PhoneConfigurationManager.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("PhoneCfgMgr", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makePhoneConfigurationManager(context, featureFlags);
                }
                phoneConfigurationManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return phoneConfigurationManager;
    }

    protected PhoneConfigurationManager(Context context, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mNotifier = TelephonyComponentFactory.getInstance().inject(PhoneNotifier.class.getName()).makeDefaultPhoneNotifier(context, featureFlags);
        DeviceConfig.addOnPropertiesChangedListener("telephony", new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.internal.telephony.PhoneConfigurationManager$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        });
        notifyCapabilityChanged();
        Phone[] phones = PhoneFactory.getPhones();
        this.mPhones = phones;
        for (Phone phone : phones) {
            registerForRadioState(phone);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        if (TextUtils.equals("telephony", properties.getNamespace())) {
            this.mHandler.sendEmptyMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE);
        }
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSimultaneousSubIdsFromPhoneIdMappingAndNotify() {
        Set set = (Set) this.mSlotsSupportingSimultaneousCellularCalls.stream().map(new Function() { // from class: com.android.internal.telephony.PhoneConfigurationManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$updateSimultaneousSubIdsFromPhoneIdMappingAndNotify$1((Integer) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.PhoneConfigurationManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PhoneConfigurationManager.m284$r8$lambda$YAa4SxNiWnG5CDTb5cCOU_FfyY((Integer) obj);
            }
        }).collect(Collectors.toSet());
        if (this.mSubIdsSupportingSimultaneousCellularCalls.equals(set)) {
            return;
        }
        log("updateSimultaneousSubIdsFromPhoneIdMapping update: " + this.mSubIdsSupportingSimultaneousCellularCalls + " -> " + set);
        this.mSubIdsSupportingSimultaneousCellularCalls.clear();
        this.mSubIdsSupportingSimultaneousCellularCalls.addAll(set);
        this.mNotifier.notifySimultaneousCellularCallingSubscriptionsChanged(this.mSubIdsSupportingSimultaneousCellularCalls);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$updateSimultaneousSubIdsFromPhoneIdMappingAndNotify$1(Integer num) {
        return Integer.valueOf(this.mPhones[num.intValue()].getSubId());
    }

    /* JADX INFO: renamed from: $r8$lambda$YAa4SxNiWnG5CDTb5cCOU_-FfyY, reason: not valid java name */
    public static /* synthetic */ boolean m284$r8$lambda$YAa4SxNiWnG5CDTb5cCOU_FfyY(Integer num) {
        return num.intValue() > -1;
    }

    private void registerForRadioState(Phone phone) {
        phone.mCi.registerForAvailable(this.mHandler, 1, phone);
    }

    private PhoneCapability getDefaultCapability() {
        if (getPhoneCount() > 1) {
            return PhoneCapability.DEFAULT_DSDS_CAPABILITY;
        }
        return PhoneCapability.DEFAULT_SSSS_CAPABILITY;
    }

    protected PhoneCapability maybeOverrideMaxActiveVoiceSubscriptions(PhoneCapability phoneCapability) {
        boolean z = phoneCapability.getLogicalModemList().size() > 1 && this.mVirtualDsdaEnabled;
        boolean zEquals = this.mMi.getMultiSimProperty().orElse(SSSS).equals(DSDA);
        if (z || zEquals) {
            return new PhoneCapability.Builder(phoneCapability).setMaxActiveVoiceSubscriptions(Math.max(phoneCapability.getMaxActiveVoiceSubscriptions(), 2)).build();
        }
        return phoneCapability;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeEnableCellularDSDASupport() {
        boolean z = getPhoneCount() > 1 && this.mMi.getMultiSimProperty().orElse(SSSS).equals(DSDA);
        RadioConfig radioConfig = this.mRadioConfig;
        boolean z2 = radioConfig != null && radioConfig.getRadioConfigProxy(null).getVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_2_2) && getPhoneCount() > 1 && getCellularStaticPhoneCapability().getMaxActiveVoiceSubscriptions() > 1;
        if (z2) {
            updateSimultaneousCallingSupport();
            this.mRadioConfig.registerForSimultaneousCallingSupportStatusChanged(this.mHandler, 106, null);
        } else if (z) {
            log("DSDA modem config detected - setting DSDA enabled");
            for (Phone phone : this.mPhones) {
                this.mSlotsSupportingSimultaneousCellularCalls.add(Integer.valueOf(phone.getPhoneId()));
            }
            updateSimultaneousSubIdsFromPhoneIdMappingAndNotify();
            notifySimultaneousCellularCallingSlotsChanged();
        }
        if (onMaybeEnableCellularDSDASupport(z2, z) || z2) {
            Log.d("PhoneCfgMgr", "maybeEnableCellularDSDASupport: registering mSubscriptionsChangedListener");
            TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) this.mContext.getSystemService(TelephonyRegistryManager.class);
            SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = this.mSubscriptionsChangedListener;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            telephonyRegistryManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener, new NetworkTypeController$$ExternalSyntheticLambda2(handler));
        }
    }

    public static PhoneConfigurationManager getInstance() {
        if (sInstance == null) {
            Log.wtf("PhoneCfgMgr", "getInstance null");
        }
        return sInstance;
    }

    private final class ConfigManagerHandler extends Handler {
        private ConfigManagerHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:48:0x010e  */
        /* JADX WARN: Instruction removed from duplicated block: B:48:0x010e, please report this as an issue */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            List<Integer> listGenerateSimultaneousCallingSupport;
            RadioConfig radioConfig;
            int i = message.what;
            if (i == 1 || i == 5) {
                PhoneConfigurationManager.log("Received EVENT_RADIO_AVAILABLE/EVENT_RADIO_ON");
                Object obj = ((AsyncResult) message.obj).userObj;
                if (obj != null && (obj instanceof Phone)) {
                    PhoneConfigurationManager.this.updatePhoneStatus((Phone) obj);
                } else {
                    PhoneConfigurationManager.log("Unable to add phoneStatus to cache. No phone object provided for event " + message.what);
                }
                PhoneConfigurationManager.this.updateRadioCapability();
                return;
            }
            if (i == 100) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult != null && asyncResult.exception == null) {
                    PhoneConfigurationManager.this.onMultiSimConfigChanged(message.arg1);
                    return;
                }
                PhoneConfigurationManager.log(message.what + " failure. Not switching multi-sim config." + asyncResult.exception);
                return;
            }
            boolean z = false;
            switch (i) {
                case 102:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2 == null || asyncResult2.exception != null) {
                        PhoneConfigurationManager.log(message.what + " failure. Not updating modem status." + asyncResult2.exception);
                    } else {
                        PhoneConfigurationManager.this.addToPhoneStatusCache(message.arg1, ((Boolean) asyncResult2.result).booleanValue());
                    }
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    if (asyncResult3 == null || asyncResult3.exception != null) {
                        PhoneConfigurationManager.log(message.what + " failure. Not getting phone capability." + asyncResult3.exception);
                    } else {
                        PhoneConfigurationManager.this.setStaticPhoneCapability((PhoneCapability) asyncResult3.result);
                        PhoneConfigurationManager.this.notifyCapabilityChanged();
                        Iterator<Listener> it = PhoneConfigurationManager.this.mListeners.iterator();
                        while (it.hasNext()) {
                            it.next().onPhoneCapabilityChanged();
                        }
                        PhoneConfigurationManager.this.maybeEnableCellularDSDASupport();
                    }
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    boolean z2 = DeviceConfig.getBoolean("telephony", "enable_virtual_dsda", false);
                    if (z2 != PhoneConfigurationManager.this.mVirtualDsdaEnabled) {
                        PhoneConfigurationManager.log("EVENT_DEVICE_CONFIG_CHANGED: from " + PhoneConfigurationManager.this.mVirtualDsdaEnabled + " to " + z2);
                        PhoneConfigurationManager.this.mVirtualDsdaEnabled = z2;
                        Iterator<Listener> it2 = PhoneConfigurationManager.this.mListeners.iterator();
                        while (it2.hasNext()) {
                            it2.next().onDeviceConfigChanged();
                        }
                    }
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                case 106:
                    PhoneConfigurationManager.log("Received EVENT_SLOTS_SUPPORTING_SIMULTANEOUS_CALL_CHANGED/DONE");
                    if (PhoneConfigurationManager.this.getPhoneCount() < 2) {
                        if (!PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.isEmpty()) {
                            PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                        }
                    } else {
                        AsyncResult asyncResult4 = (AsyncResult) message.obj;
                        if (TelephonyProperties.multi_sim_voice_capability().orElse(4).intValue() != 4 && (radioConfig = PhoneConfigurationManager.this.mRadioConfig) != null && radioConfig.getRadioConfigProxy(null).getVersion().less(RIL.RADIO_HAL_VERSION_2_3)) {
                            z = true;
                        }
                        if ((asyncResult4 != null && asyncResult4.exception == null) || z) {
                            if (z) {
                                listGenerateSimultaneousCallingSupport = PhoneConfigurationManager.this.mQtiMultiSimVoiceCallTracker.generateSimultaneousCallingSupport();
                            } else {
                                listGenerateSimultaneousCallingSupport = (List) asyncResult4.result;
                            }
                            if (!PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.isEmpty()) {
                                PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                            }
                            int phoneCount = PhoneConfigurationManager.this.getPhoneCount() - 1;
                            for (Integer num : listGenerateSimultaneousCallingSupport) {
                                int iIntValue = num.intValue();
                                if (iIntValue < 0 || iIntValue > phoneCount) {
                                    PhoneConfigurationManager.loge("Invalid slot supporting DSDA =" + iIntValue + ". Disabling DSDA.");
                                    PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                                    if (PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size() <= PhoneConfigurationManager.this.getPhoneCount() || PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size() < 2) {
                                        PhoneConfigurationManager.loge("Invalid size of DSDA slots. Disabling cellular DSDA. Size of mSlotsSupportingSimultaneousCellularCalls=" + PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size());
                                        PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                                    }
                                } else {
                                    PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.add(num);
                                }
                            }
                            if (PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size() <= PhoneConfigurationManager.this.getPhoneCount()) {
                                PhoneConfigurationManager.loge("Invalid size of DSDA slots. Disabling cellular DSDA. Size of mSlotsSupportingSimultaneousCellularCalls=" + PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size());
                                PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                            } else {
                                PhoneConfigurationManager.loge("Invalid size of DSDA slots. Disabling cellular DSDA. Size of mSlotsSupportingSimultaneousCellularCalls=" + PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.size());
                                PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(message.what);
                            sb.append(" failure. Not getting logical slots that support simultaneous calling.");
                            sb.append(asyncResult4 != null ? asyncResult4.exception : "[null]");
                            PhoneConfigurationManager.log(sb.toString());
                            PhoneConfigurationManager.this.mSlotsSupportingSimultaneousCellularCalls.clear();
                        }
                        PhoneConfigurationManager.this.updateSimultaneousSubIdsFromPhoneIdMappingAndNotify();
                        PhoneConfigurationManager.this.notifySimultaneousCellularCallingSlotsChanged();
                    }
                    break;
                default:
                    PhoneConfigurationManager.log("Unknown event: " + message.what);
                    break;
            }
        }
    }

    public void enablePhone(Phone phone, boolean z, Message message) {
        if (phone == null) {
            log("enablePhone failed phone is null");
        } else {
            phone.mCi.enableModem(z, message);
        }
    }

    public boolean getPhoneStatus(Phone phone) {
        if (phone == null) {
            log("getPhoneStatus failed phone is null");
            return false;
        }
        try {
            return getPhoneStatusFromCache(phone.getPhoneId());
        } catch (NoSuchElementException unused) {
            return true;
        } finally {
            updatePhoneStatus(phone);
        }
    }

    public void getPhoneStatusFromModem(Phone phone, Message message) {
        if (phone == null) {
            log("getPhoneStatus failed phone is null");
        }
        phone.mCi.getModemStatus(message);
    }

    public boolean getPhoneStatusFromCache(int i) throws NoSuchElementException {
        if (this.mPhoneStatusMap.containsKey(Integer.valueOf(i))) {
            return ((Boolean) this.mPhoneStatusMap.get(Integer.valueOf(i))).booleanValue();
        }
        throw new NoSuchElementException("phoneId not found: " + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhoneStatus(Phone phone) {
        phone.mCi.getModemStatus(Message.obtain(this.mHandler, 102, phone.getPhoneId(), 0));
    }

    public void addToPhoneStatusCache(int i, boolean z) {
        this.mPhoneStatusMap.put(Integer.valueOf(i), Boolean.valueOf(z));
    }

    public int getPhoneCount() {
        return this.mTelephonyManager.getActiveModemCount();
    }

    public Set<Integer> getSlotsSupportingSimultaneousCellularCalls() {
        return this.mSlotsSupportingSimultaneousCellularCalls;
    }

    public void updateSimultaneousCallingSupport() {
        log("updateSimultaneousCallingSupport: sending the request for getting the list of logical slots supporting simultaneous cellular calling");
        this.mRadioConfig.updateSimultaneousCallingSupport(Message.obtain(this.mHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION));
        log("updateSimultaneousCallingSupport: mSlotsSupportingSimultaneousCellularCalls = " + this.mSlotsSupportingSimultaneousCellularCalls);
    }

    public synchronized PhoneCapability getStaticPhoneCapability() {
        PhoneCapability phoneCapabilityMaybeOverrideMaxActiveVoiceSubscriptions;
        try {
            PhoneCapability defaultCapability = this.mStaticCapability;
            boolean z = defaultCapability == null;
            if (z) {
                defaultCapability = getDefaultCapability();
            }
            phoneCapabilityMaybeOverrideMaxActiveVoiceSubscriptions = maybeOverrideMaxActiveVoiceSubscriptions(defaultCapability);
            log("getStaticPhoneCapability: isDefault=" + z + ", caps=" + phoneCapabilityMaybeOverrideMaxActiveVoiceSubscriptions);
        } catch (Throwable th) {
            throw th;
        }
        return phoneCapabilityMaybeOverrideMaxActiveVoiceSubscriptions;
    }

    private synchronized PhoneCapability getCellularStaticPhoneCapability() {
        log("getCellularStaticPhoneCapability: mStaticCapability " + this.mStaticCapability);
        return this.mStaticCapability;
    }

    public synchronized void setStaticPhoneCapability(PhoneCapability phoneCapability) {
        log("setStaticPhoneCapability: mStaticCapability " + phoneCapability);
        this.mStaticCapability = phoneCapability;
    }

    public void updateRadioCapability() {
        log("updateRadioCapability: sending the request for getting PhoneCapability");
        this.mRadioConfig.getPhoneCapability(Message.obtain(this.mHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING));
    }

    public PhoneCapability getCurrentPhoneCapability() {
        return getStaticPhoneCapability();
    }

    public int getNumberOfModemsWithSimultaneousDataConnections() {
        return getStaticPhoneCapability().getMaxActiveDataSubscriptions();
    }

    public int getNumberOfModemsWithSimultaneousVoiceConnections() {
        return getStaticPhoneCapability().getMaxActiveVoiceSubscriptions();
    }

    public boolean isVirtualDsdaEnabled() {
        return this.mVirtualDsdaEnabled;
    }

    public void registerForSimultaneousCellularCallingSlotsChanged(Consumer<Set<Integer>> consumer) {
        this.mSimultaneousCellularCallingListeners.add(consumer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySimultaneousCellularCallingSlotsChanged() {
        log("notifying listeners of changes to simultaneous cellular calling - new state:" + this.mSlotsSupportingSimultaneousCellularCalls);
        Iterator it = this.mSimultaneousCellularCallingListeners.iterator();
        while (it.hasNext()) {
            try {
                ((Consumer) it.next()).accept(new HashSet(this.mSlotsSupportingSimultaneousCellularCalls));
            } catch (Exception e) {
                log("Unexpected Exception encountered when notifying listener: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCapabilityChanged() {
        this.mNotifier.notifyPhoneCapabilityChanged(getStaticPhoneCapability());
    }

    public void switchMultiSimConfig(int i) {
        log("switchMultiSimConfig: with numOfSims = " + i);
        if (getStaticPhoneCapability().getLogicalModemList().size() < i) {
            log("switchMultiSimConfig: Phone is not capable of enabling " + i + " sims, exiting!");
            return;
        }
        if (getPhoneCount() != i) {
            log("switchMultiSimConfig: sending the request for switching");
            this.mRadioConfig.setNumOfLiveModems(i, Message.obtain(this.mHandler, 100, i, 0));
        } else {
            log("switchMultiSimConfig: No need to switch. getNumOfActiveSims is already " + i);
        }
    }

    public boolean isRebootRequiredForModemConfigChange() {
        return this.mMi.isRebootRequiredForModemConfigChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged(int i) {
        int phoneCount = getPhoneCount();
        setMultiSimProperties(i);
        if (isRebootRequiredForModemConfigChange()) {
            log("onMultiSimConfigChanged: Rebooting.");
            ((PowerManager) this.mContext.getSystemService("power")).reboot("Multi-SIM config changed.");
            return;
        }
        log("onMultiSimConfigChanged: Rebooting is not required.");
        this.mMi.notifyPhoneFactoryOnMultiSimConfigChanged(this.mContext, i);
        broadcastMultiSimConfigChange(i);
        boolean z = false;
        int i2 = i;
        while (i2 < phoneCount) {
            SubscriptionManagerService.getInstance().markSubscriptionsInactive(i2);
            this.mPhones[i2].mCi.onSlotActiveStatusChange(SubscriptionManager.isValidPhoneId(i2));
            i2++;
            z = true;
        }
        if (z) {
            MultiSimSettingController.getInstance().onPhoneRemoved();
        }
        this.mPhones = PhoneFactory.getPhones();
        for (int i3 = phoneCount; i3 < i; i3++) {
            Phone phone = this.mPhones[i3];
            registerForRadioState(phone);
            phone.mCi.onSlotActiveStatusChange(SubscriptionManager.isValidPhoneId(i3));
        }
        if (i > 1) {
            maybeEnableCellularDSDASupport();
        } else {
            this.mSlotsSupportingSimultaneousCellularCalls.clear();
            updateSimultaneousSubIdsFromPhoneIdMappingAndNotify();
            notifySimultaneousCellularCallingSlotsChanged();
        }
        if (i <= phoneCount || i < 2) {
            Log.i("PhoneCfgMgr", "onMultiSimConfigChanged: Multi-SIM mode NOT detected.  NOT setting the default VOICE and SMS subId to -1 (No Preference)");
            return;
        }
        Log.i("PhoneCfgMgr", " onMultiSimConfigChanged: Multi-SIM mode enabled; setting VOICE & SMS subId to -1 (No Preference)");
        SubscriptionManagerService.getInstance().setDefaultVoiceSubId(-1);
        SubscriptionManagerService.getInstance().setDefaultSmsSubId(-1);
    }

    private void setMultiSimProperties(int i) {
        this.mMi.setMultiSimProperties(i);
    }

    public static void notifyMultiSimConfigChange(int i) {
        sMultiSimConfigChangeRegistrants.notifyResult(Integer.valueOf(i));
    }

    public static void registerForMultiSimConfigChange(Handler handler, int i, Object obj) {
        sMultiSimConfigChangeRegistrants.addUnique(handler, i, obj);
    }

    public static void unregisterForMultiSimConfigChange(Handler handler) {
        sMultiSimConfigChangeRegistrants.remove(handler);
    }

    public static void unregisterAllMultiSimConfigChangeRegistrants() {
        sMultiSimConfigChangeRegistrants.removeAll();
    }

    private void broadcastMultiSimConfigChange(int i) {
        log("broadcastSimSlotNumChange numOfActiveModems" + i);
        notifyMultiSimConfigChange(i);
        Intent intent = new Intent("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        intent.putExtra("android.telephony.extra.ACTIVE_SIM_SUPPORTED_COUNT", i);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public boolean setModemService(String str) {
        log("setModemService: " + str);
        boolean z = SystemProperties.getBoolean("persist.radio.allow_mock_modem", true);
        boolean z2 = SystemProperties.getBoolean("ro.boot.radio.allow_mock_modem", false);
        if (z || z2 || DEBUG) {
            RadioConfig radioConfig = this.mRadioConfig;
            if (!(radioConfig != null ? radioConfig.setModemService(str) : false)) {
                loge("setModemService: switching modem service for radioconfig fail");
                return false;
            }
            boolean modemService = false;
            for (int i = 0; i < getPhoneCount(); i++) {
                Phone phone = this.mPhones[i];
                if (phone != null) {
                    modemService = phone.mCi.setModemService(str);
                }
                if (!modemService) {
                    loge("setModemService: switch modem for radio " + i + " fail");
                    this.mRadioConfig.setModemService(null);
                    for (int i2 = 0; i2 < i; i2++) {
                        this.mPhones[i2].mCi.setModemService(null);
                    }
                    return false;
                }
            }
            return true;
        }
        loge("setModemService is not allowed");
        return false;
    }

    public String getModemService() {
        Phone phone = this.mPhones[0];
        if (phone == null) {
            return "";
        }
        return phone.mCi.getModemService();
    }

    public static class MockableInterface {
        public boolean isRebootRequiredForModemConfigChange() {
            boolean zBooleanValue = TelephonyProperties.reboot_on_modem_change().orElse(Boolean.FALSE).booleanValue();
            PhoneConfigurationManager.log("isRebootRequiredForModemConfigChange: isRebootRequired = " + zBooleanValue);
            return zBooleanValue;
        }

        public void setMultiSimProperties(int i) {
            String str;
            if (i == 2) {
                str = PhoneConfigurationManager.DSDS;
            } else if (i == 3) {
                str = PhoneConfigurationManager.TSTS;
            } else {
                str = PhoneConfigurationManager.SSSS;
            }
            PhoneConfigurationManager.log("setMultiSimProperties to " + str);
            TelephonyProperties.multi_sim_config(str);
        }

        public void notifyPhoneFactoryOnMultiSimConfigChanged(Context context, int i) {
            PhoneFactory.onMultiSimConfigChanged(context, i);
        }

        public Optional<String> getMultiSimProperty() {
            return TelephonyProperties.multi_sim_config();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("PhoneCfgMgr", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Rlog.e("PhoneCfgMgr", str);
    }
}
